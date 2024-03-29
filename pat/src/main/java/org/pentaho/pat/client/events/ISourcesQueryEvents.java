/*
 * Copyright (C) 2009 Tom Barber
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
package org.pentaho.pat.client.events;

import org.pentaho.pat.client.listeners.IQueryListener;

/**
 * A widget that implements this interface sources the events defined by the
 * {@link org.pentaho.pat.client.listeners.IQueryListener} interface.
 * 
 * @created Jun 9, 2009
 * @since 0.2
 * @author Tom Barber
 */
public interface ISourcesQueryEvents {

    /**
     * Adds a listener interface to receive query events.
     * 
     * @param listener
     *            the listener interface to add
     */
    void addQueryListener(IQueryListener listener);

    /**
     * Removes a previously added listener interface.
     * 
     * @param listener
     *            the listener interface to remove
     */
    void removeQueryListener(IQueryListener listener);
}
