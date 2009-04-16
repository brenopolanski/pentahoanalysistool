package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class DimensionPanel extends ScrollPanel {
	private ConnectionListenerCollection connectionListeners;
	private LayoutPanel layoutPanel;
	private DimensionDropWidget dimDrop;
	private static FlexTableRowDragController tableRowDragController;
	public DimensionPanel() {

		super();
		


		// Setup the main layout widget
		layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		layoutPanel.setPadding(0);
		layoutPanel.setWidgetSpacing(0);
		layoutPanel.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		this.add(layoutPanel);
		
		tableRowDragController = new FlexTableRowDragController(RootPanel.get());
		dimDrop = new DimensionDropWidget("Unused", Axis.UNUSED);
		layoutPanel.add(dimDrop, new BoxLayoutData(FillStyle.BOTH));
	}

	public void createDimensionList() {
		// Create the various components that make up the Dimension Flextable

			dimDrop.populateDimensionTable();
			
			}
	
	public static FlexTableRowDragController getDragController(){
		return tableRowDragController;
	}
}


