package org.pentaho.pat.client.widgets;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.FlexTableCellDragController;
import org.pentaho.pat.client.util.FlexTableCellDropController;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class OlapFlexTable extends FlexTable {
	/**
	   * Creates a FlexTable with the desired number of rows and columns, making
	   * each row draggable via the provided drag controller.
	   * 
	   * @param rows desired number of table rows
	   * @param cols desired number of table columns
	   * @param tableRowDragController the drag controller to enable dragging of
	   *            table rows
	   */
	private static final int HeaderRowIndex = 0;
	  private PickupDragController dragController;

	  public OlapFlexTable(int rows, int cols, FlexTableCellDragController tableCellDragController) {
		  this.insertRow(HeaderRowIndex);
		  this.getRowFormatter().addStyleName(HeaderRowIndex,"FlexTable-Header");
		  addStyleName("demo-flextable");
		  Button execute = new Button("Execute");

	    
	    for (int col = 0; col < cols; col++) {
	    	if(col==0) addColumn(execute);
	    	
	    	else addColumn("Col"+col);
	    	
	    }
	    
	    
	    Object[][] rowData = new Object[rows][cols]; 
	    for (int row = 0; row < rows; row++){
	    	
				   
		        for (int col = 0; col < cols; col++){
		        	HTML handle = new HTML("Dont Drop Here");
		        	
		        	  
				        tableCellDragController.makeDraggable(handle);
				        FlexTableCellDropController flexTableRowDropController1 = new FlexTableCellDropController(this);
						   tableCellDragController.registerDropController(flexTableRowDropController1);
						   if (col == 1 || col == 0)rowData[row][col] = handle;
						   else rowData[row][col] = new Label("");
		       }
		     
		        
	    }
	    
	
	    for (int row = 0; row < rows; row++){
	    	addRow(rowData[row]);
	    }
	    
		/*   for (int col = 0; col < cols; col++) {
			      for (int row = 0; row < rows; row++) {
			    	  HTML handle = new HTML("Dont Drop Here");
			        // create a simple panel drop target for the current cell
			        SimplePanel simplPanel = new SimplePanel();
			        simplPanel.setPixelSize(100, 100);
			        setWidget(col, row+1, simplPanel);
			        
			        //tableCellDragController.makeDraggable(handle);
			        // place a pumpkin in each panel in the cells in the first column
			        if (row == 0) {
			          simplPanel.setWidget(handle);
			        }

			        // instantiate a drop controller of the panel in the current cell
			      FlexTableCellDropController flexTableRowDropController1 = new FlexTableCellDropController(simplPanel, this);
					   tableCellDragController.registerDropController(flexTableRowDropController1);
			      }
			    }*/

	      this.setBorderWidth(5);
	      
	  //}
	 	  }
	  private void addColumn(Object columnHeading) {
		    Widget widget = createCellWidget(columnHeading);
		    int cell = getCellCount(HeaderRowIndex);
		    
		    widget.setWidth("100%");
		    widget.addStyleName("FlexTable-ColumnLabel");
		    
		    setWidget(HeaderRowIndex, cell, widget);
		    
		    getCellFormatter().addStyleName(
		        HeaderRowIndex, cell,"FlexTable-ColumnLabelCell");
		  }
		  
		  private Widget createCellWidget(Object cellObject) {
		    Widget widget = null;
		    
		    if (cellObject instanceof Widget)
		      widget = (Widget) cellObject;
		    else if(cellObject!=null)
		      widget = new Label(cellObject.toString());

		    return widget;
		  }
		  int rowIndex = 1;
		  private void addRow(Object[] cellObjects) {
		    
		    for (int cell = 0; cell < cellObjects.length; cell++) {
		      Widget widget = createCellWidget(cellObjects[cell]);
		      setWidget(rowIndex, cell, widget);
		      getCellFormatter().addStyleName(rowIndex,cell,"FlexTable-Cell");
		    }
		    rowIndex++;
		  }
		  
		  private void applyDataRowStyles() {
		    HTMLTable.RowFormatter rf = getRowFormatter();
		    
		    for (int row = 1; row < getRowCount(); ++row) {
		      if ((row % 2) != 0) {
		        rf.addStyleName(row, "FlexTable-OddRow");
		      }
		      else {
		        rf.addStyleName(row, "FlexTable-EvenRow");
		      }
		    }
		  }
}
