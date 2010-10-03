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

import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.MdxParser;
import org.olap4j.mdx.parser.MdxValidator;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;

/**
 * Implementation of PatQuery interface for MDX Queries
 * @created May 27, 2010 
 * @since 0.8
 * @author Paul Stoellberger
 * 
 */
public class MdxQuery2 implements PatQuery {

    private String catalog;
    private OlapConnection connection;
    private String name;
    private SelectNode select;
    private String mdx;
    private MdxParser mdxParser;
    
    public MdxQuery2(OlapConnection connection, String catalog) {
        this(UUID.randomUUID().toString(),connection,catalog);
    }
    
    public MdxQuery2(String name, OlapConnection connection, String catalog) {
        this(name,connection,catalog,null);
    }
    
    public MdxQuery2(String name, OlapConnection connection, String catalog, String mdx) {
        this.name = name;
        this.connection = connection;
        this.catalog = catalog;
        this.mdx = mdx;
        if (this.connection != null)
        {
            this.mdxParser = connection.getParserFactory().createMdxParser(connection);
            if (mdx != null) {
                select = mdxParser.parseSelect(mdx);
            }
        }
        else
            throw new RuntimeException("connection/mdx statement of mdx query "+ name + " can not be null");
    }
    
    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#execute()
     */
    public Query getQuery() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#execute()
     */
    public CellSet execute() throws OlapException {
        OlapStatement stmt;
        try {
            if (this.catalog != null) {
                this.connection.setCatalog(catalog);
            }
        } catch (SQLException e) {
            throw new OlapException("Error setting catalog for MDX statement: '" + catalog + "'");
        }

        stmt = connection.createStatement();
        if (mdx != null && mdx.length() > 0) {
            validate();
            return stmt.executeOlapQuery(mdx);
        }
            throw new OlapException("Can't execute blank or empty query");
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getAxes()
     */
    public Map<Axis, QueryAxis> getAxes() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getAxis(org.olap4j.Axis)
     */
    public QueryAxis getAxis(Axis axis) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getCatalog()
     */
    public Catalog getCatalog() {
        throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getCatalogName()
     */
    public String getCatalogName() {
        return this.catalog;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getCube()
     */
    public Cube getCube() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getDimension(java.lang.String)
     */
    public QueryDimension getDimension(String name) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getLocale()
     */
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getMdx()
     */
    public String getMdx() {
        return this.mdx;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getName()
     */
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getSelect()
     */
    public SelectNode getSelect() {
        return this.select;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getUnusedAxis()
     */
    public QueryAxis getUnusedAxis() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#setCatalog(java.lang.String)
     */
    public void setCatalog(String catalogName) {
        this.catalog = catalogName;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#setMdx()
     */
    public void setMdx(String mdx) {
        this.mdx = mdx;
        if (StringUtils.isNotBlank(mdx)) {
            this.select = mdxParser.parseSelect(mdx);
        }
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#setSelectDefaultMembers(boolean)
     */
    public void setSelectDefaultMembers(boolean selectDefaultMembers) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#swapAxes()
     */
    public void swapAxes() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#tearDown(boolean)
     */
    public void tearDown(boolean closeConnection) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#tearDown()
     */
    public void tearDown() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#validate()
     */
    public void validate() throws OlapException {
        MdxValidator mdxValidator =
            connection.getParserFactory().createMdxValidator(connection);
        mdxValidator.validateSelect(select);
    }
    public String getCubeName() {
        throw new UnsupportedOperationException();
    }

    public OlapConnection getConnection() {
        return this.connection;
    }

    public void setConnection(OlapConnection connection) {
        this.connection = connection;
        
    }

}
