package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.FlexTableRowDropController;

import com.google.gwt.user.client.ui.ScrollPanel;

public class DimensionPanel extends ScrollPanel {
	private ConnectionListenerCollection connectionListeners;
	private LayoutPanel layoutPanel;
	private DimensionFlexTable table1;
	private static FlexTableRowDragController tableRowDragController;
	public DimensionPanel() {

		super();
		


		// Setup the main layout widget
		layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		layoutPanel.setPadding(0);
		layoutPanel.setWidgetSpacing(0);
		layoutPanel.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		this.add(layoutPanel);
		tableRowDragController = new FlexTableRowDragController(Application.getPanel());
		
		table1 = new DimensionFlexTable(tableRowDragController);

		FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(table1);
	    tableRowDragController.registerDropController(flexTableRowDropController1);
	
	    
		layoutPanel.add(table1, new BoxLayoutData(FillStyle.BOTH));
	}

	public void createDimensionList() {
		// Create the various components that make up the Dimension Flextable

			table1.populateDimensionTable();
			
			}
	
	public static FlexTableRowDragController getDragController(){
		return tableRowDragController;
	}
}


