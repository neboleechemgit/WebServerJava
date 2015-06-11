import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 6/7/2015.
 */
public final class WebConfigXML {

    private static final String configLocation =
            System.getProperty("user.dir") + File.separator
                    + "config" + File.separator + "config.xml";

    public static Map<String, String> ReadXMLFile() throws IOException, SAXException, ParserConfigurationException {

        File fXmlFile = new File(configLocation);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc =  dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getChildNodes();

        NodeList nNode = nList.item(0).getChildNodes();

        Map<String, String> configMap = new HashMap<String, String>();

        for (int i = 0; i < nNode.getLength(); i++) {

            Node node = nNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                configMap.put(node.getNodeName(), node.getTextContent());
//                System.out.println(node.getNodeName() + " " + node.getTextContent());
            }
        }

        return configMap;

    }
}
