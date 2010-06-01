package org.pentaho.pat.server.util.serializer;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;


public class QueryDeserializer {

    private static PatQuery query;
    private static Document dom;
    private static Query qm;
    private static String xml;
    private static OlapConnection connection;
    private static InputSource source;
    final private static XPath xpath = XPathFactory.newInstance().newXPath();
    
    public static PatQuery unparse(String xml, OlapConnection connection) throws Exception {
        
        QueryDeserializer.connection = connection;
        QueryDeserializer.xml = xml;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new Exception("error creating document config");
        }
        
        source = new InputSource((new ByteArrayInputStream(xml.getBytes())));
        

        dom = db.parse((new InputSource(new StringReader(xml))));

        NodeList qm = dom.getElementsByTagName("QueryModel");
        PatQuery returnQuery = null;
        if (qm != null) {
            returnQuery = createQmQuery();
            return returnQuery;
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
        if (queryElement != null) {
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
                return new QmQuery(connection,qm);
                
            } catch (OlapException e) {
                throw new QueryParseException(e.getMessage(),e);
            }


        }
        else {
            throw new QueryParseException("Cannot parse Query Model: Query node not found and/or more than 1 Query node found");
        }


    }

    private static void manipulateQuery() throws OlapException {
        moveDims2Axis();
        
        
    }

    private static void moveDims2Axis() throws OlapException {
        NodeList axis = dom.getElementsByTagName("Axis");
        for (int i = 0; i < axis.getLength();i++) {
            if (axis.item(i).getNodeType() == Node.ELEMENT_NODE) {
            String location = null;
            Node attrNode = axis.item(i).getAttributes().getNamedItem("location");
            if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                location = attrNode.getNodeValue();
            }
            else 
                throw new OlapException("Location for Axis Element can't be null");
            
            QueryAxis qAxis = qm.getAxes().get(getAxisName(location));
            
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
            
            String searchPath = "/Query/QueryModel/Axes/Axis[@location=\"" + location + "\"]//Dimension";
            System.out.println(searchPath);
            NodeList dimensions = null;
            try {
                dimensions = (NodeList) xpath.evaluate(searchPath, dom, XPathConstants.NODESET);
                processDimension(dimensions, location);
            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

           
            }
        }
        
    }

    private static void processDimension(NodeList dimensions, String location) throws OlapException {

        for (int z = 0; z < dimensions.getLength(); z++) {
             Node attrNode = dimensions.item(z).getAttributes().getNamedItem("name");
            if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                String dimName = attrNode.getNodeValue();
                dimName = dimName.substring(1,dimName.length() - 1);
                System.out.println("dimName:"  + dimName);
                QueryDimension dim = qm.getDimension(dimName);
                
                if (dim == null)
                    throw new OlapException("Dimension not found:" + dimName);
                
                String sortOrder = null;
                attrNode = dimensions.item(z).getAttributes().getNamedItem("sortOrder");
                if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                    sortOrder = attrNode.getNodeValue();
                    dim.sort(SortOrder.valueOf(sortOrder));
                }
                
                String hierarchizeMode = null;
                attrNode = dimensions.item(z).getAttributes().getNamedItem("hierarchizeMode");
                if (attrNode != null && StringUtils.isNotBlank(attrNode.getNodeValue())) {
                    hierarchizeMode = attrNode.getNodeValue();
                    dim.setHierarchizeMode(HierarchizeMode.valueOf(hierarchizeMode));
                }
                
                qm.getAxes().get(Axis.Standard.valueOf(location)).getDimensions().add(dim);
                
                String searchPath = "//Dimension[@name = \"[" + dimName + "]\"]/Inclusions";
                System.out.println(searchPath);
                try {
                    NodeList dimChildren =  (NodeList) xpath.evaluate(searchPath, dimensions.item(z), XPathConstants.NODESET);
                    for (int k = 0; k < dimChildren.getLength();k++) {
                        String name = ((Node) xpath.evaluate("//@member", dimChildren.item(k), XPathConstants.NODE)).getNodeValue();
//                        name = name.substring(1,name.length() - 1);
                        String operator = ((Node) xpath.evaluate("//@operator", dimChildren.item(k), XPathConstants.NODE)).getNodeValue();
                        String memberDim = ((Node) xpath.evaluate("//@dimension", dimChildren.item(k), XPathConstants.NODE)).getNodeValue();
                        dim.include(Selection.Operator.valueOf(operator), memberDim, name);
                    }
                } catch (XPathExpressionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                for (int h = 0; h < dimChildren.getLength(); h++) {
//                    if ()
//                    processInclusions(dim,)
//                }
            }
            else 
                throw new OlapException("No Dimension name defined");
    
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
                    if (testCube.getUniqueName().equals(cubeName)) {
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
