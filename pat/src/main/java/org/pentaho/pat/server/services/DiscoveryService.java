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

	@Secured ({"ROLE_USER"})
	public List<String> getDimensions(String userId, String sessionId, 
			Axis axis) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	public List<String> getCubes(String userId, String sessionId);
	
	@Secured ({"ROLE_USER"})
	public Cube getCube(String userId, String sessionId, String cubeName);
	
	@Secured ({"ROLE_USER"})
	public StringTree getMembers(String userId, String sessionId,
			String dimensionName) throws OlapException;
	
	@Secured ({"ROLE_USER"})
	public String[] getDrivers();
}
