/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Discovery;
import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.StringTree;
import org.pentaho.pat.server.services.DiscoveryService;

/**
 * 
 * This is an implementation of the discovery RPC servlet.
 * 
 * @author Luc Boudreau
 *
 */
public class DiscoveryServlet extends AbstractServlet implements Discovery {

	private static final long serialVersionUID = 1L;
	
	private DiscoveryService discoveryService;
	
	public void init() throws ServletException {
		super.init();
		discoveryService = (DiscoveryService)applicationContext.getBean("discoveryService");
		if (discoveryService==null)
            throw new ServletException("No discovery service was found in the application context.");
	}
	
	public String[] getCubes(String sessionId) {
		List<String> list = this.discoveryService.getCubes(getCurrentUserId(), sessionId); 
		return list.toArray(new String[list.size()]);
	}

	public String [] getDimensions(String sessionId, Axis axis) 
	{
		List<String> dimensionsList;
		try {
			dimensionsList = this.discoveryService.getDimensions(getCurrentUserId(), sessionId, org.olap4j.Axis.valueOf(axis.name()));
			return dimensionsList.toArray(new String[dimensionsList.size()]);
		} catch (OlapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

	public StringTree getMembers(String sessionId, String dimensionName) {
		try {
			return this.discoveryService.getMembers(getCurrentUserId(), sessionId, dimensionName);
		} catch (OlapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] getDrivers() {
		return this.discoveryService.getDrivers();
	}
}
