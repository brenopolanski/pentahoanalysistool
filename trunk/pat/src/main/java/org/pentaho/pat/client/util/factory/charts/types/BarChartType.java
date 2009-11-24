package org.pentaho.pat.client.util.factory.charts.types;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.util.factory.charts.axis.ChartAxis;
import org.pentaho.pat.client.util.factory.charts.util.ChartUtils;
import org.pentaho.pat.client.util.table.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.rednels.ofcgwt.client.event.ChartClickEvent;
import com.rednels.ofcgwt.client.event.ChartClickHandler;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.axis.Label;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.Bar;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;

public class BarChartType {
      /**
         * 
         * Create a bar chart.
         * 
         * @param matrix
         *            The CellDataSet from the current query.
         * @param chartTitle
         *            The chart title.
     * @param pos 
         * @return A Chart Data object.
         */
        public ChartData getBarChartData(final CellDataSet matrix, final String chartTitle,
                Map<String, Object> chartOptions, Position pos) {
    
            matrix.getCellSetHeaders();
            final PatTableModel patTableModel = new PatTableModel(matrix);
            final BaseCell[][] rowData = patTableModel.getRowData();
            int rowColCount = 0;
            for (int i = 0; i < patTableModel.getColumnCount(); i++)
                if (rowData[0][i] instanceof MemberCell)
                     rowColCount++;
    
            // TODO Allow user defined fonts etc.
            final ChartData cd = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;"); //$NON-NLS-1$
    
            // TODO Allow user defined background color.
            cd.setBackgroundColour("#ffffff"); //$NON-NLS-1$
    
            ChartAxis ca = new ChartAxis();
            XAxis xa = ca.createXAxis(chartOptions);
    
            YAxis ya = ca.createYAxis(chartOptions);
            
            cd.setXAxis(xa);
            
                 
            cd.setYAxis(ya);
    
            final BarChart bchart2;
            if(chartOptions.containsKey("barStyle")){ //$NON-NLS-1$
            bchart2 = new BarChart((BarStyle) chartOptions.get("barStyle")); //$NON-NLS-1$
            }
            else{
                bchart2 = new BarChart();
            }
            // TODO Allow user defined tooltips.
            bchart2.setTooltip("$#val#"); //$NON-NLS-1$
    
            if (pos != null) {
                cd.setLegend(new Legend(pos, true));
            }
            final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
            final Label[] labels = new Label[data.size()];
            for (int i = 0; i < data.size(); i++) {
                final BaseCell[] cell = data.get(i);
                int rc = 0;
                while (cell[rc].getRawValue() == null)
                    rc++;
                labels[i] = new Label(cell[rc].getRawValue().toString(), 45);
    
            }
    
            xa.addLabels(labels);
            
            Float maxval = 0.0f;
            Float minval = 0.0f;
            Number cellValue = null;
            for (int i = 0; i < data.size(); i++) {
                final BaseCell[] cell = data.get(i);
                int rc = 0;
                while (cell[rc].getRawValue() == null)
                    rc++;
                if(((DataCell)cell[rowColCount]).getRawNumber()!=null){
            	cellValue = ((DataCell) cell[rowColCount]).getRawNumber();
                }
                else{
            	cellValue = 0;
                }
                {
                    final Bar bar = new Bar(cellValue);
                    bar.setColour(ChartUtils.getRandomColor());
                    bar.addChartClickHandler(new ChartClickHandler() {
    
                        public void onClick(final ChartClickEvent event) {
                            // TODO Allow chart drilling.
                            MessageBox.info("Clicked Bar", bar.getColour()); //$NON-NLS-1$
                        }
    
                    });
                    bchart2.addBars(bar);
                    if (cellValue.floatValue() > maxval)
                        maxval =(cellValue.floatValue());
                    if (cellValue.floatValue() < minval)
                        minval =(cellValue.floatValue());
                }
    
            }
            if(!chartOptions.containsKey("yaxisMax")) //$NON-NLS-1$
            ya.setMax(maxval);
            
            if(!chartOptions.containsKey("yaxisMin")) //$NON-NLS-1$
                ya.setMin(minval);
                
            
            cd.addElements(bchart2);
            return cd;
        }
}

