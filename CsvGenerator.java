import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CsvGenerator {
    // colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // target file to write to
    private static final String csvFilename = "./user_configuration_test.csv";

    private static String getIcdDomainItemID(Node node) {
        String serverName = "";
        String deviceName = "";
        String nodeName = "";
        String attributeFC = "";
        String objectText = "";
        String attributeName = "";

        Node currentNode = node;

        while (currentNode.getNodeName() != "DataAttributes") {
            currentNode = currentNode.getParentNode();
        }
        NodeList dataAttributesList = currentNode.getChildNodes();
        for (int i = 0; i < dataAttributesList.getLength(); i++) {
            Node dataAttributesNode = dataAttributesList.item(i);
            if (dataAttributesNode.getNodeName() == "AttributeFC") {
                attributeFC = dataAttributesNode.getTextContent();
            } else if (dataAttributesNode.getNodeName() == "AttributeName") {
                attributeName = dataAttributesNode.getTextContent();
            }
            dataAttributesNode = null;
        }
        dataAttributesList = null;

        while (currentNode.getNodeName() != "DataObject") {
            currentNode = currentNode.getParentNode();
        }
        NodeList dataObjectList = currentNode.getChildNodes();
        for (int i = 0; i < dataObjectList.getLength(); i++) {
            Node dataObjectNode = dataObjectList.item(i);
            if (dataObjectNode.getNodeName() == "ObjectText") {
                objectText = dataObjectNode.getTextContent();
            }
            dataObjectNode = null;
        }
        dataObjectList = null;

        while (currentNode.getNodeName() != "LogicalNode") {
            currentNode = currentNode.getParentNode();
        }
        NodeList logicalNodeList = currentNode.getChildNodes();
        for (int i = 0; i < logicalNodeList.getLength(); i++) {
            Node logicalNodeNode = logicalNodeList.item(i);
            if (logicalNodeNode.getNodeName() == "NodeText") {
                nodeName = logicalNodeNode.getTextContent();
            }
            logicalNodeNode = null;
        }
        logicalNodeList = null;

        while (currentNode.getNodeName() != "LogicalDevice") {
            currentNode = currentNode.getParentNode();
        }
        NodeList logicalDeviceList = currentNode.getChildNodes();
        for (int i = 0; i < logicalDeviceList.getLength(); i++) {
            Node logicalDeviceNode = logicalDeviceList.item(i);
            if (logicalDeviceNode.getNodeName() == "DeviceName") {
                deviceName = logicalDeviceNode.getTextContent();
            }
            logicalDeviceNode = null;
        }
        logicalDeviceList = null;

        while (currentNode.getNodeName() != "Server") {
            currentNode = currentNode.getParentNode();
        }
        NodeList serverList = currentNode.getChildNodes();
        for (int i = 0; i < serverList.getLength(); i++) {
            Node serverNode = serverList.item(i);
            if (serverNode.getNodeName() == "ServerName") {
                serverName = serverNode.getTextContent();
            }
            serverNode = null;
        }
        serverList = null;

        return serverName + deviceName + "_" + nodeName + "$" + attributeFC + "$" + objectText + "$" + attributeName;
    }

    private static String getIcdIndexDataType(Node node, String targetString) {
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
            if (dataAttributesNode.getNodeName() == "AttributeType") {
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
                if (dataAttributesNode.getNodeName() == "DataAttributes") {
                    NodeList nodeList = dataAttributesNode.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node dataNode = nodeList.item(j);
                        if (dataNode.getNodeName() == "AttributeText") {
                            dataAttributesStringList.add(dataNode.getTextContent());
                        }
                    }
                }
                ;
            }
            dataAttributesNode = null;
        }
        dataAttributesList = null;

        for (String string : dataAttributesStringList) {
            if (string.contains(targetString)) {
                index = String.valueOf(dataAttributesStringList.indexOf(string));
            }
        }

        // System.out.println(dataAttributesStringList);

        return "," + index + "," + dataType;
    }

    private static String getConfigDomainItemID(Node node) {
        String serverName = "";
        String deviceName = "";
        String nodeName = "";
        String attributeFC = "";
        String objectText = "";
        String attributeName = "";

        Node parentNode = node.getParentNode();
        NodeList CDataAttributeList = parentNode.getChildNodes();
        for(int i = 0; i < CDataAttributeList.getLength(); i++){
            Node CDataAttributeNode = CDataAttributeList.item(i);
            if(CDataAttributeNode.getNodeName() != "#text"){
                Element CDataAttributeElement = (Element) CDataAttributeNode;
                String propertyName = CDataAttributeElement.getAttribute("Name");
                if(propertyName.equals("FunctionBlockName")){
                    String propertyVal = CDataAttributeElement.getAttribute("Val");
                    // System.out.println(propertyVal);
                    String[] firstSplit = propertyVal.split("_");
                    String[] secondSplit = firstSplit[1].split("\\.");
                    // System.out.println(secondSplit[1]);
                    serverName = secondSplit[0];
                    deviceName = secondSplit[1];
                    nodeName = firstSplit[2];
                    attributeFC = firstSplit[3];
                    objectText = firstSplit[4];
                    attributeName = firstSplit[5];
                }
            }
        }

        return serverName + deviceName + "_" + nodeName + "$" + attributeFC + "$" + objectText + "$" + attributeName;
    }

    private static String getConfigIndexDataType(Node node, String targeString){
        String index = "";
        String dataType = "";

        List<String> dataAttributesStringList = new ArrayList<String>();

        Node parentNode = node.getParentNode();
        // System.out.println(parentNode.getNodeName());
        NodeList CDataAttributeList = parentNode.getChildNodes();
        for(int i = 0; i < CDataAttributeList.getLength(); i++){
            Node CDataAttributeNode = CDataAttributeList.item(i);
            if(CDataAttributeNode.getNodeName() != "#text"){
                Element CDataAttributeElement = (Element) CDataAttributeNode;
                // System.out.println(CDataAttributeNode.getNodeName());
                if(CDataAttributeElement.getAttribute("Name").equals("BasicType")){
                    dataType = CDataAttributeElement.getAttribute("Val");
                }
            }
        }

        Node currentNode = node;
        while (currentNode.getNodeName() != "CLogicalNode") {
            currentNode = currentNode.getParentNode();
        }

        // Node node = someChildNodeFromDifferentDocument;
        DocumentBuilderFactory NodeFactory = DocumentBuilderFactory.newInstance();
        NodeFactory.setNamespaceAware(true);
        DocumentBuilder nodeBuilder;
        try {
            nodeBuilder = NodeFactory.newDocumentBuilder();
            Document nodeDocument = nodeBuilder.newDocument();
            Node importedNode = nodeDocument.importNode(currentNode, true);
            nodeDocument.appendChild(importedNode);
            NodeList CDataObjectList = nodeDocument.getElementsByTagName("Property");
            for (int i = 0; i < CDataObjectList.getLength(); i++) {
                Node CDataObjectNode = CDataObjectList.item(i);
                // System.out.println(i);
                if (CDataObjectNode.getNodeName() != "#text") {
                    Element CDataObjectElement = (Element) CDataObjectNode;
                    if (CDataObjectElement.getAttribute("Name").equals("ObjectVariable")) {
                        dataAttributesStringList.add(CDataObjectElement.getAttribute("Val"));
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // System.out.println(dataAttributesStringList);

        index = String.valueOf(dataAttributesStringList.indexOf(targeString));

        return "," + index + "," + dataType;
    }

    public static boolean ParseDOM(String targetConfigurationFile) {
        // configuration file input stream
        File inputFile = new File(targetConfigurationFile);
        Scanner inputStream;

        try {
            inputStream = new Scanner(inputFile);

            // icd.xml DOM
            DocumentBuilderFactory icdFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder icdBuilder = icdFactory.newDocumentBuilder();
            Document icdDocument = icdBuilder.parse("icd.xml");
            //  target NodeList
            NodeList icdAttributeTextList = icdDocument.getElementsByTagName("AttributeText");
            
            // configuration.xml DOM
            DocumentBuilderFactory configFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder configBuilder = configFactory.newDocumentBuilder();
            Document configDocument = configBuilder.parse("configuration.xml");
            // target NodeList
            NodeList configAttributeTextList = configDocument.getElementsByTagName("Property");

            // file writer 
            FileWriter csvFileWriter = new FileWriter(csvFilename);
            PrintWriter csvOutputStream = new PrintWriter(csvFileWriter);
            csvOutputStream.println("item_id,index,data_type");

            // each line
            String csvLine;
            String domainItemID = "";
            String indexDataType = "";

            // configuration file first line
            String attributeNames = inputStream.nextLine();

            while (inputStream.hasNext()) {
                String attribute = inputStream.nextLine();

                System.out.println(ANSI_RED + "[RUNNING]" + ANSI_RESET + " scanning for " + ANSI_YELLOW + attribute + ANSI_RESET);

                // scanning icd DOM
                for (int i = 0; i < icdAttributeTextList.getLength(); i++) {
                    Node node = icdAttributeTextList.item(i);
                    String attributeText = node.getTextContent();
                    if (attributeText.contains(attribute)) {
                        // System.out.println("[raw nodes] -->" + attributeText);
                        Node grandParentNode = node.getParentNode().getParentNode();
                        String grandParentNodeName = grandParentNode.getNodeName();
                        // System.out.println("grandParentNodeName -->" + grandParentNodeName);
                        if (grandParentNodeName == "DataObject") {
                            // System.out.println("[none data attribute] -->" + attributeText);
                            domainItemID = getIcdDomainItemID(node);
                        } else if (grandParentNodeName == "DataAttributes") {
                            indexDataType = getIcdIndexDataType(node, attribute);
                        }
                    }
                }

                // scanning configuration DOM
                for (int i = 0; i < configAttributeTextList.getLength(); i++){
                    // System.out.println(i);
                    Node node = configAttributeTextList.item(i);
                    Element element = (Element) node;
                    String propertyVal = element.getAttribute("Val");
                    if(propertyVal.equals(attribute)){
                        // System.out.println("[raw nodes] " + propertyVal);
                        Node grandParentNode = node.getParentNode().getParentNode();
                        String grandParentNodeName = grandParentNode.getNodeName();
                        if(grandParentNodeName == "CDataObject"){
                            domainItemID = getConfigDomainItemID(node);
                            indexDataType = getConfigIndexDataType(node, propertyVal);
                        }
                    }
                }

                csvLine = domainItemID + indexDataType;
                csvOutputStream.println(csvLine);
                csvLine = "";
                domainItemID = "";
                indexDataType = "";
            }

            // write to file
            csvOutputStream.close();

            // free memory
            icdFactory = null;
            icdBuilder = null;
            icdDocument = null;
            icdAttributeTextList = null;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}