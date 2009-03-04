package org.pentaho.pat.client.panels;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.widgets.OlapFlexTable;
import org.pentaho.pat.client.widgets.OlapTable;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;




public class OlapPanel extends CaptionLayoutPanel{
	public static OlapTable olapTable;
	public OlapPanel(){
		super();
		
		init();
	}
	
	public void init(){
		GridLayoutData defaultGL = new GridLayoutData(1,1,true);
		defaultGL.setHorizontalAlignment(GridLayoutData.ALIGN_DEFAULT);
		defaultGL.setVerticalAlignment(GridLayoutData.ALIGN_TOP);
		final LayoutPanel layoutPanel = new LayoutPanel(new GridLayout(3, 5));

		Label test = new Label("test");
		
		

		
		GridLayoutData gl = new GridLayoutData(2, 1, true);
		
	    gl.setHorizontalAlignment(GridLayoutData.ALIGN_CENTER);
	    gl.setVerticalAlignment(GridLayoutData.ALIGN_MIDDLE);
	   // layoutPanel.add(dimensionWidget(), defaultGL);
	    layoutPanel.add(test, gl);

		//olapTable = new OlapTable(MessageFactory.getInstance());
		//this.add(olapTable);
		/*AbsolutePanel tableExamplePanel = new AbsolutePanel();
	    tableExamplePanel.setPixelSize(450, 300);*/
	    //setWidget(tableExamplePanel);
	    //this.add(tableExamplePanel);
	    // instantiate our drag controller
	    //FlexTableRowDragController tableRowDragController = new FlexTableRowDragController(
	    //    tableExamplePanel);
	    //tableRowDragController.addDragHandler(demoDragHandler);

	    // instantiate two flex tables
	    //DemoFlexTable table1 = new DemoFlexTable(5, 3, tableRowDragController);
	    //OlapFlexTable table2 = new OlapFlexTable(4, 4, DimensionPanel.tableRowDragController);
	    //tableExamplePanel.add(table1, 10, 20);
	    //tableExamplePanel.add(table2, 230, 40);
	    this.add(layoutPanel);
	    // instantiate a drop controller for each table
	    //FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(table1);
	    //FlexTableRowDropController flexTableRowDropController2 = new FlexTableRowDropController(table2);
	    //tableRowDragController.registerDropController(flexTableRowDropController1);
	    //DimensionPanel.tableRowDragController.registerDropController(flexTableRowDropController2);
	    //this.add(tableExamplePanel);
	}
	public Widget dimensionWidget(){
	   	    final ListBox<String> listBox = new ListBox<String>();
	        
	        final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();
	        model.add("foo");
	        model.add("bar");
	        model.add("baz");
	        model.add("toto");
	        model.add("tintin");	
	   return listBox;
		
	}
}
