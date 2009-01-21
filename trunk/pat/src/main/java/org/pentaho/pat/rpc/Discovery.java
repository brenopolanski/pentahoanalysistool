package org.pentaho.pat.rpc;


import org.olap4j.Axis;
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
	 * @return A list object containing all available dimensions,
	 * an empty list if there are none available or null
	 * if the given axis is not present in the cube.
	 */
	public String [] getDimensions(Axis axis);
	
	/**
	 * Returns all the cube names available on the current connection.
	 * @return A list of all the cube names available or an empty
	 * list if none are found.
	 */
	public String [] getCubes();
	
	/**
	 * Returns a tree list of all members found within a given
	 * dimension, or null if the dimension 
	 * @param dimensionName Name of the dimension that contains
	 * the member names we want.
	 * @return A StringTree of all present members.
	 */
	public StringTree getMembers(String dimensionName);
}
