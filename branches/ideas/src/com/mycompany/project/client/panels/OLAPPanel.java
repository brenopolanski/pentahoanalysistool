package com.mycompany.project.client.panels;

import com.google.gwt.user.client.ui.Composite;
import com.gwtext.client.widgets.grid.GridPanel;


public class OLAPPanel extends Composite {

	GridPanel olapPanel;
	
	public OLAPPanel() {
		super();
		
		init();
		
	}
	
	public void init(){
		
		olapPanel = new GridPanel();

		//olapPanel.setEnableDragDrop(true);
		//olapPanel.setDdGroup("myDDGroup");
		olapPanel.setEnableColumnResize(true);
		olapPanel.setSize("100%", "100%");
	}

}
