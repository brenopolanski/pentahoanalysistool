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


import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.DimensionMenu;

import com.google.gwt.user.client.ui.Label;

/**
 * Lists all Dimensions and Members and allows their including/excluding in a Query.
 * @created Aug 18, 2009 
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class DimensionBrowserWindow extends WindowPanel {

    /** The Window Title. */
    private static final String TITLE = "Dimension Browser";

    private static final DimensionMenu dimensionMenuPanel = new DimensionMenu();

    private static final LayoutPanel windowContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    
    private static final DimensionBrowserWindow dbw = new DimensionBrowserWindow();
    
    private static String query;
    
    private static String dimension;
    /**
     * Cube Browser Window Constructor.
     */
    public DimensionBrowserWindow() {
        super(TITLE);
        Label test = new Label("argh!");
        windowContentpanel.add(test, new BoxLayoutData(FillStyle.BOTH));
        windowContentpanel.add(dimensionMenuPanel, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(windowContentpanel);
        
        this.layout();
        
    }
    
    // public static void displayAllDimensions(final String queryId) {
    //    query = queryId;
    //    display(true);
    //}

    public void displayDimension(final String queryId, final String dimension) {
        query = queryId;
        DimensionBrowserWindow.dimension = dimension;
        dimensionMenuPanel.loadMembers(queryId, dimension);
        display();
    }
    
    private static void display() {
        dbw.setSize("450px", "300px");
        dbw.showModal(false);
        //dbw.show();
        dbw.layout();
    }
    

}

