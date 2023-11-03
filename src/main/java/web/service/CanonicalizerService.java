package web.service;


import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.c14n.Canonicalizer;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

import static org.apache.xml.security.c14n.Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS;


public class CanonicalizerService {

    private static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            " <doc>\n" +
            "  <a xmlns=\"\">\n" +
            "   <b xmlns=\"\">\n" +
            "    <c xmlns=\"https://www.chilkatsoft.com\">\n" +
            "     <c2 xmlns=\"https://www.chilkatsoft.com\">\n" +
            "      <d xmlns=\"\">\n" +
            "       <e xmlns=\"\"> </e>\n" +
            "      </d>\n" +
            "     </c2>\n" +
            "    </c>\n" +
            "   </b>\n" +
            "  </a>\n" +
            " </doc>";

    public static void main(String[] args) throws Exception {
//        System.out.println(canon(XML));
        Document document = convertStringToDocument("C:\\proj\\java\\canonicalizer\\file1.xml");
//        Document signDocument = sign(document);

//        System.out.println(convertDocumentToString(signDocument));
        String signValue = canon2("C:\\proj\\java\\canonicalizer\\file2.xml");



        System.out.println(signValue);


    }


    public static String canon2(String path) throws Exception {
        org.apache.xml.security.Init.init();
        Document document = convertStringToDocument("C:\\proj\\java\\canonicalizer\\file1.xml");
        String string = convertDocumentToString(document);


        try (ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
            Canonicalizer canonicalizer = Canonicalizer.getInstance(ALGO_ID_C14N_OMIT_COMMENTS);
            canonicalizer.canonicalize(string.getBytes(), buf, false);
            string = buf.toString();
        }

        return string;
    }

    public static Document convertStringToDocument(final String filename) throws ParserConfigurationException, TransformerConfigurationException {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        try {


            InputStream inputStream = new FileInputStream(filename);
            document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
        } catch (Exception e) {

        }
        convertDocumentToString(document);
        return document;
    }

    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }


}
