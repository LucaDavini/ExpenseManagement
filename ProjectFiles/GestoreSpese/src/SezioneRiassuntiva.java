
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SezioneRiassuntiva extends HBox {
    private final DiagrammaSpese diagSpese;
    private final RiassuntoSpese tabRiassuntiva;
    private final Label testoGiorni;
    private final Button btnIncrementa;
    private final Button btnDecrementa;
    private int giorniVisibili;
    
    public SezioneRiassuntiva (int giorni) {
        if (giorni < 1)
            giorniVisibili = 7;     // 1)
        else
            giorniVisibili = giorni;
        
        NumberAxis asseX = new NumberAxis();
        NumberAxis asseY = new NumberAxis();
        diagSpese = new DiagrammaSpese(asseX, asseY);
        
        tabRiassuntiva = new RiassuntoSpese();
        
        testoGiorni = new Label("Giorni visibili: " + giorniVisibili);
        btnIncrementa = new Button("+");
        btnDecrementa = new Button("-");
        HBox testo = new HBox(testoGiorni, btnIncrementa, btnDecrementa);
        testo.setSpacing(20);
        testo.setAlignment(Pos.CENTER);
        
        VBox bloccoDx = new VBox(tabRiassuntiva, testo);
        bloccoDx.setSpacing(40);
        bloccoDx.setPadding(new Insets(50, 0, 50, 0));
        
        getChildren().addAll(diagSpese, bloccoDx);
        
        setAlignment(Pos.CENTER);
        setSpacing(150);
    }
    
    public void popolaSezioneRiassuntiva (double[] speseGiorni, String categoria, double speseCategoria) {
        diagSpese.popolaDiagramma(speseGiorni);
        
        double totaleSpese = 0;
        for (double spesa : speseGiorni)
            totaleSpese += spesa;
        
        tabRiassuntiva.popolaTabella(totaleSpese, categoria, speseCategoria);
    }
    
    public boolean setGiorniVisibili(int variazione) {  // 2)
        int giorni = giorniVisibili + variazione;
        
        if (giorni < 1 || giorni > 100)     // 3)
            return false;
        
        giorniVisibili = giorni;
        testoGiorni.setText("Giorni visibili: " + giorniVisibili);
        
        return true;
    }
    
    public void aggiornaCategoria (String categoria, double spesaCategoria) {   // 4)
        tabRiassuntiva.aggiornaCategoria(categoria, spesaCategoria);
    }
    
    public void aggiornaSpese (int diffGiorni, double spesa, String categoria) {
        diagSpese.aggiungiSpesa(diffGiorni, spesa);
        tabRiassuntiva.aggiungiSpesa(spesa, categoria);
    }
    
    public int getGiorniVisibili () {
        return giorniVisibili;
    }
    
    public String getCategoria () {
        return tabRiassuntiva.getCategoria();
    }
    
    public Button getIncrementa () {
        return btnIncrementa;
    }
    
    public Button getDecrementa () {
        return btnDecrementa;
    }
}


// 1) utilizzo 7 come valore di default per i giorni visibili

// 2) metodo chiamato a seguito della pressione dei bottoni "+" o "-", che restituisce l'esito dell'operazione
// 3) imposto un'intervallo per i giorni visibili, oltre il quale impedisco la modifica

// 4) "aggiornaCategoria()" costituisce un metodo di passaggio tra "GestoreSpese", che gestisce l'evento, e "RiassuntoSpese", che ne subisce le conseguenze