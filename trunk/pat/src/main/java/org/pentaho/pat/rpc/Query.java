package org.pentaho.pat.rpc;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.query.Selection;
import org.pentaho.pat.client.util.OlapData;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * This interface defines the operations permitted on a query
 * object that was previously instanciated through the Session service.
 * @author Luc Boudreau
 */
public interface Query extends RemoteService {
	
	/**
	 * Moves a dimension to a different axis.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param axis The destination axis.
	 * @param dimensionName The name of the dimension to move.
	 * @return True if all is good.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean moveDimension(
		String sessionId, 
		Axis axis, 
		String dimensionName);
	
	/**
	 * Performs a selection of certain members in a dimension.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param dimensionName The name of the dimension on which we want to 
	 * select members.
	 * @param memberNames The actual names of the members to perform a 
	 * selection on.
	 * @param selectionType The type of selection to perform.
	 * @return True if all is well.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean createSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames, 
		Selection.Operator selectionType);	
	
	/**
	 * Removes the selection status of members inside a given dimension.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param dimensionName Name of the dimension that includes the 
	 * members to remove selection status.
	 * @param memberNames The actual member names of which we want to 
	 * remove the selection status.
	 * @return True if all is well.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean clearSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames);
	
	@Secured ({"ROLE_USER"})
	public OlapData executeQuery(String sessionId) throws OlapException;
}
