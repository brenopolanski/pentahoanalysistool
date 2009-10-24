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

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.rednels.ofcgwt.client.model.Legend.Position;

/**
 * Create the chart options panel.
 *
 * @created Oct 4, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 *
 */
public class ChartOptionsPanel extends LayoutComposite{

    private TextBox chartTitleTextBox = new TextBox();
    private TextBox xAxisTextBox = new TextBox();
    private TextBox yAxisTextBox = new TextBox();
    private TextBox bgColorTextBox = new TextBox();
    private Position pos;
    ToolButton legendOffButton = new ToolButton(ConstantFactory.getInstance().off());
    ToolButton legendTopButton = new ToolButton(ConstantFactory.getInstance().top());    
    ToolButton legendRightButton = new ToolButton(ConstantFactory.getInstance().right());
    /**
     * 
     * Chart options constructor.
     *
     */
    public ChartOptionsPanel(){
        final TabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();

        tabPanel.add(generalOptionsPanel(), ConstantFactory.getInstance().generalOptions());
        tabPanel.add(barOptionsPanel(), ConstantFactory.getInstance().barChartOptions());


        this.getLayoutPanel().add(tabPanel);
        
    }
    
    /**
     * Create the general options panel.
     *
     * @return A LayoutPanel.
     */
    private LayoutPanel generalOptionsPanel() {
        LayoutPanel generalOptionsPanel = new LayoutPanel();
        FormLayout layout = new FormLayout(
                "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                    + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                    + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                    + "p, 3dlu, p, 3dlu, p, 3dlu, p"); //$NON-NLS-1$

        
        legendOffButton.setStyle(ToolButtonStyle.RADIO);
        legendOffButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                pos = null;
                
            }
            
        });
        legendTopButton.setStyle(ToolButtonStyle.RADIO);
        legendTopButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                pos = Position.TOP;
                
            }
            
        });
        
        legendRightButton.setStyle(ToolButtonStyle.RADIO);
        legendRightButton.setEnabled(false);
        legendRightButton.setChecked(true);
        legendRightButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                pos = Position.RIGHT;
                
            }
            
        });

        PanelBuilder builder = new PanelBuilder(layout);

        builder.addSeparator(ConstantFactory.getInstance().titles());

        builder.nextLine(2);

        builder.addLabel(ConstantFactory.getInstance().chartTitle());
        builder.nextColumn(2);
        builder.add(chartTitleTextBox);
        builder.nextLine(2);

        builder.addLabel(ConstantFactory.getInstance().xaxisLabel());
        builder.nextColumn(2);
        builder.add(xAxisTextBox);
        builder.nextColumn(2);
        builder.addLabel(ConstantFactory.getInstance().yaxisLabel());
        builder.nextColumn(2);
        builder.add(yAxisTextBox);
        builder.nextLine(2);
        builder.addLabel(ConstantFactory.getInstance().legend());
        builder.nextColumn(2);
        builder.add(legendOffButton);
        builder.nextColumn(2);
        builder.add(legendTopButton);
        builder.nextColumn(2);
        builder.add(legendRightButton);
        builder.nextLine(2);
        builder.addLabel(ConstantFactory.getInstance().backgroundColor());
        builder.nextColumn(2);
        builder.add(bgColorTextBox);
        

        generalOptionsPanel.add(builder.getPanel());
        return generalOptionsPanel;
    }

    /**
     * 
     * Create the bar options panel.
     *
     * @return A LayoutPanel.
     */
    private LayoutPanel barOptionsPanel(){
        
        LayoutPanel barOptionsPanel = new LayoutPanel();
        
        final ListBox<String> listBox = new ListBox<String>();

        final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();

        model.add(ConstantFactory.getInstance().glass());
        model.add(ConstantFactory.getInstance().normal());
        model.add(ConstantFactory.getInstance().threed());
        
        listBox.addRowSelectionHandler(new RowSelectionHandler() {
            public void onRowSelection(RowSelectionEvent event) {
              int index = listBox.getSelectedIndex();
              if (index != -1) {
                //InfoPanel.show("RowSelectionHandler", listBox.getItem(index));
              }
            }
          });

        return barOptionsPanel;
    }

    /**
     *TODO JAVADOC
     * @return the chartTitleTextBox Text Value
     */
    public String getChartTitleTextBox() {
        return chartTitleTextBox.getText();
    }

    /**
     *
     *TODO JAVADOC
     * @param chartTitleTextBox the chartTitleTextBox to set
     */
    public void setChartTitleTextBox(String chartTitleTextBox) {
        this.chartTitleTextBox.setText(chartTitleTextBox);
    }

    /**
     *TODO JAVADOC
     * @return the xAxisTextBox Text Value
     */
    public String getxAxisTextBox() {
        return xAxisTextBox.getText();
    }

    /**
     *
     *TODO JAVADOC
     * @param xAxisTextBox the xAxisTextBox to set
     */
    public void setxAxisTextBox(String xAxisTextBox) {
        this.xAxisTextBox.setText(xAxisTextBox);
    }

    /**
     *TODO JAVADOC
     * @return the yAxisTextBox
     */
    public String getyAxisTextBox() {
        return yAxisTextBox.getText();
    }

    /**
     *
     *TODO JAVADOC
     * @param yAxisTextBox the yAxisTextBox to set
     */
    public void setyAxisTextBox(String yAxisTextBox) {
        this.yAxisTextBox.setText(yAxisTextBox);
    }

    /**
     *TODO JAVADOC
     * @return the bgColorTextBox
     */
    public String getBgColorTextBox() {
        return bgColorTextBox.getText();
    }

    /**
     *
     *TODO JAVADOC
     * @param bgColorTextBox the bgColorTextBox to set
     */
    public void setBgColorTextBox(String bgColorTextBox) {
        this.bgColorTextBox.setText(bgColorTextBox);
    }

    /**
     *TODO JAVADOC
     * @return the pos
     */
    public Position getPos() {
        return pos;
    }

    /**
     *
     *TODO JAVADOC
     * @param pos the pos to set
     */
    public void setPos(Position pos) {
        this.pos = pos;
    }

    
}
