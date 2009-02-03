package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.springframework.security.annotation.Secured;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface SessionService extends Service {

	
	
	
	
	
	@Secured ({"ROLE_USER"})
	public String createNewSession(String userId);
	
	@Secured ({"ROLE_USER"})
	public void releaseSession(String userId, String sessionId);
	
	
	
	@Secured ({"ROLE_USER"})
	public void saveUserSessionVariable(String userId, String sessionId, 
		String key, String value);
	
	@Secured ({"ROLE_USER"})
	public String getUserSessionVariable(String userId, String sessionId, 
			String key);
	
	@Secured ({"ROLE_USER"})
	public void deleteUserSessionVariable(String userId, String sessionId, 
		String key);
	
	
	
	
	@Secured ({"ROLE_USER"})
	public void createConnection(String userId, String sessionId, 
			String driverName, String connectStr, 
			String username, String password) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	public OlapConnection getConnection(String userId, String sessionId);
	
	@Secured ({"ROLE_USER"})
	public void releaseConnection(String userId, String sessionId);
	
	
	
	
	@Secured ({"ROLE_USER"})
	public String createNewQuery(String userId, String sessionId) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	public Query getQuery(String userId, String sessionId, String queryId);
	
	@Secured ({"ROLE_USER"})
	public List<String> getQueries(String userId, String sessionId);
	
	@Secured ({"ROLE_USER"})
	public void releaseQuery(String userId, String sessionId, String queryId);
	
	
	

}
