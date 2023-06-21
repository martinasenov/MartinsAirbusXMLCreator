package com.example.airbusxmlcreator;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.concurrent.Task;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class AirbusXMLCreator {
    public Task<Void> run(String excelPath, int columnToBeRead, String customization, String serialNumber, String dmID) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                ArrayList<String> AMMRefs = new ArrayList<>();

                XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
                XSSFSheet sheet = workbook.getSheet("Sheet1");

                int rowCount = sheet.getPhysicalNumberOfRows();

                for (int i = 1; i < rowCount; i++) {
                    String inputFilePath = sheet.getRow(i).getCell(columnToBeRead-1).getStringCellValue();
                    AMMRefs.add(inputFilePath);
                }

                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.newDocument();

                    // root element
                    Element rootElement = doc.createElement("JobCardBasket");
                    doc.appendChild(rootElement);

                    // name element
                    Element name = doc.createElement("name");
                    rootElement.appendChild(name);

                    // orientation element
                    Element orientation = doc.createElement("orientation");
                    orientation.appendChild(doc.createTextNode("portrait"));
                    rootElement.appendChild(orientation);

                    // format element
                    Element format = doc.createElement("format");
                    format.appendChild(doc.createTextNode("A4"));
                    rootElement.appendChild(format);

                    // acType element
                    Element acType = doc.createElement("acType");
                    acType.appendChild(doc.createTextNode("A318,A319,A320,A321"));
                    rootElement.appendChild(acType);

                    // customization element
                    Element customizationElement = doc.createElement("customization");
                    customizationElement.appendChild(doc.createTextNode(customization));
                    rootElement.appendChild(customizationElement);

                    // effectivity element
                    Element effectivity = doc.createElement("effectivity");
                    effectivity.appendChild(doc.createTextNode("N" + serialNumber));
                    rootElement.appendChild(effectivity);

                    // Tasks element
                    Element tasks = doc.createElement("Tasks");
                    rootElement.appendChild(tasks);

                    // here you can replace with your variable value
                    for (String AmmRef : AMMRefs) {

                        System.out.println("Processing AMM reference: " + AmmRef);

                        // Task elements
                        Element task1 = doc.createElement("Task");
                        Element dmId1 = doc.createElement("dmId");

                        if (AmmRef.length() <= 12) {
                            dmId1.appendChild(doc.createTextNode(dmID + AmmRef + "00"));
                        } else {
                            if (AmmRef.endsWith("B")) {
                                String AmmRefLastCharDeleted_B = AmmRef.substring(0, AmmRef.length() - 1);
                                dmId1.appendChild(doc.createTextNode(dmID + AmmRefLastCharDeleted_B + "10"));

                            } else if (AmmRef.endsWith("C")) {
                                String AmmRefLastCharDeleted_C = AmmRef.substring(0, AmmRef.length() - 1);
                                dmId1.appendChild(doc.createTextNode(dmID + AmmRefLastCharDeleted_C + "20"));

                            } else {
                                dmId1.appendChild(doc.createTextNode(dmID + AmmRef));
                            }
                        }

                        task1.appendChild(dmId1);

                        Element productKey1 = doc.createElement("productKey");
                        productKey1.appendChild(doc.createTextNode("[N]##" + customization + "#AMM###"));
                        task1.appendChild(productKey1);
                        Element doctype1 = doc.createElement("doctype");
                        doctype1.appendChild(doc.createTextNode("AMM"));
                        task1.appendChild(doctype1);
                        tasks.appendChild(task1);
                    }
// create the XML file
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource domSource = new DOMSource(doc);
                    StreamResult streamResult = new StreamResult(new File(excelPath + "airbusTask_package.xml"));

                    transformer.transform(domSource, streamResult);

                    System.out.println("Done creating XML File");
                    return null;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // Close any resources, if needed
                }
            }
        };
    }
}