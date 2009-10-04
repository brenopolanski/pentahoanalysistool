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

import org.gwtwidgets.client.style.Color;
import org.pentaho.pat.client.ui.panels.ChartPanel.ChartType;
import org.pentaho.pat.client.util.PatTableModel;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend;
import com.rednels.ofcgwt.client.model.Text;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.axis.Label;
import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;
import com.rednels.ofcgwt.client.model.elements.BarChart;
import com.rednels.ofcgwt.client.model.elements.LineChart;
import com.rednels.ofcgwt.client.model.elements.PieChart;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;
import com.rednels.ofcgwt.client.model.elements.dot.HollowDot;

/**
 *TODO JAVADOC
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class ChartFactory {

    /**
     *TODO JAVADOC
     * 
     * @param pie
     * @param matrix
     * @param string
     * @return
     */
    public ChartData getChart(final ChartType pie, final CellDataSet matrix, final String string) {
        if (pie.equals(ChartType.PIE))
            return getPieChartData(matrix, string);
        else if (pie.equals(ChartType.BAR))
            return getBarChartGlassData(matrix, string);
        else if (pie.equals(ChartType.LINE))
            return getLineChartData(matrix, string);
        else
            return null;
    }

    public boolean isParsableToInt(final String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (final NumberFormatException nfe) {
            return false;
        }
    }

    String getRandomColor() {
        final double r = Math.random() * 256;
        final double g = Math.random() * 256;
        final double b = Math.random() * 256;
        final Color myColor = new Color((int) r, (int) g, (int) b);
        return myColor.getHexValue();
    }

    private ChartData getBarChartGlassData(final CellDataSet matrix, final String chartTitle) {

        matrix.getCellSetHeaders();
        final PatTableModel patTableModel = new PatTableModel(matrix);
        final BaseCell[][] rowData = patTableModel.getRowData();
        int rowColCount = 0;
        for (int i = 0; i < patTableModel.getColumnCount(); i++)
            if (rowData[0][i] instanceof MemberCell)
                rowColCount++;

        final ChartData cd2 = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;");
        cd2.setBackgroundColour("#ffffff");
        final XAxis xa = new XAxis();

        cd2.setXAxis(xa);
        final YAxis ya = new YAxis();
        ya.setSteps(16);

        cd2.setYAxis(ya);
        final BarChart bchart2 = new BarChart(BarStyle.GLASS);
        bchart2.setColour("#00aa00");
        bchart2.setTooltip("$#val#");

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
            if (isParsableToInt(cell[rowColCount].getRawValue()))
                ;
            {
                bchart2.addValues(Integer.parseInt(cell[rowColCount].getRawValue()));
                if (Integer.parseInt(cell[rowColCount].getRawValue()) > maxval)
                    maxval = Integer.parseInt(cell[rowColCount].getRawValue());
            }

        }
        ya.setMax(maxval);
        cd2.addElements(bchart2);
        return cd2;
    }

    private ChartData getLineChartData(final CellDataSet matrix, final String chartTitle) {

        final PatTableModel patTableModel = new PatTableModel(matrix);
        final BaseCell[][] rowData = patTableModel.getRowData();
        int rowColCount = 0;
        for (int i = 0; i < patTableModel.getColumnCount(); i++)
            if (rowData[0][i] instanceof MemberCell)
                rowColCount++;

        final ChartData cd = new ChartData("Relative Performance",
                "font-size: 14px; font-family: Verdana; text-align: center;");

        cd.setBackgroundColour("#ffffff");

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
        int actualRow = rowColCount;
        for (int i = 0; i < patTableModel.getColumnCount() - rowColCount; i++) {
            
            final LineChart lc2 = new LineChart();
            lc2.setDotStyle(new HollowDot());
            lc2.setColour(getRandomColor());

            for (int j = 0; j < data.size(); j++) {
                final BaseCell[] cell = data.get(j);
               
                if (dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue() != null) {
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 1][actualRow].getRawValue());
                } else
                    lc2.setText(dataColHeaders[patTableModel.getOffset() - 2][actualRow].getRawValue());

                if (isParsableToInt(cell[actualRow].getRawValue()))
                {
                    lc2.addValues(Integer.parseInt(cell[actualRow].getRawValue()));
                    if (Integer.parseInt(cell[actualRow].getRawValue()) > maxval)
                        maxval = Integer.parseInt(cell[actualRow].getRawValue());
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

        ya.setMin(-1);
        cd.setYAxis(ya);

        cd.setXLegend(new Text("Annual performance over 30 years", "{font-size: 10px; color: #000000}"));

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

        final ChartData cd = new ChartData(chartTitle, "font-size: 14px; font-family: Verdana; text-align: center;");
        cd.setBackgroundColour("#ffffff");
        cd.setLegend(new Legend(Position.RIGHT, true));

        final PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setRadius(130);
        pie.setNoLabels(true);
        pie.setTooltip("#label# $#val#<br>#percent#");
        pie.setGradientFill(true);
        pie.setColours("#ff0000", "#00aa00", "#0000ff", "#ff9900", "#ff00ff");
        pie.setAnimateOnShow(true);
        pie.setAnimation(new PieChart.PieBounceAnimation(30));

        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        for (int i = 0; i < data.size(); i++) {
            final BaseCell[] cell = data.get(i);
            int rc = 0;
            while (cell[rc].getRawValue() == null)
                rc++;
            if (isParsableToInt(cell[rowColCount].getRawValue()))
                ;
            pie
                    .addSlices(new PieChart.Slice(Integer.parseInt(cell[rowColCount].getRawValue()), cell[rc]
                            .getRawValue()));
        }
        cd.addElements(pie);
        return cd;
    }

}
