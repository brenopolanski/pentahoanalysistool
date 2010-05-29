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
import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;
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
public class QmQuery implements Query {

    private org.olap4j.query.Query query;
    
    public QmQuery(org.olap4j.query.Query query) {
        this.query = query;
    }
    
    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#execute()
     */
    public org.olap4j.query.Query getQuery() {
        return this.query;
    }
    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#execute()
     */
    public CellSet execute() throws OlapException {
        return query.execute();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getAxes()
     */
    public Map<Axis, QueryAxis> getAxes() {
        return query.getAxes();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getAxis(org.olap4j.Axis)
     */
    public QueryAxis getAxis(Axis axis) {
        return query.getAxis(axis);
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getCatalog()
     */
    public String getCatalogName() {
        return query.getCube().getSchema().getCatalog().toString();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getCube()
     */
    public Cube getCube() {
        return query.getCube();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getDimension(java.lang.String)
     */
    public QueryDimension getDimension(String name) {
        return query.getDimension(name);
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getLocale()
     */
    public Locale getLocale() {
        return query.getLocale();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getMdx()
     */
    public String getMdx() {
        return query.getSelect().toString();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getName()
     */
    public String getName() {
        return query.getName();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getSelect()
     */
    public SelectNode getSelect() {
        return query.getSelect();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#getUnusedAxis()
     */
    public QueryAxis getUnusedAxis() {
        return query.getUnusedAxis();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#setCatalog(java.lang.String)
     */
    public void setCatalog(String catalogName) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#setMdx()
     */
    public void setMdx() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#setSelectDefaultMembers(boolean)
     */
    public void setSelectDefaultMembers(boolean selectDefaultMembers) {
        query.setSelectDefaultMembers(selectDefaultMembers);
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#swapAxes()
     */
    public void swapAxes() {
        query.swapAxes();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#tearDown(boolean)
     */
    public void tearDown(boolean closeConnection) {
        query.tearDown(closeConnection);
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#tearDown()
     */
    public void tearDown() {
        query.tearDown();

    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.util.serializer.Query#validate()
     */
    public void validate() throws OlapException {
        query.validate();
    }
    public String getCubeName() {
        return getCube().getUniqueName();
    }

}
