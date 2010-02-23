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
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.util.PanelUtil;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a DataPanel which holds the olap table and related widgets.
 *
 * @created Aug 12, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 *
 */
public class DataPanel extends LayoutComposite implements IQueryListener {

    private final ChartPanel ofcPanel = new ChartPanel();

    private final OlapTable olapTable;

    private final LayoutPanel fillLayoutPanel = new LayoutPanel(new BorderLayout());

    private final LayoutPanel baseLayoutPanel = getLayoutPanel();

    private final LayoutPanel mainLayoutPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

    private final String queryId;
    
    PanelUtil.PanelType dPaneltype = null;

    /**
     *DataPanel Constructor.
     *
     * @param query
     *
     */
    public DataPanel(final String query, final PanelUtil.PanelType pType) {
        super();
        this.queryId = query;
        this.dPaneltype = pType;

        olapTable = new OlapTable();
        fillLayoutPanel.add(olapTable, new BorderLayoutData(Region.CENTER));
        // FIXME remove that and use style
        DOM.setStyleAttribute(baseLayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        DOM.setStyleAttribute(mainLayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        if (pType == PanelUtil.PanelType.QM) {
        mainLayoutPanel.setPadding(0);

//        GlobalConnectionFactory.getQueryInstance().addQueryListener(DataPanel.this);
        
        final Button executeButton = new Button(ConstantFactory.getInstance().executeQuery());
        executeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                  Pat.executeQuery(DataPanel.this, Pat.getCurrQuery());
            }

        });

        final DimensionDropWidget dimDropCol = new DimensionDropWidget(ConstantFactory.getInstance().columns(),
                IAxis.COLUMNS, true, Application.tblRowDrgCont);
        final DimensionDropWidget dimDropRow = new DimensionDropWidget(ConstantFactory.getInstance().rows(),
                IAxis.ROWS, false, Application.tblRowDrgCont);
        // DimensionDropWidget dimDropFilter = new DimensionDropWidget(ConstantFactory.getInstance().filter(),
        // Axis.FILTER);
        
        final LayoutPanel buttonDropPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        buttonDropPanel.add(executeButton, new BoxLayoutData(FillStyle.HORIZONTAL));
        buttonDropPanel.add(dimDropRow, new BoxLayoutData(FillStyle.BOTH));

        
        //fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.WEST, 0.5, 0, 1000));
        mainLayoutPanel.add(buttonDropPanel, new BoxLayoutData(FillStyle.VERTICAL));
        mainLayoutPanel.add(dimDropCol, new BoxLayoutData(FillStyle.HORIZONTAL));
        
        
        // mainLayoutPanel.add(executeButton, new BorderLayoutData(Region.CENTER, true));

        baseLayoutPanel.add(mainLayoutPanel);
        }

        
    }

    @Override
    public void onLoad(){
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DataPanel.this);
    }
    
    @Override
    public void onUnload(){
        GlobalConnectionFactory.getQueryInstance().removeQueryListener(DataPanel.this);   
    }
    public void chartPosition(final Region chartPos) {

        ofcPanel.removeFromParent();
        if (chartPos == null) {

            ofcPanel.removeFromParent();
            if (!olapTable.isAttached()) {
                fillLayoutPanel.add(olapTable, new BorderLayoutData(Region.CENTER));
            }
        } else {
            switch (chartPos) {
            case WEST:
                fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.WEST, 0.5, 0, 1000));
                break;
            case EAST:
                fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.EAST, 0.5, 0, 1000));
                break;
            case NORTH:
                fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.NORTH, 0.5, 0, 1000));
                break;
            case SOUTH:
                fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.SOUTH, 0.5, 0, 1000));
                break;
            case CENTER:
                olapTable.removeFromParent();
                fillLayoutPanel.add(ofcPanel, new BorderLayoutData(Region.CENTER));
                break;
            default:
                break;
            }
            if (!olapTable.isAttached() && chartPos != Region.CENTER) {
                fillLayoutPanel.add(olapTable, new BorderLayoutData(Region.CENTER));
            }
        }

       
        WidgetHelper.invalidate(fillLayoutPanel);
        WidgetHelper.layout(fillLayoutPanel);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(final Widget sender, int sourceRow, final boolean isSourceRow, final IAxis sourceAxis, final IAxis targetAxis) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final String query, final CellDataSet matrix) {
        if (query.equals(queryId)) {
            if (mainLayoutPanel.isAttached()) {
                baseLayoutPanel.remove(mainLayoutPanel);
            }
            baseLayoutPanel.add(fillLayoutPanel);
            baseLayoutPanel.layout();
            if (Pat.getCurrQuery() != null && queryId == Pat.getCurrQuery() && this.isAttached()) {
                olapTable.setData(matrix);
                ofcPanel.onQueryExecuted(matrix);
            }
        }
    }
}

