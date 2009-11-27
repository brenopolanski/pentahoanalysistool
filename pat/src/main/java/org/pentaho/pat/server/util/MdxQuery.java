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
package org.pentaho.pat.server.util;

import java.sql.SQLException;

import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;

/**
 * Helper Class for mdx query pairing with a connection
 * 
 * @created Oct 16, 2009
 * @since 0.5.1
 * @author Paul Stoellberger
 * 
 */
public class MdxQuery {
    private OlapConnection connection = null;

    private String catalogName = null;

    private String mdx = null;

    private String id = null;

    public String getId() {
        return id;
    }

    public MdxQuery(String id, OlapConnection connection, String catalogName) {
        this.connection = connection;
        this.catalogName = catalogName;
        this.mdx = null;
        this.id = id;
    }

    public MdxQuery(String id, OlapConnection connection, String catalogName, String mdx) {
        this.connection = connection;
        this.catalogName = catalogName;
        this.mdx = mdx;
        this.id = id;
    }

    public OlapConnection getconnection() {
        return connection;
    }

    public void setconnection(OlapConnection connection) {
        this.connection = connection;
    }

    public String getMdx() {
        return mdx;
    }

    public void setMdx(String mdx) {
        this.mdx = mdx;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public CellSet execute() throws OlapException {
        OlapStatement stmt;
        try {
            if (this.catalogName != null) {
                this.connection.setCatalog(catalogName);
            }
        } catch (SQLException e) {
            throw new OlapException("Error setting catalog for MDX statement: '" + catalogName + "'");
        }

        stmt = connection.createStatement();
        if (mdx != null && mdx.length() > 0)
            return stmt.executeOlapQuery(mdx);

        throw new OlapException("Can't execute blank or empty query");
    }

}
