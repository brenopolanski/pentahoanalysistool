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
package org.pentaho.pat.client.util.factory.charts;

import java.util.Map;

import org.pentaho.pat.client.ui.panels.ChartPanel;
import org.pentaho.pat.client.ui.panels.ChartPanel.ChartType;
import org.pentaho.pat.client.util.factory.charts.types.BarChartType;
import org.pentaho.pat.client.util.factory.charts.types.LineChartType;
import org.pentaho.pat.client.util.factory.charts.types.PieChartType;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.rednels.ofcgwt.client.model.ChartData;
import com.rednels.ofcgwt.client.model.Legend.Position;

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
            PieChartType pie = new PieChartType();
            return pie.getPieChartData(matrix, chartTitle, chartOptions, chartPanel, pos);
        case LINE:
            LineChartType line = new LineChartType();
            return line.getLineChartData(matrix, chartTitle, chartOptions, chartPanel, pos);
        case BAR:
            BarChartType bar = new BarChartType();
            return bar.getBarChartData(matrix, chartTitle, chartOptions, pos, chartPanel);
        default:
            return null;
        }
    }

    /**
     * Return the chart.
     * @param chartType
     * @param matrix
     * @param chartTitle
     * @param legendPosition
     * @param chartOptions
     * @param chartPanel
     * @return
     */
    public ChartData getChart(final ChartType chartType, final CellDataSet matrix, final String chartTitle,
            Position legendPosition, Map<String, Object> chartOptions, ChartPanel chartPanel) {
        pos = legendPosition;
        ChartData cd = getChart(chartType, matrix, chartTitle, chartOptions, chartPanel);

        return cd;

    }

    /**
     * Return the chart.
     * @param chartType
     * @param matrix
     * @param chartTitle
     * @param legendPosition
     * @param chartPanel
     * @return
     */
    public ChartData getChart(final ChartType chartType, final CellDataSet matrix, final String chartTitle,
            Position legendPosition, ChartPanel chartPanel) {
        Map<String, Object> chartOptions = null;
        ChartData cd = getChart(chartType, matrix, chartTitle, chartOptions, chartPanel);

        return cd;

    }

}
