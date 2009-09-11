/*
 * Copyright (C) 2009 Luc Boudreau
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
package org.pentaho.pat.server.data.pojo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.olap4j.OlapConnection;
import org.olap4j.query.Query;

public class Session {

	private String id = null;
	
	private Map<String,Object> variables = new ConcurrentHashMap<String, Object>();

	/**
	 * Holds the current established connections. The map
	 * key is the corresponding SavedConnection id and the
	 * value is the native connection object.
	 */
	private Map<String,OlapConnection> connections = new ConcurrentHashMap<String, OlapConnection>();
	
	private Map<String,Query> queries = new ConcurrentHashMap<String, Query>();
	
	public Session(String id) {
		this.id = id;
	}
	
	public void destroy()
	{
		
	    for (Entry<String, OlapConnection> entry : this.connections.entrySet()) {
	        try {
	            OlapConnection conn = entry.getValue();
	            if (!conn.isClosed())
	                conn.close();
	        } catch (SQLException e) {
	            // nothing here.
	        }
	    }
		
	    this.connections.clear();
		this.connections=null;
		this.variables.clear();
		this.variables = null;
		this.queries.clear();
		this.queries=null;
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public OlapConnection getConnection(String connectionId) {
		return connections.get(connectionId);
	}

	public void putConnection(String connectionId, OlapConnection connection) {
		this.connections.put(connectionId, connection);
	}

	public void closeConnection(String connectionId) {
	    OlapConnection conn = this.connections.get(connectionId);
	    try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) { }
	    this.connections.remove(connectionId);
	}
	
	public List<String> getActiveConnectionsId() {
	    List<String> conns = new ArrayList<String>();
	    conns.addAll(this.connections.keySet());
	    return conns;
	}
	
	public Map<String, Query> getQueries() {
		return queries;
	}

	public void setQueries(Map<String, Query> queries) {
		this.queries = queries;
	}
}
