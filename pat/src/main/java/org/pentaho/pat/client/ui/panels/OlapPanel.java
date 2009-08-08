/*
 * Copyright (C) 2009 Tom Barber
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

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.deprecated.util.factory.ServiceFactory;
import org.pentaho.pat.client.ui.widgets.DataWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 *Creates a query tab panel for the selected cube and connection.
 * 
 * @created Aug 8, 2009
 * @since 0.5.0 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class OlapPanel extends DataWidget {

    private final String panelName;

    private final String cubeName;

    private final String connectionId;

    private String queryId;

    /**
     *TODO JAVADOC
     * 
     */
    public OlapPanel(final String cube, final String connection) {
        // Needs working out so it accounts for multiple cubes of the same name.
        panelName = cube;

        cubeName = cube;
        connectionId = connection;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#getDescription()
     */
    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#getName()
     */
    @Override
    public String getName() {
        return panelName;
    }

    @Override
    public void onLoad() {
        ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), connectionId, cubeName,
                new AsyncCallback<String>() {

                    public void onFailure(final Throwable arg0) {

                        MessageBox.alert("Warning", "Failed to create Query");
                    }

                    public void onSuccess(final String query) {
                        queryId = query;
                    }

                });
    }

    @Override
    public void onUnload() {
        ServiceFactory.getQueryInstance().deleteQuery(Pat.getSessionID(), queryId, new AsyncCallback() {

            public void onFailure(final Throwable arg0) {

                MessageBox.alert("Warning", "Unable to delete query");
            }

            public void onSuccess(final Object arg0) {

            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
     */
    @Override
    protected Widget onInitialize() {
        final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
        final CaptionLayoutPanel westPanel = new CaptionLayoutPanel();
        final ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES.toolCollapseLeft());
        westPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);
        final MainMenuPanel mainMenuPanel = new MainMenuPanel();
        westPanel.add(mainMenuPanel);

        collapseBtn3.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                layoutPanel.setCollapsed(westPanel, !layoutPanel.isCollapsed(westPanel));
                layoutPanel.layout();
            }
        });

        layoutPanel.add(westPanel, new BorderLayoutData(Region.WEST, 0.2, true));
        layoutPanel.setCollapsed(westPanel, true);

        final LayoutPanel centerPanel = new LayoutPanel();

        layoutPanel.add(centerPanel, new BorderLayoutData(true));

        return layoutPanel;
    }
}
