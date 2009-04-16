package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.i18n.PatConstants;

import com.google.gwt.core.client.GWT;
/**
 * @author Tom Barber
 *
 */
public class ConstantFactory {
	 /**
	 *TODO JAVADOC
	 */
	static PatConstants constants = null;
	  /**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public static PatConstants getInstance() {
	    if (constants == null) {
	      constants = (PatConstants) GWT.create(PatConstants.class);
	    }
	    return constants;
	  }
}
