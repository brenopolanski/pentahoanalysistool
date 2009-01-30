package org.pentaho.pat.rpc;


import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.pentaho.pat.client.util.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryAsync {

	public void getDimensions(String sessionId, Axis axis, AsyncCallback<String []> callback) throws OlapException;
	
	public void getCubes(String sessionId, AsyncCallback<String []> callback);
	
	public void getMembers(String sessionId, String dimensionName, AsyncCallback<StringTree> callback) throws OlapException;
}
