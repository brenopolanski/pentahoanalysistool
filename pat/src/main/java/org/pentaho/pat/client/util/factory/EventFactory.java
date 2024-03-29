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
package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.listeners.LabelChangeListeners;
import org.pentaho.pat.client.listeners.QueryChangeListeners;
import org.pentaho.pat.client.listeners.SelectionChangeListeners;
import org.pentaho.pat.client.listeners.OperationListener;

import com.google.gwt.core.client.GWT;

/**
 * Create Global Connection Factory to store connection listeners
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class EventFactory {

    protected static QueryChangeListeners qcl;

    public static QueryChangeListeners getQueryInstance() {
        if (qcl == null) {
            qcl = (QueryChangeListeners) GWT.create(QueryChangeListeners.class);
        }
        return qcl;
    }
    
    protected static OperationListener tl;

    public static OperationListener getOperationInstance() {
        if (tl == null) {
            tl = (OperationListener) GWT.create(OperationListener.class);
        }
        return tl;
    }

    protected static SelectionChangeListeners sl;

    public static SelectionChangeListeners getSelectionInstance() {
        if (sl == null) {
            sl = (SelectionChangeListeners) GWT.create(SelectionChangeListeners.class);
        }
        return sl;
    }

    protected static LabelChangeListeners lc;

    public static LabelChangeListeners getLabelInstance() {
        if (lc == null) {
            lc = (LabelChangeListeners) GWT.create(LabelChangeListeners.class);
        }
        return lc;
    }

}
