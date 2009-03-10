package org.pentaho.pat.client.util;

import org.pentaho.pat.client.PatMessages;

import com.google.gwt.core.client.GWT;
/**
 * @author Tom Barber
 *
 */
public class MessageFactory {
	  static PatMessages messages = null;
	  public static PatMessages getInstance() {
	    if (messages == null) {
	      messages = (PatMessages) GWT.create(PatMessages.class);
	    }
	    return messages;
	  }
}
