
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class GestoreSpese extends Application {
    private Configuratore configuratore;
    private Intestazione intestazione;
    private RegistroSpese tabellaSpese;
    private SezioneRiassuntiva sezRiassuntiva;
    
    @Override
    public void start(Stage primaryStage) {
        configuratore = new Configuratore();    // 1)
        ArchivioSpese.configura(configuratore.infoDB());
        RaccoglitoreEventi.configura(configuratore.infoLog());
        
        VBox gui = caricaGUI();     // 2)
        
        gui.setSpacing(100);    // 3)
        gui.setPadding(new Insets(50, 100, 50, 100));   // 4)
        
        ScrollPane contenitoreGUI = new ScrollPane(gui);    // 5)
        contenitoreGUI.setFitToWidth(true);     // 6)
        
        Scene scena = new Scene(contenitoreGUI, 1270, 720);
        primaryStage.setScene(scena);
        
        primaryStage.setOnCloseRequest((WindowEvent ev) -> salvaDatiCache());   // 7)
        primaryStage.show();
        
        RaccoglitoreEventi.inviaEvento(intestazione.getNickname(), "AVVIO");
    }
    
    private VBox caricaGUI () {
        List<String> datiCache = DatiUltimoAccesso.recuperaDati();  // 8)
        String nickname = datiCache.get(0);
        int giorniVisibili = Integer.parseInt(datiCache.get(1));
        Spesa spesaInSospeso = new Spesa(datiCache.get(2), datiCache.get(3), datiCache.get(4));
        
        intestazione = new Intestazione(nickname);
        tabellaSpese = new RegistroSpese(configuratore.infoCategorie());
        sezRiassuntiva = new SezioneRiassuntiva(giorniVisibili);
        
        popolaGUI(nickname, spesaInSospeso, "", giorniVisibili);
        
        // gestione eventi
        Button btnCambia = (Button) ((VBox) intestazione.getChildren().get(2)).getChildren().get(0);
        btnCambia.setOnAction((ActionEvent ev) -> cambiaUtente());
        
        tabellaSpese.setOnMouseClicked((MouseEvent ev) -> aggiornaCategoria());
        abilitaInserimentiSpese();  // 9)
        
        Button btnIncrementa = sezRiassuntiva.getIncrementa();
        Button btnDecrementa = sezRiassuntiva.getDecrementa();
        btnIncrementa.setOnAction((ActionEvent ev) -> cambiaGiorni(1));
        btnDecrementa.setOnAction((ActionEvent ev) -> cambiaGiorni(-1));
        
        return new VBox(intestazione, tabellaSpese, sezRiassuntiva);
    }
    
    private void popolaGUI (String utente, Spesa spesaInSospeso, String categoria, int giorniVisibili) {    // 10)
        ObservableList<Spesa> listaSpeseRecenti = FXCollections.observableArrayList(ArchivioSpese.recuperaSpeseRecenti(utente));
        listaSpeseRecenti.add(spesaInSospeso);  // 11)
        
        tabellaSpese.popolaRegistro(listaSpeseRecenti);
        
        double[] speseGiorni = ArchivioSpese.recuperaSpeseGiorni(utente, giorniVisibili);
        double speseCategoria = ArchivioSpese.recuperaSpeseCategoria(utente, giorniVisibili, categoria);
        
        sezRiassuntiva.popolaSezioneRiassuntiva(speseGiorni, categoria, speseCategoria);
    }
    
    private void abilitaInserimentiSpese () {
        TableColumn[] colonne = new TableColumn[3];
        for (int i = 0; i < 3; i++)
            colonne[i] = tabellaSpese.getColumns().get(i);
        
        colonne[0].setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Spesa, String>>() {          // 12)
                @Override
                public void handle(TableColumn.CellEditEvent<Spesa, String> ev) {
                    Spesa nuovaSpesa = tabellaSpese.getItems().get(ev.getTablePosition().getRow());
                    nuovaSpesa.setData(ev.getNewValue());
                    
                    if (!nuovaSpesa.getCategoria().equals("") && !nuovaSpesa.getSpesa().equals(""))  // 13)
                        aggiungiSpesa(nuovaSpesa);
                }
            }
        );
        colonne[1].setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Spesa, String>>() {     // 12)
                @Override
                public void handle(TableColumn.CellEditEvent<Spesa, String> ev) {
                    Spesa nuovaSpesa = tabellaSpese.getItems().get(ev.getTablePosition().getRow());
                    nuovaSpesa.setCategoria(ev.getNewValue());
                    
                    if (!nuovaSpesa.getData().equals("") && !nuovaSpesa.getSpesa().equals(""))  // 13)
                        aggiungiSpesa(nuovaSpesa);
                }
            }
        );
        colonne[2].setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Spesa, String>>() {         // 12)
                @Override
                public void handle(TableColumn.CellEditEvent<Spesa, String> ev) {
                    Spesa nuovaSpesa = tabellaSpese.getItems().get(ev.getTablePosition().getRow());
                    nuovaSpesa.setSpesa(ev.getNewValue());
                    
                    if (!nuovaSpesa.getData().equals("") && !nuovaSpesa.getCategoria().equals(""))  // 13)
                        aggiungiSpesa(nuovaSpesa);
                }
            }
        );
    }
    
    private void cambiaUtente () {
        String nuovoUtente = intestazione.modificaIntestazione();   // 14)
            
        if (!nuovoUtente.equals("")) {
            RaccoglitoreEventi.inviaEvento(nuovoUtente, "CAMBIO UTENTE");
            
            popolaGUI(nuovoUtente, new Spesa("", "", ""), "", sezRiassuntiva.getGiorniVisibili());  // 15)
        }
    }
    
    private void aggiornaCategoria () {
        Spesa spesaSelezionata = tabellaSpese.getSelectionModel().getSelectedItem();
        
        if (spesaSelezionata == tabellaSpese.getNuovaSpesa())   // 16)
            return;
        
        String categoria = spesaSelezionata.getCategoria();
        
        if (categoria.equals(sezRiassuntiva.getCategoria()))    // 17)
            return;
        
        RaccoglitoreEventi.inviaEvento(intestazione.getNickname(), "SELEZIONE CATEGORIA");
        
        double speseCategoria = ArchivioSpese.recuperaSpeseCategoria(intestazione.getNickname(), sezRiassuntiva.getGiorniVisibili(), categoria);
        sezRiassuntiva.aggiornaCategoria(categoria, speseCategoria);    // 18)
    }
    
    private void aggiungiSpesa (Spesa nuovaSpesa) {
        ArchivioSpese.archiviaSpesa(intestazione.getNickname(), nuovaSpesa);    // 19)
        
        RaccoglitoreEventi.inviaEvento(intestazione.getNickname(), "AGGIUNTA SPESA");
        
        tabellaSpese.scorriRighe();     // 20)
        
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date dataSpesa = formato.parse(nuovaSpesa.getData());
            
            long diff_ms = Math.abs((new Date()).getTime() - dataSpesa.getTime());     // 21)
            long diffGiorni = TimeUnit.DAYS.convert(diff_ms, TimeUnit.MILLISECONDS);
            
            if (diffGiorni < sezRiassuntiva.getGiorniVisibili()) {  // 22)
                double spesa = Double.parseDouble(nuovaSpesa.getSpesa().substring(0, nuovaSpesa.getSpesa().length() - 1));
                sezRiassuntiva.aggiornaSpese((int)diffGiorni, spesa, nuovaSpesa.getCategoria());
            }
        }
        catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void cambiaGiorni (int variazione) {
        if (!sezRiassuntiva.setGiorniVisibili(variazione))      // 23)
            return;
        
        String categoria = sezRiassuntiva.getCategoria();
        double[] speseGiorni = ArchivioSpese.recuperaSpeseGiorni(intestazione.getNickname(), sezRiassuntiva.getGiorniVisibili());
        double speseCategoria = ArchivioSpese.recuperaSpeseCategoria(intestazione.getNickname(), sezRiassuntiva.getGiorniVisibili(), categoria);
        
        sezRiassuntiva.popolaSezioneRiassuntiva(speseGiorni, categoria, speseCategoria);
        
        if (variazione > 0)
            RaccoglitoreEventi.inviaEvento(intestazione.getNickname(), "INCREMENTO G");
        else
            RaccoglitoreEventi.inviaEvento(intestazione.getNickname(), "DECREMENTO G");
    }
    
    private void salvaDatiCache () {
        String nickname = intestazione.getNickname();
        int giorniVisibili = sezRiassuntiva.getGiorniVisibili();
        
        Spesa spesaInSospeso = tabellaSpese.getNuovaSpesa();
        String[] datiInSospeso = new String[]{spesaInSospeso.getData(), spesaInSospeso.getCategoria(), spesaInSospeso.getSpesa()};
        
        DatiUltimoAccesso.memorizzaDati(nickname, giorniVisibili, datiInSospeso);
        
        RaccoglitoreEventi.inviaEvento(nickname, "CHIUSURA");
    }

    public static void main(String[] args) {
        launch();
    }
}


