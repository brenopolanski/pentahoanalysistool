package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends AbstractServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;
	
	public void init() throws ServletException {
		super.init();
		sessionService = (SessionService)applicationContext.getBean("sessionService");
	}

	public Boolean connect(String sessionId, String driverClassName, String url, 
		String username, String password) {
		try {
			sessionService.createConnection(getCurrentUserId(),sessionId, 
				driverClassName, url, username, password);
			return true;
		} catch (OlapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String createNewQuery(String sessionId) {
		try {
			return sessionService.createNewQuery(getCurrentUserId(), sessionId);
		} catch (OlapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
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
	
	

	public Boolean closeSession(String sessionId) {
		sessionService.releaseSession(getCurrentUserId(), sessionId);
		return true;
	}

	public String createSession() {
		return sessionService.createNewSession(getCurrentUserId());
	}

}
