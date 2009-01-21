package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.pentaho.pat.server.data.ConnectionManager;

public class SessionServiceImpl implements
		org.pentaho.pat.server.services.SessionService {

	private ConnectionManager connectionManager;

	public Boolean connect(String connectStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public String createNewQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean deleteQuery(String queryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean disconnect() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentCube() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean setCurrentCube(String cubeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean setCurrentQuery(String queryId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setConnectionManager(ConnectionManager manager) {
		this.connectionManager = manager;
	}

}
