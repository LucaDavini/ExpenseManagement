
import javax.xml.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

public class ValidatoreXMLLog {
    private static final String schema = "./myfiles/schemaLog.xsd";
    
    public static boolean valida (String eventoLog) {  // 1)
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            Document d = db.parse(new InputSource(new StringReader(eventoLog)));
            Schema s = sf.newSchema(new StreamSource(new File(schema)));
            
            s.newValidator().validate(new DOMSource(d));
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            if (e instanceof SAXException)
                System.out.println("Errore di validazione (ValidatoreXMLLog)");
            
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
}


// 1) valida secondo lo schema XML definito la stringa ricevuta come parametro