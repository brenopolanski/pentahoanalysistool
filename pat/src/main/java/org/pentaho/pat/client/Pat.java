package org.pentaho.pat.client;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.panels.ControlBarPanel;
import org.pentaho.pat.client.panels.DimensionPanel;
import org.pentaho.pat.client.panels.NorthPanel;
import org.pentaho.pat.client.panels.OlapPanel;
import org.pentaho.pat.client.panels.SouthPanel;
import org.pentaho.pat.client.panels.ToolBarPanel;
import org.pentaho.pat.client.test.AccordionPanel;
import org.pentaho.pat.client.util.MessageFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.RootPanel;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * @author Tom Barber
 *
 */
public class Pat implements
CollapsedListener, EntryPoint {
	DimensionPanel dimensionPanel; // Dimension GUI for defining the query model
	OlapPanel olapPanel; // Main Window
	NorthPanel northPanel; // Contains the MDX and Filter GUI
	SouthPanel drillPanel; // Contains the drill down information
	public static LayoutPanel layoutPanel;
	LayoutPanel boxPanel;
	/**
	   * Constructor.
	   * 
	   * @param constants the constants
	   */
	  public Pat() {
	    super();
	    init();
	  }

	
	   /**
	   * The content widget's layout panel.
	   */
	 

	  

	  
	  public String getDescription() {
	    return "A BorderLayout test";
	  }

	  
	  public String getName() {
	    return "Collapsed";
	  }
	
	  public void init(){
		  
		  boxPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		  layoutPanel = new LayoutPanel(new BorderLayout());
		      
		
		  
	    // Create a layout panel to align the widgets

		  final ToolBarPanel toolBarPanel = new ToolBarPanel();
		  
		  
		  toolBarPanel.addConnectionListener(dimensionPanel);
		  
		  layoutPanel.setPadding(0);

	    // north panel

	    final CaptionLayoutPanel northPanel = new CaptionLayoutPanel("MDX Panel");
	    final ImageButton collapseBtn1 = new ImageButton(
	        Caption.IMAGES.toolCollapseUp());
	    northPanel.getHeader().add(collapseBtn1, CaptionRegion.RIGHT);
	    northPanel.add(new WidgetWrapper(new HTML("Height: 20%")));

	    collapseBtn1.addClickListener(new ClickListener() {
	      public void onClick(Widget sender) {
	        layoutPanel.setCollapsed(northPanel,
	            !layoutPanel.isCollapsed(northPanel));
	        layoutPanel.layout();
	      }
	    });

	    // south panel

	    final CaptionLayoutPanel southPanel = new CaptionLayoutPanel("Drill Data");
	    final ImageButton collapseBtn2 = new ImageButton(
	        Caption.IMAGES.toolCollapseDown());
	    southPanel.getHeader().add(collapseBtn2, CaptionRegion.RIGHT);
	    southPanel.add(new WidgetWrapper(new HTML("Height: 20%")));

	    collapseBtn2.addClickListener(new ClickListener() {
	      public void onClick(Widget sender) {
	        layoutPanel.setCollapsed(southPanel,
	            !layoutPanel.isCollapsed(southPanel));
	        layoutPanel.layout();
	      }
	    });

	    layoutPanel.add(southPanel, new BorderLayoutData(Region.SOUTH,
	        0.20, true));
	    layoutPanel.setCollapsed(southPanel, true);

	    layoutPanel.addCollapsedListener(southPanel, this);

	    // west panel
	    
		
	    dimensionPanel = new DimensionPanel(MessageFactory.getInstance().dimensionPanel());
	    final ImageButton collapseBtn3 = new ImageButton(
	        Caption.IMAGES.toolCollapseLeft());
	    dimensionPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);
	    //dimensionPanel.add(new WidgetWrapper(new HTML("Width: 20%")));

	    collapseBtn3.addClickListener(new ClickListener() {
	      public void onClick(Widget sender) {
	        layoutPanel.setCollapsed(dimensionPanel, !layoutPanel.isCollapsed(dimensionPanel));
	        layoutPanel.layout();
	      }
	    });

	    layoutPanel.add(dimensionPanel, new BorderLayoutData(Region.WEST,
	        250, true));
	    layoutPanel.setCollapsed(dimensionPanel, true);

	    layoutPanel.addCollapsedListener(dimensionPanel, this);

	    // east panel

	   /* final CaptionLayoutPanel eastPanel = new CaptionLayoutPanel("East");
	    final ImageButton collapseBtn4 = new ImageButton(
	        Caption.IMAGES.toolCollapseRight());
	    eastPanel.getHeader().add(collapseBtn4, CaptionRegion.RIGHT);
	    eastPanel.add(new WidgetWrapper(new HTML("Width: 20%")));

	    collapseBtn4.addClickListener(new ClickListener() {
	      public void onClick(Widget sender) {
	        layoutPanel.setCollapsed(eastPanel, !layoutPanel.isCollapsed(eastPanel));
	        layoutPanel.layout();
	      }
	    });

	    layoutPanel.add(eastPanel, new BorderLayoutData(Region.EAST,
	        0.2, true));
	    layoutPanel.setCollapsed(eastPanel, true);

	    layoutPanel.addCollapsedListener(eastPanel, this);
*/
	    // center panel

	    //final CaptionLayoutPanel centerPanel = new CaptionLayoutPanel("Center");
	    //centerPanel.getHeader().add(Showcase.IMAGES.gwtLogoThumb().createImage());
	    olapPanel = new OlapPanel();
	    

	    layoutPanel.add(olapPanel, new BorderLayoutData(Region.CENTER, true));

	   
	   boxPanel.add(toolBarPanel, new BoxLayoutData(FillStyle.HORIZONTAL));
	    
	   boxPanel.add(layoutPanel, new BoxLayoutData(FillStyle.BOTH));
	  }
	  
	public void onCollapsedChange(Widget sender) {
		// TODO Auto-generated method stub
	    InfoPanel.show("Collapsed", "" + layoutPanel.isCollapsed(sender));
	}

	  /**
	   * 
	   * @see org.gwt.mosaic.ui.client.CollapsedListener#onCollapsedChange(com.google.gwt.user.client.ui.Widget)
	   */
	/*  @ShowcaseSource
	  public void onCollapsedChange(Widget sender) {
	    InfoPanel.show("Collapsed", "" + layoutPanel.isCollapsed(sender));
	  }
*/
	/*Panel panel; // Container Panel(FitLayout to make dynamic resizing happy)
	Panel wrapperPanel; //Contains all child panels
	Panel mainPanel; // Border Panel
	ToolBarPanel toolBarPanel; // Menu bar at top of windows
	ControlBarPanel controlBarPanel; // Control bar for selecting cubes and more
	DimensionPanel dimensionPanel; // Dimension GUI for defining the query model
	OlapPanel olapPanel; // Main Window
	NorthPanel northPanel; // Contains the MDX and Filter GUI
	SouthPanel drillPanel; // Contains the drill down information
	

	/*	panel = new Panel();
		panel.setPaddings(15);
		panel.setLayout(new FitLayout());

		
		wrapperPanel = new Panel();
		wrapperPanel.setLayout(new RowLayout());

		toolBarPanel = new ToolBarPanel();
		toolBarPanel.setWidth("100%");
		toolBarPanel.setAutoHeight(true);
	
		
		mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setWidth("100%");
		mainPanel.setMargins(0, 0, 0, 0);

		olapPanel = new OlapPanel();
		olapPanel.setWidth("100%");

		dimensionPanel = new DimensionPanel();
		dimensionPanel.setTitle(MessageFactory.getInstance().dimensionPanel());
		dimensionPanel.setWidth(250);
		dimensionPanel.setCollapsible(true);
		
		AccordionPanel accp = new AccordionPanel();
		accp.setTitle("Dimensions and Measures");
		accp.setWidth(250);
		accp.setCollapsible(true);
		
		
		controlBarPanel = new ControlBarPanel();
		controlBarPanel.setWidth("100%");
		controlBarPanel.setAutoHeight(true);
		controlBarPanel.addConnectionListener(dimensionPanel);
		toolBarPanel.addConnectionListener(controlBarPanel);
		

		northPanel = new NorthPanel();
		northPanel.setTitle(MessageFactory.getInstance().mdxPanel());
		northPanel.setHeight(100);
		northPanel.setCollapsible(true);
		northPanel.setCollapsed(true);

		drillPanel = new SouthPanel();
		drillPanel.setTitle(MessageFactory.getInstance().drillPanel());
		drillPanel.setHeight(100);
		drillPanel.setCollapsible(true);
		drillPanel.setCollapsed(true);

		BorderLayoutData centerLayoutData = new BorderLayoutData(
				RegionPosition.CENTER);
		centerLayoutData.setMargins(new Margins(5, 5, 0, 5));

		BorderLayoutData westLayoutData = new BorderLayoutData(
				RegionPosition.WEST);
		westLayoutData.setMargins(new Margins(5, 5, 0, 5));
		westLayoutData.setCMargins(new Margins(5, 5, 5, 5));
		westLayoutData.setMinSize(175);
		westLayoutData.setSplit(true);

		BorderLayoutData southLayoutData = new BorderLayoutData(
				RegionPosition.SOUTH);
		southLayoutData.setMargins(new Margins(5, 5, 0, 5));
		southLayoutData.setCMargins(new Margins(5, 5, 5, 5));
		southLayoutData.setMinSize(175);
		southLayoutData.setSplit(true);

		BorderLayoutData northLayoutData = new BorderLayoutData(
				RegionPosition.NORTH);
		northLayoutData.setMargins(new Margins(5, 5, 0, 5));
		northLayoutData.setCMargins(new Margins(5, 5, 5, 5));
		northLayoutData.setMinSize(175);
		northLayoutData.setSplit(true);

		mainPanel.add(olapPanel, centerLayoutData);
//		TabPanel tabs = new TabPanel();
//		tabs.setTabPosition(Position.TOP);  
//		tabs.setPaddings(10);
//		tabs.setWidth(250);
//		tabs.setCollapsible(true);
//		tabs.add(dimensionPanel);
//		tabs.add(accp);
//		
		mainPanel.add(dimensionPanel, westLayoutData);
//		mainPanel.add(tabs, westLayoutData);
		
		mainPanel.add(northPanel, northLayoutData);
		mainPanel.add(drillPanel, southLayoutData);

		wrapperPanel.add(toolBarPanel);
		wrapperPanel.add(controlBarPanel);
		//Not perfect solution but stops south panel being chopped off...
		wrapperPanel.add(mainPanel, new RowLayoutData("90%"));

		panel.add(wrapperPanel);
		Viewport viewport = new Viewport(panel);
*/
		  		  
	public void onModuleLoad() {
		final Viewport viewport = new Viewport();
		
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				String str = "";
				e.printStackTrace();
				Window.alert(str + e);
			}
		});	
		 viewport.add(boxPanel);
		RootPanel.get().add(viewport);
	}
	
	
	
	}



