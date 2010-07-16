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
package org.pentaho.pat.client.ui.panels.windows;

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
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.colorpicker.ColorPicker;

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
import com.rednels.ofcgwt.client.model.elements.dot.BaseDot;
import com.rednels.ofcgwt.client.model.elements.dot.HollowDot;
import com.rednels.ofcgwt.client.model.elements.dot.Star;

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

    private final TextBox chartTitleTextBox = new TextBox();

    private final TextBox xAxisTextBox = new TextBox();

    private final TextBox yAxisTextBox = new TextBox();

    private final TextBox bgColorTextBox = new TextBox();

    private Position pos;

    private BarStyle brStyle = BarStyle.NORMAL;

    private BaseDot bDot;

    private final ToolButton legendTopButton = new ToolButton(Pat.CONSTANTS.top());

    private final ToolButton legendRightButton = new ToolButton(Pat.CONSTANTS.right());

    private Map<String, Object> optionsMap = new HashMap<String, Object>();

    private final TextBox yaxisColorTextBox = new TextBox();

    private final TextBox yAxisGridColorTextBox = new TextBox();

    private final TextBox yAxisMinTextBox = new TextBox();

    private final TextBox yAxisMaxTextBox = new TextBox();

    private final TextBox xaxisColorTextBox = new TextBox();

    private final TextBox xaxisGridColorTextBox = new TextBox();

    private final TextBox xaxisMinTextBox = new TextBox();

    private final TextBox xaxisMaxTextBox = new TextBox();

    private final static String CHART_OPTIONS_PANEL = "pat-ChartOptionsPanel"; //$NON-NLS-1$
    /**
     * 
     * Chart options constructor.
     * 
     */
    public ChartOptionsPanel() {
        super();
        final TabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
        this.setStyleName(CHART_OPTIONS_PANEL);
        tabPanel.add(generalOptionsPanel(), Pat.CONSTANTS.generalOptions());
        tabPanel.add(barOptionsPanel(), Pat.CONSTANTS.barChartOptions());
        tabPanel.add(lineOptionsPanel(), Pat.CONSTANTS.lineChartOptions());
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

            public void onClick(final ClickEvent arg0) {

                basic.showModal(false);

            }

        });
        final LayoutPanel genOptPanel = new LayoutPanel();
        final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p"); //$NON-NLS-1$

        legendTopButton.setStyle(ToolButtonStyle.RADIO);
        legendTopButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                pos = Position.TOP;

            }

        });

        legendRightButton.setStyle(ToolButtonStyle.RADIO);
        legendRightButton.setEnabled(false);
        legendRightButton.setChecked(true);
        legendRightButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                pos = Position.RIGHT;

            }

        });

        final PanelBuilder builder = new PanelBuilder(layout);

        builder.addSeparator(Pat.CONSTANTS.titles());

        builder.nextLine(2);

        builder.addLabel(Pat.CONSTANTS.chartTitle());
        builder.nextColumn(2);
        builder.add(chartTitleTextBox);
        builder.nextLine(2);

        builder.addLabel(Pat.CONSTANTS.xaxisLabel());
        builder.nextColumn(2);
        builder.add(xAxisTextBox);
        builder.nextColumn(2);
        builder.addLabel(Pat.CONSTANTS.yaxisLabel());
        builder.nextColumn(2);
        builder.add(yAxisTextBox);
        builder.nextLine(2);
        builder.addLabel(Pat.CONSTANTS.legend());
        builder.nextColumn(2);
        builder.add(legendTopButton);
        builder.nextColumn(2);
        builder.nextColumn(2);
        builder.add(legendRightButton);
        builder.nextLine(2);
        builder.addLabel(Pat.CONSTANTS.backgroundColor());
        builder.nextColumn(2);
        builder.add(bgColorTextBox);
        builder.nextLine(2);
        builder.addLabel(Pat.CONSTANTS.yaxisColor());
        builder.nextColumn(2);
        builder.add(yaxisColorTextBox);
        builder.nextColumn(2);
        builder.addLabel(Pat.CONSTANTS.yaxisGridColor());
        builder.nextColumn(2);
        builder.add(yAxisGridColorTextBox);
        builder.nextLine(2);

        builder.addLabel(Pat.CONSTANTS.yaxisMin());
        builder.nextColumn(2);
        builder.add(yAxisMinTextBox);
        builder.nextColumn(2);
        builder.addLabel(Pat.CONSTANTS.yaxisMax());
        builder.nextColumn(2);
        builder.add(yAxisMaxTextBox);
        builder.nextLine(2);

        builder.nextLine(2);
        builder.addLabel(Pat.CONSTANTS.xaxisColor());
        builder.nextColumn(2);
        builder.add(xaxisColorTextBox);
        builder.nextColumn(2);
        builder.addLabel(Pat.CONSTANTS.xaxisGridColor());
        builder.nextColumn(2);
        builder.add(xaxisGridColorTextBox);
        builder.nextLine(2);

        builder.addLabel(Pat.CONSTANTS.xaxisMin());
        builder.nextColumn(2);
        builder.add(xaxisMinTextBox);
        builder.nextColumn(2);
        builder.addLabel(Pat.CONSTANTS.xaxisMax());
        builder.nextColumn(2);
        builder.add(xaxisMaxTextBox);
        builder.nextLine(2);
        genOptPanel.add(builder.getPanel());
        return genOptPanel;
    }

    /**
     * 
     * Create the bar options panel.
     * 
     * @return A LayoutPanel.
     */
    private LayoutPanel barOptionsPanel() {

        final LayoutPanel barOptionsPanel = new LayoutPanel();

        final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p"); //$NON-NLS-1$

        final ListBox<String> listBox = new ListBox<String>();

        final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();

        model.add(Pat.CONSTANTS.glass());
        model.add(Pat.CONSTANTS.normal());
        model.add(Pat.CONSTANTS.threed());

        listBox.addRowSelectionHandler(new RowSelectionHandler() {
            public void onRowSelection(final RowSelectionEvent event) {
                final int index = listBox.getSelectedIndex();
                if (index != -1) {
                    switch (listBox.getSelectedIndex()) {
                    case 0:
                        brStyle = BarStyle.GLASS;
                        break;
                    case 1:
                        brStyle = BarStyle.NORMAL;
                        break;
                    case 2:
                        brStyle = BarStyle.THREED;
                        break;
                    default:
                        throw new RuntimeException("Should not happen"); //$NON-NLS-1$
                    }
                }
            }
        });

        final PanelBuilder builder = new PanelBuilder(layout);

        builder.addSeparator(Pat.CONSTANTS.barChartOptions());

        builder.nextLine(2);

        builder.addLabel(Pat.CONSTANTS.barStyle());
        builder.nextColumn(2);
        builder.add(listBox);
        builder.nextLine(2);

        barOptionsPanel.add(builder.getPanel());
        return barOptionsPanel;
    }

    /**
     * 
     * Create the bar options panel.
     * 
     * @return A LayoutPanel.
     */
    private LayoutPanel lineOptionsPanel() {

        final LayoutPanel barOptionsPanel = new LayoutPanel();

        final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                        + "p, 3dlu, p, 3dlu, p, 3dlu, p"); //$NON-NLS-1$

        final ListBox<String> listBox = new ListBox<String>();

        final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();

        model.add(Pat.CONSTANTS.normal());
        model.add(Pat.CONSTANTS.hollow());
        model.add(Pat.CONSTANTS.star());

        listBox.addRowSelectionHandler(new RowSelectionHandler() {
            public void onRowSelection(final RowSelectionEvent event) {
                final int index = listBox.getSelectedIndex();
                if (index != -1) {
                    switch (listBox.getSelectedIndex()) {
                    case 0:
                        bDot = null;
                        break;
                    case 1:
                        bDot = new HollowDot();
                        break;
                    case 2:
                        bDot = new Star();
                        break;
                    default:
                        throw new RuntimeException("Should not happen"); //$NON-NLS-1$
                    }
                }
            }
        });

        final PanelBuilder builder = new PanelBuilder(layout);

        builder.addSeparator(Pat.CONSTANTS.lineChartOptions());

        builder.nextLine(2);

        builder.addLabel(Pat.CONSTANTS.dotStyle());
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
    public void setChartTitleTextBox(final String chartTitleTextBox) {
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
    public void setxAxisTextBox(final String xAxisTextBox) {
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
    public void setyAxisTextBox(final String yAxisTextBox) {
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
    public void setBgColorTextBox(final String bgColorTextBox) {
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
    public void setPos(final Position pos) {
        this.pos = pos;
    }

    private void createBasicWindowPanel() {

        basic = new WindowPanel(Pat.CONSTANTS.backgroundColor());
        basic.setSize("500px", "500px"); //$NON-NLS-1$ //$NON-NLS-2$
        basic.setAnimationEnabled(true);
        final ColorPicker colorPick = new ColorPicker();
        basic.setWidget(colorPick);

        basic.getHeader().add(Caption.IMAGES.window().createImage());

        basic.addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(final CloseEvent<PopupPanel> event) {
                basic = null;
            }
        });
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param brStyle
     *            the bs to set
     */
    public void setBs(final BarStyle brStyle) {
        this.brStyle = brStyle;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the bs
     */
    public BarStyle getBs() {
        return brStyle;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param brStyle
     *            the bs to set
     */
    public void setBd(final BaseDot bDot) {
        this.bDot = bDot;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the bs
     */
    public BaseDot getBd() {
        return bDot;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param barOptions
     *            the barOptions to set
     */
    public void setBarOptions(final Map<String, Object> optionsMap) {
        this.optionsMap = optionsMap;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the barOptions
     */
    public Map<String, Object> getOptionsMap() {
        optionsMap = new HashMap<String, Object>();
        optionsMap.put("barStyle", brStyle); //$NON-NLS-1$
        optionsMap.put("dotStyle", bDot); //$NON-NLS-1$

        if (yaxisColorTextBox.getText().length() > 0) {
            optionsMap.put("yaxisColor", yaxisColorTextBox.getText()); //$NON-NLS-1$
        }
        if (yAxisGridColorTextBox.getText().length() > 0) {
            optionsMap.put("yaxisGridColor", yAxisGridColorTextBox.getText()); //$NON-NLS-1$
        }
        if (yAxisMinTextBox.getText().length() > 0) {
            optionsMap.put("yaxisMin", yAxisMinTextBox.getText()); //$NON-NLS-1$
        }
        if (yAxisMaxTextBox.getText().length() > 0) {
            optionsMap.put("yaxisMax", yAxisMaxTextBox.getText()); //$NON-NLS-1$
        }
        if (xaxisColorTextBox.getText().length() > 0) {
            optionsMap.put("xaxisColor", xaxisColorTextBox.getText()); //$NON-NLS-1$
        }
        if (xaxisGridColorTextBox.getText().length() > 0) {
            optionsMap.put("xaxisGridColor", xaxisGridColorTextBox.getText()); //$NON-NLS-1$
        }
        if (xaxisMinTextBox.getText().length() > 0) {
            optionsMap.put("xaxisMin", xaxisMinTextBox.getText()); //$NON-NLS-1$
        }
        if (xaxisMaxTextBox.getText().length() > 0) {
            optionsMap.put("xaxisMax", xaxisMaxTextBox.getText()); //$NON-NLS-1$
        }
        return optionsMap;
    }

}
