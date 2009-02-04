package org.pentaho.pat.client.util;

import org.pentaho.pat.client.Messages;

import com.google.gwt.core.client.GWT;
/**
 * @author Tom Barber
 *
 */
public class MessageFactory {
	  static Messages messages = null;
	  public static Messages getInstance() {
	    if (messages == null) {
	      messages = (Messages) GWT.create(Messages.class);
	    }
	    return messages;
	  }
}
