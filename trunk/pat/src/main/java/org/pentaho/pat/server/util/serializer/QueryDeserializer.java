package org.pentaho.pat.server.util.serializer;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.olap4j.OlapConnection;
import org.olap4j.query.Query;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class QueryDeserializer {
    
    private PatQuery query;
    private static Document dom;
    private Query qm;
    private String mdx;
    private OlapConnection connection;
    
    public static Query unparse(String xml, OlapConnection connection) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new Exception("error creating document config");
        }

        dom = db.parse((new InputSource(new StringReader(xml))));
        
        NodeList qm = dom.getElementsByTagName("QueryModel");
        if (qm != null) {
            connection.getCatalogs();
        }
        
        
        
        return null;
        
    }
    

}
