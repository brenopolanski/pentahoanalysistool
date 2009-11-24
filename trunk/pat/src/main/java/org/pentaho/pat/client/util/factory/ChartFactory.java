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
package org.pentaho.pat.client.util.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwtwidgets.client.style.Color;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ChartPanel;
import org.pentaho.pat.client.ui.panels.ChartPanel.ChartType;
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
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.LineChart;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.Bar;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;
import com.rednels.ofcgwt.client.model.elements.PieChart.Slice;
import com.rednels.ofcgwt.client.model.elements.dot.BaseDot;
import com.rednels.ofcgwt.client.model.elements.dot.SolidDot;

/**
 * A basic Chart Factory that creates different OFC chart models.
 * 
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class ChartFactory {

    Position pos;

    /**
     * The Chart Factory Constructor.
     * 
     * @param chartType
     *            The ChartType Enum value.
     * @param matrix
     *            The CellDataSet from the current query.
     * @param chartTitle
     *            The chart title.
     * @param chartPanel 
     * @param pos2 
     * @return A ChartData object.
     */
    public ChartData getChart(final ChartType chartType, final CellDataSet matrix, final String chartTitle,
	    Map<String, Object> chartOptions, ChartPanel chartPanel) {
        switch (chartType) {
        case PIE:
            return getPieChartData(matrix, chartTitle, chartOptions, chartPanel);
        case LINE:
            return getLineChartData(matrix, chartTitle, chartOptions);
        case BAR:
            return getBarChartData(matrix, chartTitle, chartOptions);
        default:
            return null;
        }
    }

    public ChartData getChart(final ChartType chartType, final CellDataSet matrix, final String chartTitle,
            Position legendPosition, Map<String, Object> chartOptions, ChartPanel chartPanel) {

        ChartData cd = getChart(chartType, matrix, chartTitle, chartOptions, chartPanel);

        return cd;

    }

    public ChartData getChart(final ChartType chartType, final CellDataSet matrix, final String chartTitle,
            Position legendPosition, ChartPanel chartPanel) {
        Map<String, Object> chartOptions = null;
        ChartData cd = getChart(chartType, matrix, chartTitle, chartOptions, chartPanel);

        return cd;

    }

    /**
     * 
     * Checks if a string is able to be parsed to an integer.
     * 
     * @param i
     *            The string.
     * @return A boolean.
     */
    public boolean isParsableToInt(final String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (final NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * 
     * Create a random color for various chart objects.
     * 
     * @return A hex color value.
     */
    String getRandomColor() {
        final double r = Math.random() * 256;
        final double g = Math.random() * 256;
        final double b = Math.random() * 256;
        final Color myColor = new Color((int) r, (int) g, (int) b);
        return myColor.getHexValue();
    }

    /**
     * 
     * Create a bar chart.
     * 
     * @param matrix
     *            The CellDataSet from the current query.
     * @param chartTitle
     *            The chart title.
     * @return A Chart Data object.
     */
    private ChartData getBarChartData(final CellDataSet matrix, final String chartTitle,
            Map<String, Object> chartOptions) {

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

        XAxis xa = createXAxis(chartOptions);

        YAxis ya = createYAxis(chartOptions);
        
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
                bar.setColour(getRandomColor());
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

    private YAxis createYAxis(Map<String, Object> chartOptions){
        final YAxis ya = new YAxis();
        //ya.setSteps(16);

        if(chartOptions!=null){
        if(chartOptions.containsKey("yaxisColor")) //$NON-NLS-1$
        ya.setColour((String) chartOptions.get("yaxisColor")); //$NON-NLS-1$
        
        if(chartOptions.containsKey("yaxisGridColor")) //$NON-NLS-1$
        ya.setGridColour((String) chartOptions.get("yaxisGridColor")); //$NON-NLS-1$
        
        if(chartOptions.containsKey("yaxisMin")) //$NON-NLS-1$
        ya.setMin(Integer.parseInt((String) chartOptions.get("yaxisMin"))); //$NON-NLS-1$
        
        if(chartOptions.containsKey("yaxisMax")) //$NON-NLS-1$
        ya.setMax(Integer.parseInt((String) chartOptions.get("yaxisMax"))); //$NON-NLS-1$
        }
        return ya;

    }
    
    private XAxis createXAxis(Map<String, Object> chartOptions){
        final XAxis xa = new XAxis();
        //xa.setSteps(16);
    if(chartOptions!=null){
        if(chartOptions.containsKey("xaxisColor")) //$NON-NLS-1$
            xa.setColour((String) chartOptions.get("xaxisColor")); //$NON-NLS-1$
            
            if(chartOptions.containsKey("xaxisGridColor")) //$NON-NLS-1$
            xa.setGridColour((String) chartOptions.get("xaxisGridColor")); //$NON-NLS-1$
            
            if(chartOptions.containsKey("xaxisMin")) //$NON-NLS-1$
            xa.setMin(Integer.parseInt((String) chartOptions.get("xaxisMin"))); //$NON-NLS-1$
            
            if(chartOptions.containsKey("xaxisMax")) //$NON-NLS-1$
            xa.setMin(Integer.parseInt((String) chartOptions.get("xaxisMax")));         //$NON-NLS-1$
    }
            return xa;
            
            

    }
    private ChartData getLineChartData(final CellDataSet matrix, final String chartTitle,
            Map<String, Object> chartOptions) {

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

        final XAxis xa = createXAxis(chartOptions);

        YAxis ya = createYAxis(chartOptions);
       cd.setXAxis(xa);
        cd.setYAxis(ya);

        
        if (pos != null) {
            cd.setLegend(new Legend(pos, true));
        }
        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        final BaseCell[][] dataColHeaders = patTableModel.getColumnHeaders();
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
        int actualRow = rowColCount;
        for (int i = 0; i < patTableModel.getColumnCount() - rowColCount; i++) {

            final LineChart lc2 = new LineChart();
            if(chartOptions.containsKey("dotStyle")) //$NON-NLS-1$
            lc2.setDotStyle((BaseDot) chartOptions.get("dotStyle")); //$NON-NLS-1$
            lc2.setColour(getRandomColor());
            lc2.setTooltip("#x_label#"); //$NON-NLS-1$
            for (int j = 0; j < data.size(); j++) {
                final BaseCell[] cell = data.get(j);

                if (dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue() != null){
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue().toString());
                }
                else{
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 2][actualRow].getRawValue().toString());

                }
                if(((DataCell)cell[rowColCount]).getRawNumber()!=null){
            	cellValue = ((DataCell) cell[rowColCount]).getRawNumber();
                }
                else{
                    cellValue = 0;
                }
                {
                    final BaseDot dot = new SolidDot(cellValue);
                    dot.addChartClickHandler(new ChartClickHandler() {

                        public void onClick(final ChartClickEvent event) {
                            // TODO Allow chart drilling.
                        
                            MessageBox.info("Clicked Bar", dot.getColour()); //$NON-NLS-1$
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

    private ChartData getPieChartData(final CellDataSet matrix, final String chartTitle,
            Map<String, Object> chartOptions, final ChartPanel chartPanel) {

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
        pie.setColours(getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor());
        pie.setAnimateOnShow(true);
        pie.setAnimation(new PieChart.PieBounceAnimation(30));

        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        for (int i = 0; i < data.size(); i++) {
            final BaseCell[] cell = data.get(i);
            int rc = 0;
            while (cell[rc].getRawValue() == null)
                rc++;
            Number cellValue = null;
	    if(((DataCell)cell[rowColCount]).getRawNumber() != null){
            	cellValue = ((DataCell) cell[rowColCount]).getRawNumber();
            	
            	final int row = rc;
	    	List<String> path = ((MemberCell) cell[rc]).getMemberPath();
	    	String label =""; //$NON-NLS-1$
	    	for(int j=0; j<path.size(); j++){
	    	    label+=path.get(j);
	    	    if(j!=path.size()-1)
	    		label+=",";
	    	}
                final Slice slice = new Slice((cellValue.floatValue()), label);

                slice.addChartClickHandler(new ChartClickHandler() {

                    public void onClick(final ChartClickEvent event) {
                                               
                        ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(), DrillType.POSITION, ((MemberCell) cell[row]), new AsyncCallback<Object>(){

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
           
                        MessageBox.info("Clicked Slice", slice.getLabel()); //$NON-NLS-1$
                    }

                });
                pie.addSlices(slice);
            }
        }
        cd.addElements(pie);
        return cd;
    }

}
