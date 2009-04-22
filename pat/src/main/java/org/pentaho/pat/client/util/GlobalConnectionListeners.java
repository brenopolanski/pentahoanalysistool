/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.util;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class GlobalConnectionListeners implements SourcesConnectionEvents  {

	/** TODO JAVADOC. */
	public static ConnectionListenerCollection connectionListeners;
	
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#addConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void addConnectionListener(ConnectionListener listener) {

		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#removeConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}

	
}
