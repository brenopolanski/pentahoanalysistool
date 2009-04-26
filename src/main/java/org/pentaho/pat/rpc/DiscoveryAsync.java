package org.pentaho.pat.rpc;


import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryAsync {
	
	public void getDrivers(AsyncCallback<String[]> callback);

	public void getDimensions(String sessionId, Axis axis, AsyncCallback<String []> callback);
	
	public void getCubes(String sessionId, AsyncCallback<String []> callback);
	
	public void getMembers(String sessionId, String dimensionName, AsyncCallback<StringTree> callback);
}
