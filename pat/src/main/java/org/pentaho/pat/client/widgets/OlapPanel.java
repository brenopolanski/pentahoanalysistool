package org.pentaho.pat.client.widgets;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.panels.NorthPanel;
import org.pentaho.pat.client.panels.SouthPanel;
import org.pentaho.pat.client.util.ConstantFactory;
import org.pentaho.pat.client.util.FlexTableCellDragController;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;




public class OlapPanel extends DataWidget{
	public static OlapTable olapTable;

	/**
	 * The widget used to display source code.
	 */
	private HTML sourceWidget = null;
	
	/**
	 * The stack panel with the contents.
	 */
	private StackLayoutPanel stackPanel;
	
	private LayoutPanel panel1;
	
	private String name;

	  /**
	   * Constructor.
	   * 
	   * @param constants2 the constants
	   */
	  
	  
	public OlapPanel(String name){
		super();
		this.name = name;
	}
	
	private String createTabBarCaption(AbstractImagePrototype image, String text) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("<table cellspacing='0px' cellpadding='0px' border='0px'><thead><tr>");
		sb.append("<td valign='middle'>");
		sb.append(image.getHTML());
		sb
				.append("</td><td valign='middle' style='white-space: nowrap;'>&nbsp;");
		sb.append(text);
		sb.append("</td></tr></thead></table>");
		return sb.toString();
	}
	


	@Override
	public Widget onInitialize(){
		final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
		
		// MDX(north) panel
		final NorthPanel northPanel = new NorthPanel("MDX Panel");
		final ImageButton collapseBtn1 = new ImageButton(Caption.IMAGES
				.toolCollapseUp());
		northPanel.getHeader().add(collapseBtn1, CaptionRegion.RIGHT);

		collapseBtn1.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				layoutPanel.setCollapsed(northPanel, !layoutPanel.isCollapsed(northPanel));
				layoutPanel.layout();
			}
		});

		layoutPanel.add(northPanel, new BorderLayoutData(Region.NORTH, 100, 10,
				250));
		layoutPanel.setCollapsed(northPanel, true);
		
		
		
		// Drill(south) panel
		final SouthPanel drillPanel = new SouthPanel("Drill Data");

			final ImageButton collapseBtn2 = new ImageButton(Caption.IMAGES
					.toolCollapseDown());
			drillPanel.getHeader().add(collapseBtn2, CaptionRegion.RIGHT);

			collapseBtn2.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					layoutPanel.setCollapsed(drillPanel, !layoutPanel.isCollapsed(drillPanel));
					layoutPanel.layout();
				}
			});

			layoutPanel.add(drillPanel, new BorderLayoutData(Region.SOUTH, 100, 10,
					250));
			layoutPanel.setCollapsed(drillPanel, true);

			
			stackPanel = new StackLayoutPanel();
			layoutPanel.add(stackPanel);
			
			
			// Create the container for the main example
			panel1 = new LayoutPanel(new GridLayout(2, 4));
			panel1.setPadding(0);
			panel1.setWidgetSpacing(0);
			createLayout();
			stackPanel.add(panel1, createTabBarCaption(Pat.IMAGES.cube(),
					ConstantFactory.getInstance().data() + " (" + getName() + ")"),
					true);

			final LayoutPanel panel2 = new LayoutPanel();
			sourceWidget = new HTML();
			sourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source");
			panel2.add(sourceWidget);
			stackPanel.add(panel2,
					createTabBarCaption(Pat.IMAGES.chart(), ConstantFactory
							.getInstance().chart()
							+ " (" + getName() + ")"), true);
			
			stackPanel.showStack(0);
			
			return layoutPanel;
	

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
	    
	    // instantiate a drop controller for each table
	    //FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(table1);
	    //FlexTableRowDropController flexTableRowDropController2 = new FlexTableRowDropController(table2);
	    //tableRowDragController.registerDropController(flexTableRowDropController1);
	    //DimensionPanel.tableRowDragController.registerDropController(flexTableRowDropController2);
	    //this.add(tableExamplePanel);
	}

	private void createLayout(){
		 
		
		FlexTableCellDragController tableRowDragController = new FlexTableCellDragController(Application.getPanel());
        
		 Label rowLabel = new Label("Rows");
		 Label colLabel = new Label("Columns");
		 
		 
		 DimensionFlexTable rowTable = new DimensionFlexTable(1, 2, tableRowDragController);
    
		 DimensionFlexTable colTable = new DimensionFlexTable(1, 2, tableRowDragController);
		 
		 Grid dropGrid = new Grid(2,2);
		 
		 dropGrid.setWidget(0, 0, rowLabel);
		 dropGrid.setWidget(0, 1, rowTable);
		 dropGrid.setWidget(1, 0, colLabel);
		 dropGrid.setWidget(1, 1, colTable);
		 
		 
		 
		 panel1.add(dropGrid);
		 
	}
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}


}
