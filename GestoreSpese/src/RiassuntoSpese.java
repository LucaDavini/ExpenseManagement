
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

public class RiassuntoSpese extends TableView {
    private final ObservableList<TotaleSpesa> riassunto;
    private String categoriaSelezionata;    // 1)
    
    public RiassuntoSpese () {
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        setFixedCellSize(38);
        setPrefHeight(38*3);    // 2)
        setSelectionModel(null);
        
        TableColumn colCategoria = new TableColumn("CATEGORIA");
        TableColumn colTotale = new TableColumn("TOTALE");
        
        colCategoria.setCellValueFactory(new PropertyValueFactory("categoria"));
        colTotale.setCellValueFactory(new PropertyValueFactory("totale"));
        
        colCategoria.setSortable(false);
        colTotale.setSortable(false);
        
        riassunto = FXCollections.observableArrayList();
        
        getColumns().addAll(colCategoria, colTotale);
        setItems(riassunto);
    }
    
    public void popolaTabella (double spesaTotale, String categoria, double spesaCategoria) {
        categoriaSelezionata = categoria;
        
        riassunto.clear();
        riassunto.addAll(
                new TotaleSpesa("TOTALE", spesaTotale + "€"),
                new TotaleSpesa(categoria, spesaCategoria + "€")
        );
    }
    
    public void aggiungiSpesa (double spesa, String categoria) {
        String totSpesaAttuale = riassunto.get(0).getTotale().substring(0, riassunto.get(0).getTotale().length() - 1);  // 3)
        double nuovaSpesaTotale = Double.parseDouble(totSpesaAttuale) + spesa;
        
        riassunto.remove(0);
        riassunto.add(0, new TotaleSpesa("TOTALE", nuovaSpesaTotale + "€"));
        
        if (categoria.equals(categoriaSelezionata) && !categoria.equals("")) {    // 4)
            String totaleSpesaCategoria = riassunto.get(1).getTotale().substring(0, riassunto.get(1).getTotale().length() - 1); // 3)
            double nuovaSpesaCategoria  = Double.parseDouble(totaleSpesaCategoria) + spesa;
            
            riassunto.remove(1);
            riassunto.add(new TotaleSpesa(categoria, nuovaSpesaCategoria + "€"));
        }
    }
    
    public void aggiornaCategoria (String categoria, double spesaCategoria) {
        categoriaSelezionata = categoria;
        
        riassunto.remove(1);
        riassunto.add(new TotaleSpesa(categoria, spesaCategoria + "€"));
    }
    
    public String getCategoria () {
        return categoriaSelezionata;
    }
}


// 1) "categoriaSelezionata" velocizza il recupero della categoria visualizzata nella tabella riassuntiva per metodi che ne hanno bisogno

// 2) fisso l'altezza della tabella che sarà mantenuta sempre costante

// 3) per poter recuperare l'intero, devo eliminare il carattere "€" in fondo alla stringa
// 4) se la categoria della nuova spesa inserita corrisponde a quella nella tabella, procedo con l'aggiornamento