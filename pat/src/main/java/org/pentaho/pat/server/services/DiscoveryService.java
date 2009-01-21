package org.pentaho.pat.server.services;


import java.util.List;

import org.apache.commons.collections.list.TreeList;
import org.olap4j.Axis;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryService {

	/**
	 * Returns a list of all available dimension names on
	 * a given axis.
	 * @param axis The axis for which we want the dimensions.
	 * @param guid The id of the person who asks.
	 * @return A list object containing all available dimensions,
	 * an empty list if there are none available or null
	 * if the given axis is not present in the cube.
	 */
	public List<String> getDimensions(Axis axis, String guid);
	
	/**
	 * Returns all the cube names available on the current connection.
	 * @param guid The owner of the request.
	 * @return A list of all the cube names available or an empty
	 * list if none are found.
	 */
	public List<String> getCubes(String guid);
	
	/**
	 * Returns a tree list of all members found within a given
	 * dimension, or null if the dimension 
	 * @param dimensionName
	 * @param guid
	 * @return
	 */
	public TreeList getMembers(String dimensionName, String guid);

}
