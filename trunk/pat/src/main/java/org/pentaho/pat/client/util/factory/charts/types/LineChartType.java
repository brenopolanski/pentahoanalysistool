package org.pentaho.pat.client.util.factory.charts.types;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ChartPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.client.util.factory.charts.axis.ChartAxis;
import org.pentaho.pat.client.util.factory.charts.util.ChartUtils;
import org.pentaho.pat.client.util.table.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.DrillType;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rednels.ofcgwt.client.event.ChartClickEvent;
import com.rednels.ofcgwt.client.event.ChartClickHandler;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.axis.Label;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.LineChart;
import com.rednels.ofcgwt.client.model.elements.dot.BaseDot;
import com.rednels.ofcgwt.client.model.elements.dot.SolidDot;

public class LineChartType {
    public ChartData getLineChartData(final CellDataSet matrix, final String chartTitle,
            Map<String, Object> chartOptions, final ChartPanel chartPanel, Position pos) {

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
        final XAxis xa = ca.createXAxis(chartOptions);

        YAxis ya = ca.createYAxis(chartOptions);
       cd.setXAxis(xa);
        cd.setYAxis(ya);

        
        if (pos != null) {
            cd.setLegend(new Legend(pos, true));
        }
        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        final BaseCell[][] dataColHeaders = patTableModel.getColumnHeaders();
        final Label[] labels = new Label[data.size()];
        final MemberCell[] memberCellLabelList = new MemberCell[data.size()];
        for (int i = 0; i < data.size(); i++) {
            final BaseCell[] cell = data.get(i);
            int rc = 0;
            while (cell[rc].getRawValue() == null)
                rc++;
           
            List<String> path = ((MemberCell) cell[rc]).getMemberPath();
            StringBuffer buf = new StringBuffer();
            for(int j=0; j<path.size(); j++){
              buf.append(path.get(j));
              if(j!=path.size()-1){
        	  buf.append(",");
              }
            }
            String label = buf.toString();

         
            
            memberCellLabelList[i] = (MemberCell) cell[rc];
            labels[i] = new Label(label, 45);

        }

        xa.addLabels(labels);
        Float maxval = 0.0f;
        Float minval = 0.0f;
        Number cellValue = null;
        int actualRow = rowColCount;
        for (int i = 0; i < patTableModel.getColumnCount() - rowColCount; i++) {

            final LineChart lc2 = new LineChart();
            if(chartOptions.containsKey("dotStyle")) //$NON-NLS-1$
            lc2.setDotStyle((BaseDot) chartOptions.get("dotStyle")); //$NON-NLS-1$
            lc2.setColour(ChartUtils.getRandomColor());
            lc2.setTooltip("#x_label#"); //$NON-NLS-1$
            for (int j = 0; j < data.size(); j++) {
                final BaseCell[] cell = data.get(j);
                final int row = j;
                
                if (dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue() != null){
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue().toString());
                     
                }
                else{
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 2][actualRow].getRawValue().toString());
                 
                }
                
                
                if(((DataCell)cell[rowColCount]).getRawNumber()!=null){
            	cellValue = ((DataCell) cell[rowColCount+i]).getRawNumber();
                }
                else{
                    cellValue = 0;
                }
                {
                    final BaseDot dot = new SolidDot(cellValue);
                    dot.addChartClickHandler(new ChartClickHandler() {

                        public void onClick(final ChartClickEvent event) {
                            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(), DrillType.POSITION, memberCellLabelList[row], new AsyncCallback<Object>(){

                                public void onFailure(Throwable arg0) {
                                    MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDrill(arg0.getLocalizedMessage()));
                                }

                                public void onSuccess(Object arg0) {
                                    ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<CellDataSet>(){

                                        public void onFailure(Throwable arg0) {

                                            MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedQuery(arg0.getLocalizedMessage()));    

                                        }

                                        public void onSuccess(CellDataSet arg0) {
                                            GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                                    chartPanel, Pat.getCurrQuery(), arg0);                        

                                        }

                                    });
                                }

                            });
                        }

                    });
                    lc2.addDots(dot);
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
            
            cd.addElements(lc2);
            actualRow++;
        }

        
        return cd;
    }

}
