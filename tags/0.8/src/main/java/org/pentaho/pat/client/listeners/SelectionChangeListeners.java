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

package org.pentaho.pat.client.listeners;

import org.pentaho.pat.client.events.ISourcesSelectionEvents;

/**
 *TODO JAVADOC
 * 
 * @created Jun 2009
 * @since 0.4.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class SelectionChangeListeners implements ISourcesSelectionEvents {
    /** ConnectionListenerCollection Object. */

    private final SelectionListenerCollection selectionListeners = new SelectionListenerCollection();

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.deprecated.events.SourcesConnectionEvents#addConnectionListener
     * (org.pentaho.pat.client.deprecated.listeners.ConnectionListener)
     */
    /**
     * Initialize the connectionListeners instance.
     */
    public void addSelectionListener(final ISelectionListener listener) {
        selectionListeners.add(listener);
    }

    public void clearAllQueryListeners() {
        this.selectionListeners.clear();
    }

    public SelectionListenerCollection getQueryListeners() {
        return this.selectionListeners;
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
    public void removeSelectionListener(final ISelectionListener listener) {
        this.selectionListeners.remove(listener);
    }


}