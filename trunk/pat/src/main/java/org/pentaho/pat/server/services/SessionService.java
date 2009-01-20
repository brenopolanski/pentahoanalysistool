package org.pentaho.pat.server.services;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface SessionService {

	
	
	/**
	 * Creates a connection and associates it with a owner id.
	 * FIXME this will need improvements.
	 * @param connectStr
	 * @param guid
	 * @return
	 */
	public Boolean connect(String connectStr, String guid);
	
	/**
	 * Closes the current connection.
	 * @param guid The owner of the connection.
	 * @return True if everything is done well.
	 */
	public Boolean disconnect(String guid);	
	
	
	
	
	/**
	 * Sets the current active query. This is mainly used to persist the
	 * state of the UI. 
	 * @param queryId The id of the currently selected query.
	 * @param guid Owner ID
	 * @return True is all is good.
	 */
	public Boolean setCurrentQuery(String queryId, String guid);
	
	/**
	 * Tells which is the currently selected query.
	 * @param guid The owner of which we want to know the current GUI query.
	 * @return The name of the currently selected query.
	 */
	public String getCurrentQuery(String guid);
	
	/**
	 * Create a new query object on the connection and cubes currently 
	 * selected. 
	 * @return A unique id to identify the newly created query.
	 */
	public String createNewQuery(String guid);
	
	/**
	 * Tells what query ids are currently stored for the specified user.
	 * @param guid The owner id from which we want the current active queries.
	 * @return An array of query ids.
	 */
	public String[] getQueries(String guid);

	/**
	 * Deletes a query from the query store.
	 * @param queryId The query id to delete.
	 * @param guid The owner id of the query.
	 * @return True if all is good.
	 */
	public Boolean deleteQuery(String queryId, String guid);
	

	
	
	/**
	 * Sets the currently selected cube on the UI.
	 * @param cubeId The new selected cube.
	 * @param guid The owner of this selection.
	 * @return True if all is good.
	 */
	public Boolean setCurrentCube(String cubeId, String guid);
	
	
	/**
	 * Tells what is the currently selected cube.
	 * @param guid The owner of the currently selected cube.
	 * @return The id of the currently selected cube.
	 */
	public String getCurrentCube(String guid);

	
}
