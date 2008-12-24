package org.pentaho.pat.client;

import org.pentaho.pat.client.PATPanel;
import org.pentaho.pat.client.panels.ControlBarPanel;
import org.pentaho.pat.client.panels.DimensionPanel;
import org.pentaho.pat.client.panels.NorthPanel;
import org.pentaho.pat.client.panels.SouthPanel;
import org.pentaho.pat.client.panels.ToolBarPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PAT implements EntryPoint {
	ToolBarPanel toolBarPanel;
	ControlBarPanel controlBarPanel;
	DimensionPanel dimensionPanel;
	PATPanel patPanel;
	NorthPanel northPanel;
	SouthPanel southPanel;
	
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				String str = "";
				e.printStackTrace();
				Window.alert(str);
			}
		});

		final VerticalPanel verticalPanel = new VerticalPanel();
		toolBarPanel = new ToolBarPanel();
		toolBarPanel.setWidth(1024);
		
		
		controlBarPanel = new ControlBarPanel();
		controlBarPanel.setWidth(1024);
		controlBarPanel.addConnectionListener(toolBarPanel);
				
		
		patPanel = new PATPanel();
		
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setMargins(0, 0, 0, 0);

		BorderLayoutData centerLayoutData = new BorderLayoutData(
				RegionPosition.CENTER);
		centerLayoutData.setMargins(new Margins(5, 0, 0, 5));
		mainPanel.add(patPanel, centerLayoutData);

		
		dimensionPanel = new DimensionPanel();
		dimensionPanel.addListener(new PanelListenerAdapter() {
			@Override
			public void onCollapse(Panel panel) {
				patPanel.setTitle("West Panel was collapsed");
			}

			@Override
			public void onExpand(Panel panel) {
				patPanel.setTitle("West Panel was expanded");
			}
		});
		dimensionPanel.setTitle("Dimension Panel");
		dimensionPanel.setWidth(220);
		dimensionPanel.setHeight(600);
		// it's not more then this westPanel.setCollapsible(true);
		// westPanel.setCollapsed(true);
		dimensionPanel.setCollapsible(true);
		
		
		northPanel = new NorthPanel();
		northPanel.setTitle("MDX/Filter Panel");
		northPanel.setWidth(1024);
		northPanel.setHeight(100);
		northPanel.setCollapsible(true);
		northPanel.setCollapsed(true);
		
		
		southPanel = new SouthPanel();
		southPanel.setTitle("Drill down Panel");
		southPanel.setWidth(1024);
		southPanel.setHeight(100);
		southPanel.setCollapsible(true);
		southPanel.setCollapsed(true);
		
		BorderLayoutData westLayoutData = new BorderLayoutData(
				RegionPosition.WEST);
		westLayoutData.setMargins(new Margins(5, 5, 0, 5));
		westLayoutData.setCMargins(new Margins(5, 5, 5, 5));
		westLayoutData.setMinSize(175);
		westLayoutData.setMaxSize(400);
		westLayoutData.setSplit(true);

		BorderLayoutData southLayoutData = new BorderLayoutData(
				RegionPosition.SOUTH);
		westLayoutData.setMargins(new Margins(5, 5, 0, 5));
		westLayoutData.setCMargins(new Margins(5, 5, 5, 5));
		westLayoutData.setMinSize(175);
		westLayoutData.setMaxSize(400);
		westLayoutData.setSplit(true);
		
		BorderLayoutData northLayoutData = new BorderLayoutData(
				RegionPosition.NORTH);
		westLayoutData.setMargins(new Margins(5, 5, 0, 5));
		westLayoutData.setCMargins(new Margins(5, 5, 5, 5));
		westLayoutData.setMinSize(175);
		westLayoutData.setMaxSize(400);
		westLayoutData.setSplit(true);
		
		
		mainPanel.add(dimensionPanel, westLayoutData);
		mainPanel.add(northPanel, northLayoutData);
		mainPanel.add(southPanel, southLayoutData);
		mainPanel.setHeight(600);
		mainPanel.setWidth(1024);
		
		
		
		verticalPanel.setWidth("100%");
		verticalPanel.add(toolBarPanel);
		verticalPanel.add(controlBarPanel);
		verticalPanel.add(mainPanel);
		
		RootPanel root = RootPanel.get();
		root.setSize("1024px", "768px");
		root.add(verticalPanel);

	}

}
