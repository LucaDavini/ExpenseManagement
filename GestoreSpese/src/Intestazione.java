
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class Intestazione extends HBox {
    private final Label titolo;
    private final Label utente;
    private final TextField cambiaUtente;
    private final Button btnCambia;
    
    private boolean inserimentoAttivo;  // 1)
    
    public Intestazione (String nickname) {
        titolo = new Label("Gestore Spese");
        utente = new Label("di " + nickname);
        cambiaUtente = new TextField();
        btnCambia = new Button("CAMBIA");
        
        titolo.setFont(new Font("Arial", 30));
        utente.setFont(new Font("Arial", 20));
        
        VBox bloccoSx = new VBox();     // 2)
        VBox testoIntestazione = new VBox();
        VBox bloccoDx = new VBox(btnCambia);
        
        if (nickname.equals("")) {      // 3)
            testoIntestazione.getChildren().addAll(titolo, cambiaUtente);
            inserimentoAttivo = true;
        }
        else {
            testoIntestazione.getChildren().addAll(titolo, utente);
            inserimentoAttivo = false;
        }
        
        testoIntestazione.setAlignment(Pos.CENTER);
        bloccoDx.setAlignment(Pos.BOTTOM_RIGHT);
        
        getChildren().addAll(bloccoSx, testoIntestazione, bloccoDx);
        
        setAlignment(Pos.CENTER);
        setSpacing(200);
    }
    
    public String getNickname() {
        return utente.getText().substring(3);   // 4)
    }

    public String modificaIntestazione () {
        VBox testoIntestazione = (VBox) getChildren().get(1);
        
        if (inserimentoAttivo) {
            String nickname = cambiaUtente.getText();
            
            if (!nickname.equals("")) {     // 5)
                utente.setText("di " + nickname);
            
                testoIntestazione.getChildren().remove(cambiaUtente);
                testoIntestazione.getChildren().add(utente);

                inserimentoAttivo = false;
                
                return nickname;    // 6)
            }
        }
        else {
            testoIntestazione.getChildren().remove(utente);
            testoIntestazione.getChildren().add(cambiaUtente);
            
            cambiaUtente.clear();
            
            inserimentoAttivo = true;
        }
        
        return "";
    }
}


// 1) "inserimentoAttivo" mi permette di capire se nell'intestazione è presente il TextField "cambiaUtente" (true) o il Label "utente" (false)

// 2) costruisco un blocco vuoto per poter mantenere centrale il titolo dell'applicazione
// 3) se dalla cache non viene recuperato un nickname (nessun accesso precedente), mostro direttamente il campo di testo per l'inserimento del nuovo utente

// 4) per ottenere il nickname dal testo di "utente" devo eliminare "di " che precede l'effettivo nome dell'utente

// 5) non permetto di sottomettere un nomeutente vuoto
// 6) "modificaIntestazione" restituisce il nome utente del quale deve essere caricata l'intefaccia