/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.server.util.serializer;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.olap4j.Axis;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Does X and Y and provides an abstraction for Z.
 * @created May 27, 2010 
 * @since X.Y.Z
 * @author pmac
 * 
 */
public class QuerySerializer {
    
    private PatQuery query;
    Document dom;
    
    public QuerySerializer(PatQuery query) {
        this.query = query;
    }
    
    public String createXML() throws Exception{
        if (this.query == null)
            throw new Exception("Query object can not be null");
        
        try {
            createDocument();
            createDOMTree();
            
            OutputFormat format = new OutputFormat(dom);
            format.setIndenting(true);
            format.setEncoding("UTF-8");
            format.setVersion("1.0");
            format.setLineWidth(120);
            StringWriter st = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(st,format);
            serializer.serialize(dom);
            return st.getBuffer().toString();
            
        } catch (ParserConfigurationException e) {
            throw new Exception(e.getMessage(),e);
        }
        
    }
    
    private void createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        dom = db.newDocument();
    }
    
    private void createDOMTree(){

        Element rootEle = dom.createElement("Query");
        
        if (StringUtils.isNotBlank(query.getName())) {
            rootEle.setAttribute("name", query.getName());
        }
        
        if (StringUtils.isNotBlank(query.getCubeName())) {
            rootEle.setAttribute("cube", query.getCubeName());
        }
        
        if (StringUtils.isNotBlank(query.getCatalogName())) {
            rootEle.setAttribute("catalog", query.getCatalogName());
        }
                        
        rootEle = appendQmQuery(rootEle);
        
        dom.appendChild(rootEle);

    }
    
    private Element appendQmQuery(Element rootElement) {
        
        if (this.query.getQuery() != null) {
            Element qm = dom.createElement("QueryModel");
            
            qm = appendAxes(qm);
            rootElement.appendChild(qm);
        }
        
        if (StringUtils.isNotBlank(this.query.getMdx())) {
            Element mdx = dom.createElement("MDX");

            mdx.setNodeValue(this.query.getMdx());
            rootElement.appendChild(mdx);
        }
        
        return rootElement;
    }
    
    private Element appendAxes(Element rootElement) {
        
        Element axes = dom.createElement("Axes");
        
        QueryAxis rows = this.query.getAxes().get(Axis.ROWS);
        if (rows != null) {
            Element rowsElement = createAxisElement(rows);
            axes.appendChild(rowsElement);
        }
        
        QueryAxis columns = this.query.getAxes().get(Axis.COLUMNS);
        if (columns != null) {
            Element columnsElement = createAxisElement(columns);
            axes.appendChild(columnsElement);
        }
        
        QueryAxis filters = this.query.getAxes().get(Axis.FILTER);
        if (filters != null) {
            Element filtersElement = createAxisElement(filters);
            axes.appendChild(filtersElement);
        }
        
        QueryAxis pages = this.query.getAxes().get(Axis.PAGES);
        if (pages != null) {
            Element pagesElement = createAxisElement(pages);
            axes.appendChild(pagesElement);
        }
        
        rootElement.appendChild(axes);
        return rootElement;
        
        
    }
    
    private Element createAxisElement(QueryAxis axis) {
        Element axisElement = dom.createElement("Axis");
        axisElement.setAttribute("location",getAxisName(axis));
        
//        if (axis.isNonEmpty() == true) {
            axisElement.setAttribute("nonEmpty", "" + axis.isNonEmpty());
//        }
        
        
        if (axis.getSortOrder() != null) {
            axisElement.setAttribute("sortOrder", axis.getSortOrder().toString());
        }
        
        if (StringUtils.isNotBlank(axis.getSortIdentifierNodeName())) {
            axisElement.setAttribute("sortEvaluationLiteral", axis.getSortIdentifierNodeName());
        }
        
        Element dimensions = dom.createElement("Dimensions");
        
        
        for (QueryDimension dim : axis.getDimensions()) {
            Element d = createDimensionElement(dim);
            dimensions.appendChild(d);
        }
        if (axis.getDimensions().size() > 0) {
            axisElement.appendChild(dimensions);
        }
        
        return axisElement;
    }
    
    private Element createDimensionElement(QueryDimension dim) {
        Element dimension = dom.createElement("Dimension");
        dimension.setAttribute("name", dim.getDimension().getUniqueName());
        if (dim.getSortOrder() != null) {
            dimension.setAttribute("sortOrder", dim.getSortOrder().toString());
        }
        if (dim.getHierarchizeMode() != null) {
            dimension.setAttribute("hierarchizeMode", dim.getHierarchizeMode().toString());
        }
        
        Element inclusionsElement = dom.createElement("Inclusions");
        List<Selection> inclusions = dim.getInclusions();
        
        inclusionsElement = createSelectionsElement(inclusionsElement, inclusions);
        dimension.appendChild(inclusionsElement);
        
        Element exclusionsElement = dom.createElement("Exclusions");
        List<Selection> exclusions = dim.getExclusions();
        
        inclusionsElement = createSelectionsElement(exclusionsElement, exclusions);
        dimension.appendChild(inclusionsElement);
        
        return dimension;
    }
    
    private Element createSelectionsElement(Element rootElement, List<Selection> selections) {
        for (Selection sel : selections) {
            Element selection = dom.createElement("Selection");
            if (sel.getDimension() != null)
                selection.setAttribute("dimension", sel.getDimension().getName());
            
            selection.setAttribute("member", sel.getMember().getUniqueName());
            selection.setAttribute("operator", sel.getOperator().toString());
            
            if (sel.getSelectionContext() != null && sel.getSelectionContext().size() > 0) {
                Element context = dom.createElement("Context");
                context = createSelectionsElement(context, sel.getSelectionContext());
                selection.appendChild(context);
            }
            rootElement.appendChild(selection);
        }
        return rootElement;
    }

    public String getAxisName(QueryAxis axis) {
        switch(axis.getLocation().axisOrdinal()) {
        case -1: return "FILTER";
        case  0: return "COLUMNS";
        case  1: return "ROWS";
        case  2: return "PAGES";
        case  3: return "CHAPTERS";
        case  4: return "SECTIONS";
        default: throw new RuntimeException("Unsupported Axis-Ordinal: " + axis.getLocation().axisOrdinal());
        }
    }
    

}
