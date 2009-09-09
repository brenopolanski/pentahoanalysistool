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
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.DimensionMenu;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

/**
 * Lists all Dimensions and Members and allows their including/excluding in a Query.
 * 
 * @created Aug 18, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class DimensionBrowserWindow extends WindowPanel {

    /** The Window Title. */
    private final static String TITLE = "Dimension Browser";

    private static DimensionMenu dimensionMenuPanel = new DimensionMenu();

    private final LayoutPanel windowContentpanel = new ScrollLayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

    private final static DimensionBrowserWindow dbw = new DimensionBrowserWindow();

    /**
     * Cube Browser Window Constructor.
     */
    public DimensionBrowserWindow() {
        super(TITLE);
        windowContentpanel.add(dimensionMenuPanel, new BoxLayoutData(FillStyle.BOTH));
        Button updateQueryButton = new Button("Update Query");
        
        updateQueryButton.addClickHandler( new ClickHandler(){

            public void onClick(ClickEvent arg0) {
               
               updateQuery(); 
            }

            
            
        });
        windowContentpanel.add(updateQueryButton);
        this.setWidget(windowContentpanel);

        this.layout();

    }

    private void updateQuery() {
        ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<CellDataSet>(){

            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                MessageBox.error(ConstantFactory.getInstance().error(), "Failed to execute query");
            }

            public void onSuccess(CellDataSet arg0) {
                // TODO Auto-generated method stub
                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(DimensionBrowserWindow.this, Pat.getCurrQuery(), arg0);

                DimensionBrowserWindow.this.hide();
            }
            
        });        
    }
    
    public static void displayDimension(final String queryId, final String dimension) {
        dimensionMenuPanel.loadMembers(queryId, dimension);
        display();
    }

    // public static void displayAllDimensions(final String queryId) {
    // query = queryId;
    // display(true);
    // }

    private static void display() {
        dbw.setSize("450px", "300px");
        dbw.showModal(false);
        // dbw.show();
        dbw.layout();
    }

    
}
