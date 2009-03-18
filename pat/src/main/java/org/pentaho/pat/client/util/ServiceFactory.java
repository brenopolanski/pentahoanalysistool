/**
 * 
 */
package org.pentaho.pat.client.util;

import org.pentaho.pat.client.services.Olap4JService;
import org.pentaho.pat.client.services.Olap4JServiceAsync;
import org.pentaho.pat.rpc.Discovery;
import org.pentaho.pat.rpc.DiscoveryAsync;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.rpc.SessionAsync;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Tom Barber
 *
 */
public class ServiceFactory {
	  static Olap4JServiceAsync service = null;
	  
	  public static Olap4JServiceAsync getInstance() {
	    if (service == null) {
	      service = (Olap4JServiceAsync) GWT.create(Olap4JService.class);
	      ServiceDefTarget endpoint = (ServiceDefTarget) service;
	      String moduleRelativeURL = GWT.getModuleBaseURL() + "olap4j"; //$NON-NLS-1$
	      endpoint.setServiceEntryPoint(moduleRelativeURL);
	    }
	    return service;
	  }
	  
// TODO activate new services, as soon as RPC can be client-side included 
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
}
