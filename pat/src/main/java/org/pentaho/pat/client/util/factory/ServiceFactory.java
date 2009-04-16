/**
 * 
 */
package org.pentaho.pat.client.util.factory;


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

// TODO activate new services, as soon as RPC can be client-side included 
	  /**
	 *TODO JAVADOC
	 */
	static DiscoveryAsync dService = null;
	  
	  /**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public static DiscoveryAsync getDiscoveryInstance() {
	    if (dService == null) {
	      dService = (DiscoveryAsync) GWT.create(Discovery.class);
	      ServiceDefTarget endpoint = (ServiceDefTarget) dService;
	      String moduleRelativeURL = GWT.getModuleBaseURL() + "discovery.rpc"; //$NON-NLS-1$
	      endpoint.setServiceEntryPoint(moduleRelativeURL);
	    }
	    return dService;
	  }
	  
	  /**
	 *TODO JAVADOC
	 */
	static SessionAsync sService = null;
	  
	  /**
	 *TODO JAVADOC
	 *
	 * @return
	 */
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
