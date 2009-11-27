/*
 * Copyright (C) 2009 Luc Boudreau
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapException;
import org.pentaho.pat.rpc.IDiscovery;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.IAxis;
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
public class DiscoveryServlet extends AbstractServlet implements IDiscovery {

    private static final long serialVersionUID = 1L;

    private DiscoveryService discoveryService;

    private static final Logger LOG = Logger.getLogger(DiscoveryServlet.class);

    public void init() throws ServletException {
        super.init();
        discoveryService = (DiscoveryService) applicationContext.getBean("discoveryService"); //$NON-NLS-1$
        if (discoveryService == null) {
            throw new ServletException(Messages.getString("Servlet.DiscoveryServiceNotFound")); //$NON-NLS-1$
        }
    }

    public CubeItem[] getCubes(final String sessionId, final String connectionId) throws RpcException {
        List<CubeItem> list;
        try {
            list = this.discoveryService.getCubes(getCurrentUserId(), sessionId, connectionId);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Discovery.CantGenerateCubesList"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateCubesList")); //$NON-NLS-1$
        }
        return list.toArray(new CubeItem[list.size()]);
    }

    public String[] getDimensions(final String sessionId, final String queryId, final IAxis axis) throws RpcException {
        List<String> dimensionsList;
        try {
            dimensionsList = this.discoveryService.getDimensions(getCurrentUserId(), sessionId, queryId, (axis
                    .equals(IAxis.UNUSED)) ? null : org.olap4j.Axis.Standard.valueOf(axis.name()));
            return dimensionsList.toArray(new String[dimensionsList.size()]);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Discovery.CantGenerateDimensionsList"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateDimensionsList")); //$NON-NLS-1$
        }

    }

    public StringTree getMembers(final String sessionId, final String queryId, final String dimensionName)
            throws RpcException {
        try {
            return this.discoveryService.getMembers(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Discovery.CantGenerateMembersList"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Discovery.CantGenerateMembersList")); //$NON-NLS-1$
        }
    }

    public String[] getDrivers() throws RpcException {
        final List<String> driverNames = this.discoveryService.getDrivers();
        return driverNames.toArray(new String[driverNames.size()]);
    }
}
