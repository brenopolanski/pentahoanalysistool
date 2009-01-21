package org.pentaho.pat.server.data.impl;

import java.security.InvalidKeyException;

import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.pentaho.pat.server.data.ConnectionManager;
import org.pentaho.pat.server.data.QueryCache;
import org.pentaho.pat.server.data.QueryManager;

public class QueryManagerImpl implements QueryManager {

	private ConnectionManager connectionManager;
	
	private QueryCache queryCache;

	public String create(String connectionId, String cubeName)
			throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	public CellSet execute(String queryGuid) throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	public Query get(String queryGuid) throws InvalidKeyException,
			OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	public void release(String queryGuid) {
		// TODO Auto-generated method stub

	}
	
	public void setConnectionManager(ConnectionManager manager) {
		this.connectionManager = manager;
	}

	public void setQueryCache(QueryCache cache) {
		this.queryCache = cache;
	}

}
