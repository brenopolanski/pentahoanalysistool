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
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.windows.ChartOptionsWindow;
import org.pentaho.pat.client.util.factory.ChartFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.rednels.ofcgwt.client.ChartWidget;

/**
 * Create the chart panel, currently supports Open Flash Charts.
 *
 * @since 0.5.0 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class ChartPanel extends LayoutComposite implements QueryListener {

    
    private CellDataSet matrix;
    private final ChartWidget chart = new ChartWidget();
    private ChartFactory cf = new ChartFactory();
    private ChartType ct = ChartType.LINE;
    
    public ChartPanel(){
        GlobalConnectionFactory.getQueryInstance().addQueryListener(ChartPanel.this);

        final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());
        
        ((BoxLayout)layoutPanel.getLayout()).setAlignment(Alignment.CENTER);
        ((BoxLayout)layoutPanel.getLayout()).setOrientation(Orientation.VERTICAL);
        layoutPanel.add(buttonLayoutPanel());
        layoutPanel.add(chart, new BoxLayoutData(FillStyle.HORIZONTAL));
        
        this.getLayoutPanel().add(layoutPanel);

    }
    
    /* (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(Widget sender) {
        
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String, org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(String queryId, CellDataSet matrix) {
        if (Pat.getCurrQuery() != null && queryId == Pat.getCurrQuery() && this.isAttached()){
        this.matrix = matrix;
        chart.setChartData(cf.getChart(ct, matrix, Pat.getCurrQuery()));
        this.layout();
        }
    }

    public enum ChartType {
        PIE,
        BAR,
        LINE
   }
        
    private LayoutPanel buttonLayoutPanel(){
        final LayoutPanel buttonsBox = new LayoutPanel();
        buttonsBox.setLayout(new BoxLayout());

        ToolButton pieButton = new ToolButton(ButtonHelper.createButtonLabel(
                MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Pie Chart",
                ButtonLabelType.TEXT_ON_TOP));
        pieButton.addClickHandler(new ClickHandler(){
            
            public void onClick(ClickEvent arg0) {
                ct = ChartType.PIE;
                chart.setChartData(cf.getChart(ct, matrix, Pat.getCurrQuery()));
            }
            
        });

        ToolButton barButton = new ToolButton(ButtonHelper.createButtonLabel(
                MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Bar Chart",
                ButtonLabelType.TEXT_ON_TOP));
        barButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                ct = ChartType.BAR;
                chart.setChartData(cf.getChart(ct, matrix, Pat.getCurrQuery()));
            }
            
        });
       
        
        ToolButton lineButton = new ToolButton(ButtonHelper.createButtonLabel(
                MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Line Chart",
                ButtonLabelType.TEXT_ON_TOP));
        
        lineButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                ct = ChartType.LINE;
                chart.setChartData(cf.getChart(ct, matrix, Pat.getCurrQuery()));
            }
            
        });

        ToolButton optionsButton = new ToolButton(ButtonHelper.createButtonLabel(
                MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Options",
                ButtonLabelType.TEXT_ON_TOP));
        
        optionsButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                ChartOptionsWindow.display();
            }
            
        });

        
        buttonsBox.add(pieButton);
        buttonsBox.add(barButton);
        buttonsBox.add(lineButton);
        buttonsBox.add(optionsButton);
        return buttonsBox;
    }
}
