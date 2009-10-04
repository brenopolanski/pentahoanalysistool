/*
 * Copyright (C) 2009 Thomas Barber
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
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.util.factory.ChartFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.user.client.ui.Widget;
import com.rednels.ofcgwt.client.ChartWidget;

/**
 *TODO JAVADOC
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class ChartPanel extends LayoutComposite implements QueryListener {

    
    private CellDataSet matrix;
    final ChartWidget chart = new ChartWidget();
    ChartFactory cf = new ChartFactory();
    public ChartPanel(){
        GlobalConnectionFactory.getQueryInstance().addQueryListener(ChartPanel.this);

        
        chart.setSize("500", "400");
        
        this.getLayoutPanel().add(chart);

    }
    
    /* (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String, org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(String queryId, CellDataSet matrix) {
        if (Pat.getCurrQuery() != null && queryId == Pat.getCurrQuery() && this.isAttached()){
        this.matrix = matrix;
        chart.setChartData(cf.getChart(ChartType.LINE, matrix, "CHART!!!"));
        this.layout();
        }
    }

    public enum ChartType {
        PIE,
        BAR,
        LINE
   }
        
    
}
