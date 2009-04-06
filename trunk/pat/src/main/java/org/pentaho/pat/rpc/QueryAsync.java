package org.pentaho.pat.rpc;

import java.util.List;

import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.OlapData;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This interface defines the operations permitted on a query
 * object that was previously instanciated through the Session service.
 * @author Luc Boudreau
 */
public interface QueryAsync {
	
	public void moveDimension(
		String sessionId, 
		Axis axis, 
		String dimensionName,
		AsyncCallback callback);
	
	public void createSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames, 
		String selectionType,
		AsyncCallback callback);	
	
	
	public void clearSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames,
		AsyncCallback callback);
	
	public void executeQuery(
		String sessionId,
		AsyncCallback<OlapData> callback);
}
