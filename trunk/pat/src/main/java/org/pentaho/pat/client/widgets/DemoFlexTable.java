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
package org.pentaho.pat.client.widgets;

import org.pentaho.pat.client.util.FlexTableCellDragController;
import org.pentaho.pat.client.util.FlexTableCellDropController;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Table to demonstrate draggable rows and columns.
 */
public final class DemoFlexTable extends FlexTable {

  /**
   * Creates a FlexTable with the desired number of rows and columns, making
   * each row draggable via the provided drag controller.
   * 
   * @param rows desired number of table rows
   * @param cols desired number of table columns
   * @param tableRowDragController the drag controller to enable dragging of
   *            table rows
   */
  public DemoFlexTable(int rows, int cols, FlexTableCellDragController tableCellDragController) {
    addStyleName("demo-flextable");
      HTML handle = new HTML("[drag-here]");
      handle.addStyleName("demo-drag-handle");
      tableCellDragController.makeDraggable(handle);
    for (int col = 0; col < cols; col++) {
	      for (int row = 0; row < rows; row++) {
	        // create a simple panel drop target for the current cell
	        //SimplePanel simplePanel = new SimplePanel();
	        //simplePanel.setPixelSize(100, 100);
	        //setWidget(col, row, handle);
	        

	        // place a pumpkin in each panel in the cells in the first column
	        if (row == 0) {
	          setWidget(col, row, handle);
	        }

	        // instantiate a drop controller of the panel in the current cell
	        //SetWidgetDropController dropController = new SetWidgetDropController(simplePanel);
	        //dragController.registerDropController(dropController);
	        FlexTableCellDropController flexTableRowDropController1 = new FlexTableCellDropController(this);
		    //FlexTableRowDropController flexTableRowDropController2 = new FlexTableRowDropController(table2);
		    tableCellDragController.registerDropController(flexTableRowDropController1);
	      }
	    }
  }
  
}
