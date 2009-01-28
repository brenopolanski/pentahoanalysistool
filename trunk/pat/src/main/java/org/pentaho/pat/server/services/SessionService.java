package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.query.Query;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface SessionService extends Service {

	
	
	
	
	
	
	public String createNewSession(String userId);
	
	public void releaseSession(String userId, String sessionId);
	
	
	
	
	public void saveUserSessionVariable(String userId, String sessionId, 
		String key, String value);
	
	public String getUserSessionVariable(String userId, String sessionId, 
			String key);
	
	public void deleteUserSessionVariable(String userId, String sessionId, 
		String key);
	
	
	
	
	
	public void createConnection(String userId, String sessionId, 
			String driverName, String connectStr, 
			String username, String password) throws OlapException;
	
	public OlapConnection getConnection(String userId, String sessionId);
	
	public void releaseConnection(String userId, String sessionId);
	
	
	
	
	
	public String createNewQuery(String userId, String sessionId) throws OlapException;
	
	public Query getQuery(String userId, String sessionId, String queryId);
	
	public List<String> getQueries(String userId, String sessionId);
	
	public void releaseQuery(String userId, String sessionId, String queryId);
	
	
	

}
