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

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.ui.widgets.MeasureGrid;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.rpc.dto.IAxis;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
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

    /** The Drop Axis. */
    private org.pentaho.pat.rpc.dto.IAxis targetAxis;

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
        if(context.draggable instanceof MeasureLabel && ((MeasureLabel)context.draggable).getType() == MeasureLabel.LabelType.MEASURE){
        this.flexTable.setEngaged(true);
        }

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
        if(context.draggable instanceof MeasureLabel){
        this.flexTable.setEngaged(false);
        }
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
        final Widget widget = flexTable.getWidget(TARGETROW, 0);
        final Location widgetLocation = new WidgetLocation(widget, context.boundaryPanel);
        final Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
        context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop() + (widget.getOffsetHeight()));
    }

    @Override
    public void onPreviewDrop(final DragContext context) throws VetoDragException {
        final FlexTableRowDragController trDragController = (FlexTableRowDragController) context.dragController;
        super.onPreviewDrop(context);
        
        if(flexTable.getWidget(TARGETROW, 0) instanceof MeasureLabel && ((MeasureLabel)flexTable.getWidget(TARGETROW, 0)).getType().equals(MeasureLabel.LabelType.MEASURE) 
                && !Pat.getMeasuresAxis().equals(IAxis.UNUSED) && !flexTable.getAxis().equals(Pat.getMeasuresAxis()) && !flexTable.getAxis().equals(IAxis.UNUSED)){
            throw new VetoDragException();
        }
        else if ((trDragController.getDraggableTable().getAxis()==null && (!flexTable.getAxis().equals(Pat.getMeasuresAxis())&& !flexTable.getAxis().equals(IAxis.UNUSED)) && !Pat.getMeasuresAxis().equals(IAxis.UNUSED))){
            throw new VetoDragException();
        }
        else if((trDragController.getDraggableTable().getParent().getParent().getParent() instanceof MeasureGrid && ((DimensionFlexTable)trDragController.getDraggableTable().getParent().getParent().getParent().getParent()).getAxis().equals(flexTable.getAxis()))){
            throw new VetoDragException();
        }
        else if(trDragController.getDraggableTable() == flexTable){
            throw new VetoDragException();
        }
    }

    /**
     * Positioner.
     * 
     * @param context
     *            the context
     * 
     * @return the widget
     */
    private Widget newPositioner(final DragContext context) {
        final Widget panel = new SimplePanel();
        panel.addStyleName(CSS_DEMO_TABLE_POSITIONER);
        panel.setPixelSize(flexTable.getOffsetWidth(), 1);
        return panel;
    }

}
