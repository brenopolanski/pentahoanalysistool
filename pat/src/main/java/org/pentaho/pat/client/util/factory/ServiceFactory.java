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

import org.pentaho.pat.rpc.Discovery;
import org.pentaho.pat.rpc.DiscoveryAsync;
import org.pentaho.pat.rpc.Query;
import org.pentaho.pat.rpc.QueryAsync;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.rpc.SessionAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

// TODO: Auto-generated Javadoc
/**
 * The Class ServiceFactory.
 * 
 * @author Tom Barber
 */
public class ServiceFactory {

	/** The d service. */
	private static DiscoveryAsync dService = null;

	/** The s service. */
	private static SessionAsync sService = null;

	/** The q service. */
	private static QueryAsync qService = null;

	/**
	 * Gets the discovery instance.
	 * 
	 * @return the discovery instance
	 */
	public static DiscoveryAsync getDiscoveryInstance() {
		if (dService == null) {
			dService = (DiscoveryAsync) GWT.create(Discovery.class);
			final ServiceDefTarget endpoint = (ServiceDefTarget) dService;
			final String moduleRelativeURL = GWT.getModuleBaseURL() + "discovery.rpc"; //$NON-NLS-1$
			endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		return dService;
	}

	/**
	 * Gets the query instance.
	 * 
	 * @return the query instance
	 */
	public static QueryAsync getQueryInstance() {
		if (qService == null) {
			qService = (QueryAsync) GWT.create(Query.class);
			final ServiceDefTarget endpoint = (ServiceDefTarget) qService;
			final String moduleRelativeURL = GWT.getModuleBaseURL() + "query.rpc"; //$NON-NLS-1$
			endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		return qService;
	}

	/**
	 * Gets the session instance.
	 * 
	 * @return the session instance
	 */
	public static SessionAsync getSessionInstance() {
		if (sService == null) {
			sService = (SessionAsync) GWT.create(Session.class);
			final ServiceDefTarget endpoint = (ServiceDefTarget) sService;
			final String moduleRelativeURL = GWT.getModuleBaseURL() + "session.rpc"; //$NON-NLS-1$
			endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		return sService;
	}

}
