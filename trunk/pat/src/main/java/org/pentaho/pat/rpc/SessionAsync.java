package org.pentaho.pat.rpc;

import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
@SuppressWarnings("unchecked")
public interface SessionAsync {

	
	public void createSession(AsyncCallback<String> callback);
	
	public void closeSession(String sessionId, AsyncCallback callback);
	

	
	
	
	
	public void connect(String sessionId, CubeConnection connection, AsyncCallback callback);
	
	public void disconnect(String sessionId, AsyncCallback callback);	
	
	public void getConnection(String sessionId, String connectionName, AsyncCallback<CubeConnection> callback);
	
	public void saveConnection(String sessionId, CubeConnection connection, AsyncCallback callback);
	
	public void getSavedConnections(String sessionId, AsyncCallback<String[]> callback);
	
	public void deleteSavedConnection(String sessionId, String connectionName, AsyncCallback callback);
	
	
	
	
	public void createNewQuery(String sessionId, AsyncCallback<String> callback);
	
	public void setCurrentQuery(String sessionId, String queryId, AsyncCallback callback);
	
	public void getCurrentQuery(String sessionId, AsyncCallback<String> callback);
	
	public void getQueries(String sessionId, AsyncCallback<String[]> callback);

	public void deleteQuery(String sessionId, String queryId, AsyncCallback callback);

	
	
	
	
	public void getCurrentCube(String sessionId, AsyncCallback<String> callback);
	
	public void setCurrentCube(String sessionId, String cubeId, AsyncCallback callback);
}
