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

import org.gwt.mosaic.core.client.DOM;
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
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.QuerySaveModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *Creates a query tab panel for the selected cube and connection.
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class OlapPanel extends DataWidget {

    private transient String panelName = null;

    private transient String cubeName = null;

    private transient CubeItem cubeItem = null;

    private transient String connectionId = null;

    private transient String queryId = null;

    private transient LayoutPanel baselayoutPanel;

    public OlapPanel() {
        // Needs working out so it accounts for multiple cubes of the same name.
        super();
    }

    /**
     * OLAP Panel Constructor.
     * 
     */
    public OlapPanel(final CubeItem cube, final String connection) {
        super();
        // Needs working out so it accounts for multiple cubes of the same name.
        panelName = cube.getName();

        cubeItem = cube;
        cubeName = cube.getName();
        connectionId = connection;

        ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), connectionId, cubeName,
                new AsyncCallback<String>() {

                    public void onFailure(final Throwable arg0) {

                        MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedCreateQuery(arg0.getLocalizedMessage()));
                        LogoPanel.spinWheel(false);
                    }

                    public void onSuccess(final String query) {
                        queryId = query;
                        Pat.setCurrQuery(query);
                        Pat.setCurrConnection(connectionId);
                        Pat.setCurrCubeName(cubeName);
                        Pat.setCurrCube(cubeItem);
                        initializeWidget();
                    }
                });

    }

    /**
     * 
     * OlapPanel constructor when loading a saved query model.
     * 
     * @param query
     * @param qsm
     */
    public OlapPanel(final String query, final QuerySaveModel qsm) {
        super();
        queryId = query;
        Pat.setCurrQuery(query);
        Pat.setCurrCube(qsm.getCube());
        Pat.setCurrCubeName(qsm.getCubeName());
        Pat.setCurrConnection(qsm.getConnection());

        panelName = qsm.getCubeName();
        cubeItem = qsm.getCube();
        connectionId = qsm.getConnection();
        initializeWidget();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#nitializeWidgets()
     */

    public String getConnectionId() {
        return connectionId;
    }

    public String getCube() {
        return cubeName;
    }

    public CubeItem getCubeItem() {
        return cubeItem;
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
    public void onUnload() {
        LogoPanel.spinWheel(true);
        ServiceFactory.getQueryInstance().deleteQuery(Pat.getSessionID(), queryId, new AsyncCallback<Object>() {

            public void onFailure(final Throwable arg0) {

                MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDeleteQuery(
                        arg0.getLocalizedMessage()));
                LogoPanel.spinWheel(false);
            }

            public void onSuccess(final Object arg0) {
                LogoPanel.spinWheel(false);
            }

        });

    }

    public void setConnectionId(final String connectionId) {
        this.connectionId = connectionId;
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

    @Override
    protected void initializeWidget() {
        LogoPanel.spinWheel(true);
        baselayoutPanel = new LayoutPanel(new BorderLayout());
        // FIXME remove that and use style
        DOM.setStyleAttribute(baselayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

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

        final DataPanel dPanel = new DataPanel(queryId);
        centerPanel.add(dPanel);

        final MainMenuPanel mainMenuPanel = new MainMenuPanel(dPanel);
        westPanel.add(mainMenuPanel);

        baselayoutPanel.add(westPanel, new BorderLayoutData(Region.WEST, 0.2, 10, 200, true));
        baselayoutPanel.setCollapsed(westPanel, false);

        baselayoutPanel.add(centerPanel, new BorderLayoutData(Region.CENTER, true));

        getLayoutPanel().add(baselayoutPanel);
        LogoPanel.spinWheel(false);
    }

}
