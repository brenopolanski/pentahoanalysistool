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

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * DimensionFlexTable creates a flextable that accepts drag and drop widgets for OLAP axis.
 *
 * @author tom(at)wamonline.org.uk
 */
public final class DimensionFlexTable extends FlexTable {

	/**
	 *Creates a {@link FlexTableRowDragController}.
	 */
    	private final FlexTableRowDragController trdc;

	/**
	 * Creates the flextable, empty apart from a spacer label to stop the table collapsing.
	 *
	 * @param tableRowDragController the table row drag controller
	 */
	public DimensionFlexTable(final FlexTableRowDragController tableRowDragController) {
		addStyleName("demo-flextable"); //$NON-NLS-1$

		this.trdc = tableRowDragController;
		final Label spacerLabel = new Label(""); //$NON-NLS-1$
		spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER"); //$NON-NLS-1$


		setWidget(0, 0, spacerLabel);

	}

	/**
	 * Populates the {@link DimensionFlexTable} with the axis specified.
	 * @param targetAxis the target axis
	 */
	public void populateDimensionTable(final Axis targetAxis) {
		this.clear();

		ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), targetAxis, new AsyncCallback<String[]>() {

			public void onFailure(final Throwable arg0) {
				// TODO use standardized message dialog when implemented
				MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionList(arg0.getLocalizedMessage()));
			}

			public void onSuccess(final String[] arg0) {
				for (int row = 0; row < arg0.length; row++) {
					final Label handle = new Label(arg0[row]);
					handle.addStyleName("drag-Dimension"); //$NON-NLS-1$
					setWidget(row, 0, handle);
					trdc.makeDraggable(handle);
				}

			}
		});

	}

	/**
	 *Clears the populated Dimension Table
	 *
	 */
	public void clearDimensionTable() {
		this.clear();
		final Label spacerLabel = new Label(""); //$NON-NLS-1$
		spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER"); //$NON-NLS-1$
	}
}
