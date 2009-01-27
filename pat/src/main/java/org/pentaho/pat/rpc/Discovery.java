package org.pentaho.pat.rpc;


import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.pentaho.pat.client.util.StringTree;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface Discovery {

	/**
	 * Returns a list of all available dimension names on
	 * a given axis.
	 * @param axis The axis for which we want the dimensions.
	 * @param guid Identifies the window session id that requested the operation.
	 * @return A list object containing all available dimensions,
	 * an empty list if there are none available or null
	 * if the given axis is not present in the cube.
	 * @throws OlapException If something goes wrong.
	 */
	public String [] getDimensions(String guid, Axis axis) throws OlapException;
	
	/**
	 * Returns all the cube names available on the current connection.
	 * @param guid Identifies the window session id that requested the operation.
	 * @return A list of all the cube names available or an empty
	 * list if none are found.
	 */
	public String [] getCubes(String guid);
	
	/**
	 * Returns a tree list of all members found within a given
	 * dimension, or null if the dimension 
	 * @param guid Identifies the window session id that requested the operation.
	 * @param dimensionName Name of the dimension that contains
	 * the member names we want.
	 * @return A StringTree of all present members.
	 * @throws OlapException If something goes wrong.
	 */
	public StringTree getMembers(String guid, String dimensionName) throws OlapException;
}
