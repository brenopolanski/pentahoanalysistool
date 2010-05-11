/*
 * Copyright (C) 2009 Paul Stoellberger
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

import org.pentaho.pat.client.util.Operation;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.user.client.ui.Widget;

/**
 * This subclass of {@link ArrayList} assumes
 * that all objects added to it will be of type {@link org.pentaho.pat.client.listeners.ITableListener}.
 * 
 * @created Apr 14, 2010
 * @author Paul Stoellberger
 */
public class TableListenerCollection extends ArrayList<ITableListener> {

    /**
     * 
     */
    private static final long serialVersionUID = -3567349113647466922L;


    public void fireOperationExecuted(final Widget sender, String queryId, Operation operation) {
    	ArrayList<ITableListener> tableList = (ArrayList<ITableListener>) this.clone();
        for (ITableListener listener : tableList) {
            listener.onOperationExecuted(queryId, operation);
        }
    }

    public void fireDrillThroughExecuted(final Widget sender, String queryId, String[][] result) {
        ArrayList<ITableListener> tableList = (ArrayList<ITableListener>) this.clone();
        for (ITableListener listener : tableList) {
            listener.onDrillThroughExecuted(queryId, result);
        }
    }


}
