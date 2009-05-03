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

package org.pentaho.pat.client.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;

/**
 * A helper class for implementers of the SourcesConnectionEvents interface. This
 * subclass of {@link ArrayList} assumes that all objects added to it will be of
 * type {@link org.pentaho.pat.client.listeners.ConnectionListener}.
 */
public class ConnectionListenerCollection extends ArrayList<ConnectionListener> {

	private static final long serialVersionUID = 1L;
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