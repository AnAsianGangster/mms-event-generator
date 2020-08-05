import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CsvGenerator {

    private static final String targetString = "SCADA_Q2C_Sync_Activated";
    private static final String csvFilename = "./user_configuration_test.csv";


    private static String getDomainItemID(Node node){
        String serverName = "";
        String deviceName = "";
        String nodeName = "";
        String attributeFC = "";
        String objectText = "";
        String attributeName = "";

        Node currentNode = node;

        while(currentNode.getNodeName() != "DataAttributes"){
            currentNode = currentNode.getParentNode();
        }
        NodeList dataAttributesList = currentNode.getChildNodes();
        for(int i = 0; i < dataAttributesList.getLength(); i++){
            Node dataAttributesNode = dataAttributesList.item(i);
            if(dataAttributesNode.getNodeName() == "AttributeFC"){
                attributeFC = dataAttributesNode.getTextContent();
            } else if (dataAttributesNode.getNodeName() == "AttributeName") {
                attributeName = dataAttributesNode.getTextContent();
            }
            dataAttributesNode = null;
        }
        dataAttributesList = null;

        while(currentNode.getNodeName() != "DataObject"){
            currentNode = currentNode.getParentNode();
        }
        NodeList dataObjectList = currentNode.getChildNodes();
        for(int i = 0; i < dataObjectList.getLength(); i++){
            Node dataObjectNode = dataObjectList.item(i);
            if(dataObjectNode.getNodeName() == "ObjectText"){
                objectText = dataObjectNode.getTextContent();
            }
            dataObjectNode = null;
        }
        dataObjectList = null;

        while(currentNode.getNodeName() != "LogicalNode"){
            currentNode = currentNode.getParentNode();
        }
        NodeList logicalNodeList = currentNode.getChildNodes();
        for(int i = 0; i < logicalNodeList.getLength(); i++){
            Node logicalNodeNode = logicalNodeList.item(i);
            if(logicalNodeNode.getNodeName() == "NodeText"){
                nodeName = logicalNodeNode.getTextContent();
            }
            logicalNodeNode = null;
        }
        logicalNodeList = null;


        while(currentNode.getNodeName() != "LogicalDevice"){
            currentNode = currentNode.getParentNode();
        }
        NodeList logicalDeviceList = currentNode.getChildNodes();
        for(int i = 0; i < logicalDeviceList.getLength(); i++){
            Node logicalDeviceNode = logicalDeviceList.item(i);
            if(logicalDeviceNode.getNodeName() == "DeviceName"){
                deviceName = logicalDeviceNode.getTextContent();
            }
            logicalDeviceNode = null;
        }
        logicalDeviceList = null;

        while(currentNode.getNodeName() != "Server"){
            currentNode = currentNode.getParentNode();
        }
        NodeList serverList = currentNode.getChildNodes();
        for(int i = 0; i < serverList.getLength(); i++){
            Node serverNode = serverList.item(i);
            if(serverNode.getNodeName() == "ServerName"){
                serverName = serverNode.getTextContent();
            }
            serverNode = null;
        }
        serverList = null;

        return serverName + deviceName + "_" + nodeName + "$" + attributeFC + "$" + objectText + "$" + attributeName;
    }

    private static String getIndexDataType(Node node){
        String index = "";
        String dataType = "";

        List<String> dataAttributesStringList = new ArrayList<String>();

        Node currentNode = node;

        while (currentNode.getNodeName() != "DataAttributes") {
            currentNode = currentNode.getParentNode();
        }

        NodeList dataAttributesList = currentNode.getChildNodes();
        for (int i = 0; i < dataAttributesList.getLength(); i++) {
            Node dataAttributesNode = dataAttributesList.item(i);
            if(dataAttributesNode.getNodeName() == "AttributeType"){
                dataType = dataAttributesNode.getTextContent();
            }
            dataAttributesNode = null;
        }
        dataAttributesList = null;

        currentNode = currentNode.getParentNode();
        dataAttributesList = currentNode.getChildNodes();
        for (int i = 0; i < dataAttributesList.getLength(); i++) {
            Node dataAttributesNode = dataAttributesList.item(i);
            if (dataAttributesNode.getNodeName() != "#text") {
                if(dataAttributesNode.getNodeName() == "DataAttributes"){
                    NodeList nodeList = dataAttributesNode.getChildNodes();
                    for(int j = 0; j < nodeList.getLength(); j++){
                        Node dataNode = nodeList.item(j);
                        if(dataNode.getNodeName() == "AttributeText"){
                            dataAttributesStringList.add(dataNode.getTextContent());
                        }
                    }
                };
            }
            dataAttributesNode = null;
        }
        dataAttributesList = null;

        for (String string : dataAttributesStringList) {
            if(string.contains(targetString)){
                index = String.valueOf(dataAttributesStringList.indexOf(string));
            }
        }

        // System.out.println(dataAttributesStringList);

        return "," + index + "," + dataType;
    }

    public static void main(String[] args) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("icd.xml");

            NodeList attributeTextList = document.getElementsByTagName("AttributeText");

            String csvLine = "";
            String domainItemID = "";
            String indexDataType = "";

            for(int i = 0; i < attributeTextList.getLength(); i++){
                Node node = attributeTextList.item(i);
                String attributeText = node.getTextContent();
                if(attributeText.contains(targetString)){
                    // System.out.println("[raw nodes] -->" + attributeText);
                    Node grandParentNode = node.getParentNode().getParentNode();
                    String grandParentNodeName = grandParentNode.getNodeName();
                    // System.out.println("grandParentNodeName -->" + grandParentNodeName);
                    if(grandParentNodeName == "DataObject"){
                        // System.out.println("[none data attribute] -->" + attributeText);
                        domainItemID = getDomainItemID(node);
                    } else if (grandParentNodeName == "DataAttributes"){
                        indexDataType = getIndexDataType(node);
                    };
                }
            }

            csvLine = domainItemID + indexDataType;

            FileWriter csvFileWriter = new FileWriter(csvFilename);
            PrintWriter csvOutputStream = new PrintWriter(csvFileWriter);
            csvOutputStream.println("item_id,index,data_type");
            csvOutputStream.println(csvLine);
            csvOutputStream.close();

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}