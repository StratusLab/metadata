package eu.stratuslab.marketplace.metadata;

import static eu.stratuslab.marketplace.metadata.MetadataNamespaceContext.SLREQ_NS_URI;

import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.XMLSignature;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("restriction")
public final class ValidateXMLSignature {

    private ValidateXMLSignature() {

    }

    public static String validate(Document doc) {

        boolean isValid = true;

        Map<String, String> endorserInfo = getEndorserInfo(doc);

        Node signature = extractSignature(doc);

        Object[] result = MetadataUtils.isSignatureOK(endorserInfo, signature);

        isValid = ((Boolean) result[0]).booleanValue();
        String message = (String) result[1];

        if (isValid) {
            return message;
        } else {
            throw new MetadataException(message);
        }
    }

    private static Map<String, String> getEndorserInfo(Document doc) {

        Map<String, String> info = new HashMap<String, String>();

        String[] names = { "email", "subject", "issuer" };

        for (String name : names) {
            String value = extractTextContent(doc, name);
            if (name != null) {
                info.put(name, value);
            }
        }

        return info;
    }

    private static Node extractSignature(Document doc) {
        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS,
                "Signature");
        Node node = null;

        if (nl.getLength() > 0) {
            node = nl.item(0);
        } else {
            throw new MetadataException("no signature");
        }

        return node;
    }

    private static String extractTextContent(Document doc, String name) {
        NodeList nl = doc.getElementsByTagNameNS(SLREQ_NS_URI, name);
        if (nl.getLength() > 0) {
            return nl.item(0).getTextContent();
        }
        return null;
    }
}
