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
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a DataPanel which holds the olap table and related widgets.
 * 
 * @created Aug 12, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DataPanel extends LayoutComposite implements QueryListener {

    OlapTable olapTable;

    LayoutPanel fillLayoutPanel = new LayoutPanel();

    final LayoutPanel baseLayoutPanel = getLayoutPanel();

    final LayoutPanel mainLayoutPanel = new LayoutPanel(new BorderLayout());

    private String queryId;

    /**
     *DataPanel Constructor.
     * @param query 
     * 
     */
    public DataPanel(String query) {
        this.queryId = query;
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DataPanel.this);
        
        mainLayoutPanel.setPadding(0);

        final Button executeButton = new Button(ConstantFactory.getInstance().executeQuery());
        executeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), Pat.getCurrQuery(),
                        new AsyncCallback<CellDataSet>() {

                            public void onFailure(final Throwable arg0) {
                                // TODO Auto-generated method stub
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedQuery(arg0.getLocalizedMessage()));
                            }

                            public void onSuccess(final CellDataSet result1) {
                                // TODO Auto-generated method stub
                                // onQueryExecuted(Pat.getCurrQuery(), result1);
                                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                        DataPanel.this, Pat.getCurrQuery(), result1);
                                // olapTable.setData(result1);
                            }

                        });

            }

        });

        final DimensionDropWidget dimDropCol = new DimensionDropWidget(ConstantFactory.getInstance().columns(),
                Axis.COLUMNS, true);
        final DimensionDropWidget dimDropRow = new DimensionDropWidget(ConstantFactory.getInstance().rows(), Axis.ROWS,
                false);
        // DimensionDropWidget dimDropFilter = new DimensionDropWidget(ConstantFactory.getInstance().filter(),
        // Axis.FILTER);
        olapTable = new OlapTable();
        final LayoutPanel buttonDropPanel = new LayoutPanel(new BoxLayout());
        buttonDropPanel.add(executeButton, new BoxLayoutData(FillStyle.VERTICAL));
        buttonDropPanel.add(dimDropCol, new BoxLayoutData(FillStyle.BOTH));
        fillLayoutPanel.add(olapTable);

        mainLayoutPanel.add(buttonDropPanel, new BorderLayoutData(Region.NORTH, 0.2, 50, 200));
        mainLayoutPanel.add(dimDropRow, new BorderLayoutData(Region.WEST, 0.2, 50, 200));
        // mainLayoutPanel.add(executeButton, new BorderLayoutData(Region.CENTER, true));

        baseLayoutPanel.add(mainLayoutPanel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(final Widget sender) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final String query, final CellDataSet matrix) {
        if (query.equals(queryId))
        {
        baseLayoutPanel.remove(mainLayoutPanel);
        baseLayoutPanel.add(fillLayoutPanel);
        baseLayoutPanel.layout();
        
            // TODO why is this called twice? why two instances of the same object?
            olapTable.setData(matrix);
        }
    }
}
