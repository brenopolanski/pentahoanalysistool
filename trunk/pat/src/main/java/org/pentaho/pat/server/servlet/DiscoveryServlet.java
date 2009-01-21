/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import org.olap4j.Axis;
import org.pentaho.pat.client.util.StringTree;
import org.pentaho.pat.rpc.Discovery;
import org.pentaho.pat.server.services.DiscoveryService;

/**
 * 
 * This is an implementation of the discovery RPC servlet.
 * 
 * @author Luc Boudreau
 *
 */
public class DiscoveryServlet extends GenericServlet implements
		Discovery {

	private static final long serialVersionUID = 1L;
	
	private DiscoveryService discoveryService;

	public String[] getCubes() {
		List<String> list = this.discoveryService.getCubes(getUserId()); 
		return list.toArray(new String[list.size()]);
	}

	public String[] getDimensions(Axis axis) {
		// TODO Auto-generated method stub
		return null;
	}

	public StringTree getMembers(String dimensionName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setDiscoveryService(DiscoveryService service) {
		this.discoveryService = service;
	}
}
