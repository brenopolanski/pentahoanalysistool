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

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.pentaho.pat.client.util.colorpicker.ColorPicker;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.rednels.ofcgwt.client.model.Legend.Position;
import com.rednels.ofcgwt.client.model.elements.BarChart.BarStyle;

/**
 * Create the chart options panel.
 * 
 * @created Oct 4, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class ChartOptionsPanel extends LayoutComposite {

    private WindowPanel basic;

    private TextBox chartTitleTextBox = new TextBox();

    private TextBox xAxisTextBox = new TextBox();

    private TextBox yAxisTextBox = new TextBox();

    private TextBox bgColorTextBox = new TextBox();

    private Position pos;

    private BarStyle bs = BarStyle.NORMAL;

    private ToolButton legendTopButton = new ToolButton(ConstantFactory.getInstance().top());

    private ToolButton legendRightButton = new ToolButton(ConstantFactory.getInstance().right());

    private Map<String, Object> optionsMap = new HashMap<String, Object>();

    private TextBox yaxisColorTextBox = new TextBox();

    private TextBox yAxisGridColorTextBox = new TextBox();

    private TextBox yAxisMinTextBox = new TextBox();

    private TextBox yAxisMaxTextBox = new TextBox();

    private TextBox xaxisColorTextBox = new TextBox();

    private TextBox xaxisGridColorTextBox = new TextBox();

    private TextBox xaxisMinTextBox = new TextBox();

    private TextBox xaxisMaxTextBox = new TextBox();

    /**
     * 
     * Chart options constructor.
     * 
     */
    public ChartOptionsPanel() {
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
        createBasicWindowPanel();
        bgColorTextBox.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {

                basic.showModal(false);

            }

        });
        LayoutPanel generalOptionsPanel = new LayoutPanel();
        FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p"); //$NON-NLS-1$

        legendTopButton.setStyle(ToolButtonStyle.RADIO);
        legendTopButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {
                pos = Position.TOP;

            }

        });

        legendRightButton.setStyle(ToolButtonStyle.RADIO);
        legendRightButton.setEnabled(false);
        legendRightButton.setChecked(true);
        legendRightButton.addClickHandler(new ClickHandler() {

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
        builder.add(legendTopButton);
        builder.nextColumn(2);
        builder.nextColumn(2);
        builder.add(legendRightButton);
        builder.nextLine(2);
        builder.addLabel(ConstantFactory.getInstance().backgroundColor());
        builder.nextColumn(2);
        builder.add(bgColorTextBox);
        builder.nextLine(2);
        builder.addLabel(ConstantFactory.getInstance().yaxisColor());
        builder.nextColumn(2);
        builder.add(yaxisColorTextBox);
        builder.nextColumn(2);
        builder.addLabel(ConstantFactory.getInstance().yaxisGridColor());
        builder.nextColumn(2);
        builder.add(yAxisGridColorTextBox);
        builder.nextLine(2);
        
        builder.addLabel(ConstantFactory.getInstance().yaxisMin());
        builder.nextColumn(2);
        builder.add(yAxisMinTextBox);
        builder.nextColumn(2);
        builder.addLabel(ConstantFactory.getInstance().yaxisMax());
        builder.nextColumn(2);
        builder.add(yAxisMaxTextBox);
        builder.nextLine(2);
        
        builder.nextLine(2);
        builder.addLabel(ConstantFactory.getInstance().xaxisColor());
        builder.nextColumn(2);
        builder.add(xaxisColorTextBox);
        builder.nextColumn(2);
        builder.addLabel(ConstantFactory.getInstance().xaxisGridColor());
        builder.nextColumn(2);
        builder.add(xaxisGridColorTextBox);
        builder.nextLine(2);
        
        builder.addLabel(ConstantFactory.getInstance().xaxisMin());
        builder.nextColumn(2);
        builder.add(xaxisMinTextBox);
        builder.nextColumn(2);
        builder.addLabel(ConstantFactory.getInstance().xaxisMax());
        builder.nextColumn(2);
        builder.add(xaxisMaxTextBox);
        builder.nextLine(2);
        generalOptionsPanel.add(builder.getPanel());
        return generalOptionsPanel;
    }

    /**
     * 
     * Create the bar options panel.
     * 
     * @return A LayoutPanel.
     */
    private LayoutPanel barOptionsPanel() {

        LayoutPanel barOptionsPanel = new LayoutPanel();

        FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p"); //$NON-NLS-1$

        final ListBox<String> listBox = new ListBox<String>();

        final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();

        model.add(ConstantFactory.getInstance().glass());
        model.add(ConstantFactory.getInstance().normal());
        model.add(ConstantFactory.getInstance().threed());

        listBox.addRowSelectionHandler(new RowSelectionHandler() {
            public void onRowSelection(RowSelectionEvent event) {
                int index = listBox.getSelectedIndex();
                if (index != -1) {
                    switch (listBox.getSelectedIndex()) {
                    case 0:
                        setBs(BarStyle.GLASS);
                        break;
                    case 1:
                        setBs(BarStyle.NORMAL);
                        break;
                    case 2:
                        setBs(BarStyle.THREED);
                        break;
                    default:
                        throw new RuntimeException("Should not happen"); //$NON-NLS-1$
                    }
                }
            }
        });

        PanelBuilder builder = new PanelBuilder(layout);

        builder.addSeparator(ConstantFactory.getInstance().barChartOptions());

        builder.nextLine(2);

        builder.addLabel(ConstantFactory.getInstance().barStyle());
        builder.nextColumn(2);
        builder.add(listBox);
        builder.nextLine(2);

        barOptionsPanel.add(builder.getPanel());
        return barOptionsPanel;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the chartTitleTextBox Text Value
     */
    public String getChartTitleTextBox() {
        return chartTitleTextBox.getText();
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param chartTitleTextBox
     *            the chartTitleTextBox to set
     */
    public void setChartTitleTextBox(String chartTitleTextBox) {
        this.chartTitleTextBox.setText(chartTitleTextBox);
    }

    /**
     *TODO JAVADOC
     * 
     * @return the xAxisTextBox Text Value
     */
    public String getxAxisTextBox() {
        return xAxisTextBox.getText();
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param xAxisTextBox
     *            the xAxisTextBox to set
     */
    public void setxAxisTextBox(String xAxisTextBox) {
        this.xAxisTextBox.setText(xAxisTextBox);
    }

    /**
     *TODO JAVADOC
     * 
     * @return the yAxisTextBox
     */
    public String getyAxisTextBox() {
        return yAxisTextBox.getText();
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param yAxisTextBox
     *            the yAxisTextBox to set
     */
    public void setyAxisTextBox(String yAxisTextBox) {
        this.yAxisTextBox.setText(yAxisTextBox);
    }

    /**
     *TODO JAVADOC
     * 
     * @return the bgColorTextBox
     */
    public String getBgColorTextBox() {
        return bgColorTextBox.getText();
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param bgColorTextBox
     *            the bgColorTextBox to set
     */
    public void setBgColorTextBox(String bgColorTextBox) {
        this.bgColorTextBox.setText(bgColorTextBox);
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
     * 
     *TODO JAVADOC
     * 
     * @param pos
     *            the pos to set
     */
    public void setPos(Position pos) {
        this.pos = pos;
    }

    private void createBasicWindowPanel() {

        basic = new WindowPanel(ConstantFactory.getInstance().backgroundColor()); 
        basic.setSize("500px", "500px"); //$NON-NLS-1$ //$NON-NLS-2$
        basic.setAnimationEnabled(true);
        ColorPicker cp = new ColorPicker();
        basic.setWidget(cp);

        basic.getHeader().add(Caption.IMAGES.window().createImage());

        basic.addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(CloseEvent<PopupPanel> event) {
                basic = null;
            }
        });
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param bs
     *            the bs to set
     */
    public void setBs(BarStyle bs) {
        this.bs = bs;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the bs
     */
    public BarStyle getBs() {
        return bs;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param barOptions
     *            the barOptions to set
     */
    public void setBarOptions(Map<String, Object> optionsMap) {
        this.optionsMap = optionsMap;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the barOptions
     */
    public Map<String, Object> getOptionsMap() {
        optionsMap = new HashMap<String, Object>();
        optionsMap.put("barStyle", bs); //$NON-NLS-1$
        return optionsMap;
    }

}
