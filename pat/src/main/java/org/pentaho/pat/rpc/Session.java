package org.pentaho.pat.rpc;

import java.util.List;

import org.olap4j.OlapException;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface Session {

	
	/**
	 * Creates a session on the application.
	 * @return The created session GUID to send back.
	 */
	public String createSession();
	
	public void closeSession(String sessionId);
	

	
	
	
	
	
	public void connect(String sessionId, String driverClassName, String url, 
			String username, String password) throws OlapException;
	
	public Boolean disconnect(String sessionId);	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setCurrentQuery(String sessionId, String queryId);
	
	/**
	 * Tells which is the currently selected query.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @return The name of the currently selected query.
	 */
	public String getCurrentQuery(String sessionId);
	
	/**
	 * Create a new query object on the connection and cubes currently 
	 * selected. 
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @return A unique id to identify the newly created query.
	 * @throws OlapException 
	 */
	public String createNewQuery(String sessionId) throws OlapException;
	
	/**
	 * Tells what query ids are currently stored for the specified user.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @return A list of all queries available.
	 */
	public List<String> getQueries(String sessionId);

	/**
	 * Deletes a query from the query store.
	 * @param guid Identifies the window session id that requested the operation.
	 * @param sessionId The query id to delete.
	 */
	public void deleteQuery(String guid, String sessionId);
	

	

	public void setCurrentCube(String sessionId, String cubeId);
	
	
	/**
	 * Tells what is the currently selected cube.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @return The id of the currently selected cube.
	 */
	public String getCurrentCube(String sessionId);

	
}
