/**
 * 
 */
package org.pentaho.pat.client.panels;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;

import com.gwtext.client.widgets.Window;

/**
 * @author Tom Barber
 *
 */
public class ConnectionPanel extends Window implements SourcesConnectionEvents{
/*TODO
 * The Connection Panel is the Window that pops up when a user needs to establish a connection to a/the mondrian server.
 * I like the design of Kettles and it would help people who are familiar with Pentaho. It also needs to support XML/A, and provide
 * feedback when the connection is made.	
 */
	static String queryTypeGroup = "QUERY_TYPE"; //$NON-NLS-1$
	boolean connectionEstablished = false;
	ConnectionListenerCollection connectionListeners;

	public ConnectionPanel() {
		super();

		init();
	}
	
	public void init() {

	}
	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * addConnectionListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	public void addConnectionListener(ConnectionListener listener) {
		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * removeClickListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	public void removeClickListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}

}
