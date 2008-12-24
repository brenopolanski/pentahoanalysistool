package org.pentaho.pat.client;

import org.pentaho.pat.client.PATPanel;
import org.pentaho.pat.client.panels.ConnectionPanel;
import org.pentaho.pat.client.panels.ControlBarPanel;
import org.pentaho.pat.client.panels.DimensionPanel;
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
import com.gwtext.client.widgets.layout.FitLayout;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PAT implements EntryPoint {
	ToolBarPanel toolBarPanel;
	ControlBarPanel controlBarPanel;
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
		      public void onUncaughtException(Throwable e) {
		    	  String str = "";
		    		  e.printStackTrace();
		        Window.alert(str);
		      }
		});
		/*Panel panel = new Panel();  
		panel.setBorder(false);  
		panel.setPaddings(15);  
		panel.setLayout(new FitLayout());*/
		final VerticalPanel verticalPanel = new VerticalPanel();
		toolBarPanel = new ToolBarPanel();
		//toolBarPanel.setHeight(30);
		controlBarPanel = new ControlBarPanel();
		//controlBarPanel.setHeight(30);
		controlBarPanel.addConnectionListener(toolBarPanel);
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout()); 
		mainPanel.setMargins(0, 0, 0, 0); 
		
		final PATPanel patPanel = new PATPanel();
		//patPanel.setSize("100%", "100%");
		//patPanel.setTitle("blah");
		BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER); 
		centerLayoutData.setMargins(new Margins(5, 0, 0, 5)); 
		mainPanel.add(patPanel, centerLayoutData);
		mainPanel.setHeight(600);
		 Panel westPanel = new Panel(); 
		 westPanel.addListener(new PanelListenerAdapter() { 
			 public void onCollapse(Panel panel) { 
				 patPanel.setTitle("West Panel was collapsed"); 
			 }

		 public void onExpand(Panel panel) { 
			 patPanel.setTitle("West Panel was expanded"); 
			 } 
		 }); 
		 westPanel.setTitle("Dimension Panel"); 
		 westPanel.setLayout(new FitLayout()); 
		 westPanel.setWidth(220);
		 westPanel.setHeight(600);
		 //it's not more then this westPanel.setCollapsible(true); westPanel.setCollapsed(true);
		 westPanel.setCollapsible(true);
		 BorderLayoutData westLayoutData = new BorderLayoutData(RegionPosition.WEST); 
		 westLayoutData.setMargins(new Margins(5, 5, 0, 5)); 
		 westLayoutData.setCMargins(new Margins(5, 5, 5, 5)); 
		 westLayoutData.setMinSize(175); 
		 westLayoutData.setMaxSize(400); 
		 westLayoutData.setSplit(true);

		 mainPanel.add(westPanel, westLayoutData); 
		
		verticalPanel.add(toolBarPanel);
		verticalPanel.add(controlBarPanel);
		verticalPanel.add(mainPanel);
		//mainPanel.setSize("100%", "100%");
		RootPanel root = RootPanel.get();
		root.setSize("1024px", "768px");
		root.add(verticalPanel);
		
		
			}
		
	
}
