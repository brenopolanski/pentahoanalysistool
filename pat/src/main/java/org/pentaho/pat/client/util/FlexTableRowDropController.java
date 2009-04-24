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
package org.pentaho.pat.client.util;

import org.pentaho.pat.rpc.beans.Axis;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * TODO JAVADOC.
 * 
 * @author bugg
 */
public class FlexTableRowDropController extends AbstractPositioningDropController {

	/** TODO JAVADOC. */
	private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner"; //$NON-NLS-1$

	/** TODO JAVADOC. */
	private final FlexTable flexTable;

	/** TODO JAVADOC. */
	private final IndexedPanel flexTableRowsAsIndexPanel = new IndexedPanel() {

		public Widget getWidget(final int index) {
			return flexTable.getWidget(index, 0);
		}

		public int getWidgetCount() {
			return flexTable.getRowCount();
		}

		public int getWidgetIndex(final Widget child) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(final int index) {
			throw new UnsupportedOperationException();
		}
	};

	/** TODO JAVADOC. */
	private Widget positioner = null;

	/** TODO JAVADOC. */
	private int targetRow;

	/** TODO JAVADOC. */
	private org.pentaho.pat.rpc.beans.Axis targetAxis;

	/**
	 * TODO JAVADOC.
	 * 
	 * @param flexTable the flex table
	 */
	public FlexTableRowDropController(final FlexTable flexTable) {
		super(flexTable);
		this.flexTable = flexTable;
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @param flexTable the flex table
	 * @param rows the rows
	 */
	public FlexTableRowDropController(final FlexTable flexTable, final Axis rows) {
		super(flexTable);
		this.flexTable = flexTable;
		targetAxis = rows;
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @param context the context
	 * 
	 * @return the widget
	 */
	Widget newPositioner(final DragContext context) {
		final Widget p = new SimplePanel();
		p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
		p.setPixelSize(flexTable.getOffsetWidth(), 1);
		return p;
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onDrop(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onDrop(final DragContext context) {
		final FlexTableRowDragController trDragController = (FlexTableRowDragController) context.dragController;
		FlexTableUtil.moveRow(trDragController.getDraggableTable(), flexTable, trDragController.getDragRow(), targetRow + 1, targetAxis);
		super.onDrop(context);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onEnter(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onEnter(final DragContext context) {
		super.onEnter(context);
		positioner = newPositioner(context);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onLeave(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onLeave(final DragContext context) {
		positioner.removeFromParent();
		positioner = null;
		super.onLeave(context);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onMove(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onMove(final DragContext context) {
		super.onMove(context);
		targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(context.mouseX, context.mouseY),
				LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;

		final Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);
		final Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
		final Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
		context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop() + (targetRow == -1 ? 0 : w.getOffsetHeight()));
	}

}
