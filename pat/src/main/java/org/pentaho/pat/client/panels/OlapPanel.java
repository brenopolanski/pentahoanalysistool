package org.pentaho.pat.client.panels;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.widgets.OlapFlexTable;
import org.pentaho.pat.client.widgets.OlapTable;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.gwtext.client.widgets.Panel;



public class OlapPanel extends CaptionLayoutPanel{
	public static OlapTable olapTable;
	public OlapPanel(){
		super();
		
		init();
	}
	
	public void init(){
		//olapTable = new OlapTable(MessageFactory.getInstance());
		//this.add(olapTable);
		AbsolutePanel tableExamplePanel = new AbsolutePanel();
	    tableExamplePanel.setPixelSize(450, 300);
	    //setWidget(tableExamplePanel);
	    this.add(tableExamplePanel);
	    // instantiate our drag controller
	    //FlexTableRowDragController tableRowDragController = new FlexTableRowDragController(
	    //    tableExamplePanel);
	    //tableRowDragController.addDragHandler(demoDragHandler);

	    // instantiate two flex tables
	    //DemoFlexTable table1 = new DemoFlexTable(5, 3, tableRowDragController);
	    OlapFlexTable table2 = new OlapFlexTable(2, 2, DimensionPanel.tableRowDragController);
	    //tableExamplePanel.add(table1, 10, 20);
	    tableExamplePanel.add(table2, 230, 40);

	    // instantiate a drop controller for each table
	    //FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(table1);
	    //FlexTableRowDropController flexTableRowDropController2 = new FlexTableRowDropController(table2);
	    //tableRowDragController.registerDropController(flexTableRowDropController1);
	    //DimensionPanel.tableRowDragController.registerDropController(flexTableRowDropController2);
	    this.add(tableExamplePanel);
	}
}
