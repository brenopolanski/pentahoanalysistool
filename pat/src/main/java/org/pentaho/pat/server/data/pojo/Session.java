package org.pentaho.pat.server.data.pojo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.olap4j.OlapConnection;
import org.olap4j.query.Query;

public class Session {

	private String id = null;
	
	private Map<String,String> variables = new ConcurrentHashMap<String, String>();

	private OlapConnection connection = null;
	
	private Map<String,Query> queries = new ConcurrentHashMap<String, Query>();
	
	
	public void destroy()
	{
		// TODO code what'S needed to release a session properly.
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
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
