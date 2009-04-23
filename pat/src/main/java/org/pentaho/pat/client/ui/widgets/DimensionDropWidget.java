package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates the Drag and Drop enabled dimension widget.
 *
 * @author tom(at)wamonline.org.uk
 */
public class DimensionDropWidget extends Grid  implements ConnectionListener{

	/** The Olap4j Axis. */
	private final Axis dimAxis;

	/** Creates the DimensionFlexTable. */
	private DimensionFlexTable table1;

	/**
	 * Creates the widget structure.
	 *
	 * @param labelText the label text
	 * @param targetAxis the target axis
	 */
	public DimensionDropWidget(final String labelText, final Axis targetAxis) {

		super(2, 1);
		GlobalConnectionFactory.getInstance().addConnectionListener(DimensionDropWidget.this);
		this.dimAxis = targetAxis;
		init(labelText, dimAxis);
	}

	/**
	 * Initialization.
	 *
	 * @param labelText the label text
	 * @param targetAxis the target axis
	 */
	public final void init(final String labelText, final Axis targetAxis) {

		table1 = new DimensionFlexTable(DimensionPanel.getTableRowDragController());

		final FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(table1, targetAxis);
		DimensionPanel.getTableRowDragController().registerDropController(flexTableRowDropController1);
		final Label dropLabel = new Label(labelText);
		dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
		table1.setStyleName("dropTable"); //$NON-NLS-1$
		this.setWidget(0, 0, dropLabel);

		this.setWidget(1, 0, table1);
	}

	/**
	 * Populate the Dimension table on the passed axis.
	 */
	public final void populateDimensionTable() {

		table1.populateDimensionTable(dimAxis);

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		table1.clearDimensionTable();
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}
}
