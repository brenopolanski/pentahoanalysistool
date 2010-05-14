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

package org.pentaho.pat.client.listeners;

import java.util.ArrayList;

import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.ui.Widget;

/**
 * A helper class for implementers of the SourcesConnectionEvents interface. This subclass of {@link ArrayList} assumes
 * that all objects added to it will be of type {@link org.pentaho.pat.client.listeners.IQueryListener}.
 * 
 * @created Apr 23, 2009
 * @author tom(at)wamonline.org.uk
 */
public class SelectionListenerCollection extends ArrayList<ISelectionListener> {

    private static final long serialVersionUID = 1L;

    /**
     * Fires to all classes that implement QueryListener that the query has changed
     * 
     * @param sender
     *            The sender widget of the query changed event
     * @param tree 
     * @param iAxis 
     */
    public void fireSelectionChanged(final Widget sender, StringTree tree, final String type) {
    	ArrayList<ISelectionListener> queryChangedList = (ArrayList<ISelectionListener>) this.clone();
        for (ISelectionListener listener : queryChangedList) {
            listener.onSelectionChange(sender, tree, type);
        }
    }

}
