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
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.windows.ConnectMondrianPanel;
import org.pentaho.pat.client.ui.panels.windows.ConnectXmlaPanel;
import org.pentaho.pat.client.ui.panels.windows.ConnectionManagerPanel;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

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
    private static final String TITLE = Pat.CONSTANTS.registerNewConnection();

    /** Mondrian Panel. */
    private static ConnectMondrianPanel connectMondrian;

    /** Xmla Panel. */
    private static ConnectXmlaPanel connectXmla;

    private final Button cmCancelButton = new Button(Pat.CONSTANTS.close());

    private final static LayoutPanel MAINCONTENTPANEL = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    private final static LayoutPanel WINCONTENTPANEL = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

    private final static ConnectionManagerWindow CONNMANGRWINDOW = new ConnectionManagerWindow();

    /** A Tab Layout Panel. */
    private final static TabLayoutPanel TABPANEL = new TabLayoutPanel();

    /**
     * Close the tabs.
     */
    public static void closeTabs() {
        closeTabs(true);
    }
    /**
     * Close the tabs, optional refresh.
     * @param refresh
     */
    public static void closeTabs(boolean refresh) {
        for (final int i = 0; i < TABPANEL.getWidgetCount();) {
            TABPANEL.remove(i);
        }
        WINCONTENTPANEL.remove(TABPANEL);
        if (refresh)
            refreshWindow();
    }

    /**
     * Display.
     */
    public static void display() {
        display(true);
    }
    
    public static void display(Boolean refresh) {
        if (refresh) {
            closeTabs();
        }
        else
            CONNMANGRWINDOW.showModal();
            
    }
    public static void close() {
        CONNMANGRWINDOW.hide();
    }

    /**
     * Display.
     * @param cc
     */
    public static void display(final CubeConnection cc) {
        if (TABPANEL.getWidgetCount() == 0) {
        
        if (cc.getConnectionType() == CubeConnection.ConnectionType.Mondrian) {
            connectMondrian = new ConnectMondrianPanel(cc);
            TABPANEL.add(connectMondrian, Pat.CONSTANTS.mondrian());
        }
        if (cc.getConnectionType() == CubeConnection.ConnectionType.XMLA) {
            connectXmla = new ConnectXmlaPanel(cc);
            TABPANEL.add(connectXmla, Pat.CONSTANTS.xmla());
            
        }
        TABPANEL.selectTab(0);
        WINCONTENTPANEL.add(TABPANEL, new BoxLayoutData(FillStyle.VERTICAL));        
        refreshWindow();
        }
    }

    /**
     * Show new connection.
     */
    public static void showNewConnection() {
        if (TABPANEL.getWidgetCount() == 0) {
            connectMondrian = new ConnectMondrianPanel();
            connectXmla = new ConnectXmlaPanel();
            TABPANEL.add(connectMondrian, Pat.CONSTANTS.mondrian());
            TABPANEL.add(connectXmla, Pat.CONSTANTS.xmla());
            TABPANEL.selectTab(0);
            WINCONTENTPANEL.add(TABPANEL, new BoxLayoutData(FillStyle.VERTICAL));
            refreshWindow();
        }
    }

    private static void refreshWindow() {
        CONNMANGRWINDOW.invalidate();
        ConnectionManagerPanel.refreshConnectionList();
        CONNMANGRWINDOW.showModal();
        CONNMANGRWINDOW.layout();
    }

    /**
     * Connection Window Constructor.
     */
    public ConnectionManagerWindow() {
        super(TITLE);

        cmCancelButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {

                ConnectionManagerWindow.this.hide();

            }
        });
        WINCONTENTPANEL.add(new ConnectionManagerPanel(), new BoxLayoutData(FillStyle.BOTH));
        MAINCONTENTPANEL.add(WINCONTENTPANEL, new BoxLayoutData(FillStyle.BOTH));
        MAINCONTENTPANEL.add(cmCancelButton);
        this.setWidget(MAINCONTENTPANEL);
        this.layout();

    }

}
