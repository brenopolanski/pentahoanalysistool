/*
 * Copyright (C) 2009 Paul Stoellberger
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
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.ConnectMondrianPanel;
import org.pentaho.pat.client.ui.panels.ConnectXmlaPanel;
import org.pentaho.pat.client.ui.panels.ConnectionManagerPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;

/**
 * Connection Manager Window
 * 
 * @created Aug 3, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class ConnectionManagerWindow extends WindowPanel {

    /** The Window Title. */
    private static final String TITLE = ConstantFactory.getInstance().registerNewConnection();

    /** Mondrian Panel. */
    private transient static ConnectMondrianPanel connectMondrian;

    /** Xmla Panel. */
    private transient static ConnectXmlaPanel connectXmla;

    private final static LayoutPanel windowContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    
    private final static ConnectionManagerWindow connectionManagerWindow = new ConnectionManagerWindow();

    /** A Tab Layout Panel. */
    private transient final static TabLayoutPanel tabPanel = new TabLayoutPanel();

    /**
     * Connection Window Constructor.
     */
    public ConnectionManagerWindow() {
        super(TITLE);
        
      
        windowContentpanel.add(new ConnectionManagerPanel(), new BoxLayoutData(FillStyle.BOTH));
//        connectMondrian = new ConnectMondrianPanel();
//        connectXmla = new ConnectXmlaPanel();
//        tabPanel.setPadding(5);
//        tabPanel.add(connectMondrian, ConstantFactory.getInstance().mondrian());
//        tabPanel.add(connectXmla, ConstantFactory.getInstance().xmla());
//        tabPanel.selectTab(0);
//        windowContentpanel.add(tabPanel, new BoxLayoutData(FillStyle.VERTICAL));
        // GlobalConnectionFactory.getInstance().addConnectionListener(ConnectionManagerWindow.this);
        this.setWidget(windowContentpanel);
        this.layout();
        
    }
    
    public static void closeTabs() {
        for(int i = 0; i < tabPanel.getWidgetCount();) {
            tabPanel.remove(i);
        }
        windowContentpanel.remove(tabPanel);
        refreshWindow();
    }

    public static void display() {
                refreshWindow();
            }

    private static void refreshWindow() {
//        
//        int preferredWidth = Window.getClientWidth();
//        preferredWidth = Math.max(preferredWidth / 3, 256);
//
//        connectionManagerWindow.setPixelSize(preferredWidth, 356);
//
//        if (connectionManagerWindow.getOffsetWidth() < preferredWidth)
//            connectionManagerWindow.setPixelSize(preferredWidth, 356);
        connectionManagerWindow.invalidate();
        ConnectionManagerPanel.refreshConnectionList();
        connectionManagerWindow.showModal();
        connectionManagerWindow.layout();
    }
    
    public static void display(final CubeConnection cc) {
        if (cc.getConnectionType() == CubeConnection.ConnectionType.Mondrian)
            // TODO uncomment when implemented
            // connectMondrian = new ConnectMondrianPanel(cc);
            tabPanel.selectTab(0);

        if (cc.getConnectionType() == CubeConnection.ConnectionType.XMLA)
            // TODO uncomment when implemented
            // connectXmla = new ConnectXmlaPanel(cc)
            tabPanel.selectTab(1);
        display();
    }

    public static void showNewConnection() {
        // TODO add when implemented
        // ConnectionManagerPanel.refresh();
        if (tabPanel.getWidgetCount() == 0) {
            connectMondrian = new ConnectMondrianPanel();
            connectXmla = new ConnectXmlaPanel();
            tabPanel.add(connectMondrian, ConstantFactory.getInstance().mondrian());
            tabPanel.add(connectXmla, ConstantFactory.getInstance().xmla());
            tabPanel.selectTab(0);
            windowContentpanel.add(tabPanel, new BoxLayoutData(FillStyle.VERTICAL));
            tabPanel.layout();
            refreshWindow();
        }
    }

}
