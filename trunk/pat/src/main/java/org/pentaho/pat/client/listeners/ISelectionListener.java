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

import java.util.EventListener;

import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.query.IAxis;

import com.google.gwt.user.client.ui.Widget;

/**
 * The Interface ConnectionListener.
 * 
 * @author tom(at)wamonline.org.uk
 */
public interface ISelectionListener extends EventListener {

    void onSelectionChange(String queryID, Widget sender, StringTree tree, String type);

	void onSelectionCleared(String currQuery, MeasureLabel label, int[] is, IAxis iAxis);

    void onMoveRow(String currQuery, int oldrow, int newrow);

    void onMoveCol(String currQuery, int oldcol, int newcol);

}
