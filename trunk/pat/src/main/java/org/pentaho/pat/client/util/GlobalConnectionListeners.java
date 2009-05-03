/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */
package org.pentaho.pat.client.util;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;

/**
 * Register you widget with GlobalConnectionListeners so that it knows when a db connection is established.
 * Rewritten from panel level, to allow for easier connection building.
 * @author tom(at)wamonline.org.uk
 *
 */
public class GlobalConnectionListeners implements SourcesConnectionEvents  {

	/** ConnectionListenerCollection Object. */
	private static ConnectionListenerCollection connectionListeners;
	
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#addConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	/**
	 * Initialize the connectionListeners instance.
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
	/**
	 * Remove a connectionListener
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}

	public static ConnectionListenerCollection getConnectionListeners() {
		return connectionListeners;
	}

	public static void setConnectionListeners(
			ConnectionListenerCollection connectionListeners) {
		GlobalConnectionListeners.connectionListeners = connectionListeners;
	}

	
}
