package org.pentaho.pat.client.ui.panels;

import java.util.List;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.client.util.FlexTableCellDragController;
import org.pentaho.pat.client.util.factory.GuidFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class DimensionPanel extends ScrollPanel implements ConnectionListener,
SourcesConnectionEvents {
	private static final String AXIS_NONE = "none"; //$NON-NLS-1$
	private static final String AXIS_UNUSED = "UNUSED"; //$NON-NLS-1$
	private static final String AXIS_FILTER = "FILTER"; //$NON-NLS-1$
	private static final String AXIS_COLUMNS = "COLUMNS"; //$NON-NLS-1$
	private static final String AXIS_ROWS = "ROWS"; //$NON-NLS-1$
	private static final String AXIS_PAGES = "PAGES"; //$NON-NLS-1$
	private static final String AXIS_CHAPTERS = "CHAPTERS"; //$NON-NLS-1$
	private static final String AXIS_SECTIONS = "SECTIONS"; //$NON-NLS-1$
	private ConnectionListenerCollection connectionListeners;
	private LayoutPanel layoutPanel;
	
	public DimensionPanel(){
		
		super();
		// Setup the main layout widget
		layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		layoutPanel.setPadding(0);
		layoutPanel.setWidgetSpacing(0);
		layoutPanel.setSize("100%", "100%");
		createDimensionList();
		this.add(layoutPanel);
	}
	
	private void createDimensionList(){
		
        
        FlexTableCellDragController tableRowDragController = new FlexTableCellDragController(Application.getPanel());
        
        DimensionFlexTable table1 = new DimensionFlexTable(2, 2, tableRowDragController);
    
        layoutPanel.add(table1, new BoxLayoutData(FillStyle.BOTH));
	}
	
	public static void populateDimensions(List axis) {
		
	}

	
	
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub

	}
	
	public void addConnectionListener(ConnectionListener listener) {
		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);
	}

	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}

	}
}
