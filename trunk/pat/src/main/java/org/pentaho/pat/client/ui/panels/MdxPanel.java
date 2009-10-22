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
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.DataWidget;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 *Creates a mdx query tab panel for the selected cube and connection.
 * 
 * @created Oct 21, 2009
 * @since 0.5.1
 * @author Paul Stoellberger
 * 
 */
public class MdxPanel extends DataWidget implements IQueryListener {

    private String panelName = null;

    private String catalog = null;

    private String cube = null;

    private String connectionId = null;

    private String queryId = null;

    private OlapTable olapTable = null;

    private TextArea mdxArea = null;

    private LayoutPanel baselayoutPanel;

    private Button executeButton;

    public MdxPanel() {
        // Needs working out so it accounts for multiple cubes of the same name.
        super();
     
       
    }

    /**
     * OLAP Panel Constructor.
     * 
     */
    public MdxPanel(final CubeItem cube, final String connection) {
        super();
        // Needs working out so it accounts for multiple cubes of the same name.
        panelName = ConstantFactory.getInstance().mdx()+ " : " + cube.getName() ; 

        GlobalConnectionFactory.getQueryInstance().addQueryListener(MdxPanel.this);

        this.cube = cube.getName();
        this.connectionId = connection;
        this.catalog = cube.getCatalog();

        ServiceFactory.getQueryInstance().createNewMdxQuery(Pat.getSessionID(), connectionId, catalog,
                new AsyncCallback<String>() {

            public void onFailure(final Throwable arg0) {

                MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedCreateQuery(arg0.getLocalizedMessage()));
                LogoPanel.spinWheel(false);

            }

            public void onSuccess(final String query) {
                queryId = query;
                Pat.setCurrQuery(query);
                Pat.setCurrConnection(connectionId);
                
                initializeWidget();
                LogoPanel.spinWheel(false);
     
            }
        });

    }

    protected void initializeWidget() {

        baselayoutPanel = new LayoutPanel();


        LogoPanel.spinWheel(true);
        // FIXME remove that and use style
        DOM.setStyleAttribute(baselayoutPanel.getElement(),"background", "white");



        final LayoutPanel centerPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        mdxArea = new TextArea();
        mdxArea.setWidth("100%");
        mdxArea.setHeight("150px");
        ScrollPanel spMdx = new ScrollPanel(mdxArea);
        centerPanel.add(spMdx, new BoxLayoutData(FillStyle.HORIZONTAL));

        executeButton = new Button(ConstantFactory.getInstance().executeQuery());

        centerPanel.add(executeButton);

        olapTable = new OlapTable();
        centerPanel.add(olapTable,new BoxLayoutData(FillStyle.BOTH));


        baselayoutPanel.add(centerPanel);
        
        getLayoutPanel().add(baselayoutPanel);
        LogoPanel.spinWheel(false);



        executeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().setMdxQuery(Pat.getSessionID(), queryId,mdxArea.getText(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                .failedQuery(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(Object arg0) {
                        ServiceFactory.getQueryInstance().executeMdxQuery(Pat.getSessionID(), Pat.getCurrQuery(),
                                new AsyncCallback<CellDataSet>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedQuery(arg0.getLocalizedMessage()));
                            }

                            public void onSuccess(final CellDataSet result1) {
                                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                        MdxPanel.this, Pat.getCurrQuery(), result1);
                            }

                        });
                    }

                });


            }

        });

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

                MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDeleteQuery(arg0.getLocalizedMessage()));
                LogoPanel.spinWheel(false);
            }

            public void onSuccess(final Object arg0) {
                LogoPanel.spinWheel(false);
            }

        });

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

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
     */


    public void onQueryChange(Widget sender) {
        // TODO Auto-generated method stub

    }

    public void onQueryExecuted(String queryId, CellDataSet matrix) {
        if (this.queryId.equals(queryId))
        {
            
            if (Pat.getCurrQuery() != null && queryId == Pat.getCurrQuery() && this.isAttached()) {
                olapTable.setData(matrix);
            }
        }

    }
    
    @Override
    protected void onLoad() {
        if (queryId != null) {
            Pat.setCurrQuery(queryId);
            Pat.setCurrConnection(connectionId);
        }
        
    }


}
