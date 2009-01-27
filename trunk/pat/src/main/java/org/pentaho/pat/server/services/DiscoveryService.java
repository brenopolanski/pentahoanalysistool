package org.pentaho.pat.server.services;


import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.pentaho.pat.client.util.StringTree;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryService {

	/**
	 * Returns a list of all available dimension names on
	 * a given axis.
	 * @param guid The id of the person who asks.
	 * @param axis The axis for which we want the dimensions.
	 * @return A list object containing all available dimensions,
	 * an empty list if there are none available or null
	 * if the given axis is not present in the cube.
	 */
	public List<String> getDimensions(String guid, Axis axis) throws OlapException;
	
	/**
	 * Returns all the cube names available on the current connection.
	 * @param guid The owner of the request.
	 * @return A list of all the cube names available or an empty
	 * list if none are found.
	 */
	public List<String> getCubes(String guid);
	
	/**
	 * Get a cube object.
	 * @param name The name of the cube we want.
	 * @return The cube object.
	 */
	public Cube getCube(String guid, String name);
	
	/**
	 * Returns a tree list of all members found within a given
	 * dimension, or null if the dimension 
	 * @param guid
	 * @param dimensionName
	 * @return
	 * @throws OlapException 
	 */
	public StringTree getMembers(String guid, String dimensionName) throws OlapException;

}
