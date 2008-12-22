package org.pentaho.pat.client;

import org.pentaho.pat.client.panels.ConnectionPanel;
import org.pentaho.pat.client.panels.ControlBarPanel;
import org.pentaho.pat.client.panels.DimensionPanel;
import org.pentaho.pat.client.panels.ToolBarPanel;
import com.google.gwt.user.client.ui.FlowPanel;



public class PATPanel extends FlowPanel {
/*TODO
 * Generic ToolBar stuff that goes with every OLAP program under the sun.
 */
	ToolBarPanel toolBarPanel;
	DimensionPanel dimensionPanel;
	ConnectionPanel connectionWindow;
	ControlBarPanel controlBar;
	
	public PATPanel() {
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
		this.add(toolBarPanel);
		this.add(controlBar);
		
			}


}
