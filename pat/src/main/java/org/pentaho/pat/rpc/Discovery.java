package org.pentaho.pat.rpc;


import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.pentaho.pat.client.util.StringTree;
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
	 */
	public List<String> getDrivers();
	
	/**
	 * Returns a list of all available dimension names on
	 * a given axis.
	 * @param axis The axis for which we want the dimensions.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @return A list object containing all available dimensions,
	 * an empty list if there are none available or null
	 * if the given axis is not present in the cube.
	 * @throws OlapException If something goes wrong.
	 */
	@Secured ({"ROLE_USER"})
	public String [] getDimensions(String sessionId, Axis axis) throws OlapException;
	
	/**
	 * Returns all the cube names available on the current connection.
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @return A list of all the cube names available or an empty
	 * list if none are found.
	 */
	@Secured ({"ROLE_USER"})
	public String [] getCubes(String sessionId);
	
	/**
	 * Returns a tree list of all members found within a given
	 * dimension, or null if the dimension 
	 * @param sessionId Identifies the window session id that requested the operation.
	 * @param dimensionName Name of the dimension that contains
	 * the member names we want.
	 * @return A StringTree of all present members.
	 * @throws OlapException If something goes wrong.
	 */
	@Secured ({"ROLE_USER"})
	public StringTree getMembers(String sessionId, String dimensionName) throws OlapException;
}
