/*
 * Copyright 2008 Fred Sauer
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pentaho.pat.client.util;


import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;

/**
 * DropController which allows a widget to be dropped on a SimplePanel drop
 * target when the drop target does not yet have a child widget.
 */
public class FlexTableCellDropController extends AbstractPositioningDropController {
	
	 private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner";

	private int targetRow;
	private int targetColumn;
  private final FlexTable flexTable;
  public FlexTableCellDropController(FlexTable flexTable) {
    super(flexTable);
    this.flexTable = flexTable;
  }
  private Widget positioner = null;
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

  @Override
  public void onDrop(DragContext context) {
	  FlexTableCellDragController trDragController = (FlexTableCellDragController) context.dragController;
	  FlexTableUtil.moveCell(trDragController.getDraggableTable(), flexTable,
		        trDragController.getDragRow(), trDragController.getDragCol(), targetRow+1, targetColumn);
	  //dropTarget.setWidget(context.draggable);
    super.onDrop(context);
  }

  @Override
  public void onPreviewDrop(DragContext context) throws VetoDragException {
  /*  if (dropTarget.getWidget() != null) {
      throw new VetoDragException();
    }*/
    super.onPreviewDrop(context);
  }
  
  @Override
  public void onEnter(DragContext context) {
    super.onEnter(context);
    positioner = newPositioner(context);
  }

  @Override
  public void onMove(DragContext context) {
    super.onMove(context);
    targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
        context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;
    targetColumn = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
            context.mouseX, context.mouseY), LocationWidgetComparator.RIGHT_HALF_COMPARATOR) - 1;

    
    Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);
    Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
    Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
    context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
        + (targetRow == -1 ? 0 : w.getOffsetHeight()));
  }
  
  Widget newPositioner(DragContext context) {
	    Widget p = new SimplePanel();
	    p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
	    p.setPixelSize(flexTable.getOffsetWidth(), 1);
	    return p;
	  }

}

