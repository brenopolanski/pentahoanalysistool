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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Utility class to manipulate {@link FlexTable FlexTables}.
 */
public class FlexTableUtil {

  /**
   * Copy an entire FlexTable from one FlexTable to another. Each element is
   * copied by creating a new {@link HTML} widget by calling
   * {@link FlexTable#getHTML(int, int)} on the source table.
   *
   * @param sourceTable the FlexTable to copy a row from
   * @param targetTable the FlexTable to copy a row to
   * @param sourceRow the index of the source row
   * @param targetRow the index before which to insert the copied row
   */
  public static void copyRow(FlexTable sourceTable, FlexTable targetTable, int sourceRow,
      int targetRow) {
    targetTable.insertRow(targetRow);
    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
      HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
      targetTable.setWidget(targetRow, col, html);
    }
    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
  }

  /**
   * Copy an entire FlexTable from one FlexTable to another. Each element is
   * copied by creating a new {@link HTML} widget by calling
   * {@link FlexTable#getHTML(int, int)} on the source table.
   *
   * @param sourceTable the FlexTable to copy a row from
   * @param targetTable the FlexTable to copy a row to
   * @param sourceRow the index of the source row
   * @param targetRow the index before which to insert the copied row
   */
  public static void copyCell(FlexTable sourceTable, FlexTable targetTable, int sourceRow, int sourceCol,
      int targetRow, int targetCol) {
    targetTable.insertRow(targetRow);
    HTML html = new HTML(sourceTable.getHTML(sourceRow, sourceCol));
    targetTable.setWidget(targetRow, sourceCol, html);
    /*for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
      
      targetTable.setWidget(targetRow, col, html);
    }*/
    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
  }
  /**
   * Move an entire FlexTable from one FlexTable to another. Elements are moved
   * by attempting to call {@link FlexTable#getWidget(int, int)} on the source
   * table. If no widget is found (because <code>null</code> is returned), a
   * new {@link HTML} is created instead by calling
   * {@link FlexTable#getHTML(int, int)} on the source table.
   *
   * @param sourceTable the FlexTable to move a row from
   * @param targetTable the FlexTable to move a row to
   * @param sourceRow the index of the source row
   * @param targetRow the index before which to insert the moved row
   */
  public static void moveRow(FlexTable sourceTable, FlexTable targetTable, int sourceRow,
      int targetRow) {
    if (sourceTable == targetTable && sourceRow >= targetRow) {
      sourceRow++;
    }
    
    targetTable.insertRow(targetRow);
    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
      Widget w = sourceTable.getWidget(sourceRow, col);
      if (w != null) {
        targetTable.setWidget(targetRow, col, w);
      } else {
        HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
        targetTable.setWidget(targetRow, col, html);
      }
    }
    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
    sourceTable.removeRow(sourceRow);
  }

  /**
   * Move an entire FlexTable from one FlexTable to another. Elements are moved
   * by attempting to call {@link FlexTable#getWidget(int, int)} on the source
   * table. If no widget is found (because <code>null</code> is returned), a
   * new {@link HTML} is created instead by calling
   * {@link FlexTable#getHTML(int, int)} on the source table.
   *
   * @param sourceTable the FlexTable to move a row from
   * @param targetTable the FlexTable to move a row to
   * @param sourceRow the index of the source row
   * @param targetRow the index before which to insert the moved row
   */
  public static void moveCell(FlexTable sourceTable, FlexTable targetTable, int sourceRow, int sourceCol,
     int targetRow, int targetColumn) {
	/*  if (sourceTable == targetTable && sourceRow >= targetRow) {
	      sourceRow++;
	    }*/
	  Widget w = sourceTable.getWidget(sourceRow, sourceCol);
	  if (targetRow==0) insertColumn(targetTable,targetRow,targetColumn,w);	  
	  else insertCell(targetTable,targetRow,targetColumn,w, sourceRow, sourceCol);
	  /*    if (sourceTable == targetTable && sourceRow >= targetRow) {
      sourceRow++;
    }
    
    
    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
      Widget w = sourceTable.getWidget(sourceRow, col);
      if (w != null) {
        targetTable.setWidget(targetRow, col, w);
      } else {
        HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
        targetTable.setWidget(targetRow, col, html);
      }
    }
    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
    sourceTable.removeRow(sourceRow);*/
  }
  private static void insertCell(FlexTable targetTable, int targetRow,
		int targetColumn, Widget w, int sourceRow, int sourceColumn) {

	  if (sourceRow != targetRow && sourceColumn!=targetColumn){
		  targetTable.setWidget(targetRow, targetColumn, w);
		
			 targetTable.setWidget(sourceRow, sourceColumn, new Label("")); //$NON-NLS-1$
	  }
}

/**
   * Copies the CSS style of a source row to a target row.
   *
   * @param sourceTable
   * @param targetTable
   * @param sourceRow
   * @param targetRow
   */
  private static void copyRowStyle(FlexTable sourceTable, FlexTable targetTable, int sourceRow,
      int targetRow) {
    String rowStyle = sourceTable.getRowFormatter().getStyleName(sourceRow);
    targetTable.getRowFormatter().setStyleName(targetRow, rowStyle);
  }

  
  public void insertColumn(FlexTable targetTable, Object columnHeading) {
	  	int HeaderRowIndex = 0;
	    Widget widget = createCellWidget(columnHeading);
	    int cell = targetTable.getCellCount(HeaderRowIndex);
	    
	    widget.setWidth("100%"); //$NON-NLS-1$
	    widget.addStyleName("FlexTable-ColumnLabel"); //$NON-NLS-1$
	    
	    targetTable.setWidget(HeaderRowIndex, cell, widget);
	    
	    targetTable.getCellFormatter().addStyleName(
	        HeaderRowIndex, cell,"FlexTable-ColumnLabelCell"); //$NON-NLS-1$
	  }
  
  public static void insertColumn(FlexTable targetTable, int targetRow, int targetColumn, Widget columnHeading) {
	  	int HeaderRowIndex = 0;
	    
	    int cell = targetTable.getCellCount(HeaderRowIndex);
	    if (targetColumn <= cell){
	    
	    columnHeading.setWidth("100%"); //$NON-NLS-1$
	    columnHeading.addStyleName("FlexTable-ColumnLabel"); //$NON-NLS-1$
	    
	    for (int i=0; i < targetTable.getRowCount(); i++){
	    	for (int j=cell; j > targetColumn; j--){
	    	Widget w = targetTable.getWidget(i, j-1);
	    	if (w!=null)targetTable.setWidget(i, j, w);
	    	}
	    }
	    targetTable.setWidget(targetRow, targetColumn, columnHeading);
	    
	    targetTable.getCellFormatter().addStyleName(
	        HeaderRowIndex, cell,"FlexTable-ColumnLabelCell"); //$NON-NLS-1$
	    }
	  }
	  
	  private Widget createCellWidget(Object cellObject) {
	    Widget widget = null;

	    if (cellObject instanceof Widget)
	      widget = (Widget) cellObject;
	    else
	      widget = new Label(cellObject.toString());

	    return widget;
	  }
	  int rowIndex = 1;
	  public void addRow(FlexTable targetTable, Object[] cellObjects) {
	    
	    for (int cell = 0; cell < cellObjects.length; cell++) {
	      Widget widget = createCellWidget(cellObjects[cell]);
	      targetTable.setWidget(rowIndex, cell, widget);
	      targetTable.getCellFormatter().addStyleName(rowIndex,cell,"FlexTable-Cell"); //$NON-NLS-1$
	    }
	    rowIndex++;
	  }
	  
	
}

