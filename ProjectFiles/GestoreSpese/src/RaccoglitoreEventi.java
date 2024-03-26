
import com.thoughtworks.xstream.*;
import java.io.*;
import java.net.*;

public class RaccoglitoreEventi {
    private static String ipClient;
    private static String ipServerLog;
    private static int portaServerLog;
    
    public static void configura (String[] infoLog) {
        ipClient = infoLog[0];
        ipServerLog = infoLog[1];
        portaServerLog = Integer.parseInt(infoLog[2]);
    }
    
    public static void inviaEvento (String nickname, String tipoEvento) {
        try (Socket sock = new Socket(ipServerLog, portaServerLog);
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            ) {
            EventoGUI nuovoEvento = new EventoGUI(ipClient, nickname, tipoEvento);
            
            XStream streamXML = new XStream();
            streamXML.useAttributeFor(EventoGUI.class, "nomeApp");      // 1)
            streamXML.useAttributeFor(EventoGUI.class, "ipClient");
            streamXML.useAttributeFor(EventoGUI.class, "timestamp");
            streamXML.useAttributeFor(EventoGUI.class, "nickname");
            streamXML.useAttributeFor(EventoGUI.class, "tipoEvento");
            String eventoXML = streamXML.toXML(nuovoEvento);

            dos.writeUTF(eventoXML);    // 2)
        }
        catch (IOException e) {
            System.out.println("Connessione con ServerLog non riuscita");
            System.err.println(e.getMessage());
        }
    }
}


// 1) faccio serializzare gli attributi di EventoGUI come attributi, anziché come elementi
// 2) trasferisco al server di log una stringa in formato XML che descrive l'evento catturato