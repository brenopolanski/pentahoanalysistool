/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Discovery;
import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.StringTree;
import org.pentaho.pat.server.services.DiscoveryService;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * This is an implementation of the discovery RPC servlet.
 * 
 * @author Luc Boudreau
 *
 */
public class DiscoveryServlet extends AbstractServlet implements
		Discovery, InitializingBean {

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
