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

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwtwidgets.client.style.Color;
import org.pentaho.pat.client.ui.panels.ChartPanel.ChartType;
import org.pentaho.pat.client.util.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
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
import com.rednels.ofcgwt.client.model.elements.LineChart;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.Bar;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;
import com.rednels.ofcgwt.client.model.elements.PieChart.Slice;
import com.rednels.ofcgwt.client.model.elements.dot.BaseDot;
import com.rednels.ofcgwt.client.model.elements.dot.HollowDot;
import com.rednels.ofcgwt.client.model.elements.dot.SolidDot;

/**
 * A basic Chart Factory that creates different OFC chart models.
 * 
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class ChartFactory {

    /**
     * The Chart Factory Constructor.
     * 
     * @param chartType
     *            The ChartType Enum value.
     * @param matrix
     *            The CellDataSet from the current query.
     * @param chartTitle
     *            The chart title.
     * @return A ChartData object.
     */
    public ChartData getChart(final ChartType chartType, final CellDataSet matrix, final String chartTitle) {
        if (chartType.equals(ChartType.PIE))
            return getPieChartData(matrix, chartTitle);
        else if (chartType.equals(ChartType.BAR))
            return getBarChartData(matrix, chartTitle);
        else if (chartType.equals(ChartType.LINE))
            return getLineChartData(matrix, chartTitle);
        else
            return null;
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
    private ChartData getBarChartData(final CellDataSet matrix, final String chartTitle) {

        matrix.getCellSetHeaders();
        final PatTableModel patTableModel = new PatTableModel(matrix);
        final BaseCell[][] rowData = patTableModel.getRowData();
        int rowColCount = 0;
        for (int i = 0; i < patTableModel.getColumnCount(); i++)
            if (rowData[0][i] instanceof MemberCell)
                rowColCount++;

        // TODO Allow user defined fonts etc.
        final ChartData cd = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;");

        // TODO Allow user defined background color.
        cd.setBackgroundColour("#ffffff");

        final XAxis xa = new XAxis();

        cd.setXAxis(xa);
        final YAxis ya = new YAxis();

        // TODO Allow users to select the steppage.
        ya.setSteps(16);

        cd.setYAxis(ya);

        // TODO Allow users to change the Bar Style.
        final BarChart bchart2 = new BarChart(BarStyle.GLASS);

        // TODO Allow user defined tooltips.
        bchart2.setTooltip("$#val#");

        // TODO Allow user defined Legend on or off and position.
        cd.setLegend(new Legend(Position.RIGHT, true));

        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        final Label[] labels = new Label[data.size()];
        for (int i = 0; i < data.size(); i++) {
            final BaseCell[] cell = data.get(i);
            int rc = 0;
            while (cell[rc].getRawValue() == null)
                rc++;
            labels[i] = new Label(cell[rc].getRawValue(), 45);

        }

        xa.addLabels(labels);

        int maxval = 0;
        for (int i = 0; i < data.size(); i++) {
            final BaseCell[] cell = data.get(i);
            int rc = 0;
            while (cell[rc].getRawValue() == null)
                rc++;

            if (isParsableToInt(cell[rowColCount].getRawValue())) {
                final Bar bar = new Bar(Integer.parseInt(cell[rowColCount].getRawValue()));
                bar.setColour(getRandomColor());
                bar.addChartClickHandler(new ChartClickHandler() {

                    public void onClick(final ChartClickEvent event) {
                        // TODO Allow chart drilling.
                        MessageBox.info("Clicked Bar", bar.getColour());
                    }

                });
                bchart2.addBars(bar);
                if (Integer.parseInt(cell[rowColCount].getRawValue()) > maxval)
                    maxval = Integer.parseInt(cell[rowColCount].getRawValue());
            }

        }
        ya.setMax(maxval);
        cd.addElements(bchart2);
        return cd;
    }

    private ChartData getLineChartData(final CellDataSet matrix, final String chartTitle) {

        final PatTableModel patTableModel = new PatTableModel(matrix);
        final BaseCell[][] rowData = patTableModel.getRowData();
        int rowColCount = 0;
        for (int i = 0; i < patTableModel.getColumnCount(); i++)
            if (rowData[0][i] instanceof MemberCell)
                rowColCount++;

        // TODO Allow user defined fonts etc.
        final ChartData cd = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;");

        // TODO Allow user defined background color.
        cd.setBackgroundColour("#ffffff");

        // TODO Allow user defined Legend on or off and position.
        cd.setLegend(new Legend(Position.RIGHT, true));
        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        final BaseCell[][] dataColHeaders = patTableModel.getColumnHeaders();
        final Label[] labels = new Label[data.size()];
        for (int i = 0; i < data.size(); i++) {
            final BaseCell[] cell = data.get(i);
            int rc = 0;
            while (cell[rc].getRawValue() == null)
                rc++;
            labels[i] = new Label(cell[rc].getRawValue(), 45);

        }

        int maxval = 0;
        int minval = 0;
        int actualRow = rowColCount;
        for (int i = 0; i < patTableModel.getColumnCount() - rowColCount; i++) {

            final LineChart lc2 = new LineChart();
            lc2.setDotStyle(new HollowDot());
            lc2.setColour(getRandomColor());

            for (int j = 0; j < data.size(); j++) {
                final BaseCell[] cell = data.get(j);

                if (dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue() != null)
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue());
                else
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 2][actualRow].getRawValue());

                if (isParsableToInt(cell[actualRow].getRawValue())) {
                    final BaseDot dot = new SolidDot(Integer.parseInt(cell[actualRow].getRawValue()));
                    dot.addChartClickHandler(new ChartClickHandler() {

                        public void onClick(final ChartClickEvent event) {
                            // TODO Allow chart drilling.
                            MessageBox.info("Clicked Bar", dot.getColour());
                        }

                    });
                    lc2.addDots(dot);
                    if (Integer.parseInt(cell[actualRow].getRawValue()) > maxval)
                        maxval = Integer.parseInt(cell[actualRow].getRawValue());
                    if (Integer.parseInt(cell[actualRow].getRawValue()) < minval)
                        minval = Integer.parseInt(cell[actualRow].getRawValue());
                }

            }
            cd.addElements(lc2);
            actualRow++;
        }

        final XAxis xa = new XAxis();
        xa.setSteps(2);
        xa.addLabels(labels);
        cd.setXAxis(xa);

        final YAxis ya = new YAxis();

        ya.setMin(minval);
        cd.setYAxis(ya);

        // cd.setXLegend(new Text(xLegend, "{font-size: 10px; color: #000000}"));

        ya.setMax(maxval);

        return cd;
    }

    private ChartData getPieChartData(final CellDataSet matrix, final String chartTitle) {

        matrix.getCellSetHeaders();
        final PatTableModel patTableModel = new PatTableModel(matrix);
        final BaseCell[][] rowData = patTableModel.getRowData();
        int rowColCount = 0;
        for (int i = 0; i < patTableModel.getColumnCount(); i++)
            if (rowData[0][i] instanceof MemberCell)
                rowColCount++;

        // TODO Allow user defined fonts etc.
        final ChartData cd = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;");

        // TODO Allow user defined background color.
        cd.setBackgroundColour("#ffffff");

        // TODO Allow user defined Legend on or off and position.
        cd.setLegend(new Legend(Position.RIGHT, true));

        final PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setRadius(130);
        pie.setNoLabels(true);
        
        // TODO Allow user defined tooltips.
        pie.setTooltip("#label# $#val#<br>#percent#");
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
            if (isParsableToInt(cell[rowColCount].getRawValue())) {
                final Slice slice = new Slice(Integer.parseInt(cell[rowColCount].getRawValue()), cell[rc].getRawValue());

                slice.addChartClickHandler(new ChartClickHandler() {

                    public void onClick(final ChartClickEvent event) {
                        // TODO Allow chart drilling.
                        MessageBox.info("Clicked Slice", slice.getLabel());
                    }

                });
                pie.addSlices(slice);
            }
        }
        cd.addElements(pie);
        return cd;
    }

}
