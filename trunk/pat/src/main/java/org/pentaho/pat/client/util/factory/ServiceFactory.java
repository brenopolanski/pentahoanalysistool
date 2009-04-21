/**
 * 
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
