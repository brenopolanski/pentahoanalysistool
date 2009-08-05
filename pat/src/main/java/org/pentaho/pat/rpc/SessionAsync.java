package org.pentaho.pat.rpc;

import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.exceptions.RpcException;

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
	
	public void connect(String sessionId, String connectionId, AsyncCallback callback);
	
	public void disconnect(String sessionId, String connectionId, AsyncCallback callback);	
	
	public void getConnection(String sessionId, String connectionId, AsyncCallback<CubeConnection> callback);
	
	public void getConnections(String sessionId, AsyncCallback<CubeConnection[]> callback);
	
	public void getActiveConnections(String sessionId, AsyncCallback<CubeConnection[]> callback);
	
	public void saveConnection(String sessionId, CubeConnection connection, AsyncCallback<String> callback);
	
	public void deleteConnection(String sessionId, String connectionId, AsyncCallback callback);
	
}
