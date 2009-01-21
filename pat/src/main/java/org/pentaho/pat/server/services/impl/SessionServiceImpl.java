package org.pentaho.pat.server.services.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.olap4j.OlapConnection;
import org.pentaho.pat.server.data.ConnectionManager;

public class SessionServiceImpl implements
		org.pentaho.pat.server.services.SessionService {

	Logger log = Logger.getLogger(this.getClass());
	
	private ConnectionManager connectionManager;

	
	public void setConnectionManager(ConnectionManager manager) {
		this.connectionManager = manager;
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


	public String createNewQuery(String guid) {
		// TODO Auto-generated method stub
		return null;
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

}
