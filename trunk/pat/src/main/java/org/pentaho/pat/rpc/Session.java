package org.pentaho.pat.rpc;

import org.pentaho.pat.rpc.dto.CubeConnection;
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
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public void closeSession(String sessionId) throws RpcException;
	

	
	
	
	/**
	 * Establishes a connection using the provided CubeConnection object.
	 * @param sessionId The window session id.
	 * @param connectionId The connection UUID to activate
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
    public void connect(String sessionId, String connectionId) throws RpcException;
	
	/**
	 * Closes the current connection.
	 * @param sessionId The window session id.
	 * @param connectionId The connection UUID to deactivate
	 * @return True if all is good.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public void disconnect(String sessionId, String connectionId) throws RpcException;
	
	/**
	 * Loads a saved connection object for the database. The connection
	 * will not be established. It is only returned to the GUI.
	 * @param sessionId The window session id.
	 * @param connectionId The connection UUID to load.
	 * @return The connection object corresponding to the name passed 
	 * as a parameter. Null if no connection of that name exist.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public CubeConnection getConnection(String sessionId, String connectionId) throws RpcException;
	
	/**
	 * Returns a list of all current opened connections.
	 * @param sessionId The window session ID
	 * @return  An array of connections
	 * @throws RpcException
	 */
	@Secured ({"ROLE_USER"})
	public CubeConnection[] getActiveConnections(String sessionId) throws RpcException;
	
	/**
	 * Saves a connection parameters for later use.
	 * @param sessionId The window session id.
	 * @param connection The connection object to save.
	 * @return The new connection UUID
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public String saveConnection(String sessionId, CubeConnection connection) throws RpcException;
	
	/**
	 * Returns all the currently saved connections for a given user.
	 * @param sessionId The window session id.
	 * @return An array of connection names.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
    public CubeConnection[] getConnections(String sessionId) throws RpcException;

	/**
	 * Deletes a saved user connection.
	 * @param sessionId The window session id.
	 * @param connectionId The UUID of the connection to delete.
	 * @throws RpcException If anything goes sour.
	 */
	public void deleteConnection(String sessionId, String connectionId) throws RpcException;
}
