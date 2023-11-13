package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @PostMapping("/generate-forms")
    public String generateForms(@RequestParam int numStudents, Model model) {
        model.addAttribute("numForms", numStudents);
        return "forms";
    }
    @PostMapping("/store-data")
    public String storeData(@ModelAttribute ListStudent listStudent , Model models) {
        List<Student> itemList = listStudent.getItemList();
       try {


           // Check if the file already exists

           File xmlFile = new File("data.xml");
           if (xmlFile.exists()) {
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document document = dBuilder.parse(xmlFile);

               for (int i = 0; i < itemList.size(); i++) {

                   Element root = document.getDocumentElement();
                   Element studentRecord = document.createElement("Student");

                   String studentid = itemList.get(i).getId();
                   studentRecord.setAttribute("ID", studentid);

                   Element gpa = document.createElement("GPA");
                   Text studentGpa = document.createTextNode(String.valueOf(itemList.get(i).getGpa()));
                   gpa.appendChild(studentGpa);

                   Element firstname = document.createElement("FirstName");
                   Text studentfname = document.createTextNode(itemList.get(i).getFirstName());
                   firstname.appendChild(studentfname);
                   Element lastName = document.createElement("LastName");
                   Text studentlname = document.createTextNode(itemList.get(i).getLastName());
                   lastName.appendChild(studentlname);
                   Element gender = document.createElement("Gender");
                   Text studentgender = document.createTextNode(itemList.get(i).getGender());
                   gender.appendChild(studentgender);

                   Element level = document.createElement("Level");
                   Text studentlevel = document.createTextNode(String.valueOf(itemList.get(i).getLevel()));
                   level.appendChild(studentlevel);
                   Element address = document.createElement("Address");
                   Text studentaddress = document.createTextNode(String.valueOf(itemList.get(i).getAddress()));
                   address.appendChild(studentaddress);
                   studentRecord.appendChild(firstname);
                   studentRecord.appendChild(lastName);
                   studentRecord.appendChild(gender);
                   studentRecord.appendChild(gpa);
                   studentRecord.appendChild(level);
                   studentRecord.appendChild(address);
                   //append student record in unversity

                   root.appendChild(studentRecord);
                   // Print the XML document

                   javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
                   javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
                   javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
                   javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(xmlFile);
                   transformer.transform(source, result);


               }
           }else {

               DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
               Document document = documentBuilder.newDocument();

               Element root = document.createElement("University");
               document.appendChild(root);
               for (int i = 0; i < itemList.size(); i++) {
                   Element studentRecord = document.createElement("Student");

                   String studentid = itemList.get(i).getId();
                   studentRecord.setAttribute("ID", studentid);

                   Element firstname = document.createElement("FirstName");
                   Text studentfname = document.createTextNode(itemList.get(i).getFirstName());
                   firstname.appendChild(studentfname);
                   Element lastName = document.createElement("LastName");
                   Text studentlname = document.createTextNode(itemList.get(i).getLastName());
                   lastName.appendChild(studentlname);
                   Element gender = document.createElement("Gender");
                   Text studentgender = document.createTextNode(itemList.get(i).getGender());
                   gender.appendChild(studentgender);
                   Element gpa = document.createElement("GPA");
                   Text studentGpa = document.createTextNode(String.valueOf(itemList.get(i).getGpa()));
                   gpa.appendChild(studentGpa);
                   Element level = document.createElement("Level");
                   Text studentlevel = document.createTextNode(String.valueOf(itemList.get(i).getLevel()));
                   level.appendChild(studentlevel);
                   Element address = document.createElement("Address");
                   Text studentaddress = document.createTextNode(String.valueOf(itemList.get(i).getAddress()));
                   address.appendChild(studentaddress);
                   studentRecord.appendChild(firstname);
                   studentRecord.appendChild(lastName);
                   studentRecord.appendChild(gender);
                   studentRecord.appendChild(gpa);
                   studentRecord.appendChild(level);
                   studentRecord.appendChild(address);
                   //append student record in unversity

                   root.appendChild(studentRecord);
                    }
                   TransformerFactory transformerFactory = TransformerFactory.newInstance();
                   Transformer transformer = transformerFactory.newTransformer();
                   DOMSource source = new DOMSource(document);

                   File xml2File = new File("data.xml");
                   StreamResult result = new StreamResult(xmlFile);
                   transformer.transform(source, result);

           }
       }catch(Exception e)
           {
               System.out.println("error");
               return "error";
           }

        return "search-delete";
    }

    @PostMapping("/searchById")
    public String searchById(@RequestParam String studentId, Model model) {
        List<Student> resultList = new ArrayList<>();

        try {
            File xmlFile = new File("data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            String expression = "//Student[@ID='" + studentId + "']";
            NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element studentElement = (Element) nodeList.item(i);

                Student student = new Student();
                student.setId((studentElement.getAttribute("ID")));
                student.setFirstName(studentElement.getElementsByTagName("FirstName").item(0).getTextContent());
                student.setLastName(studentElement.getElementsByTagName("LastName").item(0).getTextContent());
                student.setGender(studentElement.getElementsByTagName("Gender").item(0).getTextContent());
                student.setGpa(Double.parseDouble(studentElement.getElementsByTagName("GPA").item(0).getTextContent()));
                student.setLevel(Integer.parseInt(studentElement.getElementsByTagName("Level").item(0).getTextContent()));
                student.setAddress(studentElement.getElementsByTagName("Address").item(0).getTextContent());

                resultList.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        model.addAttribute("resultList", resultList);
        return "result";
    }



    @PostMapping("/searchByName")
    public String searchByName(@RequestParam String studentName, Model model) {
        List<Student> resultList = new ArrayList<>();

        try {
            File xmlFile = new File("data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            // Use XPath to search for students by FirstName
            String expression = "//Student[contains(FirstName, '" + studentName + "')]";
            NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element studentElement = (Element) nodeList.item(i);

                Student student = new Student();
                student.setId((studentElement.getAttribute("ID")));
                student.setFirstName(studentElement.getElementsByTagName("FirstName").item(0).getTextContent());
                student.setLastName(studentElement.getElementsByTagName("LastName").item(0).getTextContent());
                student.setGender(studentElement.getElementsByTagName("Gender").item(0).getTextContent());
                student.setGpa(Double.parseDouble(studentElement.getElementsByTagName("GPA").item(0).getTextContent()));
                student.setLevel(Integer.parseInt(studentElement.getElementsByTagName("Level").item(0).getTextContent()));
                student.setAddress(studentElement.getElementsByTagName("Address").item(0).getTextContent());

                resultList.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        model.addAttribute("resultList", resultList);
        return "result";
    }



    @PostMapping("/searchByGpa")
    public String searchByGpa(@RequestParam String studentGpa, Model model) {
        List<Student> resultList = new ArrayList<>();

        try {
            File xmlFile = new File("data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            // Use XPath to search for students by GPA
            String expression = "//Student[GPA='" + studentGpa + "']";
            NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element studentElement = (Element) nodeList.item(i);

                Student student = new Student();
                student.setId((studentElement.getAttribute("ID")));
                student.setFirstName(studentElement.getElementsByTagName("FirstName").item(0).getTextContent());
                student.setLastName(studentElement.getElementsByTagName("LastName").item(0).getTextContent());
                student.setGender(studentElement.getElementsByTagName("Gender").item(0).getTextContent());
                student.setGpa(Double.parseDouble(studentElement.getElementsByTagName("GPA").item(0).getTextContent()));
                student.setLevel(Integer.parseInt(studentElement.getElementsByTagName("Level").item(0).getTextContent()));
                student.setAddress(studentElement.getElementsByTagName("Address").item(0).getTextContent());

                resultList.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        model.addAttribute("resultList", resultList);
        return "result";
    }


    private static boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }




    private Student getStudentFromXmlElement(Element studentElement) {
        Student student = new Student();
        student.setId((studentElement.getAttribute("ID")));
        student.setFirstName(studentElement.getElementsByTagName("FirstName").item(0).getTextContent());
        student.setLastName(studentElement.getElementsByTagName("LastName").item(0).getTextContent());
        student.setGender(studentElement.getElementsByTagName("Gender").item(0).getTextContent());
        student.setGpa(Double.parseDouble(studentElement.getElementsByTagName("GPA").item(0).getTextContent()));
        student.setLevel(Integer.parseInt(studentElement.getElementsByTagName("Level").item(0).getTextContent()));
        student.setAddress(studentElement.getElementsByTagName("Address").item(0).getTextContent());
        return student;
    }


    @PostMapping("/deleteById")
    public String deleteById(@RequestParam String studentId, Model model) {
        boolean recordDeleted = false;
        boolean recordNotFound = true;

        try {
            File xmlFile = new File("data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);

            NodeList nodeList = document.getElementsByTagName("Student");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element studentElement = (Element) nodeList.item(i);

                if (studentElement.getAttribute("ID").equalsIgnoreCase(studentId)) {
                    studentElement.getParentNode().removeChild(studentElement);

                    // Save the updated XML document
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(document);
                    StreamResult result = new StreamResult(xmlFile);
                    transformer.transform(source, result);

                    recordDeleted = true;
                    recordNotFound = false;
                    break; // Exit the loop once the record is deleted
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        model.addAttribute("recordDeleted", recordDeleted);
        model.addAttribute("recordNotFound", recordNotFound);

        return "delete-result";
    }


}
