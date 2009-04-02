package org.pentaho.pat.server.services;


import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.pentaho.pat.rpc.beans.StringTree;

import org.springframework.security.annotation.Secured;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryService extends Service {

    /**
     * Retreives a list of dimensions currently placed on a 
     * given dimension. For dimensions not currently used, 
     * pass a null value as the Axis parameter.
     * @param userId The id of the user who requests this operation.
     * @param sessionId The id of the current session into which
     * to perform this operation.
     * @param axis The axis for which we want the current dimensions.
     * @return A list of dimension names.
     * @throws OlapException If something goes sour.
     */
	@Secured ({"ROLE_USER"})
	public List<String> getDimensions(String userId, String sessionId, 
			Axis axis) throws OlapException;
	
	/**
	 * Retreives a list of available cubes for the current connection.
	 * One must first create a connection via the Session service.
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	@Secured ({"ROLE_USER"})
	public List<String> getCubes(String userId, String sessionId);
	
	@Secured ({"ROLE_USER"})
	Cube getCube(String userId, String sessionId, String cubeName);
	
	@Secured ({"ROLE_USER"})
	public StringTree getMembers(String userId, String sessionId,
			String dimensionName) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	public String[] getDrivers();
}
