/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Discovery;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.messages.Messages;
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

	private transient DiscoveryService discoveryService;

	private transient Logger log = Logger.getLogger(DiscoveryServlet.class);

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
            log.error(Messages.getString("Servlet.Discovery.CantGenerateCubesList"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateCubesList")); //$NON-NLS-1$
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
			    (axis.equals(Axis.UNUSED))?null:org.olap4j.Axis.Standard.valueOf(axis.name()));
			return dimensionsList.toArray(new String[dimensionsList.size()]);
		} catch (OlapException e) {
		    log.error(Messages.getString("Servlet.Discovery.CantGenerateDimensionsList"),e); //$NON-NLS-1$
		    throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateDimensionsList")); //$NON-NLS-1$
		}
		
		
	}

	public StringTree getMembers(String sessionId, String dimensionName) throws RpcException
	{
		try {
			return this.discoveryService.getMembers(getCurrentUserId(), sessionId, dimensionName);
		} catch (OlapException e) {
		    log.error(Messages.getString("Servlet.Discovery.CantGenerateMembersList"),e); //$NON-NLS-1$
		    throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateMembersList")); //$NON-NLS-1$
		}
	}
	
	public String[] getDrivers() throws RpcException
	{
	    List<String> driverNames = this.discoveryService.getDrivers();
		return driverNames.toArray(new String[driverNames.size()]);
	}
}
