/*
 */

package org.pentaho.pat.client.services;

import org.pentaho.pat.client.util.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Tom Barber
 * 
 */
public interface Olap4JServiceAsync {
	public void connect(String connectStr, String guid, AsyncCallback callback);
	public void disconnect(String guid, AsyncCallback callback);
	public void setCube(String cubeName, String guid, AsyncCallback callback);
	public void getDimensions(String axis, String guid, AsyncCallback callback);
	public void getCubes(String guid, AsyncCallback callback);
	public void getMembers(String dimName, String guid, AsyncCallback<StringTree> callback);
}
