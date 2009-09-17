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

import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.user.client.ui.Widget;

/**
 * A helper class for implementers of the SourcesConnectionEvents interface. This
 * subclass of {@link ArrayList} assumes that all objects added to it will be of
 * type {@link org.pentaho.pat.client.deprecated.listeners.ConnectionListener}.
 */
public class QueryListenerCollection extends ArrayList<QueryListener> {

	private static final long serialVersionUID = 1L;

	public void fireQueryChanged(final Widget sender){
		for(QueryListener listener:this){
			listener.onQueryChange(sender);
		}
	}
	
	public void fireQueryExecuted(final Widget sender,final String queryId, final CellDataSet matrix) {
		for(QueryListener listener:this) {
			listener.onQueryExecuted(queryId, matrix);
		}
	}
}
