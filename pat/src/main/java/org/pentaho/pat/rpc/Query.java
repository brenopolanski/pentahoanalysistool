package org.pentaho.pat.rpc;

import java.util.List;

import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;

import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.util.Matrix;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * This interface defines the operations permitted on a query
 * object that was previously instanciated through the Session service.
 * @author Luc Boudreau
 */
public interface Query extends RemoteService {

    /**
     * Tells the server that we want to create a new query. 
     * It will create a new query against the currently selected cube.
     * You can set the currently selected cube with Session.setCurrentCube.
     * It returns a unique identification string to identify the query created.
     * @param sessionId The window session id.
     * @return The unique ame of the created query.
     * @throws RpcException If something goes sour.
     */
    @Secured ({"ROLE_USER"})
    public String createNewQuery(String sessionId) throws RpcException;
    
    /**
     * Tells the backend that from now on, operations will be performed on
     * the specified query.
     * @param sessionId The window session id.
     * @param queryId The name of the query we'll be performing operations from now on.
     * @throws RpcException If something goes sour.
     */
    @Secured ({"ROLE_USER"})
    public void setCurrentQuery(String sessionId, String queryId) throws RpcException;
    
    /**
     * Tells which is the currently selected query.
     * @param sessionId The window session id.
     * @return The unique id of the currently selected query.
     * @throws RpcException If something goes sour.
     */
    @Secured ({"ROLE_USER"})
    public String getCurrentQuery(String sessionId) throws RpcException;
    
    
    /**
     * Returns the list of currently created queries.
     * @param sessionId The window session id.
     * @return An array of query unique names.
     * @throws RpcException If something goes sour.
     */
    @Secured ({"ROLE_USER"})
    public String[] getQueries(String sessionId) throws RpcException;

    /**
     * Closes and releases a given query.
     * @param sessionId The window session id.
     * @param queryId The query we want to close and release.
     * @return True if all is good.
     */
    @Secured ({"ROLE_USER"})
    public void deleteQuery(String sessionId, String queryId) throws RpcException;

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
	public CellDataSet executeQuery2(String sessionId) throws RpcException;
	
	// TODO is this the way we want mdx to work?
	@Secured ({"ROLE_USER"})
	public CellDataSet executeMdxQuery(String sessionId, String mdx) throws RpcException;

}
