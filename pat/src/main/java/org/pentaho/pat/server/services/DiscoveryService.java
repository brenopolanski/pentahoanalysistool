package org.pentaho.pat.server.services;

import org.pentaho.pat.client.util.StringTree;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryService {

	public String[] getDimensions(String axis, String guid);
	
	public String[][] getCubes(String guid);
	
	public StringTree getMembers(String dimName, String guid);

}
