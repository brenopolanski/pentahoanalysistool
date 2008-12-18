package com.mycompany.project.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.mycompany.project.client.panels.ConnectionPanel;
import com.mycompany.project.client.panels.DimensionPanel;
import com.mycompany.project.client.panels.ControlBarPanel;
import com.mycompany.project.client.panels.ToolBarPanel;



public class OLAPTabPanel extends FlexTable {
/*TODO
 * Generic ToolBar stuff that goes with every OLAP program under the sun.
 */
	ToolBarPanel toolBarPanel;
	DimensionPanel dimensionPanel;
	ConnectionPanel connectionWindow;
	ControlBarPanel controlBar;
	
	public OLAPTabPanel() {
		super();

		init();
	}

	/**
	 * 
	 */
	private void init() {
		toolBarPanel = new ToolBarPanel();
		dimensionPanel = new DimensionPanel();
		connectionWindow = new ConnectionPanel();
		controlBar = new ControlBarPanel();
		connectionWindow.addConnectionListener(dimensionPanel);
		connectionWindow.addConnectionListener(controlBar);
			}


}