// 1) recupero i dati per la configurazione iniziale
// 2) carico separatemente l'interfaccia riprendendola come valore di ritorno della apposita funzione
// 3) inserisco uno spazio tra le varie sezioni dell'interfaccia
// 4) separo gli elementi dal bordo della finestra
// 5) inserisco "gui" in uno scrollpane per ottenere la scrollbar automatica
// 6) espando l'interfaccia per l'intera larghezza della finestra
// 7) alla chiusura del programma, salvo in cache i dati per il prossimo accesso

// 8) recupero i dati dalla cache
// 9) nonostante faccia parte della gestione eventi, per una migliore leggibilità del codice ho preferito separare la gestione dei nuovi inserimenti

// 10) separo il popolamento della tabella e della sezione riassuntiva per poterla riutilizzare in più occasioni (es. cambio utente)
// 11) aggiungo una spesa fittizia che costituirà il modulo di inserimento nella tabella (contiene eventuali dati recuperati dalla cache)

// 12) per ogni colonna quando l'utente ha inserito un nuovo valore lo salvo e controllo se ha inserito tutti i dati richiesti
// 13) se anche gli altri campi sono stati inseriti, procedo con il salvataggio della nuova spesa

// 14) "intestazione.getNickname()" ritorna il nome del nuovo utente se c'è effettivamente stato il cambio. Stringa vuota altrimenti
// 15) popolo l'interfaccia con i dati del nuovo utente

// 16) escludo l'ultima riga da questa funzionalità
// 17) non proseguo se la categoria selezionata è la stessa di prima
// 18) aggiorno solo la riga della categoria nella tabella riassuntiva

// 19) archivio la nuova spesa
// 20) aggiorno il registro spese, inserendo opportunamente la nuova spesa (mantengo ordine cronologico)
// 21) determino i giorni di distanza con la data della nuova spesa inserita
// 22) aggiorno la sezione riassuntiva solo se la nuova spesa rientra nei giorni visibili

// 23) ritorna false se non è stato possibile modificare i giorni