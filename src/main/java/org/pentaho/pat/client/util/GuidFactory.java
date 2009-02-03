package org.pentaho.pat.client.util;

/**
 * @author Tom Barber
 *
 */
public class GuidFactory {
	  static String guid = null;
	  
	  public static String getGuid() {
	    if (guid == null) {
	      guid = Long.toString(System.currentTimeMillis());
	    }
	    return guid;
	  }
}
