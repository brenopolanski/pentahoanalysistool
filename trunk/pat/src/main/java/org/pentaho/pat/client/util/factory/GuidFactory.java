package org.pentaho.pat.client.util.factory;

/**
 * @author Tom Barber
 *
 */
public class GuidFactory {
	  /**
	 *TODO JAVADOC
	 */
	static String guid = null;
	  
	  /**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public static String getGuid() {
	    if (guid == null) {
	      guid = Long.toString(System.currentTimeMillis());
	    }
	    return guid;
	  }
}
