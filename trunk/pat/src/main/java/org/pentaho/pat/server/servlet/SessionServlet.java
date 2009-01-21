package org.pentaho.pat.server.servlet;

import java.util.List;

import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends GenericServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;

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
	
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}

}
