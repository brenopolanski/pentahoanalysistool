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
	ControlBarPanel controlBarPanel;
	
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
		controlBarPanel = new ControlBarPanel();
		controlBarPanel.addConnectionListener(dimensionPanel);
		//TODO Will need re-enabling when connect panel works.... 
		//controlBarPanel.addConnectionListener(controlBarPanel);
		this.add(toolBarPanel);
		this.add(controlBarPanel);
		
			}


}
