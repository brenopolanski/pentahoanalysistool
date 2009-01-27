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
public class DiscoveryServlet extends GenericServlet implements
		Discovery {

	private static final long serialVersionUID = 1L;
	
	private DiscoveryService discoveryService;
	
	public void setDiscoveryService(DiscoveryService service) {
		this.discoveryService = service;
	}

	public String[] getCubes(String guid) {
		List<String> list = this.discoveryService.getCubes(guid); 
		return list.toArray(new String[list.size()]);
	}

	public String[] getDimensions(String guid, Axis axis) throws OlapException {
		List<String> dimensionsList = 
			this.discoveryService.getDimensions(guid, axis);
		return dimensionsList.toArray(new String[dimensionsList.size()]);
	}

	public StringTree getMembers(String guid, String dimensionName) throws OlapException {
		return this.discoveryService.getMembers(guid, dimensionName);
	}
}
