
import java.text.*;
import java.util.*;
import javafx.beans.binding.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.util.*;

public class RegistroSpese extends TableView<Spesa> {
    private final ObservableList<String> listaCategorie;
    private final ObservableList<Spesa> speseRecenti;
    
    public RegistroSpese (String[] categorie) {
        listaCategorie = FXCollections.observableArrayList(Arrays.asList(categorie));
        speseRecenti = FXCollections.observableArrayList();
        
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        
        TableColumn colData = new TableColumn("DATA");
        TableColumn colCategoria = new TableColumn("CATEGORIA");
        TableColumn colSpesa = new TableColumn("SPESA");
        
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colSpesa.setCellValueFactory(new PropertyValueFactory<>("spesa"));
                
        getColumns().addAll(colData, colCategoria, colSpesa);
        setItems(speseRecenti);
        
        colData.setSortable(false);         // 1)
        colCategoria.setSortable(false);
        colSpesa.setSortable(false);
        
        impostaEditabilita();    // 2)
    }
    
    public void popolaRegistro (List<Spesa> listaSpeseRecenti) {
        boolean cambioAltezza = (speseRecenti.size() != listaSpeseRecenti.size());
        
        speseRecenti.clear();
        
        speseRecenti.addAll(listaSpeseRecenti);
        
        if (cambioAltezza) {
            setFixedCellSize(38);       // 3)
            prefHeightProperty().bind(fixedCellSizeProperty().multiply(Bindings.size(getItems()).add(1)));
            minHeightProperty().bind(prefHeightProperty());
            maxHeightProperty().bind(prefHeightProperty());
        }
        
        impostaEditabilita();   // 4)
    }
    
    public Spesa getNuovaSpesa () {     // 5)
        return (Spesa) getItems().get(speseRecenti.size() - 1);
    }
    
    public void scorriRighe () {
        Spesa nuovaSpesa = getNuovaSpesa();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        
        int index = 0;
        for (; index < speseRecenti.size() - 1; index++) {
            try {
                if (formato.parse(nuovaSpesa.getData()).before(formato.parse(getItems().get(index).getData())))     // 6)
                    break;
            }
            catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        }
        
        speseRecenti.remove(speseRecenti.size() - 1);   // 7)
        speseRecenti.add(index, nuovaSpesa);
        
        if (speseRecenti.size() == 11)  // 8)
            speseRecenti.remove(0);
        
        speseRecenti.add(new Spesa("", "", ""));    // 9) 
        
        impostaEditabilita();   // 4)
    }
    
    private void impostaEditabilita () {
        setEditable(true);
        
        TableColumn[] colonne = new TableColumn[3];
        for (int i = 0; i < 3; i++)
            colonne[i] = getColumns().get(i);
        
        colonne[0].setCellFactory(col -> {             // 10)
            Callback<TableColumn<Spesa, String>, TableCell<Spesa, String>> tipoInserimento = TextFieldTableCell.forTableColumn();
            
            TableCell<Spesa, String> cella = tipoInserimento.call((TableColumn<Spesa, String>)col);
            cella.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cella.getTableRow();
                if (row.getIndex() != speseRecenti.size() - 1)
                    cella.setEditable(false);
            });
            
            return cella ;
        });
        colonne[1].setCellFactory(col -> {        // 10)
            Callback<TableColumn<Spesa, String>, TableCell<Spesa, String>> tipoInserimento = ChoiceBoxTableCell.forTableColumn(listaCategorie);
            
            TableCell<Spesa, String> cella = tipoInserimento.call((TableColumn<Spesa, String>)col);
            cella.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cella.getTableRow();
                if (row.getIndex() != speseRecenti.size() - 1)
                    cella.setEditable(false);
            });
            
            return cella ;
        });
        colonne[2].setCellFactory(col -> {            // 10)
            Callback<TableColumn<Spesa, String>, TableCell<Spesa, String>> tipoInserimento = TextFieldTableCell.forTableColumn();
            
            TableCell<Spesa, String> cella = tipoInserimento.call((TableColumn<Spesa, String>)col);
            cella.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cella.getTableRow();
                if (row.getIndex() != speseRecenti.size() - 1)
                    cella.setEditable(false);
            });
            
            return cella ;
        });
    }
}


// 1) evito che l'utente possa modificare l'ordine delle righe
// 2) metodo privato per separare dal costruttore la gestione dell'editabilità delle celle della tabella

// 3) modifico l'altezza della tabella ogni volta che cambia la lunghezza della lista, in modo che non si visualizzino righe vuote aggiunte di default
// 4) l'editabilità del'ultima riga si perde con la modifica della lista e deve essere quindi reimpostata

// 5) utility per recuperare i nuovi valori inseriti dall'utente nella tabella

// 6) usando "before" riesco a inserire la nuova data come più recente tra quelle uguali
// 7) tolgo la nuova spesa per riaggiungerla nella posizione corretta
// 8) non voglio mostrare più di 10 righe nel registro, quindi, se arrivo al limite, scorro, eliminando la spesa meno recente
// 9) riaggiungo il modulo di inserimento come spesa vuota

// 10) per ogni colonna rendo editabile solo l'ultima riga, attribuendogli un elemento per inserire nuovi dati