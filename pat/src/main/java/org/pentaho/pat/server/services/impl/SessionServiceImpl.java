package org.pentaho.pat.server.services.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.pentaho.pat.server.data.ConnectionManager;
import org.pentaho.pat.server.data.QueryManager;
import org.pentaho.pat.server.services.SessionService;

public class SessionServiceImpl implements
		org.pentaho.pat.server.services.SessionService {

	Logger log = Logger.getLogger(this.getClass());
	
	private ConnectionManager connectionManager;

	private SessionService sessionService;
	
	private QueryManager queryManager;

	
	public void setConnectionManager(ConnectionManager manager) {
		this.connectionManager = manager;
	}
	
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}
	
	public void setQueryManager(QueryManager manager) {
		this.queryManager = manager;
	}


	public Boolean connect(String guid, String driverClassName, String url,
			String username, String password) {
		
		OlapConnection conn = null;
		
		try {
			conn = this.connectionManager.connect(
				guid, driverClassName, url, username, password);
			if (conn != null &&
					!conn.isClosed())
			{
				if (log.isDebugEnabled())
					log.debug("Connection created successfully.");
				
				return true;
			}
			else
			{
				log.error("An error was encountered while creating a new connection.");
				return false;
			}
		}
		
		catch (SQLException e) {
			log.error(e.getMessage());
			return false;
		}
	}


	public String createNewQuery(String guid) throws OlapException {
		return this.queryManager.create(
			guid, this.sessionService.getCurrentCube(guid));
	}


	public Boolean deleteQuery(String guid, String queryId) {
		// TODO Auto-generated method stub
		return null;
	}


	public Boolean disconnect(String guid) {
		this.connectionManager.disconnect(guid);
		return true;
	}


	public String getCurrentCube(String guid) {
		// TODO Auto-generated method stub
		return null;
	}


	public String getCurrentQuery(String guid) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<String> getQueries(String guid) {
		// TODO Auto-generated method stub
		return null;
	}


	public Boolean setCurrentCube(String guid, String cubeId) {
		// TODO Auto-generated method stub
		return null;
	}


	public Boolean setCurrentQuery(String guid, String queryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getCubes(String guid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCubes(String guid, List<String> cubes) {
		// TODO Auto-generated method stub
		
	}

	public Boolean closeSession(String guid) {
		// TODO Auto-generated method stub
		return null;
	}

	public String createSession() {
		// TODO Auto-generated method stub
		return null;
	}

}
