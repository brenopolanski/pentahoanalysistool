/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.OlapPanel;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Axis;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionFlexTable extends FlexTable {

    private Boolean horizontal = false;

    private final FlexTableRowDragController trdc;

    public DimensionFlexTable(final FlexTableRowDragController tableRowDragController, final Boolean orientation) {
        addStyleName("demo-flextable"); //$NON-NLS-1$
        horizontal = orientation;

        this.trdc = tableRowDragController;
        final Label spacerLabel = new Label(""); //$NON-NLS-1$
        spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER"); //$NON-NLS-1$
        this.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        setWidget(0, 0, spacerLabel);
    }

    public void clearDimensionTable() {
        this.clear();
        final Label spacerLabel = new Label(""); //$NON-NLS-1$
        spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER"); //$NON-NLS-1$
        DimensionFlexTable.this.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        DimensionFlexTable.this.setWidget(0, 0, spacerLabel);
    }

    public void populateDimensionTable(final Axis targetAxis) {
         //

        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), OlapPanel.getQueryId(), targetAxis,
                new AsyncCallback<String[]>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), "Failed to get Dim List");
                    }

                    public void onSuccess(final String[] arg0) {
                        
                        if(arg0.length==0)
                            {
                                clearDimensionTable();
                            }
                        
                        for (int row = 0; row < arg0.length; row++) {
                            final Label handle = new Label(arg0[row]);
                            handle.addStyleName("drag-Dimension"); //$NON-NLS-1$
                            trdc.makeDraggable(handle);
                            if (!horizontal){
                                setWidget(row, 0, handle);
                                DimensionFlexTable.this.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
                            }
                            else{
                                setWidget(0, row, handle);
                                DimensionFlexTable.this.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
                            }
                        }

                    }
                });

    }

}
