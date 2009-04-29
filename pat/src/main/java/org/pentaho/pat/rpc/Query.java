package org.pentaho.pat.rpc;

import java.util.List;

import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.OlapData;
import org.pentaho.pat.rpc.exceptions.RpcException;
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
	 * You must first make sure to call Sesison.setCurrentQuery() to inform
	 * the backend of your current query selection.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param axis The destination axis.
	 * @param dimensionName The name of the dimension to move.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public void moveDimension(
		String sessionId, 
		Axis axis, 
		String dimensionName) throws RpcException;
	/**
	 * Performs a selection of certain members in a dimension.
	 * You must first make sure to call Sesison.setCurrentQuery() to inform
     * the backend of your current query selection.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param dimensionName The name of the dimension on which we want to 
	 * select members.
	 * @param memberNames The actual names of the members to perform a 
	 * selection on.
	 * @param selectionType The type of selection to perform.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public void createSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames, 
		String selectionType) throws RpcException;	
	
	/**
	 * Removes the selection status of members inside a given dimension.
	 * You must first make sure to call Sesison.setCurrentQuery() to inform
     * the backend of your current query selection.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param dimensionName Name of the dimension that includes the 
	 * members to remove selection status.
	 * @param memberNames The actual member names of which we want to 
	 * remove the selection status.
	 * @return True if all is well.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public void clearSelection(
		String sessionId, 
		String dimensionName, 
		List<String> memberNames) throws RpcException;
	
	/**
	 * Executes the current query.
	 * You must first make sure to call Sesison.setCurrentQuery() to inform
     * the backend of your current query selection.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @return The result of the query execution.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public OlapData executeQuery(String sessionId) throws RpcException;
}
