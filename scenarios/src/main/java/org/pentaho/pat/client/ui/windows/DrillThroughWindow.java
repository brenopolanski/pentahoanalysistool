/*
 * Copyright (C) 2010 Paul Stoellberger
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

import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.widgets.ResultSetTable;
import org.pentaho.pat.rpc.dto.TableDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Lists all Connections and Cubes and allows creation of new queries.
 * 
 * @created Aug 7, 2009
 * @since 0.7.0
 * @author Paul Stoellberger
 * 
 */
public class DrillThroughWindow extends WindowPanel {

    /** The Window Title. */
    private static final String TITLE = "Drill through window";

    private static ResultSetTable rTable = new ResultSetTable();
    
    private final LayoutPanel winContentpanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    private final ToolButton closeButton = new ToolButton("Close");

    private final static DrillThroughWindow DTW = new DrillThroughWindow();


    /**
     * Display.
     * @param connectionId
     */
    public static void display(final TableDataSet tableData) {
        DTW.setSize("750px", "500px"); //$NON-NLS-1$ //$NON-NLS-2$
        
        rTable.setData(tableData);
        DTW.showModal(false);
        DTW.layout();


    }

    /**
     * Cube Browser Window Constructor.
     */
    public DrillThroughWindow() {
        super(TITLE);
        winContentpanel.add(rTable, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(winContentpanel);
        this.layout();
        
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {

                DTW.hide();

            }
        });

    }

}
