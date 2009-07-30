package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
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
	 * Validates that a user session id is valid. 
	 * @param userId The owner of the session
	 * @param sessionId The session unique id
	 * @throws SecurityException If either the userId or the sessionId are invalid.
	 */
	public void validateSession(String userId, String sessionId) 
	    throws SecurityException;
	
	/**
	 * Validates that this userId exists.
	 * @param userId The user unique id to validate.
	 * @throws SecurityException If this user doesn't exist.
	 */
	public void validateUser(String userId) throws SecurityException;
	
	
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
    public Session getSession(String userId, String sessionId);

	
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
	 * @param connectionId The connection UUID to activate
	 * @throws OlapException If the connection fails.
	 */
	@Secured ({"ROLE_USER"})
	public void connect(String userId, String sessionId, 
			String connectionId) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	public String createConnection(String userId, String sessionId,
            String driverName, String connectStr, String username,
            String password) throws OlapException;
	
	/**
     * Establishes a connection according to the supplied parameters.
     * @param userId The owner of the connection to establish.
     * @param sessionId The session id under which the connection will be stored.
     * @param sc A SavedConnection object describing the connection to establish.
     * @throws OlapException If the connection fails.
     */
    @Secured ({"ROLE_USER"})
    public void connect(String userId, String sessionId, 
            SavedConnection sc) throws OlapException;

	
    /**
     * Returns the underlying OlapConnection for a given
     * connection UUID. The connection needs to be activated
     * prior to this call.
     * @param userId The owner of the connection
     * @param sessionId The session id under which the connection is
     * @param connectionId The sasved connection UUID for which we want the
     * underlying native connection.
     * @return The olapConnection object. Null if none are found.
     */
	@Secured ({"ROLE_USER"})
	public OlapConnection getNativeConnection(
	        String userId, String sessionId, String connectionId);
	
	/**
	 * Closes a connection.
	 * @param userId The owner of the connection.
	 * @param sessionId The session id that contains the connection
	 * @param connectionId The connection UUID
	 */
	@Secured ({"ROLE_USER"})
	public void disconnect(String userId, String sessionId,
	        String connectionId);
	
	/**
	 * Retreives a saved connection.
	 * @param userId The owner of the connection.
	 * @param connectionId The UUID of the connection.
	 * @return The SavedConnection object, null if not found.
	 */
	@Secured ({"ROLE_USER"})
	@Transactional(readOnly=true)
    public SavedConnection getConnection(String userId, String connectionId);
	
	/**
	 * Returns a list of saved connections associated to a user.
	 * @param userId The owner of the connections we want the names of.
	 * @return A list of connection names, null if the user doesn't exist.
	 */
	@Secured ({"ROLE_USER"})
	@Transactional(readOnly=true)
    public List<SavedConnection> getConnections(String userId);
    
	/**
     * Returns a list of currently active connections associated to a user.
     * @param userId The owner of the connections we want the names of.
     * @param sessionId The session in which to seek for active connections
     * @return A list of connection names, null if the user doesn't exist.
     */
    @Secured ({"ROLE_USER"})
    @Transactional(readOnly=true)
    public List<SavedConnection> getActiveConnections(String userId, String sessionId);
    
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
    public void deleteConnection(String userId, String connectionId);
}
