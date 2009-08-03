package org.pentaho.pat.server.services;


import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.pentaho.pat.rpc.dto.StringTree;

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
     * @param the query UUID where to lookup the dimensions
     * @param axis The axis for which we want the current dimensions.
     * @return A list of dimension names.
     * @throws OlapException If something goes sour.
     */
	@Secured ({"Users"})
	public List<String> getDimensions(String userId, String sessionId, 
			String queryId, Axis.Standard axis) throws OlapException;
	
	/**
	 * Retreives a list of available cubes for the current connection.
	 * One must first create a connection via the Session service.
	 * @param userId The id of the user who requests this operation.
     * @param sessionId The id of the current session into which
     * @param connectionId The connection UUID to use
     * to perform this operation.
	 * @return A list of cubes.
	 */
	@Secured ({"Users"})
	public List<String> getCubes(String userId, String sessionId,
	    String connectionId) throws OlapException;
	
	@Secured ({"Users"})
	public Cube getCube(String userId, String sessionId,
	    String connectionId, String cubeName) throws OlapException;
	
	/**
	 * Returns a tree representation of the members included in a 
	 * given dimension. The representation only includes their names.
	 * @param userId The id of the user who requests this operation.
     * @param sessionId The id of the current session into which
     * to perform this operation.
     * @param queryId The id of the query to seek into.
	 * @param dimensionName The name of which we want the tree of members.
	 * @return A {@link StringTree} representation of the members included
	 * in a dimension.
	 * @throws OlapException If anything goes sour.
	 */
	@Secured ({"Users"})
	public StringTree getMembers(String userId, String sessionId,
		String queryId, String dimensionName) throws OlapException;
	
	/**
	 * Scans and updates if necessary the current Java classloader 
	 * for registered JDBC drivers.
	 * @return A list of JDBC driver class names.
	 */
	@Secured ({"Users"})
	public List<String> getDrivers();
}
