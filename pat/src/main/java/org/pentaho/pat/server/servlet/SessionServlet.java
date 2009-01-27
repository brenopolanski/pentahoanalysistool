package org.pentaho.pat.server.servlet;

import java.util.List;

import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends GenericServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;
	
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}

	public Boolean connect(String guid, String driverClassName, String url, 
		String username, String password) {
		
		return this.sessionService.connect(
				guid, driverClassName, url, username, password);
		
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
		this.sessionService.disconnect(guid);
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
	
	

	public Boolean closeSession(String guid) {
		// TODO Auto-generated method stub
		return null;
	}

	public String createSession(String guid) {
		// TODO Auto-generated method stub
		return null;
	}

}
