
import java.io.*;
import java.net.*;

public class GestoreEventi {
    private static final int portaRicezione = 8080;
    private static final String fileLog = "./myfiles/log.xml";
    
    public static void main(String[] args) {
        try (ServerSocket servsock = new ServerSocket(portaRicezione);
            ) {
            System.out.println("Server attivo:");
            
            while (true) {  // 1)
                try (Socket sock = servsock.accept();
                        DataInputStream dis = new DataInputStream(sock.getInputStream());
                    ) {
                    String eventoXML = dis.readUTF();
                    
                    
                    if (ValidatoreXMLLog.valida(eventoXML))     // 2)
                        logEvento(eventoXML);
                    else
                        System.out.println("Schema XML non rispettato:\n" + eventoXML);
                }
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void logEvento (String eventoXML) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileLog, true));  // 3)
            ) {
            dos.writeUTF(eventoXML + "\n");
            
            System.out.println(eventoXML);  // 4)
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    } 
}


// 1) con un ciclo infinito permetto al server di accettare più richieste di connessione
// 2) inserisco la stringa nel log solo se rispetta lo schema xml

// 3) apro lo stream con scrittura in append
// 4) mostro in console l'evento loggato