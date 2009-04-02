package org.pentaho.pat.rpc;

import org.pentaho.pat.rpc.beans.CubeConnection;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface SessionAsync {

	
	public void createSession(AsyncCallback<String> callback);
	
	public void closeSession(String sessionId, AsyncCallback<Boolean> callback);
	

	
	
	
	
//	public void connect(String sessionId, String driverClassName, String url, 
//			String username, String password, AsyncCallback<Boolean> callback);
	public void connect(String sessionId, CubeConnection connection, 
	    AsyncCallback<Boolean> callback);
	
	public void disconnect(String sessionId, AsyncCallback<Boolean> callback);	
	
	public void getConnection(String sessionId, String connectionName, AsyncCallback<CubeConnection> callback);
	
	public void saveConnection(String sessionId, CubeConnection connection, AsyncCallback<Boolean> callback);
	
	public void getSavedConnections(String sessionId, AsyncCallback<String[]> callback);
	
	public void deleteSavedConnection(String sessionId, String connectionName, AsyncCallback<Boolean> callback);
	
	
	
	
	
	
	public void setCurrentQuery(String sessionId, String queryId, AsyncCallback<Boolean> callback);
	
	public void getCurrentQuery(String sessionId, AsyncCallback<String> callback);
	
	public void createNewQuery(String sessionId, AsyncCallback<String> callback);
	
	public void getQueries(String sessionId, AsyncCallback<String[]> callback);

	public void deleteQuery(String sessionId, String queryId, AsyncCallback<Boolean> callback);

	
	
	
	
	
	
	public void setCurrentCube(String sessionId, String cubeId, AsyncCallback<Boolean> callback);
	
	
	public void getCurrentCube(String sessionId, AsyncCallback<String> callback);

	
}
