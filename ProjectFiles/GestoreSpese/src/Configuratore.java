
import com.thoughtworks.xstream.*;
import java.io.*;
import java.nio.file.*;

public class Configuratore {
    private final String fileConfig;
    private final DatiConfigurazione infoConfigurazione;
    
    public Configuratore () {
        fileConfig = "./myfiles/config.xml";
        String xml = null;
        
        if ((new File(fileConfig)).length() > 0 && ValidatoreXML.valida(fileConfig)) {  // 1)
            try {
                xml = new String(Files.readAllBytes(Paths.get(fileConfig)));
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
            
            infoConfigurazione = (DatiConfigurazione) (new XStream()).fromXML(xml);
        }
        else {      // 2)
            String[] listaCategorie = new String[]{"", "abbigliamento", "casa", "cibo", "salute", "spostamenti", "svago"};
            infoConfigurazione = new DatiConfigurazione ("localhost", 3306, "root", "", "localhost", "localhost", 8080, listaCategorie);
        }
    }
    
    public String[] infoDB () {
        return infoConfigurazione.getInfoDB();
    }
    
    public String[] infoLog () {
        return infoConfigurazione.getInfoLog();
    }
    
    public String[] infoCategorie () {
        return infoConfigurazione.getInfoCategorie();
    }
}


// 1) recupero i dati dal file di configurazione solo se questo non è vuoto e se i dati hanno uno schema xml valido
// 2) se non riesco a leggere dal file di configurazione, utilizzo dei valori di default