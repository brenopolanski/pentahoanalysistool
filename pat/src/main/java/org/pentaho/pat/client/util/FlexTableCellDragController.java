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

import org.pentaho.pat.client.Pat;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;

/**
 * Allows table rows to dragged by their handle.
 */
public final class FlexTableCellDragController extends PickupDragController {

  private static final String CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY = "demo-FlexTableRowExample-table-proxy";

  private FlexTable draggableTable;

  private int dragRow;

  public FlexTableCellDragController(AbsolutePanel tableExamplePanel) {
    super(Pat.layoutPanel, false);
    setBehaviorDragProxy(true);
    setBehaviorMultipleSelection(false);
  }

  @Override
  public void dragEnd() {
    super.dragEnd();

    // cleanup
    draggableTable = null;
  }

  @Override
  public void setBehaviorDragProxy(boolean dragProxyEnabled) {
    if (!dragProxyEnabled) {
      // TODO implement drag proxy behavior
      throw new IllegalArgumentException();
    }
    super.setBehaviorDragProxy(dragProxyEnabled);
  }

  @Override
  protected BoundaryDropController newBoundaryDropController(AbsolutePanel boundaryPanel,
      boolean allowDroppingOnBoundaryPanel) {
    if (allowDroppingOnBoundaryPanel) {
      throw new IllegalArgumentException();
    }
    return super.newBoundaryDropController(boundaryPanel, allowDroppingOnBoundaryPanel);
  }

  @Override
  protected Widget newDragProxy(DragContext context) {
    SimplePanel proxy;
    proxy = new SimplePanel();
    proxy.addStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
    draggableTable = (FlexTable) context.draggable.getParent().getParent();
    //dragRow = getWidgetRow(context.draggable, draggableTable);
    //FlexTableUtil.copyRow(draggableTable, proxy, dragRow, 0);
    return proxy;
  }

  FlexTable getDraggableTable() {
    return draggableTable;
  }

  int getDragRow() {
    return dragRow;
  }

  private int getWidgetRow(Widget widget, FlexTable table) {
    for (int row = 0; row < table.getRowCount(); row++) {
      for (int col = 0; col < table.getCellCount(row); col++) {
        Widget w = table.getWidget(row, col);
        if (w == widget) {
          return row;
        }
      }
    }
    throw new RuntimeException("Unable to determine widget row");
  }
}
