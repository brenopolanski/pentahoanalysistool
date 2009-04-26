package org.pentaho.pat.server.data.pojo;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.olap4j.OlapConnection;
import org.olap4j.query.Query;

public class Session {

	private String id = null;
	
	private Map<String,Object> variables = new ConcurrentHashMap<String, Object>();

	private OlapConnection connection = null;
	
	private Map<String,Query> queries = new ConcurrentHashMap<String, Query>();
	
	public Session(String id) {
		this.id = id;
	}
	
	public void destroy()
	{
		try {
			if (this.connection!=null&&
				!this.connection.isClosed())
				this.connection.close();
		} catch (SQLException e) {
			// nothing here.
		}
		this.connection=null;
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

	public OlapConnection getConnection() {
		return connection;
	}

	public void setConnection(OlapConnection connection) {
		this.connection = connection;
	}

	public Map<String, Query> getQueries() {
		return queries;
	}

	public void setQueries(Map<String, Query> queries) {
		this.queries = queries;
	}

	
	
}
