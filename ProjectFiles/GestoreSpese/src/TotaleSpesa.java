
import javafx.beans.property.*;

public class TotaleSpesa {
    private final SimpleStringProperty categoria;
    private final SimpleStringProperty totaleSpesa;
    
    public TotaleSpesa (String categoria, String totale) {
        if (categoria.equals("")) {     // 1)
            categoria = "selez. riga tab";
            totale = "";
        }
        
        this.categoria = new SimpleStringProperty(categoria);
        totaleSpesa = new SimpleStringProperty(totale);
    }
    
    // GETTERS
    public String getCategoria () {
        return categoria.get();
    }
    
    public String getTotale () {
        return totaleSpesa.get();
    }
}


// 1) se non ricevo una categoria, inserisco dei valori di default