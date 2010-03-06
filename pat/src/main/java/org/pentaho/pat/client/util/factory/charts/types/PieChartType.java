package org.pentaho.pat.client.util.factory.charts.types;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ChartPanel;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.client.util.factory.charts.util.ChartUtils;
import org.pentaho.pat.client.util.table.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.AbstractBaseCell;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rednels.ofcgwt.client.event.ChartClickEvent;
import com.rednels.ofcgwt.client.event.ChartClickHandler;
import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.elements.PieChart.Slice;

public class PieChartType {

    public ChartData getPieChartData(final CellDataSet matrix, final String chartTitle,
            Map<String, Object> chartOptions, final ChartPanel chartPanel, Position pos) {

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

        if (pos != null) {
            cd.setLegend(new Legend(pos, true));
        }
        final PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setRadius(130);
        pie.setNoLabels(true);

        // TODO Allow user defined tooltips.
        pie.setTooltip("#label# $#val#<br>#percent#"); //$NON-NLS-1$
        pie.setGradientFill(true);
        pie.setColours(ChartUtils.getRandomColor(), ChartUtils.getRandomColor(), ChartUtils.getRandomColor(),
                ChartUtils.getRandomColor(), ChartUtils.getRandomColor());
        pie.setAnimateOnShow(true);
        pie.setAnimation(new PieChart.PieBounceAnimation(30));

        final List<AbstractBaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        for (int i = 0; i < data.size(); i++) {
            final AbstractBaseCell[] cell = data.get(i);
            int rc = 0;
            while (cell[rc].getRawValue() == null)
                rc++;
            Number cellValue = null;
            if (((DataCell) cell[rowColCount]).getRawNumber() != null) {
                cellValue = ((DataCell) cell[rowColCount]).getRawNumber();

                final int row = rc;
                List<String> path = ((MemberCell) cell[rc]).getMemberPath();

                StringBuffer buf = new StringBuffer();
                for (int j = 0; j < path.size(); j++) {
                    buf.append(path.get(j));
                    if (j != path.size() - 1) {
                        buf.append(","); //$NON-NLS-1$
                    }
                }
                String label = buf.toString();
                final Slice slice = new Slice((cellValue.floatValue()), label);

                slice.addChartClickHandler(new ChartClickHandler() {

                    public void onClick(final ChartClickEvent event) {
                        LogoPanel.spinWheel(true);
                        ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                                DrillType.POSITION, ((MemberCell) cell[row]), new AsyncCallback<Object>() {

                                    public void onFailure(Throwable arg0) {
                                        LogoPanel.spinWheel(false);
                                        MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory
                                                .getInstance().failedDrill(arg0.getLocalizedMessage()));
                                    }

                                    public void onSuccess(Object arg0) {
                                        ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(),
                                                Pat.getCurrQuery(), new AsyncCallback<CellDataSet>() {

                                                    public void onFailure(Throwable arg0) {
                                                        LogoPanel.spinWheel(false);
                                                        MessageBox.alert(ConstantFactory.getInstance().error(),
                                                                MessageFactory.getInstance().failedQuery(
                                                                        arg0.getLocalizedMessage()));

                                                    }

                                                    public void onSuccess(CellDataSet arg0) {
                                                        LogoPanel.spinWheel(false);
                                                        GlobalConnectionFactory
                                                                .getQueryInstance()
                                                                .getQueryListeners()
                                                                .fireQueryExecuted(chartPanel, Pat.getCurrQuery(), arg0);

                                                    }

                                                });
                                    }

                                });

                    }

                });
                pie.addSlices(slice);
            }
        }
        cd.addElements(pie);
        return cd;
    }

}
