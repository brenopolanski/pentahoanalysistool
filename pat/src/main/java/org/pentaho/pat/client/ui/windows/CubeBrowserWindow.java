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


import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.CubeMenu;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.panels.OlapPanel;
import org.pentaho.pat.client.ui.widgets.CubeTreeItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Lists all Connections and Cubes and allows creation of new queries.
 * @created Aug 7, 2009 
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class CubeBrowserWindow extends WindowPanel {

    /** The Window Title. */
    private static final String TITLE = "Cube Browser";

    private static final CubeMenu cubeMenuPanel = new CubeMenu();

    private static final LayoutPanel windowContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    
    private static final ToolButton qmQueryButton = new ToolButton("New Query");

    private static final CubeBrowserWindow cbw = new CubeBrowserWindow();
    /**
     * Cube Browser Window Constructor.
     */
    public CubeBrowserWindow() {
        super(TITLE);
        windowContentpanel.add(cubeMenuPanel, new BoxLayoutData(FillStyle.BOTH));
        LayoutPanel newQueryButtonPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        setupQueryButtons();
        newQueryButtonPanel.add(qmQueryButton);
        windowContentpanel.add(newQueryButtonPanel, new BoxLayoutData(FillStyle.VERTICAL));
        this.setWidget(windowContentpanel);
        this.layout();
        
    }
//    
//    @Override
//    public Dimension getPreferredSize() {
//      return new Dimension(384, 384);
//    }
    
    private void setupQueryButtons() {
        qmQueryButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent arg0) {
                if (cubeMenuPanel.getCubeTree().getSelectedItem() != null) {
                    final CubeTreeItem selected = (CubeTreeItem) cubeMenuPanel.getCubeTree().getSelectedItem().getWidget();
                    if (selected.getType() == CubeTreeItem.ItemType.CONNECTION) {
                        MessageBox.info("Selected Item", "Connection: " + selected.getConnectionId());
                    }
                    if (selected.getType() == CubeTreeItem.ItemType.CUBE) {
                        // MessageBox.info("Selected Item", "Connection: " + selected.getConnectionId() + " Cube: " + selected.getCube());
                        OlapPanel olappanel = new OlapPanel(selected.getCube(), selected.getConnectionId());
                        MainTabPanel.displayContentWidget(olappanel);   
                    }
                    cbw.hide();
                }
            }
        });
        qmQueryButton.setEnabled(false);
    }
    
    public static void display() {
        display(true);
    }

    public static void display(final String connectionId) {
        cubeMenuPanel.loadCubes(connectionId);
        display(false);
    }
    
    private static void display(boolean refreshCubes) {
        qmQueryButton.setEnabled(false);
        cbw.setSize("450px", "300px");
        if (refreshCubes) {
            cubeMenuPanel.loadCubes();
        }
        cbw.showModal(false);
        cbw.layout();
    }
    
    public static void enableQmQuery(boolean enabled) {
        qmQueryButton.setEnabled(true);
    }
    
    

}

