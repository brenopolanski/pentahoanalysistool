package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.i18n.PatConstants;

import com.google.gwt.core.client.GWT;
// TODO: Auto-generated Javadoc

/**
 * The Class ConstantFactory.
 * 
 * @author Tom Barber
 */
public class ConstantFactory {
	
	/** TODO JAVADOC. */
	private static PatConstants constants = null;
	
	/**
	 * TODO JAVADOC.
	 * 
	 * @return the instance
	 */
	public static PatConstants getInstance() {
		if (constants == null) {
			constants = (PatConstants) GWT.create(PatConstants.class);
		}
		return constants;
	}
}
