/*
 * Copyright (C) 2010 Thomas Barber
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
package org.pentaho.pat.client.util.dnd;

import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @created Feb 25, 2010
 * @since 0.6.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public interface FlexTableRowDragController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#dragEnd()
	 */
	/**
	 * Fire on drag end.
	 */
	public abstract void dragEnd();

	/**
	 * get the source table.
	 * 
	 * @return the draggable table
	 */
	public abstract MeasureLabel getDraggableTable();

	/**
	 * Get the source row.
	 * 
	 * @return the drag row
	 */
	public abstract int getDragRow();

	/**
	 * Get the source column.
	 * 
	 * @return the drag row
	 */
	public abstract int getDragCol();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#setBehaviorDragProxy(boolean)
	 */
	/**
	 * The proxy drag behaviour
	 * 
	 * @param dragProxyEnabled
	 *            boolean indicator
	 */
	public abstract void setBehaviorDragProxy(final boolean dragProxyEnabled);

	public abstract void makeDraggable(Widget widget);

	public abstract void makeNotDraggable(Widget widget);

}