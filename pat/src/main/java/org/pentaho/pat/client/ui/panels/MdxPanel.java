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
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.AbstractDataWidget;
import org.pentaho.pat.client.ui.widgets.MDXRichTextArea;
import org.pentaho.pat.client.util.PanelUtil;
import org.pentaho.pat.client.util.PanelUtil.PanelType;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.QuerySaveModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *Creates a mdx query tab panel for the selected cube and connection.
 * 
 * @created Oct 21, 2009
 * @since 0.5.1
 * @author Paul Stoellberger
 * 
 */
public class MdxPanel extends AbstractDataWidget implements IQueryListener {

    private String panelName = null;

    private String cube = null;

    private CubeItem cubeItem = null;

    private String connectionId = null;
    
    private CubeConnection connection = null;

    private String queryId = null;

    private DataPanel olapTable = null;
    
    private LayoutPanel tableLayoutPanel = new LayoutPanel();

    private final MDXRichTextArea mdxArea = new MDXRichTextArea();

    /**
     * Mdx Panel Constructor.
     * 
     */
    public MdxPanel(final CubeItem cube, final CubeConnection connection) {
        this(cube,connection,null, null);

    }
    
    public MdxPanel(final String queryId, final QuerySaveModel qsm, final String mdx) {
        this(qsm.getCube(),qsm.getConnection(),mdx, queryId);
        Pat.setCurrCube(qsm.getCube());
        Pat.setCurrCubeName(qsm.getCubeName());
        Pat.setCurrConnectionId(qsm.getConnection().getId());
        Pat.setCurrConnection(qsm.getConnection());
        Pat.setCurrPanelType(PanelType.MDX);

    }
    
    public MdxPanel(final String name, final CubeItem cube, final CubeConnection connection, final String mdx) {
        this(cube, connection,mdx, null);
        panelName = name;
    }

