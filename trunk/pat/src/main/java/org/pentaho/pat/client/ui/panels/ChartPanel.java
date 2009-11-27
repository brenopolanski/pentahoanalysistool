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
package org.pentaho.pat.client.ui.panels;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.ui.windows.ChartOptionsWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.charts.ChartFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.rednels.ofcgwt.client.ChartWidget;
import com.rednels.ofcgwt.client.model.Legend.Position;

/**
 * Create the chart panel, currently supports Open Flash Charts.
 * 
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class ChartPanel extends LayoutComposite {

    /**
     * Chart Type Enum.
     * 
     * @author tom(at)wamonline.org.uk
     * @since 0.5.0
     */
    public enum ChartType {
        PIE, BAR, LINE
    }

    private CellDataSet matrix;

    private final ChartWidget chart = new ChartWidget();

    private final ChartFactory cf = new ChartFactory();

    private ChartType ct = ChartType.LINE;

    private final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    private final LayoutPanel chartLayoutPanel = new LayoutPanel();

    private String chartTitle = new String();

    private String xAxisLabel = new String();

    private String yAxisLabel = new String();

    private Position pos;

    private Map<String, Object> optionsMap = new HashMap<String, Object>();

    /**
     * Chart Panel Constructor.
     */
    public ChartPanel() {
        // GlobalConnectionFactory.getQueryInstance().addQueryListener(ChartPanel.this);

        ((BoxLayout) layoutPanel.getLayout()).setAlignment(Alignment.CENTER);
        ((BoxLayout) layoutPanel.getLayout()).setOrientation(Orientation.VERTICAL);
        layoutPanel.add(createBtnLayoutPanel());
        chartLayoutPanel.add(chart);
        layoutPanel.add(chartLayoutPanel, new BoxLayoutData(FillStyle.BOTH));

        this.getLayoutPanel().add(layoutPanel);

    }

    /**
     *TODO JAVADOC
     * 
     * @return the chartTitle
     */
    public String getChartTitle() {
        return chartTitle;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the ct
     */
    public ChartType getCt() {
        return ct;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the pos
     */
    public Position getPos() {
        return pos;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the xAxisLabel
     */
    public String getxAxisLabel() {
        return xAxisLabel;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the yAxisLabel
     */
    public String getyAxisLabel() {
        return yAxisLabel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(final Widget sender) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final CellDataSet matrix) {

        this.matrix = matrix;
        chart.setChartData(cf.getChart(ct, matrix, chartTitle, pos, optionsMap, ChartPanel.this));

        this.layout();

    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param chartTitle
     *            the chartTitle to set
     */
    public void setChartTitle(final String chartTitle) {
        this.chartTitle = chartTitle;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param ct
     *            the ct to set
     */
    public void setCt(final ChartType chartType) {
        this.ct = chartType;
    }

    /**
     *TODO JAVADOC
     * 
     * @param barOptions
     */
    public void setOptionsMap(final Map<String, Object> optionsMap) {
        this.optionsMap = optionsMap;

    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param pos
     *            the pos to set
     */
    public void setPos(final Position pos) {
        this.pos = pos;

    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param xAxisLabel
     *            the xAxisLabel to set
     */
    public void setxAxisLabel(final String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param yAxisLabel
     *            the yAxisLabel to set
     */
    public void setyAxisLabel(final String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    /**
     *TODO JAVADOC
     * 
     */
    public void updateChart() {
        chart.setChartData(cf.getChart(ct, matrix, chartTitle, pos, optionsMap, ChartPanel.this));
        chartLayoutPanel.add(chart);
        this.layout();
    }

    /**
     * 
     * Button Panel for chart panel.
     * 
     * @return A LayoutPanel containing chart buttons.
     */
    private LayoutPanel createBtnLayoutPanel() {
        final LayoutPanel buttonsBox = new LayoutPanel();
        buttonsBox.setLayout(new BoxLayout());

        final ToolButton pieButton = new ToolButton(ButtonHelper.createButtonLabel(MessageBox.MESSAGEBOX_IMAGES
                .dialogInformation(), ConstantFactory.getInstance().pieChart(), ButtonLabelType.TEXT_ON_TOP));
        pieButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ct = ChartType.PIE;
                chart.setChartData(cf.getChart(ct, matrix, chartTitle, pos, optionsMap, ChartPanel.this));

            }

        });

        final ToolButton barButton = new ToolButton(ButtonHelper.createButtonLabel(MessageBox.MESSAGEBOX_IMAGES
                .dialogInformation(), ConstantFactory.getInstance().barChart(), ButtonLabelType.TEXT_ON_TOP));

        barButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {
                ct = ChartType.BAR;
                chart.setChartData(cf.getChart(ct, matrix, chartTitle, pos, optionsMap, ChartPanel.this));
            }
        });

        final ToolButton lineButton = new ToolButton(ButtonHelper.createButtonLabel(MessageBox.MESSAGEBOX_IMAGES
                .dialogInformation(), ConstantFactory.getInstance().lineChart(), ButtonLabelType.TEXT_ON_TOP));

        lineButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ct = ChartType.LINE;
                chart.setChartData(cf.getChart(ct, matrix, chartTitle, pos, optionsMap, ChartPanel.this));
            }
        });

        final ToolButton optionsButton = new ToolButton(ButtonHelper.createButtonLabel(MessageBox.MESSAGEBOX_IMAGES
                .dialogInformation(), ConstantFactory.getInstance().chartOptions(), ButtonLabelType.TEXT_ON_TOP));

        optionsButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {
                ChartOptionsWindow.display(ChartPanel.this);
            }
        });

        buttonsBox.add(pieButton);
        buttonsBox.add(barButton);
        buttonsBox.add(lineButton);
        buttonsBox.add(optionsButton);
        return buttonsBox;
    }
}
