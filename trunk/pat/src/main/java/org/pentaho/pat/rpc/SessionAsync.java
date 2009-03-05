package org.pentaho.pat.rpc;

import java.util.List;

import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;

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
	

	
	
	
	
	public void connect(String sessionId, String driverClassName, String url, 
			String username, String password, AsyncCallback<Boolean> callback) throws OlapException;
	
	public void disconnect(String sessionId, AsyncCallback<Boolean> callback);	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setCurrentQuery(String sessionId, String queryId, AsyncCallback<Boolean> callback);
	
	public void getCurrentQuery(String sessionId, AsyncCallback<String> callback);
	
	public void createNewQuery(String sessionId, AsyncCallback<String> callback) throws OlapException;
	
	public void getQueries(String sessionId, AsyncCallback<List<String>> callback);

	public void deleteQuery(String guid, String sessionId, AsyncCallback<Boolean> callback);

	public void getQuery(String sessionId, String queryId, AsyncCallback<SelectNode> callback);
	
	
	
	
	
	
	
	public void setCurrentCube(String sessionId, String cubeId, AsyncCallback<Boolean> callback);
	
	
	public void getCurrentCube(String sessionId, AsyncCallback<String> callback);

	
}
