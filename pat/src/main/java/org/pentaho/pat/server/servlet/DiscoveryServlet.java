/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
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
public class DiscoveryServlet extends AbstractServlet implements
		Discovery {

	private static final long serialVersionUID = 1L;
	
	private DiscoveryService discoveryService;
	
	public void setDiscoveryService(DiscoveryService service) {
		this.discoveryService = service;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (this.discoveryService==null)
			throw new Exception("A discoveryService is required.");
	}

	public String[] getCubes(String sessionId) {
		List<String> list = this.discoveryService.getCubes(getCurrentUserId(), sessionId); 
		return list.toArray(new String[list.size()]);
	}

	public String[] getDimensions(String sessionId, Axis axis) throws OlapException {
		List<String> dimensionsList = 
			this.discoveryService.getDimensions(getCurrentUserId(), sessionId, axis);
		return dimensionsList.toArray(new String[dimensionsList.size()]);
	}

	public StringTree getMembers(String sessionId, String dimensionName) throws OlapException {
		return this.discoveryService.getMembers(getCurrentUserId(), sessionId, dimensionName);
	}
	
	public List<String> getDrivers() {
		return this.discoveryService.getDrivers();
	}
}
