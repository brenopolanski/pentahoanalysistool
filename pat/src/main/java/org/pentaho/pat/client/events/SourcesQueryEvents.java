/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Jun 9, 2009 
 * @author Tom Barber
 */
package org.pentaho.pat.client.events;

import org.pentaho.pat.client.listeners.QueryListener;

/**
 * A widget that implements this interface sources the events defined by the
 * {@link org.pentaho.pat.client.deprecated.listeners.QueryListener} interface.
 */
public interface SourcesQueryEvents {

	/**
	 * Adds a listener interface to receive query events.
	 * 
	 * @param listener the listener interface to add
	 */
	void addQueryListener(QueryListener listener);

	/**
	 * Removes a previously added listener interface.
	 * 
	 * @param listener the listener interface to remove
	 */
	void removeQueryListener(QueryListener listener);
}
