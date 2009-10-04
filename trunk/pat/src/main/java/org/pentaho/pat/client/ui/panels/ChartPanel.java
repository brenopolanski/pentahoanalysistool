/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.util.PatTableModel;
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
        this.matrix = matrix;
        chart.setChartData(getPieChartData());
        this.layout();
    }

    private ChartData getPieChartData() {
        
        matrix.getCellSetHeaders();
        PatTableModel patTableModel = new PatTableModel(matrix);
        final List<BaseCell[]> colData = Arrays.asList(patTableModel.getColumnHeaders());
        BaseCell[][] rowData = patTableModel.getRowData();
        int rowColCount =0; 
        for (int i = 0; i< patTableModel.getColumnCount(); i++){
            if (rowData[0][i] instanceof MemberCell)
                rowColCount++;
        }

        ChartData cd = new ChartData("Sales by Region", "font-size: 14px; font-family: Verdana; text-align: center;");
        cd.setBackgroundColour("#ffffff");
        cd.setLegend(new Legend(Position.RIGHT, true));

        PieChart pie = new PieChart();        
        pie.setAlpha(0.5f);
        pie.setRadius(130);
        pie.setNoLabels(true);
        pie.setTooltip("#label# $#val#<br>#percent#");
        pie.setGradientFill(true);
        pie.setColours("#ff0000", "#00aa00", "#0000ff", "#ff9900", "#ff00ff");

        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        for (int i=0; i<data.size(); i++){
            BaseCell[] cell = data.get(i);
            if (cell[0].getRawValue()!=null)
            pie.addSlices(new PieChart.Slice(Integer.parseInt(cell[1].getRawValue()), cell[0].getRawValue()));
            
            else
                pie.addSlices(new PieChart.Slice(Integer.parseInt(cell[2].getRawValue()), cell[1].getRawValue()));
        }
        cd.addElements(pie);
        return cd;
}
    
}
