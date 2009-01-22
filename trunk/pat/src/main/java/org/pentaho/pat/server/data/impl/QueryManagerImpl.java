package org.pentaho.pat.server.data.impl;

import java.sql.SQLException;

import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.pentaho.pat.server.data.ConnectionManager;
import org.pentaho.pat.server.data.QueryCache;
import org.pentaho.pat.server.data.QueryManager;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;

public class QueryManagerImpl implements QueryManager {

	private ConnectionManager connectionManager;
	
	private QueryCache queryCache;

	private SessionService sessionService;

	private DiscoveryService discoveryService;

	public String create(String userId, String cubeName) throws OlapException {
		Query query;
		try {
			query = new Query(cubeName, this.discoveryService.getCube(
				userId, this.sessionService.getCurrentCube(userId)));
		} catch (SQLException e) {
			throw new OlapException(e.getMessage(),e);
		}
		String queryId = this.queryCache.put(query.getSelect());
		this.sessionService.setCurrentQuery(userId, queryId);
		return null;
	}

	public CellSet execute(String userId, String queryId) throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	public Query get(String userId, String queryId) throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	public void release(String userId, String queryId) {
		// TODO Auto-generated method stub

	}
	
	public void setConnectionManager(ConnectionManager manager) {
		this.connectionManager = manager;
	}

	public void setQueryCache(QueryCache cache) {
		this.queryCache = cache;
	}
	
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}
	
	public void setDiscoveryService(DiscoveryService service) {
		this.discoveryService = service;
	}

}
