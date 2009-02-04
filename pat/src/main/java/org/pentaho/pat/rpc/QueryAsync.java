package org.pentaho.pat.rpc;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.query.Selection;
import org.pentaho.pat.client.util.OlapData;

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
		AsyncCallback<Boolean> callback);
	
	public void createSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames, 
		Selection.Operator selectionType,
		AsyncCallback<Boolean> callback);	
	
	
	public void clearSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames,
		AsyncCallback<Boolean> callback);
	
	public void executeQuery(
		String sessionId,
		AsyncCallback<OlapData> callback) 
		throws OlapException;
}
