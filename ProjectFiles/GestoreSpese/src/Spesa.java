
import javafx.beans.property.*;

public class Spesa {
    private final SimpleStringProperty data;
    private final SimpleStringProperty categoria;
    private final SimpleStringProperty spesa;
    
    public Spesa (String data, String categoria, String spesa) {
        this.data = new SimpleStringProperty(data);
        this.categoria = new SimpleStringProperty(categoria);
        this.spesa = new SimpleStringProperty(spesa);
    }
    
    // GETTERS
    public String getData () {
        return data.get();
    }
    
    public String getCategoria () {
        return categoria.get();
    }
    
    public String getSpesa () {
        return spesa.get();
    }
    
    // SETTERS      1)
    public void setData (String data) {
        this.data.set(data);
    }
    
    public void setCategoria (String categoria) {
        this.categoria.set(categoria);
    }
    
    public void setSpesa (String spesa) {
        this.spesa.set(spesa);
    }
}


// 1) utilizzo i setters per memorizzare gli inserimenti dell'utente