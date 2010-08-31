/**
 * 
 */
package org.pentaho.pat.client.util.factory.charts.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ChartPanel;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.client.util.factory.charts.axis.ChartAxis;
import org.pentaho.pat.client.util.factory.charts.util.ChartUtils;
import org.pentaho.pat.client.util.table.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.AbstractBaseCell;
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
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.Bar;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;
/**
 * 
 * @author tom(at)wamonline.org.uk
 *
 */
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
            Map<String, Object> chartOptions, Position pos, final ChartPanel chartPanel) {

        matrix.getCellSetHeaders();
        final PatTableModel patTableModel = new PatTableModel(matrix);
        final AbstractBaseCell[][] rowData = patTableModel.getRowData();
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
        if (chartOptions.containsKey("barStyle")) { //$NON-NLS-1$
            bchart2 = new BarChart((BarStyle) chartOptions.get("barStyle")); //$NON-NLS-1$
        } else {
            bchart2 = new BarChart();
        }

        if (pos != null) {
            cd.setLegend(new Legend(pos, true));
        }
        final List<AbstractBaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        int dataColCount = data.get(0).length - rowColCount;
        final List<Label> labels = new ArrayList<Label>();
        final MemberCell[] memberCellLabelList = new MemberCell[data.size()];
        for (int i = 0; i < data.size(); i++) {
            final AbstractBaseCell[] cell = data.get(i);

            List<String> path = ((MemberCell) cell[rowColCount - 1]).getMemberPath();
            StringBuffer buf = new StringBuffer();
            for (int j = 0; j < path.size(); j++) {
                buf.append(path.get(j));
                if (j != path.size() - 1) {
                    buf.append(","); //$NON-NLS-1$
                }
            }
            String label = buf.toString();

            memberCellLabelList[i] = (MemberCell) cell[rowColCount - 1];

            for (int j = 0; j < dataColCount; j++) {
                labels.add(new Label(label, 45));
            }
        }

        xa.addLabels(labels);

        Float maxval = 0.0f;
        Float minval = 0.0f;
        Number cellValue = null;

        for (int i = 0; i < data.size(); i++) {
            final AbstractBaseCell[] cell = data.get(i);
            final int row = i;

            // TODO Allow user defined tooltips.
            bchart2.setTooltip("#val#"); //$NON-NLS-1$

            for (int j = rowColCount; j < cell.length; j++) {

                if (((DataCell) cell[j]).getRawNumber() != null) {
                    cellValue = ((DataCell) cell[j]).getRawNumber();
                } else {
                    cellValue = 0;
                }
                {
                    final Bar bar = new Bar(cellValue);
                    bar.setColour(ChartUtils.getRandomColor());
                    bar.addChartClickHandler(new ChartClickHandler() {

                        public void onClick(final ChartClickEvent event) {
                            LogoPanel.spinWheel(true);
                            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                                    Pat.getCurrDrillType(), memberCellLabelList[row], new AsyncCallback<Object>() {

                                        public void onFailure(Throwable arg0) {
                                            LogoPanel.spinWheel(false);
                                            MessageBox.alert(Pat.CONSTANTS.error(), MessageFactory
                                                    .getInstance().failedDrill(arg0.getLocalizedMessage()));
                                        }

                                        public void onSuccess(Object arg0) {
                                            Pat.executeQuery(chartPanel,Pat.getCurrQuery());
                                        }

                                    });

                        }

                    });

                    if (cellValue.floatValue() > maxval)
                        maxval = (cellValue.floatValue());
                    if (cellValue.floatValue() < minval)
                        minval = (cellValue.floatValue());
                    bchart2.addBars(bar);
                }

            }

        }
        if (!chartOptions.containsKey("yaxisMax")) //$NON-NLS-1$
            ya.setMax(maxval);

        if (!chartOptions.containsKey("yaxisMin")) //$NON-NLS-1$
            ya.setMin(minval);

        cd.addElements(bchart2);
        return cd;
    }
}
