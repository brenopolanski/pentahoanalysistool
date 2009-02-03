/**
 * 
 */
package org.pentaho.pat.client.util;

import org.pentaho.pat.client.services.Olap4JService;
import org.pentaho.pat.client.services.Olap4JServiceAsync;
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
}
