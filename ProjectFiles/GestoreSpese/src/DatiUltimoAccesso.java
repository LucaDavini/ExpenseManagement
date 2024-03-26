
import java.io.*;
import java.util.*;

public class DatiUltimoAccesso {
    private static final String fileCache = "./myfiles/cache.bin";
    private static final int quantiDati = 5;
    
    public static List<String> recuperaDati () {
        List<String> datiCache = new ArrayList<>();
        
        if (new File("./myfiles/cache.bin").length() > 0) {     // 1)
            try (DataInputStream dis = new DataInputStream(new FileInputStream(fileCache));
                ) {
                for (int i = 0; i < quantiDati; i++)
                    datiCache.add(dis.readUTF());
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        else {      // 2)
            datiCache.add("");
            datiCache.add("7");
            datiCache.add("");
            datiCache.add("");
            datiCache.add("");
        }
        
        return datiCache;
    }
    
    public static void memorizzaDati (String nickname, int giorniVisibili, String[] datiInSospeso) {
        List<String> daMemorizzare = new ArrayList<>();     // 3)
        
        daMemorizzare.add(nickname);
        daMemorizzare.add(Integer.toString(giorniVisibili));
        daMemorizzare.addAll(Arrays.asList(datiInSospeso));
        
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileCache));
            ) {
            for (String dato : daMemorizzare)
                dos.writeUTF(dato);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}


// 1) prima di leggere controllo che il file di cache contenga dei dati
// 2) se la cache è vuota, utilizzo valori di default

// 3) uso una lista per raggruppare i dati, in modo da rendere più leggibile il codice relativo alla scrittura sul file