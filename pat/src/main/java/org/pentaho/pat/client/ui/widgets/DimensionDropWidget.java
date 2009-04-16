package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class DimensionDropWidget extends Grid {
	/**
	 * Widget for allowing user to drop dimension rows/columns and mdx filters
	 * onto canvas
	 * 
	 * @param labelText
	 * 
	 * @author tom(at)wamonline.org.uk
	 * @return
	 */
	private Axis dimAxis;
	private DimensionFlexTable table1;

	public DimensionDropWidget(String labelText, Axis targetAxis) {

		super(2, 1);

		this.dimAxis = targetAxis;
		init(labelText, dimAxis);
	}

	public void init(String labelText, Axis targetAxis) {

		table1 = new DimensionFlexTable(DimensionPanel.getDragController());

		FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(table1, targetAxis);
		DimensionPanel.getDragController().registerDropController(flexTableRowDropController1);
		Label dropLabel = new Label(labelText);
		dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
		table1.setStyleName("dropTable"); //$NON-NLS-1$
		this.setWidget(0, 0, dropLabel);

		this.setWidget(1, 0, table1);
	}

	public void populateDimensionTable() {

		table1.populateDimensionTable(dimAxis);

	}
}
