public class DatiConfigurazione {
    private final String ipDB;
    private final int portaDB;
    private final String userDB;
    private final String passwordDB;
    
    private final String ipClient;
    private final String ipServerLog;
    private final int portaServerLog;
    
    private final String[] listaCategorie;
    
    public DatiConfigurazione (String ipDB, int portaDB, String userDB, String passwordDB, String ipClient, 
            String ipServerLog, int portaServerLog, String[] listaCategorie) {
        this.ipDB = ipDB;
        this.portaDB = portaDB;
        this.userDB = userDB;
        this.passwordDB = passwordDB;
        this.ipClient = ipClient;
        this.ipServerLog = ipServerLog;
        this.portaServerLog = portaServerLog;
        this.listaCategorie = listaCategorie;
    }
    
    public String[] getInfoDB () {          // 1)
        return new String[]{ipDB, Integer.toString(portaDB), userDB, passwordDB};
    }
    
    public String[] getInfoLog () {         // 1)
        return new String[]{ipClient, ipServerLog, Integer.toString(portaServerLog)};
    }
    
    public String[] getInfoCategorie () {   // 1)
        return listaCategorie;
    }
}


// 1) permetto di recuperare i dati suddivisi in gruppi