package org.pentaho.pat.server.servlet;

import java.util.List;

import org.olap4j.OlapException;
import org.pentaho.pat.Constants;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends AbstractServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;
	
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (this.sessionService==null)
			throw new Exception("A sessionService is required.");
	}

	public Boolean connect(String sessionId, String driverClassName, String url, 
		String username, String password) throws OlapException {
		this.sessionService.createConnection(getCurrentUserId(),sessionId, 
			driverClassName, url, username, password);
		return true;
	}

	public String createNewQuery(String sessionId) throws OlapException {
		return this.sessionService.createNewQuery(getCurrentUserId(), sessionId);
	}

	public Boolean deleteQuery(String sessionId, String queryId) {
		this.sessionService.releaseQuery(getCurrentUserId(), sessionId, queryId);
		return true;
	}

	public Boolean disconnect(String sessionId) {
		this.sessionService.releaseConnection(getCurrentUserId(),sessionId);
		return true;
	}

	public String getCurrentCube(String sessionId) {
		return this.sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_CUBE_NAME);
	}

	public String getCurrentQuery(String sessionId) {
		return this.sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_QUERY_NAME);
	}

	public List<String> getQueries(String sessionId) {
		return this.sessionService.getQueries(getCurrentUserId(), sessionId);
	}

	public Boolean setCurrentCube(String sessionId, String cubeId) {
		this.sessionService.saveUserSessionVariable(getCurrentUserId(), 
			sessionId, Constants.CURRENT_CUBE_NAME, cubeId);
		return true;
	}

	public Boolean setCurrentQuery(String sessionId, String queryId) {
		this.sessionService.saveUserSessionVariable(getCurrentUserId(), 
				sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		return true;
	}
	
	

	public Boolean closeSession(String sessionId) {
		this.sessionService.releaseSession(getCurrentUserId(), sessionId);
		return true;
	}

	public String createSession() {
		return this.sessionService.createNewSession(getCurrentUserId());
	}

}
