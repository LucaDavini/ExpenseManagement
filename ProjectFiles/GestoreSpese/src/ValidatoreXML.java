
import javax.xml.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

public class ValidatoreXML {
    private static final String schema = "./myfiles/schemaConfig.xsd";
    
    public static boolean valida (String fileConfig) {  // 1)
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            Document d = db.parse(new File(fileConfig));
            Schema s = sf.newSchema(new StreamSource(new File(schema)));
            
            s.newValidator().validate(new DOMSource(d));
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            if (e instanceof SAXException)
                System.out.println("Errore di validazione (ValidatoreXML)");
            
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
}


// 1) valida secondo lo schema XML definito il contenuto del file ricevuto come parametro