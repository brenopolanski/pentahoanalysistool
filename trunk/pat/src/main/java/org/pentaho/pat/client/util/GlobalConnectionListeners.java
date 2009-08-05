/*
 * Copyright (C) 2009 Thomas Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */

package org.pentaho.pat.client.util;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;

/**
 * Register you widget with GlobalConnectionListeners so that it knows when a db connection is established. Rewritten
 * from panel level, to allow for easier connection building.
 * 
 * @created Apr 23, 2009
 * @since 0.3.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class GlobalConnectionListeners implements SourcesConnectionEvents {

    /** ConnectionListenerCollection Object. */
    private final ConnectionListenerCollection connectionListeners = new ConnectionListenerCollection();

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.deprecated.events.SourcesConnectionEvents#addConnectionListener
     * (org.pentaho.pat.client.deprecated.listeners.ConnectionListener)
     */
    /**
     * Initialize the connectionListeners instance.
     */
    public void addConnectionListener(final ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    public void clearAllConnectionListeners() {
        this.connectionListeners.clear();
    }

    public ConnectionListenerCollection getConnectionListeners() {
        return this.connectionListeners;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.pentaho.pat.client.events.SourcesConnectionEvents# removeConnectionListener
     * (org.pentaho.pat.client.deprecated.listeners.ConnectionListener)
     */
    /**
     * Remove a connectionListener
     */
    public void removeConnectionListener(final ConnectionListener listener) {
        this.connectionListeners.remove(listener);
    }

}
