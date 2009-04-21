package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

// TODO: Auto-generated Javadoc
/**
 * TODO JAVADOC.
 * 
 * @author bugg
 */
public class DimensionDropWidget extends Grid {

	/** TODO JAVADOC. */
	private final Axis dimAxis;
	
	/** TODO JAVADOC. */
	private DimensionFlexTable table1;

	/**
	 * TODO JAVADOC.
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
	 * TODO JAVADOC.
	 * 
	 * @param labelText the label text
	 * @param targetAxis the target axis
	 */
	public void init(final String labelText, final Axis targetAxis) {

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
	 * TODO JAVADOC.
	 */
	public void populateDimensionTable() {

		table1.populateDimensionTable(dimAxis);

	}
}
