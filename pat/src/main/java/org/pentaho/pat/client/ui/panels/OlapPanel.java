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
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.AbstractDataWidget;
import org.pentaho.pat.client.util.PanelUtil;
import org.pentaho.pat.client.util.PanelUtil.PanelType;
import org.pentaho.pat.client.util.factory.EventFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.QuerySaveModel;
import org.pentaho.pat.rpc.dto.query.IAxis;

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
public class OlapPanel extends AbstractDataWidget implements IQueryListener{

    private String panelName = null;

    private String cubeName = null;

    private CubeItem cubeItem = null;

    private String connectionId = null;
    
    private CubeConnection connection = null;

    private String queryId = null;

    private LayoutPanel baselayoutPanel;

    LayoutPanel centerPanel = new LayoutPanel(new BorderLayout());
    
    private final CaptionLayoutPanel westPanel = new CaptionLayoutPanel();
    
    private  MainSouthPanel msPanel = null;
   
    public OlapPanel() {
        // Needs working out so it accounts for multiple cubes of the same name.
        super();
    }

    @Override
    public void onLoad(){
        EventFactory.getQueryInstance().addQueryListener(OlapPanel.this);
    }
    /**
     * OLAP Panel Constructor.
     *
     */
    public OlapPanel(final CubeItem cube, final CubeConnection cubeConnection) {
        super();
        // Needs working out so it accounts for multiple cubes of the same name.
        panelName = cube.getName();
        
        cubeItem = cube;
        cubeName = cube.getName();
        connectionId = cubeConnection.getId();
        connection = cubeConnection;
        ServiceFactory.getQueryInstance().createNewQuery(Pat.getSessionID(), connectionId, cubeName,
                new AsyncCallback<String>() {

                    public void onFailure(final Throwable arg0) {

                        MessageBox.alert(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                .failedCreateQuery(arg0.getLocalizedMessage()));
                        LogoPanel.spinWheel(false);
                    }

                    public void onSuccess(final String query) {
                        queryId = query;
                        Pat.setCurrQuery(query);
                        Pat.setCurrConnectionId(connectionId);
                        Pat.setCurrCubeName(cubeName);
                        Pat.setCurrCube(cubeItem);
                        Pat.setCurrConnection(connection);
                        Pat.setCurrPanelType(PanelType.QM);
                        initializeWidget();
                        if (Pat.getApplicationState().isExecuteQuery()) {
                            Pat.executeQuery(OlapPanel.this,getQueryId());
                        }

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
        Pat.setCurrConnectionId(qsm.getConnection().getId());
        Pat.setCurrConnection(qsm.getConnection());
        Pat.setCurrPanelType(PanelType.QM);

        panelName = qsm.getName();
        cubeItem = qsm.getCube();
        connectionId = qsm.getConnection().getId();
        connection = qsm.getConnection();
        initializeWidget();
        if (Pat.getApplicationState().isExecuteQuery()) {
            Pat.executeQuery(OlapPanel.this,getQueryId());
        }


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

                MessageBox.alert(Pat.CONSTANTS.error(), MessageFactory.getInstance().failedDeleteQuery(
                        arg0.getLocalizedMessage()));
                LogoPanel.spinWheel(false);
            }

            public void onSuccess(final Object arg0) {
                LogoPanel.spinWheel(false);
            }


        });
        EventFactory.getQueryInstance().removeQueryListener(OlapPanel.this);
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


        
        final ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES.toolCollapseLeft());
        westPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);

        collapseBtn3.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                baselayoutPanel.setCollapsed(westPanel, !baselayoutPanel.isCollapsed(westPanel));
                baselayoutPanel.layout();
            }
        });

        
        final DataPanel dPanel = new DataPanel(queryId, PanelUtil.PanelType.QM, this);
        final PropertiesPanel pPanel = new PropertiesPanel(dPanel, PanelUtil.PanelType.QM);
        
        // FIXME remove that and use style 
        DOM.setStyleAttribute(pPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        

        msPanel = new MainSouthPanel(centerPanel);
        if (Pat.getApplicationState().isExecuteQuery()) {
            westPanel.setVisible(false);
            msPanel.setVisible(false);
        }
        centerPanel.clear();
        centerPanel.add(pPanel, new BorderLayoutData(Region.NORTH,true));
        centerPanel.add(dPanel,new BorderLayoutData(Region.CENTER,true));
        centerPanel.add(msPanel,new BorderLayoutData(Region.SOUTH,0.3, 20, 300, true));
        
        msPanel.setVisible(false);
        
        final MainMenuPanel mainMenuPanel = new MainMenuPanel(dPanel);
        westPanel.add(mainMenuPanel);

        baselayoutPanel.add(westPanel, new BorderLayoutData(Region.WEST, 0.2, 10, 200, true));
        if (Pat.getApplicationState().isExecuteQuery() && Pat.getApplicationState().getLoadQueryName() != null) {
            baselayoutPanel.setCollapsed(westPanel, true); 
        }
        else {
            baselayoutPanel.setCollapsed(westPanel, false);
        }
        

        baselayoutPanel.add(centerPanel, new BorderLayoutData(Region.CENTER, true));
        
        getLayoutPanel().add(baselayoutPanel);
        getLayoutPanel().layout();
        LogoPanel.spinWheel(false);
    }

    public void onQueryChange(final Widget sender, int sourceRow, final boolean isSourceRow, final IAxis sourceAxis, final IAxis targetAxis) {
        // TODO Auto-generated method stub
       
    }

    public void onQueryExecuted(String _queryId, CellDataSet matrix) {
        if (queryId != null && queryId.equals(_queryId) && this.isAttached()) {
            westPanel.setVisible(false);
            msPanel.setVisible(false);
            baselayoutPanel.invalidate();
            baselayoutPanel.layout();

        }
    }


    public void setWestPanelVisible(boolean istrue){
    	westPanel.setVisible(istrue);
    	baselayoutPanel.invalidate();
    	baselayoutPanel.layout();
    }

    public void onQueryFailed(String _queryId) {
        if (queryId != null && queryId.equals(_queryId) && this.isAttached()) {
            westPanel.setVisible(true);
            msPanel.setVisible(false);
            baselayoutPanel.invalidate();
            baselayoutPanel.layout();

        }

        
    }

    @Override
    public CubeConnection getCubeConnection() {
        return connection;
    }

    @Override
    public PanelType getPanelType() {
        return PanelType.QM;
    }

    public void onQueryStartExecution(String _queryId) {
        if (queryId != null && queryId.equals(_queryId) && this.isAttached()) {
            westPanel.setVisible(false);
            msPanel.setVisible(false);
            baselayoutPanel.invalidate();
            baselayoutPanel.layout();

        }
        
    }

}

