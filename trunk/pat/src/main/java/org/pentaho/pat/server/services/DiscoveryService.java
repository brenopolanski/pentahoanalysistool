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
public interface DiscoveryService extends Service {

	public List<String> getDimensions(String userId, String sessionId, 
			Axis axis) throws OlapException;
	
	public List<String> getCubes(String userId, String sessionId);
	
	public Cube getCube(String userId, String sessionId, String cubeName);
	
	public StringTree getMembers(String userId, String sessionId,
			String dimensionName) throws OlapException;
}
