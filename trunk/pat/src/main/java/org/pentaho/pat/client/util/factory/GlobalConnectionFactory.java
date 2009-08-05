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

package org.pentaho.pat.client.util.factory;

import org.pentaho.pat.client.util.GlobalConnectionListeners;
import org.pentaho.pat.client.util.QueryChangeListeners;

import com.google.gwt.core.client.GWT;

/**
 * Create Global Connection Factory to store connection listeners
 * 
 * @created Apr 23 2009
 * @since 0.3.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class GlobalConnectionFactory {

    private static GlobalConnectionListeners gcl;

    public static QueryChangeListeners qcl;

    public static GlobalConnectionListeners getInstance() {
        if (gcl == null)
            gcl = (GlobalConnectionListeners) GWT.create(GlobalConnectionListeners.class);
        return gcl;
    }

    public static QueryChangeListeners getQueryInstance() {
        if (qcl == null)
            qcl = (QueryChangeListeners) GWT.create(QueryChangeListeners.class);
        return qcl;
    }
}
