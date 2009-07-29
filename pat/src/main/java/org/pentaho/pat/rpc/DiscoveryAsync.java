package org.pentaho.pat.rpc;


import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryAsync {
	
	public void getDrivers(AsyncCallback<String[]> callback);

	public void getDimensions(String sessionId, String queryId, Axis axis, AsyncCallback<String []> callback);
	
	public void getCubes(String sessionId, String connectionId, AsyncCallback<String []> callback);
	
	public void getMembers(String sessionId, String queryId, String dimensionName, AsyncCallback<StringTree> callback);
}
