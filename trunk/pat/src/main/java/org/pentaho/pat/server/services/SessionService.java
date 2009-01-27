package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.OlapException;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface SessionService {

	/**
	 * Creates a session on the application.
	 * @return The created session GUID to send back.
	 */
	public String createSession();
	
	/**
	 * Closes a session id.
	 * @param guid The id of the session to close.
	 * @return True if all is well.
	 */
	public Boolean closeSession(String guid);
	
	
	/**
	 * Creates a connection and associates it with a owner id.
	 * @param guid User id who sent this request
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
	 * @param guid User id who sent this request
	 * @return True if everything is done well.
	 */
	public Boolean disconnect(String guid);	
	
	
	
	
	/**
	 * Sets the current active query. This is mainly used to persist the
	 * state of the UI. 
	 * @param guid User id who sent this request
	 * @param queryId The id of the currently selected query.
	 * @return True is all is good.
	 */
	public Boolean setCurrentQuery(String guid, String queryId);
	
	/**
	 * Tells which is the currently selected query.
	 * @param guid User id who sent this request
	 * @return The name of the currently selected query.
	 */
	public String getCurrentQuery(String guid);
	
	/**
	 * Create a new query object on the connection and cubes currently 
	 * selected.
	 * @param guid User id who sent this request 
	 * @return A unique id to identify the newly created query.
	 * @throws OlapException If something goes wrong
	 */
	public String createNewQuery(String guid) throws OlapException;
	
	/**
	 * Tells what query ids are currently stored for the specified user.
	 * @param guid User id who sent this request
	 * @return A list of all queries available.
	 */
	public List<String> getQueries(String guid);

	/**
	 * Deletes a query from the query store.
	 * @param guid User id who sent this request
	 * @param queryId The query id to delete.
	 * @return True if all is good.
	 */
	public Boolean deleteQuery(String guid, String queryId);
	

	
	
	/**
	 * Sets the currently selected cube on the UI.
	 * @param guid User id who sent this request
	 * @param cubeId The new selected cube.
	 * @return True if all is good.
	 */
	public Boolean setCurrentCube(String guid, String cubeId);
	
	
	/**
	 * Tells what is the currently selected cube.
	 * @param guid User id who sent this request
	 * @return The id of the currently selected cube.
	 */
	public String getCurrentCube(String guid);

	/**
	 * Sets the current available cubes
	 * @param guid User id who sent this request
	 * @param cubes The currently available cubes for a user.
	 */
	public void setCubes(String guid, List<String> cubes);
	
	/**
	 * Returns the currently available cube names
	 * @param guid User id
	 * @return The list of cubes
	 */
	public List<String> getCubes(String guid);
}
