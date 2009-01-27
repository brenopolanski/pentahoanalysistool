package org.pentaho.pat.rpc;

import java.util.List;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface Session {

	
	/**
	 * Creates a session on the application.
	 * @param guid Identifies the window session id that requested the operation.
	 * @return The created session GUID to send back.
	 */
	public String createSession(String guid);
	
	/**
	 * Closes a session id.
	 * @param guid The id of the session to close.
	 * @return True if all is well.
	 */
	public Boolean closeSession(String guid);
	
	
	/**
	 * Creates a connection and associates it with a owner id.
	 * @param guid Identifies the window session id that requested the operation.
	 * @param driverClassName
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean connect(String guid, String driverClassName, String url, 
			String username, String password);
	
	
	
	
	
	/**
	 * Closes the current connection.
	 * @param guid Identifies the window session id that requested the operation.
	 * @return True if everything is done well.
	 */
	public Boolean disconnect(String guid);	
	
	
	
	
	/**
	 * Sets the current active query. This is mainly used to persist the
	 * state of the UI. 
	 * @param guid Identifies the window session id that requested the operation.
	 * @param queryId The id of the currently selected query.
	 * @return True is all is good.
	 */
	public Boolean setCurrentQuery(String guid, String queryId);
	
	/**
	 * Tells which is the currently selected query.
	 * @param guid Identifies the window session id that requested the operation.
	 * @return The name of the currently selected query.
	 */
	public String getCurrentQuery(String guid);
	
	/**
	 * Create a new query object on the connection and cubes currently 
	 * selected. 
	 * @param guid Identifies the window session id that requested the operation.
	 * @return A unique id to identify the newly created query.
	 */
	public String createNewQuery(String guid);
	
	/**
	 * Tells what query ids are currently stored for the specified user.
	 * @param guid Identifies the window session id that requested the operation.
	 * @return A list of all queries available.
	 */
	public List<String> getQueries(String guid);

	/**
	 * Deletes a query from the query store.
	 * @param guid Identifies the window session id that requested the operation.
	 * @param queryId The query id to delete.
	 * @return True if all is good.
	 */
	public Boolean deleteQuery(String guid, String queryId);
	

	
	
	/**
	 * Sets the currently selected cube on the UI.
	 * @param guid Identifies the window session id that requested the operation.
	 * @param cubeId The new selected cube.
	 * @return True if all is good.
	 */
	public Boolean setCurrentCube(String guid, String cubeId);
	
	
	/**
	 * Tells what is the currently selected cube.
	 * @param guid Identifies the window session id that requested the operation.
	 * @return The id of the currently selected cube.
	 */
	public String getCurrentCube(String guid);

	
}
