package org.pentaho.pat.server.util.serializer;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.olap4j.Axis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.SortOrder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class QueryDeserializer {

    private static PatQuery query;
    private static Document dom;
    private static Query qm;
    private static String mdx;
    private static OlapConnection connection;

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
        PatQuery returnQuery = null;
        if (qm != null) {
            returnQuery = createQmQuery();
        } else if (dom.getElementsByTagName("MDX") != null) {
            NodeList mdxq = dom.getElementsByTagName("MDX");
            returnQuery = createMdxQuery();
        }



        return null;

    }

    private static PatQuery createMdxQuery() {
        // TODO Auto-generated method stub
        return null;
    }

    private static PatQuery createQmQuery() throws QueryParseException {

        NodeList queryElement = dom.getElementsByTagName("Query");
        if (queryElement != null && queryElement.getLength() != 1) {
            Node qnode = queryElement.item(0);

            Node nameAttr = qnode.getAttributes().getNamedItem("name");
            String queryName = null;
            if (nameAttr != null && StringUtils.isNotBlank(nameAttr.getNodeValue())) {
                queryName = nameAttr.getNodeValue();
            }


            Node qnodeAttr = qnode.getAttributes().getNamedItem("cube");
            String cubeName = null;
            if (qnodeAttr != null && StringUtils.isNotBlank(qnodeAttr.getNodeValue())) {
                cubeName = qnodeAttr.getNodeValue();
            }


            if (!StringUtils.isNotBlank(cubeName)) {
                throw new QueryParseException("Cube for query not defined");
            }

            Node catAttr = qnode.getAttributes().getNamedItem("catalog");
            String catalogName = null;
            if (catAttr != null && StringUtils.isNotBlank(catAttr.getNodeValue())) {
                catalogName = qnodeAttr.getNodeValue();
            }

            try {
                qm = createEmptyQuery(queryName,catalogName, cubeName);
                manipulateQuery();
            } catch (OlapException e) {
                throw new QueryParseException(e.getMessage(),e);
            }


        }
        else {
            throw new QueryParseException("Cannot parse Query Model: Query node not found and/or more than 1 Query node found");
        }

        return null;
    }

    private static void manipulateQuery() throws OlapException {
        moveDims2Axis();
        
        
    }

    private static void moveDims2Axis() throws OlapException {
        NodeList axes = dom.getElementsByTagName("Axes");
        assert axes.getLength() == 1;
        NodeList axis = axes.item(0).getChildNodes();
        for (int i = 0; i < axis.getLength();i++) {
            String location = null;
            Node attrNode = axis.item(i).getAttributes().getNamedItem("location");
            if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                location = attrNode.getNodeValue();
            }
            
            QueryAxis qAxis = query.getAxes().get(getAxisName(location));
            
            String nonEmpty = null;
            attrNode = axis.item(i).getAttributes().getNamedItem("nonEmpty");
            if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                nonEmpty = attrNode.getNodeValue();
                qAxis.setNonEmpty(Boolean.getBoolean(nonEmpty));
            }
            
            String sortOrder = null;
            attrNode = axis.item(i).getAttributes().getNamedItem("sortOrder");
            if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                sortOrder = attrNode.getNodeValue();
            }
            
            String sortEvaluationLiteral = null;
            attrNode = axis.item(i).getAttributes().getNamedItem("sortEvaluationLiteral");
            if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                sortEvaluationLiteral = attrNode.getNodeValue();
            }

            if (StringUtils.isNotBlank(sortOrder)) {
                if (StringUtils.isNotBlank(sortEvaluationLiteral)) {
                    qAxis.sort(SortOrder.valueOf(sortOrder),sortEvaluationLiteral);
                }
                else {
                    qAxis.sort(SortOrder.valueOf(sortOrder));
                }
            }

            NodeList dimensions = axis.item(i).getChildNodes().item(0).getChildNodes();
            for (int k = 0; k < dimensions.getLength();k++) {
                
                processDimension(dimensions, location, nonEmpty, sortOrder, sortEvaluationLiteral);
            }
        }
        
    }

    private static void processDimension(NodeList dimensions, String location, String nonEmpty, String sortOrder,
            String sortEvaluationLiteral) {
        String name = null;
        for (int z = 0; z < dimensions.getLength(); z++) {
//             Node attrNode = dimensions.item(z).getAttributes().getNamedItem("sortOrder");
//            if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
//                sortEvaluationLiteral = attrNode.getNodeValue();
//            }
    
        }
    }

    private static Query createEmptyQuery(String queryName, String catalogName, String cubeName) throws OlapException {
        if (!StringUtils.isNotBlank(catalogName)) {
            try {
                connection.setCatalog(catalogName);
            } catch (SQLException e) {
                throw new OlapException(e.getMessage(),e);
            }
        }

        Cube cube = null;
        final NamedList<Catalog> catalogs = connection.getCatalogs();
        for (int k = 0; k < catalogs.size(); k++) {
            final NamedList<Schema> schemas = catalogs.get(k).getSchemas();
            for (int j = 0; j < schemas.size(); j++) {
                final NamedList<Cube> cubes = schemas.get(j).getCubes();
                final Iterator<Cube> iter = cubes.iterator();
                while (iter.hasNext() && cube == null) {
                    final Cube testCube = iter.next();
                    if (testCube.getName().equals(cubeName)) {
                        cube = testCube;
                    }
                }
            }
        }
        if (cube != null) {
            try {
                Query q = new Query(queryName,cube);
                return q;
            } catch (SQLException e) {
                throw new OlapException("Error creating query :" + queryName,e);
            }
        }
        else
            throw new OlapException("No Cube with name: " + cubeName + " found");

    }

    public static Axis.Standard getAxisName(String location) {
        if(location != null) {
            return org.olap4j.Axis.Standard.valueOf(location);
        }
        return null;
        
    }
    

}
