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

import java.util.Locale;
import java.util.Map;

import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;

/**
 * Does X and Y and provides an abstraction for Z.
 * @created May 27, 2010 
 * @since X.Y.Z
 * @author pmac
 * 
 */
public interface PatQuery {
    
    public org.olap4j.query.Query getQuery();

    /**
     * Returns the MDX parse tree behind this Query. The returned object is
     * generated for each call to this function. Altering the returned
     * SelectNode object won't affect the query itself.
     * @return A SelectNode object representing the current query structure.
     */
    public SelectNode getSelect();

    /**
     * Returns the String representation of the MDX Query
     */
    public String getMdx();
    
    /**
     * Sets the MDX for the Query Object 
     */
    public void setMdx(String mdx);
    
    /**
     * Gets the Catalog of the Query Object
     */
    public Catalog getCatalog();
    
    public String getCatalogName();
    
    public OlapConnection getConnection();
    
    public void setConnection(OlapConnection connection);
    
    /**
     * Sets the Catalog for the Query Object
     */
    public void setCatalog(String catalogName);
    
    
    /**
     * Returns the underlying cube object that is used to query against.
     * @return The Olap4j's Cube object.
     */
    public Cube getCube();
    
    /**
     * Returns the underlying cube Name 
     * @return Cube Name String.
     */
    public String getCubeName();

    /**
     * Returns the Olap4j's Dimension object according to the name
     * given as a parameter. If no dimension of the given name is found,
     * a null value will be returned.
     * @param name The name of the dimension you want the object for.
     * @return The dimension object, null if no dimension of that
     * name can be found.
     */
    public QueryDimension getDimension(String name);

    /**
     * Swaps rows and columns axes. Only applicable if there are two axes.
     */
    public void swapAxes();
    
    public QueryAxis getAxis(Axis axis);

    /**
     * Returns a map of the current query's axis.
     * <p>Be aware that modifications to this list might
     * have unpredictable consequences.</p>
     * @return A standard Map object that represents the
     * current query's axis.
     */
    public Map<Axis, QueryAxis> getAxes();
    
    /**
     * Returns the fictional axis into which all unused dimensions are stored.
     * All dimensions included in this axis will not be part of the query.
     * @return The QueryAxis representing dimensions that are currently not
     * used inside the query.
     */
    public QueryAxis getUnusedAxis();

    /**
     * Safely disposes of all underlying objects of this
     * query.
     * @param closeConnection Whether or not to call the
     * {@link OlapConnection#close()} method of the underlying
     * connection.
     */
    public void tearDown(boolean closeConnection);

    /**
     * Safely disposes of all underlying objects of this
     * query and closes the underlying {@link OlapConnection}.
     * <p>Equivalent of calling Query.tearDown(true).
     */
    public void tearDown();
    
    /**
     * Validates the current query structure. If a dimension axis has
     * been placed on an axis but no selections were performed on it,
     * the default hierarchy and default member will be selected. This
     * can be turned off by invoking the
     * {@link PatQuery#setSelectDefaultMembers(boolean)} method.
     * @throws OlapException If the query is not valid, an exception
     * will be thrown and it's message will describe exactly what to fix.
     */
    public void validate() throws OlapException;

    /**
     * Executes the query against the current OlapConnection and returns
     * a CellSet object representation of the data.
     *
     * @return A proper CellSet object that represents the query execution
     *     results.
     * @throws OlapException If something goes sour, an OlapException will
     *     be thrown to the caller. It could be caused by many things, like
     *     a stale connection. Look at the root cause for more details.
     */
    public CellSet execute() throws OlapException;
    
    /**
     * Returns this query's name. There is no guarantee that it is unique
     * and is set at object instanciation.
     * @return This query's name.
     */
    public String getName();

    /**
     * Returns the current locale with which this query is expressed.
     * @return A standard Locale object.
     */
    public Locale getLocale();


    /**
     * Behavior setter for a query. By default, if a dimension is placed on
     * an axis but no selections are made, the default hierarchy and
     * the default member will be selected when validating the query.
     * This behavior can be turned off by this setter.
     * @param selectDefaultMembers Enables or disables the default
     * member and hierarchy selection upon validation.
     */
    public void setSelectDefaultMembers(boolean selectDefaultMembers);
    
    public boolean equals(Object obj);
    
    public int hashCode();

}
