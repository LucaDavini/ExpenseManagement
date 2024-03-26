
import java.text.*;
import java.util.*;

public class EventoGUI {
    private final String nomeApp;
    private final String ipClient;
    private final String timestamp;
    private final String nickname;
    private final String tipoEvento;
    
    public EventoGUI (String ipClient, String nickname, String tipoEvento) {
        nomeApp = "Gestore Spese";
        this.ipClient = ipClient;
        timestamp = new SimpleDateFormat("[dd/MM/ HH:mm:ss Z(z)]").format(new Date());  // 1)
        this.nickname = nickname;
        this.tipoEvento = tipoEvento;
    }
}

// 1) do al timestamp un particolare formato, contenente anche la timezone(z)