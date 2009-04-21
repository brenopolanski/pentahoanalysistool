package org.pentaho.pat.client.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * A helper class for implementers of the SourcesConnectionEvents interface. This
 * subclass of {@link ArrayList} assumes that all objects added to it will be of
 * type {@link @link org.pentaho.pat.client.ConnectionListener}.
 */
public class ConnectionListenerCollection extends ArrayList {

	/**
	 * Fire connection broken.
	 * 
	 * @param sender the sender
	 */
	public void fireConnectionBroken(final Widget sender) {
		for (final Iterator it = iterator(); it.hasNext();) {
			final ConnectionListener listener = (ConnectionListener) it.next();
			listener.onConnectionBroken(sender);
		}
	}

	/**
	 * Fires a click event to all listeners.
	 * 
	 * @param sender the widget sending the event.
	 */
	public void fireConnectionMade(final Widget sender) {
		for (final Iterator it = iterator(); it.hasNext();) {
			final ConnectionListener listener = (ConnectionListener) it.next();
			listener.onConnectionMade(sender);
		}
	}

}