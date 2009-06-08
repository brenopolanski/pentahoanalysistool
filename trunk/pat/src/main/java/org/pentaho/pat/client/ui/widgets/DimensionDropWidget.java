/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */

package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.ui.panels.ToolBarPanel;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.Axis;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates the Drag and Drop enabled dimension widget.
 *
 * @author tom(at)wamonline.org.uk
 */
public class DimensionDropWidget extends Grid  implements ConnectionListener, QueryListener{

	/** The Olap4j Axis. */
	private final Axis dimAxis;

	/** Creates the DimensionFlexTable. */
	private DimensionFlexTable table1;

	private FlexTableRowDropController flexTableRowDropController1;
	/**
	 * Creates the widget structure.
	 *
	 * @param labelText the label text
	 * @param targetAxis the target axis
	 */
	public DimensionDropWidget(final String labelText, final Axis targetAxis) {

		super(2, 1);
		GlobalConnectionFactory.getInstance().addConnectionListener(DimensionDropWidget.this);
		GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);
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

		final Label dropLabel = new Label(labelText);
		dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
		table1.setStyleName("dropTable"); //$NON-NLS-1$
		this.setWidget(0, 0, dropLabel);

		this.setWidget(1, 0, table1);
	}
	@Override
	public void onLoad(){
		flexTableRowDropController1 = new FlexTableRowDropController(table1, dimAxis);
		DimensionPanel.getTableRowDragController().registerDropController(flexTableRowDropController1);

	}
	
	@Override
	public void onUnload(){
	    DimensionPanel.getTableRowDragController().unregisterDropController(flexTableRowDropController1);
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
		table1.clearDimensionTable();
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
	 */
	public void onQueryChange(Widget sender) {
	
		//table1.populateDimensionTable(dimAxis);
	}
}
