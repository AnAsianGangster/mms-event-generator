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

    // input file
    private static final String configurationFilename = "./mms_configuration.csv";
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

    public static void main(String[] args) {
        // configuration file input stream
        File inputFile = new File(configurationFilename);
        Scanner inputStream;

        try {
            inputStream = new Scanner(inputFile);

            // icd.xml reader
            DocumentBuilderFactory icdFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder icdBuilder = icdFactory.newDocumentBuilder();
            Document icdDocument = icdBuilder.parse("icd.xml");

            // icd DOM
            NodeList icdAttributeTextList = icdDocument.getElementsByTagName("AttributeText");

            // TODO configuration.xml reader and DOM builder

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

                csvLine = domainItemID + indexDataType;
                csvOutputStream.println(csvLine);
                csvLine = null;
                domainItemID = null;
                indexDataType = null;
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

    }
}