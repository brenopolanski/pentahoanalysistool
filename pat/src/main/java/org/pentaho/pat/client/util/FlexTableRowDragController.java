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
package org.pentaho.pat.client.util;

import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * FlexTableRowDragController allows you to drag cell widgets from flextable to flextable.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class FlexTableRowDragController extends PickupDragController {

	/** CSS tag. */
	private static final String CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY = "demo-FlexTableRowExample-table-proxy"; //$NON-NLS-1$

	/** The flextable. */
	private DimensionFlexTable draggableTable;

	/** The source row. */
	private int dragRow;

	/** The source row. */
    private int dragCol;

	/**
	 * Constructor.
	 * 
	 * @param boundaryPanel the boundary panel
	 */
	public FlexTableRowDragController(final AbsolutePanel boundaryPanel) {
		super(boundaryPanel, false);
		setBehaviorDragProxy(true);
		setBehaviorMultipleSelection(false);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#dragEnd()
	 */
	/**
	 * Fire on drag end.
	 */
	@Override
	public void dragEnd() {
		super.dragEnd();

		// cleanup
		draggableTable = null;
	}

	/**
	 * get the source table.
	 * 
	 * @return the draggable table
	 */
	DimensionFlexTable getDraggableTable() {
		return draggableTable;
	}

	/**
	 * Get the source row.
	 * 
	 * @return the drag row
	 */
	int getDragRow() {
		return dragRow;
	}

	/**
     * Get the source column.
     * 
     * @return the drag row
     */
    int getDragCol() {
        return dragCol;
    }
    
	/**
	 * Get the Widget Row.
	 * 
	 * @param widget the widget
	 * @param table the table
	 * 
	 * @return the widget row
	 */
	private int getWidgetRow(final Widget widget, final DimensionFlexTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			for (int col = 0; col < table.getCellCount(row); col++) {
				final Widget w = table.getWidget(row, col);
				if (w == widget) {
					return row;
				}
			}
		}
		throw new RuntimeException("Unable to determine widget row"); //$NON-NLS-1$ // NOPMD by bugg on 20/04/09 20:17
	}

    /**
     * Get the Widget Row.
     * 
     * @param widget the widget
     * @param table the table
     * 
     * @return the widget row
     */
    private int getWidgetCol(final Widget widget, final DimensionFlexTable table) {
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getCellCount(row); col++) {
                final Widget w = table.getWidget(row, col);
                if (w == widget) {
                    return col;
                }
            }
        }
        throw new RuntimeException("Unable to determine widget col"); //$NON-NLS-1$ // NOPMD by bugg on 20/04/09 20:17
    }

	
	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#newBoundaryDropController(com.google.gwt.user.client.ui.AbsolutePanel, boolean)
	 */
	/**
	 *  Boundary Controller
	 *  @param boundaryPanel the boundaryPanel
	 *  @param allowDroppingOnBoundaryPanel boolean to allow drops
	 *  
	 *  @return the BoundaryDropContoller
	 */
	@Override
	protected BoundaryDropController newBoundaryDropController(final AbsolutePanel boundaryPanel, final boolean allowDroppingOnBoundaryPanel) {
		if (allowDroppingOnBoundaryPanel) {
			throw new IllegalArgumentException();
		}
		return super.newBoundaryDropController(boundaryPanel, allowDroppingOnBoundaryPanel);
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#newDragProxy(com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	/**
	 * New Drag Proxy
	 * @param context the context
	 * 
	 * @return the proxy widget
	 */
	@Override
	protected Widget newDragProxy(final DragContext context) {
		DimensionFlexTable proxy;
		proxy = new DimensionFlexTable();
		proxy.addStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
		draggableTable = (DimensionFlexTable) context.draggable.getParent();
		dragRow = getWidgetRow(context.draggable, draggableTable);
		dragCol = getWidgetCol(context.draggable, draggableTable);
		FlexTableUtil.copyRow(draggableTable, proxy, dragRow, 0);
		return proxy;
	}

	/* (non-Javadoc)
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#setBehaviorDragProxy(boolean)
	 */
	/**
	 * The proxy drag behaviour
	 * 
	 * @param dragProxyEnabled boolean indicator
	 */
	@Override
	public void setBehaviorDragProxy(final boolean dragProxyEnabled) {
		if (!dragProxyEnabled) {
			// TODO implement drag proxy behavior
			throw new IllegalArgumentException();
		}
		super.setBehaviorDragProxy(dragProxyEnabled);
	}

}
