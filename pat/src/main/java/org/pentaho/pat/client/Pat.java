package org.pentaho.pat.client;

import org.pentaho.pat.client.panels.ControlBarPanel;
import org.pentaho.pat.client.panels.DimensionPanel;
import org.pentaho.pat.client.panels.NorthPanel;
import org.pentaho.pat.client.panels.OlapPanel;
import org.pentaho.pat.client.panels.SouthPanel;
import org.pentaho.pat.client.panels.ToolBarPanel;
import org.pentaho.pat.client.util.MessageFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * @author Tom Barber
 *
 */
public class Pat implements EntryPoint {
	Panel panel; // Container Panel(FitLayout to make dynamic resizing happy)
	Panel wrapperPanel; //Contains all child panels
	Panel mainPanel; // Border Panel
	ToolBarPanel toolBarPanel; // Menu bar at top of windows
	ControlBarPanel controlBarPanel; // Control bar for selecting cubes and more
	DimensionPanel dimensionPanel; // Dimension GUI for defining the query model
	OlapPanel olapPanel; // Main Window
	NorthPanel northPanel; // Contains the MDX and Filter GUI
	SouthPanel drillPanel; // Contains the drill down information
	
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				String str = "";
				e.printStackTrace();
				Window.alert(str + e);
			}
		});

		panel = new Panel();
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

		controlBarPanel = new ControlBarPanel();
		controlBarPanel.setWidth("100%");
		controlBarPanel.setAutoHeight(true);
		controlBarPanel.addConnectionListener(dimensionPanel);

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
		mainPanel.add(dimensionPanel, westLayoutData);
		mainPanel.add(northPanel, northLayoutData);
		mainPanel.add(drillPanel, southLayoutData);

		wrapperPanel.add(toolBarPanel);
		wrapperPanel.add(controlBarPanel);
		//Not perfect solution but stops south panel being chopped off...
		wrapperPanel.add(mainPanel, new RowLayoutData("90%"));

		panel.add(wrapperPanel);
		Viewport viewport = new Viewport(panel);

	}

}
