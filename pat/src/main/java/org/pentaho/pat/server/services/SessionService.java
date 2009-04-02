package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.Session;
import org.springframework.security.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface SessionService extends Service {

	
	
	
	
	/**
	 * Creates a new session space.
	 * @param userId The owner of the session to create.
	 * @return The session unique ID.
	 */
	@Secured ({"ROLE_USER"})
	public String createNewSession(String userId);
	
	/**
	 * Closes and deletes a session.
	 * @param userId The owner of the session to close.
	 * @param sessionId The session id we want to close.
	 */
	@Secured ({"ROLE_USER"})
	public void releaseSession(String userId, String sessionId);
	
	/**
	 * Accesses a session object directly.
	 * @param userId The owner of the required session.
	 * @param sessionId The id of the session we want.
	 * @return The session object, null if none found.
	 */
	@Secured ({"ROLE_ADMIN"})
    Session getSession(String userId, String sessionId);

	
	/**
	 * Stores an object in the user session space for later use.
	 * @param userId The owner of the session into which we want to store.
	 * @param sessionId The id of the session into which to store the variable.
	 * @param key The key under which to store the variable.
	 * @param value The object to store into the session space.
	 */
	@Secured ({"ROLE_USER"})
	public void saveUserSessionVariable(String userId, String sessionId, 
		String key, Object value);
	
	/**
	 * Retreives a session variable object.
	 * @param userId The owner of the session.
	 * @param sessionId The session od from which we want a variable retreived.
	 * @param key The key under which the variable is stored.
	 * @return The stored object, null if none found.
	 */
	@Secured ({"ROLE_USER"})
	public Object getUserSessionVariable(String userId, String sessionId, 
			String key);
	
	/**
	 * Removes a session variable object from the session space.
	 * @param userId The owner of the session.
	 * @param sessionId The id of the session that contains the variable.
	 * @param key The key under which the variable is stored.
	 */
	@Secured ({"ROLE_USER"})
	public void deleteUserSessionVariable(String userId, String sessionId, 
		String key);
	
	
	
	/**
	 * Establishes a connection according to the supplied parameters.
	 * @param userId The owner of the connection to establish.
	 * @param sessionId The session id under which the connection will be stored.
	 * @param driverName The Olap4j driver to use.
	 * @param connectStr The URL to send to the Olap4j driver.
	 * @param username The connection username to connect.
	 * @param password The username's password.
	 * @throws OlapException If the connection fails.
	 */
	@Secured ({"ROLE_USER"})
	public void createConnection(String userId, String sessionId, 
			String driverName, String connectStr, 
			String username, String password) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	OlapConnection getConnection(String userId, String sessionId);
	
	/**
	 * Closes a connection.
	 * @param userId The owner of the connection.
	 * @param sessionId The session id to which the 
	 */
	@Secured ({"ROLE_USER"})
	public void releaseConnection(String userId, String sessionId);
	
	/**
	 * Retreives a saved connection.
	 * @param userId The owner of the connection.
	 * @param connectionName The name of the connection.
	 * @return The SavedConnection object, null if not found.
	 */
	@Secured ({"ROLE_USER"})
	@Transactional(readOnly=true)
    public SavedConnection getSavedConnection(String userId, String connectionName);
	
	/**
	 * Returns a list of saved connections associated to a user.
	 * @param userId The owner of the connections we want the names of.
	 * @return A list of connection names, null if the user doesn't exist.
	 */
	@Secured ({"ROLE_USER"})
	@Transactional(readOnly=true)
    public List<SavedConnection> getSavedConnections(String userId);
    
	/**
	 * Saves a connection in a user account.
	 * @param userId The owner of the connection. The saved connection
	 * will be associated to his account.
	 * @param connection The connection to save.
	 */
	@Secured ({"ROLE_USER"})
	@Transactional(readOnly=false)
    public void saveConnection(String userId, SavedConnection connection);
	
	/**
	 * Deletes a saved connection from a user account.
	 * @param userId The owner of the connection to delete.
	 * @param connectionName The name of the connection to delete.
	 */
	@Secured ({"ROLE_USER"})
	@Transactional(readOnly=false)
    public void deleteSavedConnection(String userId, String connectionName);
	
	
	
	/**
	 * Creates a new query for a given session.
	 * @param userId The owner of the query to create.
	 * @param sessionId The session id into which we want to create a new query.
	 * @return A unique query identification number.
	 * @throws OlapException If creating the query fails.
	 */
	@Secured ({"ROLE_USER"})
	public String createNewQuery(String userId, String sessionId) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	Query getQuery(String userId, String sessionId, String queryId);
	
	/**
	 * Returns a list of the currently created queries inside a
	 * given session.
	 * @param userId The owner of the session and queries. 
	 * @param sessionId The unique id of the session for which we want the current opened queries.
	 * @return A list of query names.
	 */
	@Secured ({"ROLE_USER"})
	public List<String> getQueries(String userId, String sessionId);
	
	/**
	 * Releases and closes a query inside a given session.
	 * @param userId The owner of the query.
	 * @param sessionId The unique session id for which we want to close and delete a query.
	 * @param queryId The unique id of the query to close and release.
	 */
	@Secured ({"ROLE_USER"})
	public void releaseQuery(String userId, String sessionId, String queryId);

}
