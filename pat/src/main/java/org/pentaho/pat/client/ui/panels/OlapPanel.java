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
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.util.factory.ServiceFactory;

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

    private String panelName = null;

    private String cubeName = null;

    private String connectionId = null;

    private String queryId = null;

    public OlapPanel() {
        // Needs working out so it accounts for multiple cubes of the same name.
        super();
    }

    /**
     *TODO JAVADOC
     * 
     */
    public OlapPanel(final String cube, final String connection) {
        super();
        // Needs working out so it accounts for multiple cubes of the same name.
        panelName = cube;

        cubeName = cube;
        connectionId = connection;

    }

    public String getCube() {
        return cubeName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#getDescription()
     */
    @Override
    public String getDescription() {
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

    public String getQuery() {
        return queryId;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the queryId
     */
    public String getQueryId() {
        return queryId;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
        ServiceFactory.getQueryInstance().deleteQuery(Pat.getSessionID(), queryId, new AsyncCallback<Object>() {

            public void onFailure(final Throwable arg0) {

                MessageBox.alert(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().errorDeleteQuery());
            }

            public void onSuccess(final Object arg0) {

            }

        });

    }

    public void setCube(final String name) {
        cubeName = name;
    }

    public void setName(final String name) {
        panelName = name;
    }

    public void setQuery(final String name) {
        queryId = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
     */
    @Override
    protected Widget onInitialize() {
        final LayoutPanel baselayoutPanel = new LayoutPanel(new BorderLayout());
        ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), connectionId, cubeName,
                new AsyncCallback<String>() {

                    public void onFailure(final Throwable arg0) {

                        MessageBox.alert(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().errorCreateQuery());
                    }

                    public void onSuccess(final String query) {
                        queryId = query;
                        Pat.setCurrQuery(query);

                        final LayoutPanel centerPanel = new LayoutPanel();
                        final CaptionLayoutPanel westPanel = new CaptionLayoutPanel();
                        final ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES.toolCollapseLeft());
                        westPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);

                        collapseBtn3.addClickHandler(new ClickHandler() {
                            public void onClick(final ClickEvent event) {
                                baselayoutPanel.setCollapsed(westPanel, !baselayoutPanel.isCollapsed(westPanel));
                                baselayoutPanel.layout();
                            }
                        });

                        final MainMenuPanel mainMenuPanel = new MainMenuPanel();
                        westPanel.add(mainMenuPanel);

                        final DataPanel dPanel = new DataPanel();
                        centerPanel.add(dPanel);

                        baselayoutPanel.add(westPanel, new BorderLayoutData(Region.WEST, 0.2, 10, 200, true));
                        baselayoutPanel.setCollapsed(westPanel, false);

                        baselayoutPanel.add(centerPanel, new BorderLayoutData(Region.CENTER, true));

                        // final Button bt = new Button("Test Dim Browser");
                        // bt.addClickHandler(new ClickHandler() {
                        //
                        // public void onClick(final ClickEvent arg0) {
                        // // TODO Test Code!
                        // // DimensionBrowserWindow.displayDimension(queryId, "Time");
                        //
                        // }
                        // });
                        //
                        // centerPanel.add(bt);
                        // layoutPanel.layout();
                    }

                });

        return baselayoutPanel;

    }
}
