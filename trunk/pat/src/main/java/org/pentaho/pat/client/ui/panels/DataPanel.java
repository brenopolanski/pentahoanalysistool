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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.IAxis;
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
public class DataPanel extends LayoutComposite implements IQueryListener {

    ChartPanel ofcPanel = new ChartPanel();
    
    OlapTable olapTable;

    LayoutPanel fillLayoutPanel = new LayoutPanel(new BorderLayout());

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
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedQuery(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final CellDataSet result1) {
                        GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                DataPanel.this, Pat.getCurrQuery(), result1);
                    }

                });

            }

        });

        final DimensionDropWidget dimDropCol = new DimensionDropWidget(ConstantFactory.getInstance().columns(),
                IAxis.COLUMNS, true, Application.tableRowDragController);
        final DimensionDropWidget dimDropRow = new DimensionDropWidget(ConstantFactory.getInstance().rows(), IAxis.ROWS,
                false, Application.tableRowDragController);
        // DimensionDropWidget dimDropFilter = new DimensionDropWidget(ConstantFactory.getInstance().filter(),
        // Axis.FILTER);
        olapTable = new OlapTable();
        final LayoutPanel buttonDropPanel = new LayoutPanel(new BoxLayout());
        buttonDropPanel.add(executeButton, new BoxLayoutData(FillStyle.VERTICAL));
        buttonDropPanel.add(dimDropCol, new BoxLayoutData(FillStyle.BOTH));
        
        fillLayoutPanel.add(olapTable, new BorderLayoutData(Region.CENTER));

        
        fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.WEST, 0.5, 50, 200));
        
        
        
        mainLayoutPanel.add(buttonDropPanel, new BorderLayoutData(Region.NORTH, 0.2, 50, 200));
        mainLayoutPanel.add(dimDropRow, new BorderLayoutData(Region.WEST, 0.2, 50, 200));
        // mainLayoutPanel.add(executeButton, new BorderLayoutData(Region.CENTER, true));

        baseLayoutPanel.add(mainLayoutPanel);
        // FIXME remove that and use style
        DOM.setStyleAttribute(baseLayoutPanel.getElement(),"background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        DOM.setStyleAttribute(mainLayoutPanel.getElement(),"background", "white"); //$NON-NLS-1$ //$NON-NLS-2$


    }

    public void chartPosition(Region chartPos){
                
        ofcPanel.removeFromParent();
        switch(chartPos){
        case WEST:
            fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.WEST, 0.5, 50, 200));
            break;
        case EAST:
            fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.EAST, 0.5, 50, 200));
            break;
        case NORTH:
            fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.NORTH, 0.5, 50, 200));
            break;
        case SOUTH:
            fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.SOUTH, 0.5, 50, 200));
            break;
        case CENTER:
            
         default:
             ofcPanel.removeFromParent();
        }
        WidgetHelper.invalidate(ofcPanel);
        WidgetHelper.layout(ofcPanel);
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
            if (Pat.getCurrQuery() != null && queryId == Pat.getCurrQuery() && this.isAttached()) {
                olapTable.setData(matrix);
                ofcPanel.onQueryExecuted(matrix);
            }
        }
    }
}
