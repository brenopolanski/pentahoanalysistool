package org.pentaho.pat.server.services;

import java.util.List;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface SessionService {

	
	
	/**
	 * Creates a connection and associates it with a owner id.
	 * @param driverClassName
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean connect(String driverClassName, String url, String username,
		String password);
	
	
	
	
	
	/**
	 * Closes the current connection.
	 * @return True if everything is done well.
	 */
	public Boolean disconnect();	
	
	
	
	
	/**
	 * Sets the current active query. This is mainly used to persist the
	 * state of the UI. 
	 * @param queryId The id of the currently selected query.
	 * @return True is all is good.
	 */
	public Boolean setCurrentQuery(String queryId);
	
	/**
	 * Tells which is the currently selected query.
	 * @return The name of the currently selected query.
	 */
	public String getCurrentQuery();
	
	/**
	 * Create a new query object on the connection and cubes currently 
	 * selected. 
	 * @return A unique id to identify the newly created query.
	 */
	public String createNewQuery();
	
	/**
	 * Tells what query ids are currently stored for the specified user.
	 * @return A list of all queries available.
	 */
	public List<String> getQueries();

	/**
	 * Deletes a query from the query store.
	 * @param queryId The query id to delete.
	 * @return True if all is good.
	 */
	public Boolean deleteQuery(String queryId);
	

	
	
	/**
	 * Sets the currently selected cube on the UI.
	 * @param cubeId The new selected cube.
	 * @return True if all is good.
	 */
	public Boolean setCurrentCube(String cubeId);
	
	
	/**
	 * Tells what is the currently selected cube.
	 * @return The id of the currently selected cube.
	 */
	public String getCurrentCube();

	
}
