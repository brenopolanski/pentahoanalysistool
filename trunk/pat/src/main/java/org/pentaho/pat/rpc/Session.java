package org.pentaho.pat.rpc;

import org.pentaho.pat.rpc.beans.CubeConnection;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The session RPC interface is responsible for screen operations. It exposes
 * calls to establish connections, start sessions and so forth.
 * 
 * @author Luc Boudreau
 */
public interface Session extends RemoteService {

	/**
	 * Creates a new session. Call this method for every browser window that
	 * gets opened. It will return to you a unique identification string
	 * which you will need to provide for all future calls.
	 * @return A unique session identification string.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public String createSession() throws RpcException;
	
	/**
	 * Destroys the session on the server. Call this method when the browser
	 * window is closed.
	 * @param sessionId The session id to close and destroy.
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean closeSession(String sessionId) throws RpcException;
	

	
	
	
	/**
	 * Establishes a connection using the provided CubeConnection object.
	 * @param sessionId The window session id.
	 * @param connection The connection parameters.
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
    public Boolean connect(String sessionId, CubeConnection connection) throws RpcException;
	
	/**
	 * Closes the current connection.
	 * @param sessionId The window session id.
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean disconnect(String sessionId) throws RpcException;
	
	/**
	 * Loads a saved connection object for the database. The connection
	 * will not be established. It is only returned to the GUI.
	 * @param sessionId The window session id.
	 * @param connectionName The name of the connection to load.
	 * @return The connection object corresponding to the name passed 
	 * as a parameter. Null if no connection of that name exist.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public CubeConnection getConnection(String sessionId, String connectionName) throws RpcException;
	
	/**
	 * Saves a connection parameters for later use.
	 * @param sessionId The window session id.
	 * @param connection The connection object to save.
	 * @return True is all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean saveConnection(String sessionId, CubeConnection connection) throws RpcException;
	
	/**
	 * Returns all the currently saved connections for a given user.
	 * @param sessionId The window session id.
	 * @return An array of connection names.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
    public CubeConnection[] getSavedConnections(String sessionId) throws RpcException;
	
	public Boolean deleteSavedConnection(String sessionId, String connectionName) throws RpcException;
    
	
	
	/**
	 * Tells the server that we want to create a new query. 
	 * It will create a new query against the currently selected cube.
	 * You can set the currently selected cube with Session.setCurrentCube.
	 * It returns a unique identification string to identify the query created.
	 * @param sessionId The window session id.
	 * @return The unique ame of the created query.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
    public String createNewQuery(String sessionId) throws RpcException;
	
	/**
	 * Tells the backend that from now on, operations will be performed on
	 * the specified query.
	 * @param sessionId The window session id.
	 * @param queryId The name of the query we'll be performing operations from now on.
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean setCurrentQuery(String sessionId, String queryId) throws RpcException;
	
	/**
	 * Tells which is the currently selected query.
	 * @param sessionId The window session id.
	 * @return The unique id of the currently selected query.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public String getCurrentQuery(String sessionId) throws RpcException;
	
	
	/**
	 * Returns the list of currently created queries.
	 * @param sessionId The window session id.
	 * @return An array of query unique names.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public String[] getQueries(String sessionId) throws RpcException;

	/**
	 * Closes and releases a given query.
	 * @param sessionId The window session id.
	 * @param queryId The query we want to close and release.
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean deleteQuery(String sessionId, String queryId) throws RpcException;
	
	
	
	
	/**
	 * Returns which cube the server is currently using for 
	 * discovery and query operations. 
	 * @param sessionId The window session id.
	 * @return The name of the currently selected cube.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
    public String getCurrentCube(String sessionId) throws RpcException;
	
	/**
	 * Tells the server which is the current cube we want to work with.
	 * All the following operations will assume that we are using this cube.
	 * To obtain a list of available cubes, refer to the Discovery RPC interface.
	 * @param sessionId The window session id.
	 * @param cubeId The cubeId we want to work with.
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean setCurrentCube(String sessionId, String cubeId) throws RpcException;

	
}
