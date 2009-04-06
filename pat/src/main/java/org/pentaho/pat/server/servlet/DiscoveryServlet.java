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
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.services.DiscoveryService;

import com.mysql.jdbc.Messages;

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
		discoveryService = (DiscoveryService)applicationContext.getBean("discoveryService"); //$NON-NLS-1$
		if (discoveryService==null)
            throw new ServletException(Messages.getString("Servlet.DiscoveryServiceNotFound")); //$NON-NLS-1$
	}
	
	public String[] getCubes(String sessionId) throws RpcException
	{
		List<String> list;
        try {
            list = this.discoveryService.getCubes(getCurrentUserId(), sessionId);
        } catch (OlapException e) {
            throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateCubesList"),e); //$NON-NLS-1$
        } 
		return list.toArray(new String[list.size()]);
	}

	public String [] getDimensions(String sessionId, Axis axis) throws RpcException
	{
		List<String> dimensionsList;
		try {
			dimensionsList = this.discoveryService.getDimensions(
			    getCurrentUserId(), 
			    sessionId, 
			    org.olap4j.Axis.valueOf(axis.name()));
			return dimensionsList.toArray(new String[dimensionsList.size()]);
		} catch (OlapException e) {
		    throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateDimensionsList"),e); //$NON-NLS-1$
		}
		
		
	}

	public StringTree getMembers(String sessionId, String dimensionName) throws RpcException
	{
		try {
			return this.discoveryService.getMembers(getCurrentUserId(), sessionId, dimensionName);
		} catch (OlapException e) {
		    throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateMembersList"),e); //$NON-NLS-1$
		}
	}
	
	public String[] getDrivers()
	{
	    List<String> driverNames = this.discoveryService.getDrivers();
		return driverNames.toArray(new String[driverNames.size()]);
	}
}
