/**
 * 
 */
package org.pentaho.pat.client.util;

import org.pentaho.pat.rpc.Discovery;
import org.pentaho.pat.rpc.DiscoveryAsync;
import org.pentaho.pat.rpc.Query;
import org.pentaho.pat.rpc.QueryAsync;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.rpc.SessionAsync;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Tom Barber
 *
 */
public class ServiceFactory {
	
	  static DiscoveryAsync dService = null;
	  
	  public static DiscoveryAsync getDiscoveryInstance() {
	    if (dService == null) {
	      dService = (DiscoveryAsync) GWT.create(Discovery.class);
	      ServiceDefTarget endpoint = (ServiceDefTarget) dService;
	      String moduleRelativeURL = GWT.getModuleBaseURL() + "discovery.rpc"; //$NON-NLS-1$
	      endpoint.setServiceEntryPoint(moduleRelativeURL);
	    }
	    return dService;
	  }
	  
	  static SessionAsync sService = null;
	  
	  public static SessionAsync getSessionInstance() {
	    if (sService == null) {
	      sService = (SessionAsync) GWT.create(Session.class);
	      ServiceDefTarget endpoint = (ServiceDefTarget) sService;
	      String moduleRelativeURL = GWT.getModuleBaseURL() + "session.rpc"; //$NON-NLS-1$
	      endpoint.setServiceEntryPoint(moduleRelativeURL);
	    }
	    return sService;
	  }

	  static QueryAsync qService = null;
	  
	  public static QueryAsync getQueryInstance() {
	    if (qService == null) {
	      qService = (QueryAsync) GWT.create(Query.class);
	      ServiceDefTarget endpoint = (ServiceDefTarget) qService;
	      String moduleRelativeURL = GWT.getModuleBaseURL() + "session.rpc"; //$NON-NLS-1$
	      endpoint.setServiceEntryPoint(moduleRelativeURL);
	    }
	    return qService;
	  }

}
