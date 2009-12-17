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
package org.pentaho.pat.client.util.dnd;

import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.rpc.dto.IAxis;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * FlexTableRowDropConroller allows flextable cell drops.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class FlexTableRowDropController extends AbstractPositioningDropController {

    /** CSS Tag. */
    private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner"; //$NON-NLS-1$

    /** Flextable object. */
    private final DimensionFlexTable flexTable;

    /** Positioner Widget. */
    private Widget positioner = null;

    /** Target Row. */
    private final static int TARGETROW = 0;

    /** Target Row. */
    private final static int TARGETCOL = 0;
    private int targetRow;


    /** The Drop Axis. */
    private org.pentaho.pat.rpc.dto.IAxis targetAxis;

    private IndexedPanel flexTableRowsAsIndexPanel = new IndexedPanel() {

	    public Widget getWidget(int index) {
	      return flexTable.getWidget(index, 0);
	    }

	    public int getWidgetCount() {
	      return flexTable.getRowCount();
	    }

	    public int getWidgetIndex(Widget child) {
	      throw new UnsupportedOperationException();
	    }

	    public boolean remove(int index) {
	      throw new UnsupportedOperationException();
	    }
	  };


    
    /**
     * Constructor.
     * 
     * @param flexTable
     *            the flex table
     */
    public FlexTableRowDropController(final DimensionFlexTable flexTable) {
        super(flexTable);
        this.flexTable = flexTable;
    }

    /**
     * Constructor.
     * 
     * @param flexTable
     *            the flex table
     * @param rows
     *            the rows
     */
    public FlexTableRowDropController(final DimensionFlexTable flexTable, final IAxis rows) {
        super(flexTable);
        this.flexTable = flexTable;
        targetAxis = rows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onDrop(com.allen_sauer.gwt.dnd.client.DragContext)
     */
    /**
     * Fires on drop.
     * 
     * @param context
     *            the context
     */
    @Override
    public void onDrop(final DragContext context) {
        final FlexTableRowDragController trDragController = (FlexTableRowDragController) context.dragController;
        FlexTableUtil.moveRow(trDragController.getDraggableTable(), flexTable, trDragController.getDragRow(),
                TARGETROW + 1, trDragController.getDragCol(), TARGETCOL + 1, targetAxis);
        super.onDrop(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onEnter(com.allen_sauer.gwt.dnd.client.DragContext)
     */
    /**
     * Fires when the widget enters the drop zone.
     * 
     * @param context
     *            the context
     */
    @Override
    public void onEnter(final DragContext context) {
        super.onEnter(context);
        positioner = newPositioner(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onLeave(com.allen_sauer.gwt.dnd.client.DragContext)
     */
    /**
     * Fires on leaving the drop zone.
     * 
     * @param context
     *            the context
     */
    @Override
    public void onLeave(final DragContext context) {
        positioner.removeFromParent();
        positioner = null;
        super.onLeave(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.allen_sauer.gwt.dnd.client.drop.AbstractDropController#onMove(com.allen_sauer.gwt.dnd.client.DragContext)
     */
    /**
     * Fires when the widget is moved.
     * 
     * @param context
     *            the context
     */
    @Override
    public void onMove(final DragContext context) {
        super.onMove(context);
        targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
                context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;
/*
        final Widget w = flexTable.getWidget(targetRow, 0);
        final Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
        final Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
        context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop() + (w.getOffsetHeight()));
  */      

        if (flexTable.getRowCount() > 0) {
          Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);
          Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
          Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
          context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
              + (targetRow == -1 ? 0 : w.getOffsetHeight()));
        }

    }

    @Override
    public void onPreviewDrop(final DragContext context) throws VetoDragException {
        final FlexTableRowDragController trDragController = (FlexTableRowDragController) context.dragController;
        super.onPreviewDrop(context);
        if (trDragController.getDraggableTable() == flexTable)
            throw new VetoDragException();
    }

    /**
     * Positioner.
     * 
     * @param context
     *            the context
     * 
     * @return the widget
     */
    Widget newPositioner(final DragContext context) {
        final Widget p = new SimplePanel();
        p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
        p.setPixelSize(flexTable.getOffsetWidth(), 1);
        return p;
    }

}
