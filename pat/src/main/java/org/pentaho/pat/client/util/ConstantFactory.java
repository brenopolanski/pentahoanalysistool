package org.pentaho.pat.client.util;

import org.pentaho.pat.client.PatConstants;
import org.pentaho.pat.client.PatMessages;

import com.google.gwt.core.client.GWT;
/**
 * @author Tom Barber
 *
 */
public class ConstantFactory {
	  static PatConstants constants = null;
	  public static PatConstants getInstance() {
	    if (constants == null) {
	      constants = (PatConstants) GWT.create(PatConstants.class);
	    }
	    return constants;
	  }
}
