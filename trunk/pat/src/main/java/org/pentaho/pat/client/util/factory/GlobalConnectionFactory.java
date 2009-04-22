/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.util.GlobalConnectionListeners;

import com.google.gwt.core.client.GWT;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class GlobalConnectionFactory {

	public static GlobalConnectionListeners gcl;
	
	public static GlobalConnectionListeners getInstance() {
		if (gcl == null) {
			gcl = (GlobalConnectionListeners) GWT.create(GlobalConnectionListeners.class);
		}
		return gcl;
	}
}
