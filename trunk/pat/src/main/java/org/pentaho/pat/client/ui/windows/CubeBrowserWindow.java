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
import org.pentaho.pat.client.ui.panels.CubeMenu;

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

    private final static LayoutPanel windowContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    

    /**
     * Cube Browser Window Constructor.
     */
    public CubeBrowserWindow() {
        super(TITLE);
        windowContentpanel.add(cubeMenuPanel, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(windowContentpanel);
        this.layout();
        
    }
//    
//    @Override
//    public Dimension getPreferredSize() {
//      return new Dimension(384, 384);
//    }
    
    public void display() {
        display(true);
    }

    public void display(final String connectionId) {
        cubeMenuPanel.loadCubes(connectionId);
        display(false);
    }
    
    private void display(boolean refreshCubes) {
        this.setSize("300px", "300px");
        if (refreshCubes) {
            cubeMenuPanel.loadCubes();
        }
        this.showModal(false);
        this.layout();
    }
    
    

}

