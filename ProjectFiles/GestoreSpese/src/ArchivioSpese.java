
import java.sql.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class ArchivioSpese {
    private static String indirizzoDB;
    private static String userDB;
    private static String passwordDB;
    
    public static void configura (String[] infoDB) {
        indirizzoDB = "jdbc:mysql://" + infoDB[0] + ":" + infoDB[1] + "/gestorespese";  // 1)
        userDB = infoDB[2];
        passwordDB = infoDB[3];
    }
    
    public static List<Spesa> recuperaSpeseRecenti(String utente) {      // 2)
        List<Spesa> speseRecenti = new ArrayList<>();
        
        try (Connection co = DriverManager.getConnection(indirizzoDB, userDB, passwordDB);
                PreparedStatement ps = co.prepareStatement("SELECT data, categoria, spesa FROM spese WHERE nickname = ? ORDER BY data DESC LIMIT 10");  // 3)
            ) {
            ps.setString(1, utente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String data = new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("data"));    // 4)
                String categoria = rs.getString("categoria");
                String spesa = rs.getDouble("spesa") + "€";
                
                speseRecenti.add(0, new Spesa(data, categoria, spesa));     // 5)
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return speseRecenti;
    }
    
    public static double[] recuperaSpeseGiorni(String utente, int giorni) {
        double[] speseGiorni = new double[giorni];
        Arrays.fill(speseGiorni, 0);    // 6)
        
        try (Connection co = DriverManager.getConnection(indirizzoDB, userDB, passwordDB);
                PreparedStatement ps = co.prepareStatement("SELECT DATEDIFF(CURRENT_DATE, data) AS differenza, SUM(spesa) AS somma FROM spese "
                                + "WHERE nickname = ? AND data > DATE_SUB(CURRENT_DATE, INTERVAL ? DAY) GROUP BY data");    // 7)
            ) {
            ps.setString(1, utente);
            ps.setInt(2, giorni);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int indice = (giorni - rs.getInt("differenza")) - 1;    // 8)
                speseGiorni[indice] = rs.getDouble("somma");
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return speseGiorni;
    }
    
    public static double recuperaSpeseCategoria(String utente, int giorni, String categoria) {
        try (Connection co = DriverManager.getConnection(indirizzoDB, userDB, passwordDB);
                PreparedStatement ps = co.prepareStatement("SELECT SUM(spesa) AS somma FROM spese "
                        + "WHERE nickname = ? AND data > DATE_SUB(CURRENT_DATE, INTERVAL ? DAY) AND categoria = ?");
            ) {
            ps.setString(1, utente);
            ps.setInt(2, giorni);
            ps.setString(3, categoria);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next())
                return rs.getDouble("somma");
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return 0;
    }
    
    public static void archiviaSpesa (String utente, Spesa nuovaSpesa) {
        DateTimeFormatter formatoDataSpesa = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(nuovaSpesa.getData(), formatoDataSpesa);       // 9)
        
        double spesa = Double.parseDouble(nuovaSpesa.getSpesa().substring(0, nuovaSpesa.getSpesa().length() - 1));  // 10)
        
        try (Connection co = DriverManager.getConnection(indirizzoDB, userDB, passwordDB);
                PreparedStatement ps = co.prepareStatement("INSERT INTO spese(nickname, data, categoria, spesa) VALUES (?, ?, ?, ?)");
            ) {
            ps.setString(1, utente);
            ps.setString(2, data.toString());
            ps.setString(3, nuovaSpesa.getCategoria());
            ps.setDouble(4, spesa);
            
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}


// 1) salvo direttamente l'indirizzo completo per non doverlo ricostruire ogni volta

// 2) metodo per recuperare le spese recenti per il popolamento della tabella "RegistroSpese"
// 3) recupero le spese più recenti dell'utente, per un massimo di 10 righe
// 4) modifico il formato della data, poiché non mi piace quello impostato da mysql
// 5) inserisco gli elementi dalla cima per rispettare l'ordine del registro

// 6) inizializzo l'array con tutti zeri, come se non ci fossero state spese
// 7) recupero la somma delle spese giornaliere degli ultimi giorni insieme alla differenza tra il giorno corrente e il giorno della spesa
// 8) utilizzo la differenza fra i giorni per inserire i dati nell'array, ordinandoli per data crescente

// 9) trasformo la data nel formato richiesto da mysql
// 10) dalla spesa presente nell'oggetto devo eliminare l'ultimo carattere ("€"), per poterlo castare

