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
	

    /**
     * Moves a dimension from an axis to another.
     * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
     * @param axis The axis into which we want the dimension to be put. 
     * Null to remove it from the current query.
     * @param dimensionName The name of the dimension to move.
     * @return True if all is good.
     */
	@Secured ({"ROLE_USER"})
	public Boolean moveDimension(
		String userId, 
		String sessionId,
		Axis axis, 
		String dimensionName);
	

	/**
	 * Creates a selection of members on a given dimension.
	 * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
	 * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
	 * @param dimensionName The name of the dimension onto which to
	 * apply the selection.
	 * @param memberNames A list of member names to select.
	 * @param selectionType The type of selection to perform.
	 * @return True if all is good.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean createSelection(
		String userId, 
		String sessionId,
		String dimensionName, 
		List<String> memberNames, 
		Selection.Operator selectionType);	
	

	/**
	 * Unselects members from a dimension inside a query.
	 * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
	 * @param dimensionName The name of the dimension that contains the
	 * members to deselect.
	 * @param memberNames A list of member names to deselect.
	 * @return True if all is good.
	 */
	@Secured ({"ROLE_USER"})
	public Boolean clearSelection(
		String userId, 
		String sessionId,
		String dimensionName, 
		List<String> memberNames);
	
	/**
	 * Executes a query.
	 * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
	 * @return The resultset of the query as a OlapData object.
	 * @throws OlapException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public OlapData executeQuery(String userId, 
			String sessionId) throws OlapException;
}
