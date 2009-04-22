package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Dimension Panel lists the dimensions that are currently in the unused
 * axis.
 *
 * @author tom(at)wamonline.org.uk
 */
public class DimensionPanel extends ScrollPanel implements ConnectionListener {

    /** The Widget that allows you to drag and drop dimensions on it. */
    private final transient DimensionDropWidget dimDrop;

    /** The Drag Controller. */
    private static FlexTableRowDragController tableRowDragController;

    /**
     * Returns the drag controller.
     *
     * @return tableRowDragController
     */
    public static FlexTableRowDragController getTableRowDragController() {
	return tableRowDragController;
    }

    /**
     * Set the drag controller.
     *
     * @param tableRowDragController
     *            Accepts a FlexTableRowDragController
     */
    private static void setTableRowDragController(
	    final FlexTableRowDragController tableRowDragController) {
	DimensionPanel.tableRowDragController = tableRowDragController;
    }

    /**
     * Constructor.
     */
    public DimensionPanel() {

	super();

	// Setup the main layout widget
	LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
		Orientation.VERTICAL));
	layoutPanel.setPadding(0);
	layoutPanel.setWidgetSpacing(0);
	layoutPanel.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

	setTableRowDragController(new FlexTableRowDragController(Application
		.getBottomPanel()));
	dimDrop = new DimensionDropWidget(ConstantFactory.getInstance()
		.unused(), Axis.UNUSED);
	dimDrop.setSize("100%", "100%");
	layoutPanel.add(dimDrop, new BoxLayoutData(FillStyle.BOTH));
	// this.add(layoutPanel);
	this.add(dimDrop);
    }

    /**
     * Creates a list of dimensions for the axis specified for the widget.
     */
    public final void createDimensionList() {
	// Create the various components that make up the Dimension Flextable

	dimDrop.populateDimensionTable();

    }

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}
}
