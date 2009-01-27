package org.pentaho.pat.server.servlet;

import java.util.List;

import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends GenericServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;

	public Boolean connect(String driverClassName, String url, 
		String username, String password) {
		
		return this.sessionService.connect(
			getUserId(), driverClassName, url, username, password);
		
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
		this.sessionService.disconnect(getUserId());
		return true;
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

	public Boolean closeSession(String guid) {
		// TODO Auto-generated method stub
		return null;
	}

	public String createSession() {
		// TODO Auto-generated method stub
		return null;
	}

}
