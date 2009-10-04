/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.util.PatTableModel;
import org.pentaho.pat.client.util.factory.ChartFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.elements.PieChart;

/**
 *TODO JAVADOC
 * 
 * @author bugg
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
        chart.setChartData(cf.getChart(ChartType.BAR, matrix, "CHART!!!"));
        this.layout();
        }
    }

    public enum ChartType {
        PIE,
        BAR,
        LINE
   }
        
    
}
