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
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * @created Aug 12, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 *
 */
public class DataPanel extends LayoutComposite implements QueryListener{

    OlapTable olapTable;
    
    LayoutPanel fillLayoutPanel = new LayoutPanel();
    
    final LayoutPanel baseLayoutPanel = getLayoutPanel();
    final LayoutPanel mainLayoutPanel = new LayoutPanel(new GridLayout(2, 3));
    /**
     *TODO JAVADOC
     *
     */
    public DataPanel() {

        
        mainLayoutPanel.setPadding(0);
        
        final Button executeButton = new Button("Execute Query");
        executeButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
               ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<CellDataSet>(){

                public void onFailure(Throwable arg0) {
                    // TODO Auto-generated method stub
                    MessageBox.error("Error", "Query Execution Failed");
                }

                public void onSuccess(CellDataSet result1) {
                    // TODO Auto-generated method stub
                    onQueryExecuted(Pat.getCurrQuery(), result1);
                    olapTable.setData(result1);
                }
                   
               });
                
            }
            
        });

        DimensionDropWidget dimDropCol = new DimensionDropWidget(ConstantFactory.getInstance().columns(), Axis.COLUMNS, true);
        DimensionDropWidget dimDropRow = new DimensionDropWidget(ConstantFactory.getInstance().rows(), Axis.ROWS, false);
        //        DimensionDropWidget dimDropFilter = new DimensionDropWidget(ConstantFactory.getInstance().filter(), Axis.FILTER);
        olapTable = new OlapTable();
        fillLayoutPanel.add(olapTable);
        
        mainLayoutPanel.add(executeButton);
        mainLayoutPanel.add(dimDropCol);
        mainLayoutPanel.add(dimDropRow);
        
        baseLayoutPanel.add(mainLayoutPanel);
        
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String, org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(String queryId, CellDataSet matrix) {        
        baseLayoutPanel.remove(mainLayoutPanel);
        baseLayoutPanel.add(fillLayoutPanel);
    }
}
