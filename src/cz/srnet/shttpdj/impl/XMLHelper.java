/*
 * $Id: XMLHelper.java,v 1.4 2003/04/30 23:46:12 stepan Exp $
 * 
 * Copyright (C)2003 Stepan Roh &lt;src@srnet.cz&gt;
 * 
 * Free to use, free to redistribute, free to modify, NO WARRANTY.
 */
package cz.srnet.shttpdj.impl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import cz.srnet.shttpdj.GenericException;

/**
 * Utility methods for XML.
 * 
 * @author <a href="mailto:src@srnet.cz">Stepan Roh</a>
 * @version $Revision: 1.4 $ $Date: 2003/04/30 23:46:12 $
 */
public class XMLHelper {

    /**
     * Singleton.
     */
    private XMLHelper() {
        super();
    }

    /** shared factory */
    private static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

    /**
     * Parse file as XML.
     * 
     * @param f file to parse
     * @return DOM document
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document parseDocument(File f) throws IOException, ParserConfigurationException, SAXException {
        Document doc;
        DocumentBuilder builder;
        synchronized (builderFactory) {
            doc = builderFactory.newDocumentBuilder().parse(f);
        }
        return doc;
    }
    
    /**
     * Returns first DOM node of type element contained within given element.
     *  
     * @param elem element
     */
    public static Element getContainedElement(Element elem) {
        Node n = elem.getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                return e;
            }
            n = n.getNextSibling();
        }
        return null;
    }
    
    /**
     * Returns attribute value.
     * 
     * @param elem element
     * @param name attribute name
     * @param def default value
     * @param required is attribute required?
     * @return attribute value or (if <code>required</code> is <code>false</code>) default value
     * @throws GenericException if <code>required</code> is <code>true</code> and attribute is missing
     */
    public static String getAttributeValue(Element elem, String name, String def, boolean required) throws GenericException {
        if (elem.hasAttribute(name)) {
            return elem.getAttribute(name);
        } else {
            if (required) {
                throw new GenericException("Attribute " + name + " is required");
            }
        }
        return def;
    }

}

/*
 * $Log: XMLHelper.java,v $
 * Revision 1.4  2003/04/30 23:46:12  stepan
 * javadoc fixes
 *
 * Revision 1.3  2003/04/30 23:00:11  stepan
 * added javadoc
 *
 * Revision 1.2  2003/04/30 17:00:47  stepan
 * added getAttributeValue()
 *
 * Revision 1.1  2003/04/21 00:34:37  stepan
 * lot of code and libs added
 *
 */