package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.i18n.PatConstants;
import org.pentaho.pat.client.i18n.PatMessages;

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
