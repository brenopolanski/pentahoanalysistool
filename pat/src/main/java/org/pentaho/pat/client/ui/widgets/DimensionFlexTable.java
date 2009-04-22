/*
 * Copyright 2008 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
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
		final Label spacerLabel = new Label("");
		spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER");


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
				MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionList(arg0.getLocalizedMessage())); //$NON-NLS-1$
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
		// TODO Auto-generated method stub
		this.clear();
		final Label spacerLabel = new Label("");
		spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER");
	}
}