    public MdxPanel(final CubeItem cube, final CubeConnection connection, final String mdx, final String _queryId) {
        super();
        // Needs working out so it accounts for multiple cubes of the same name.
        panelName = ConstantFactory.getInstance().mdx() + " : " + cube.getName(); //$NON-NLS-1$
        this.queryId = _queryId;
        this.cubeItem = cube;
        this.cube = cube.getName();
        this.connection = connection;
        this.connectionId = connection.getId();
        final String catalog = cube.getCatalog();
        Pat.setCurrCube(cube);
        Pat.setCurrCubeName(cube.getName());
        Pat.setCurrConnectionId(connection.getId());
        Pat.setCurrConnection(connection);
        Pat.setCurrPanelType(PanelType.MDX);

        if (queryId == null) {
            ServiceFactory.getQueryInstance().createNewMdxQuery(Pat.getSessionID(), connectionId, catalog,mdx,
                    new AsyncCallback<String>() {

                public void onFailure(final Throwable arg0) {

                    MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                            .failedCreateQuery(arg0.getLocalizedMessage()));
                    LogoPanel.spinWheel(false);

                }

                public void onSuccess(final String query) {
                    queryId = query;
                    Pat.setCurrQuery(query);
                    Pat.setCurrConnectionId(connectionId);
                    Pat.setCurrConnection(connection);
                    Pat.setCurrPanelType(PanelType.MDX);
                    initializeWidget();
                    LogoPanel.spinWheel(false);
                    if (mdx != null) {
                        ServiceFactory.getQueryInstance().setMdxQuery(Pat.getSessionID(), queryId, mdx,
                                new AsyncCallback<Object>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedQuery(arg0.getLocalizedMessage()));
                            }

                            public void onSuccess(final Object arg0) {
                                if (Pat.getApplicationState().isExecuteQuery()) {
                                    Pat.executeQuery(MdxPanel.this,getQueryId());
                                }
                            }
                        });


                    }

                }
            });
        }
        else {
            Pat.setCurrQuery(queryId);
            initializeWidget();
            if (Pat.getApplicationState().isExecuteQuery()) {
                if (mdx != null) {
                    executeMdx(mdx);
                }
            }
        }
        
        if (mdx != null) {
            this.mdxArea.setText(mdx);
        }

    }



    @Override
    protected void initializeWidget() {

        final LayoutPanel baselayoutPanel = new LayoutPanel(new BorderLayout());

        LogoPanel.spinWheel(true);
        // FIXME remove that and use style
        DOM.setStyleAttribute(baselayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        final LayoutPanel centerPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));        
        
        final LayoutPanel northPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        
        mdxArea.setWidth("100%"); //$NON-NLS-1$
        mdxArea.setHeight("150px"); //$NON-NLS-1$
        final ScrollPanel spMdx = new ScrollPanel(mdxArea);
        northPanel.add(spMdx, new BoxLayoutData(FillStyle.HORIZONTAL));

        final Button executeButton = new Button(ConstantFactory.getInstance().executeQuery());

        northPanel.add(executeButton);

        final CaptionLayoutPanel collapsePanel = new CaptionLayoutPanel(ConstantFactory.getInstance().mdx());

        final ImageButton collapseBtn = new ImageButton(Caption.IMAGES.toolCollapseUp());
        collapsePanel.getHeader().add(collapseBtn, CaptionRegion.RIGHT);

        ClickHandler collapseClick = new ClickHandler() {
            public void onClick(final ClickEvent event) {
                collapsePanel.setCollapsed(!collapsePanel.isCollapsed());
                if (collapsePanel.isCollapsed())
                    collapseBtn.setImage(Caption.IMAGES.toolCollapseDown().createImage());
                else
                    collapseBtn.setImage(Caption.IMAGES.toolCollapseUp().createImage());
                
                MdxPanel.this.layout();
                
            }
        };
        collapsePanel.getHeader().addClickHandler(collapseClick);
        collapsePanel.add(northPanel);
        
        centerPanel.add(collapsePanel, new BoxLayoutData(FillStyle.HORIZONTAL));
                
        olapTable = new DataPanel(queryId, PanelUtil.PanelType.MDX, null);
        final PropertiesPanel propPanel = new PropertiesPanel(olapTable, PanelUtil.PanelType.MDX);
        
        centerPanel.add(propPanel,new BoxLayoutData(FillStyle.HORIZONTAL));
        
        tableLayoutPanel.add(olapTable);
        centerPanel.add(tableLayoutPanel, new BoxLayoutData(FillStyle.BOTH));

        
        baselayoutPanel.add(centerPanel);
        
        

        executeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                
                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryStartsExecution(MdxPanel.this, queryId);
                mdxArea.formatMDX();
                executeMdx(mdxArea.getText());
            }

        });
        
        getLayoutPanel().add(baselayoutPanel);
        getLayoutPanel().layout();
        LogoPanel.spinWheel(false);

    }

    private void executeMdx(String mdx) {
        ServiceFactory.getQueryInstance().setMdxQuery(Pat.getSessionID(), queryId, mdx,
                new AsyncCallback<Object>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedQuery(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final Object arg0) {
                        ServiceFactory.getQueryInstance().executeMdxQuery(Pat.getSessionID(),
                                queryId, new AsyncCallback<CellDataSet>() {

                                    public void onFailure(final Throwable arg0) {
                                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                .getInstance().failedQuery(arg0.getLocalizedMessage()));
                                    }

                                    public void onSuccess(final CellDataSet result1) {
                                        GlobalConnectionFactory.getQueryInstance().getQueryListeners()
                                                .fireQueryExecuted(MdxPanel.this, queryId, result1);
                                    }

                                });
                    }

                });


    }
    
    public CubeItem getCubeItem() {
        return cubeItem;
    }

    public String getCube() {
        return cube;
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
        ServiceFactory.getQueryInstance().deleteMdxQuery(Pat.getSessionID(), queryId, new AsyncCallback<Object>() {

            public void onFailure(final Throwable arg0) {

                MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDeleteQuery(
                        arg0.getLocalizedMessage()));
                LogoPanel.spinWheel(false);
            }

            public void onSuccess(final Object arg0) {
                LogoPanel.spinWheel(false);
            }

        });
        GlobalConnectionFactory.getQueryInstance().removeQueryListener(MdxPanel.this);
    }

    public void setCube(final String name) {
        cube = name;
    }

    public void setName(final String name) {
        panelName = name;
    }

    public void setQuery(final String name) {
        queryId = name;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(final String connectionId) {
        this.connectionId = connectionId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
     */

    public void onQueryChange(final Widget sender, int sourceRow, final boolean isSourceRow, final IAxis sourceAxis, final IAxis targetAxis) {
        // TODO Auto-generated method stub

    }

    public void onQueryExecuted(final String queryId, final CellDataSet matrix) {
        if (this.queryId.equals(queryId) && this.isAttached()) {
            this.layout();
        }


    }

	public void onQueryPivoted(String queryId) {
		
	}

    public void onQueryStartExecution(String queryId) {
    }

    public void onQueryFailed(String queryId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public CubeConnection getCubeConnection() {
        return connection;
    }

    @Override
    public PanelType getPanelType() {
        return PanelType.MDX;
    }

}
