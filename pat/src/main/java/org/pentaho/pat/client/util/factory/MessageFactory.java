package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.i18n.PatMessages;

import com.google.gwt.core.client.GWT;
// TODO: Auto-generated Javadoc

/**
 * The Class MessageFactory.
 * 
 * @author Tom Barber
 */
public class MessageFactory {
	
	/** TODO JAVADOC. */
	private static PatMessages messages = null;
	
	/**
	 * TODO JAVADOC.
	 * 
	 * @return the instance
	 */
	public static PatMessages getInstance() {
		if (messages == null) {
			messages = (PatMessages) GWT.create(PatMessages.class);
		}
		return messages;
	}
}
