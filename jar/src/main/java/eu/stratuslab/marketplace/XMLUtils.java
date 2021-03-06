package eu.stratuslab.marketplace;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XMLUtils {

    private static final Integer DEFAULT_INDENT = Integer.valueOf(4);

    private XMLUtils() {

    }

    public static DocumentBuilder newDocumentBuilder() {
        return newDocumentBuilder(true);
    }

    public static DocumentBuilder newDocumentBuilder(boolean validating) {

        try {

            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            dbfac.setNamespaceAware(true);
            dbfac.setValidating(validating);

            return dbfac.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document newDocument() {

        DocumentBuilder db = newDocumentBuilder();
        return db.newDocument();
    }

    public static Transformer newTransformer(boolean indented) {

        String indentFlag = (indented) ? "yes" : "no";

        try {

            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute("indent-number", DEFAULT_INDENT);

            Transformer transformer;
            transformer = factory.newTransformer();
            transformer
                    .setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, indentFlag);
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");

            return transformer;

        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String documentToString(Document doc) {
        return documentToString(doc, false);
    }

    public static Document documentFromString(String xml) throws SAXException,
            IOException {

        Reader reader = new StringReader(xml);
        InputSource source = new InputSource(reader);
        DocumentBuilder db = newDocumentBuilder(false);
        return db.parse(source);
    }

    public static String documentToString(Document doc, boolean indented) {

        try {

            Transformer transformer = XMLUtils.newTransformer(indented);

            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();

        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

    }

}
