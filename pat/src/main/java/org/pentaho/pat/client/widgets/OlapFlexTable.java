package org.pentaho.pat.client.widgets;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.FlexTableCellDragController;

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
	    HTML handle = new HTML("Dont Drop Here");
	    /*HTML other = new HTML("Blank");
	    setWidget(0,0, execute);
	    setHTML(0,1, "Blank");
	    setWidget(1,1, handle);
	    setWidget(1,0,other);*/
	    tableCellDragController.makeDraggable(handle);
		/*tableRowDragController.makeDraggable(other);  
		*/
		// initialize our drag controller

	    
	    for (int col = 0; col < cols; col++) {
	    	if(col==0) addColumn(execute);
	    	
	    	else addColumn("Col"+col);
	    	
	    }
	    Object[][] rowData = new Object[rows][cols]; 
	    for (int row = 0; row < rows; row++){
	    	
	    	SimplePanel simplePanel = new SimplePanel();
		        simplePanel.setPixelSize(100, 100);
		        //simplePanel.setWidget(handle);
		       // SetWidgetDropController flexTableRowDropController1 = new SetWidgetDropController(simplePanel, this);
			   //tableCellDragController.registerDropController(flexTableRowDropController1);
		        for (int col = 0; col < cols; col++){
		        	
		        	System.out.println(row);
		    	   System.out.println(col);
		    	   rowData[row][col] = simplePanel;
		    	         
		       }
		        System.out.println(rowData[0]);
		        
	    }
	    
	    for (int row = 0; row < rows; row++){
	    	addRow(rowData[row]);
	    }
	    
/*		   for (int col = 0; col < cols; col++) {
			      for (int row = 0; row < rows; row++) {
			        // create a simple panel drop target for the current cell
			        SimplePanel simplePanel = new SimplePanel();
			        simplePanel.setPixelSize(100, 100);
			        setWidget(col, row, simplePanel);
			        

			        // place a pumpkin in each panel in the cells in the first column
			        if (row == 0) {
			          simplePanel.setWidget(handle);
			        }

			        // instantiate a drop controller of the panel in the current cell
			        
			      }
			    }
*/
	      this.setBorderWidth(5);
	      
	  //}
	 	  }
	  private void addColumn(Object columnHeading) {
		    Widget widget = createCellWidget(columnHeading);
		    int cell = this.getCellCount(HeaderRowIndex);
		    
		    widget.setWidth("100%");
		    widget.addStyleName("FlexTable-ColumnLabel");
		    
		    this.setWidget(HeaderRowIndex, cell, widget);
		    
		    this.getCellFormatter().addStyleName(
		        HeaderRowIndex, cell,"FlexTable-ColumnLabelCell");
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
		  private void addRow(Object[] cellObjects) {
		    
		    for (int cell = 0; cell < cellObjects.length; cell++) {
		      Widget widget = createCellWidget(cellObjects[cell]);
		      System.out.println(rowIndex);
		      System.out.println(cell);
		      this.setWidget(rowIndex, cell, widget);
		     // this.getCellFormatter().addStyleName(rowIndex,cell,"FlexTable-Cell");
		    }
		    rowIndex++;
		  }
		  
		  private void applyDataRowStyles() {
		    HTMLTable.RowFormatter rf = this.getRowFormatter();
		    
		    for (int row = 1; row < this.getRowCount(); ++row) {
		      if ((row % 2) != 0) {
		        rf.addStyleName(row, "FlexTable-OddRow");
		      }
		      else {
		        rf.addStyleName(row, "FlexTable-EvenRow");
		      }
		    }
		  }
}
