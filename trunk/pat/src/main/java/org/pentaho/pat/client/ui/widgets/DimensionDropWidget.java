package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates the Drag and Drop enabled dimension widget.
 *
 * @author tom(at)wamonline.org.uk
 */
public class DimensionDropWidget extends Grid {

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
}
