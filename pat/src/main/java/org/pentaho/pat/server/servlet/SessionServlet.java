package org.pentaho.pat.server.servlet;

import java.util.List;

import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.SessionService;
import org.springframework.beans.factory.InitializingBean;

public class SessionServlet extends AbstractServlet implements Session, InitializingBean {

	private static final long serialVersionUID = 1L;
	
	private static SessionService sessionService;
	
	public void setSessionService(SessionService service) {
		sessionService = service;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (sessionService==null)
			throw new Exception("A sessionService is required.");
	}

	public Boolean connect(String sessionId, String driverClassName, String url, 
		String username, String password) throws OlapException {
		sessionService.createConnection(getCurrentUserId(),sessionId, 
			driverClassName, url, username, password);
		return true;
	}

	public String createNewQuery(String sessionId) throws OlapException {
		return sessionService.createNewQuery(getCurrentUserId(), sessionId);
	}

	public Boolean deleteQuery(String sessionId, String queryId) {
		sessionService.releaseQuery(getCurrentUserId(), sessionId, queryId);
		return true;
	}

	public Boolean disconnect(String sessionId) {
		sessionService.releaseConnection(getCurrentUserId(),sessionId);
		return true;
	}

	public String getCurrentCube(String sessionId) {
		return (String)sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_CUBE_NAME);
	}

	public String getCurrentQuery(String sessionId) {
		return (String)sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_QUERY_NAME);
	}

	public List<String> getQueries(String sessionId) {
		return sessionService.getQueries(getCurrentUserId(), sessionId);
	}

	public Boolean setCurrentCube(String sessionId, String cubeId) {
		sessionService.saveUserSessionVariable(getCurrentUserId(), 
			sessionId, Constants.CURRENT_CUBE_NAME, cubeId);
		return true;
	}

	public Boolean setCurrentQuery(String sessionId, String queryId) {
		sessionService.saveUserSessionVariable(getCurrentUserId(), 
				sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		return true;
	}
	
	public SelectNode getQuery(String sessionId, String queryId) {
		return sessionService.getQuery(getCurrentUserId(), 
				sessionId, queryId);
	}
	
	

	public Boolean closeSession(String sessionId) {
		sessionService.releaseSession(getCurrentUserId(), sessionId);
		return true;
	}

	public String createSession() {
		return sessionService.createNewSession(getCurrentUserId());
	}

}
