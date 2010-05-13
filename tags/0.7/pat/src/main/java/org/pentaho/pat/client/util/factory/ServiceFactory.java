/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */
package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.rpc.IDiscovery;
import org.pentaho.pat.rpc.IDiscoveryAsync;
import org.pentaho.pat.rpc.IPlatform;
import org.pentaho.pat.rpc.IPlatformAsync;
import org.pentaho.pat.rpc.IQuery;
import org.pentaho.pat.rpc.IQueryAsync;
import org.pentaho.pat.rpc.ISession;
import org.pentaho.pat.rpc.ISessionAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * The Class ServiceFactory.
 * 
 * @created Aug 3, 2009
 * @since 0.4.0
 * @author Tom Barber
 */
public class ServiceFactory {

    /** The discovery service. */
    private static IDiscoveryAsync dService = null;

    /** The session service. */
    private static ISessionAsync sService = null;

    /** The query service. */
    private static IQueryAsync qService = null;
    
    /** The platform service */
    private static IPlatformAsync pService = null;

    /**
     * Gets the discovery instance.
     * 
     * @return the discovery instance
     */
    public static IDiscoveryAsync getDiscoveryInstance() {
        if (dService == null) {
            dService = (IDiscoveryAsync) GWT.create(IDiscovery.class);
            final ServiceDefTarget endpoint = (ServiceDefTarget) dService;
            final String moduleRelativeURL = Pat.getBaseUrl() + "discovery.rpc"; //$NON-NLS-1$
            endpoint.setServiceEntryPoint(moduleRelativeURL);
        }
        return dService;
    }

    /**
     * Gets the query instance.
     * 
     * @return the query instance
     */
    public static IQueryAsync getQueryInstance() {
        if (qService == null) {
            qService = (IQueryAsync) GWT.create(IQuery.class);
            final ServiceDefTarget endpoint = (ServiceDefTarget) qService;
            final String moduleRelativeURL = Pat.getBaseUrl()  + "query.rpc"; //$NON-NLS-1$
            endpoint.setServiceEntryPoint(moduleRelativeURL);
        }
        return qService;
    }

    /**
     * Gets the session instance.
     * 
     * @return the session instance
     */
    public static ISessionAsync getSessionInstance() {
        if (sService == null) {
            sService = (ISessionAsync) GWT.create(ISession.class);
            final ServiceDefTarget endpoint = (ServiceDefTarget) sService;
            final String moduleRelativeURL = Pat.getBaseUrl()  + "session.rpc"; //$NON-NLS-1$
            endpoint.setServiceEntryPoint(moduleRelativeURL);
        }
        return sService;
    }
    
    /**
     * Gets the platform instance.
     * 
     * @return the platform instance
     */
    public static IPlatformAsync getPlatformInstance() {
        if (pService == null) {
            pService = (IPlatformAsync) GWT.create(IPlatform.class);
            final ServiceDefTarget endpoint = (ServiceDefTarget) pService;
            final String moduleRelativeURL = Pat.getBaseUrl()  + "platform.rpc"; //$NON-NLS-1$
            endpoint.setServiceEntryPoint(moduleRelativeURL);
        }
        return pService;
    }

}
