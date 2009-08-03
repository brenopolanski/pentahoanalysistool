package org.pentaho.pat.rpc;


import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface Discovery extends RemoteService {

	/**
	 * Fetches a list of JDBC drivers available for use.
	 * @return A list of java JDBC driver names.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"Users"})
	public String[] getDrivers() throws RpcException;
	
	/**
	 * Returns a list of all available dimension names on
	 * a given axis.
	 * You first specify which cube you're currently exploring via the
	 * Session.setCurrentCube() call.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param queryId
	 * @param axis The axis for which we want the dimensions. Use null for unused dimensions.
	 * @return A list object containing all available dimensions,
	 * an empty list if there are none available or null
	 * if the given axis is not present in the cube.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"Users"})
	public String [] getDimensions(
	        String sessionId, 
	        String queryId, 
	        Axis axis) throws RpcException;
	
	/**
	 * Returns all the cube names available on the current connection.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param connectionId
	 * @return A list of all the cube names available or an empty
	 * list if none are found.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"Users"})
	public String [] getCubes(
	        String sessionId, 
	        String connectionId) throws RpcException;
	
	/**
	 * Returns a tree list of all members found within a given
	 * dimension, or null if the dimension
	 * You first specify which cube you're currently exploring via the
     * Session.setCurrentCube() call. 
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param The query UUID
	 * @param dimensionName Name of the dimension that contains
	 * the member names we want.
	 * @return A StringTree of all present members.
	 * @throws RpcException If something goes sour.
	 */
	@Secured ({"Users"})
	public StringTree getMembers(
	        String sessionId, 
	        String queryId,
	        String dimensionName) throws RpcException;
}
