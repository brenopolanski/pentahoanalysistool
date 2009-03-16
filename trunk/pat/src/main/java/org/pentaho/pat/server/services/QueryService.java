package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.query.Selection;
import org.pentaho.pat.rpc.beans.OlapData;
import org.springframework.security.annotation.Secured;

/**
 * This interface defines the operations permitted on a query
 * object.
 * @author Luc Boudreau
 */
public interface QueryService extends Service {
	

	@Secured ({"ROLE_USER"})
	public Boolean moveDimension(
		String userId, 
		String sessionId,
		Axis axis, 
		String dimensionName);
	

	@Secured ({"ROLE_USER"})
	public Boolean createSelection(
		String userId, 
		String sessionId,
		String dimensionName, 
		List<String> memberNames, 
		Selection.Operator selectionType);	
	

	@Secured ({"ROLE_USER"})
	public Boolean clearSelection(
		String userId, 
		String sessionId,
		String dimensionName, 
		List<String> memberNames);
	
	@Secured ({"ROLE_USER"})
	public OlapData executeQuery(String userId, 
			String sessionId) throws OlapException;
}
