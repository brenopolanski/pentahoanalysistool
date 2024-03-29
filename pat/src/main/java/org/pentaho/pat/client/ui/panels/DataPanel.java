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
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.DimensionSimplePanel;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.ui.widgets.QueryDesignTable;
import org.pentaho.pat.client.ui.widgets.QueryTrashWidget;
import org.pentaho.pat.client.util.PanelUtil;
import org.pentaho.pat.client.util.factory.EventFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.query.IAxis;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
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
    
    private final Label throbberLabel = new Label();
    
    PanelUtil.PanelType dPaneltype = null;

	private OlapPanel olapPanel;

	/**
     *DataPanel Constructor.
     *
     * @param query
     *
     */
	public DataPanel(final String query, final PanelUtil.PanelType pType, OlapPanel parent) {
	    super();
	    this.olapPanel = parent;
	    this.queryId = query;
	    this.dPaneltype = pType;
	    QueryDesignTable dropTable = new QueryDesignTable(this.queryId, false);
	    QueryDesignTable filterTable = new QueryDesignTable(this.queryId, true);
	    olapTable = new OlapTable();
	    fillLayoutPanel.add(olapTable, new BorderLayoutData(Region.CENTER));
	    // FIXME remove that and use style
	    DOM.setStyleAttribute(baseLayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
	    DOM.setStyleAttribute(mainLayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

	    if (Pat.getApplicationState().isExecuteQuery()) {
            onQueryStartExecution(queryId);
        }
	    
	    if (pType == PanelUtil.PanelType.QM) {
	        mainLayoutPanel.setPadding(0);


	        dropTable.setWidget(0, 1, new DimensionSimplePanel(false, IAxis.COLUMNS));
	        dropTable.setWidget(1, 0, new DimensionSimplePanel(false, IAxis.ROWS));

	        //dropTable.setSize("100%", "100%");

	        filterTable.setWidget(0, 0, new DimensionSimplePanel(false, IAxis.FILTER));
	        QueryTrashWidget trashPanel = new QueryTrashWidget();
	        LayoutPanel subMainLayoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
	        subMainLayoutPanel.add(dropTable, new BoxLayoutData(FillStyle.BOTH));
	        subMainLayoutPanel.add(filterTable, new BoxLayoutData(FillStyle.HORIZONTAL));
	        filterTable.setWidth("100%");
	        mainLayoutPanel.add(subMainLayoutPanel, new BoxLayoutData(FillStyle.BOTH));
	        mainLayoutPanel.add(trashPanel, new BoxLayoutData(100.0, 100.0));
	        ((BoxLayout) mainLayoutPanel.getLayout()).setAlignment(Alignment.END);
	        if (!Pat.getApplicationState().isExecuteQuery()) {
	            baseLayoutPanel.add(mainLayoutPanel);
	        }
	    }


	}

    @Override
    public void onLoad(){
        EventFactory.getQueryInstance().addQueryListener(DataPanel.this);
    }
    
    @Override
    public void onUnload(){
        EventFactory.getQueryInstance().removeQueryListener(DataPanel.this);   
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

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final String query, final CellDataSet matrix) {

        if (query.equals(queryId) && this.isAttached()) {
            if (throbberLabel.isAttached()) {
                baseLayoutPanel.remove(throbberLabel);
            }
            if (mainLayoutPanel.isAttached()) {
                baseLayoutPanel.remove(mainLayoutPanel);
            }
            baseLayoutPanel.setLayout(new BorderLayout());
            baseLayoutPanel.add(fillLayoutPanel);
            baseLayoutPanel.layout();
            if (Pat.getCurrQuery() != null && queryId == Pat.getCurrQuery() && this.isAttached()) {
                olapTable.setData(matrix);
                ofcPanel.onQueryExecuted(matrix);
            }
        }
    	
    }

    public void swapWindows(){
    	if (mainLayoutPanel.isAttached()) {
            baseLayoutPanel.remove(mainLayoutPanel);
            baseLayoutPanel.setLayout(new BorderLayout());
            baseLayoutPanel.add(fillLayoutPanel);
            baseLayoutPanel.invalidate();
            baseLayoutPanel.layout();
        }
    	else if (fillLayoutPanel.isAttached()){
    		baseLayoutPanel.remove(fillLayoutPanel);
            baseLayoutPanel.setLayout(new FillLayout());
            baseLayoutPanel.add(mainLayoutPanel);
            olapPanel.setWestPanelVisible(true);
            baseLayoutPanel.invalidate();
            baseLayoutPanel.layout();
            
            mainLayoutPanel.invalidate();
            mainLayoutPanel.layout();
            
    	}
        
    }

    public void onQueryStartExecution(String queryId) {
        if (queryId.equals(this.queryId)) {
            if (mainLayoutPanel.isAttached()) {
                baseLayoutPanel.remove(mainLayoutPanel);
            }
            if (fillLayoutPanel.isAttached()) {
                baseLayoutPanel.remove(fillLayoutPanel);
            }

            if (!throbberLabel.isAttached()) {
                throbberLabel.setStyleName("Throbber-loading"); //$NON-NLS-1$
                throbberLabel.addStyleName("throbber"); //$NON-NLS-1$
                throbberLabel.setSize("100px", "100px"); //$NON-NLS-1$ //$NON-NLS-2$
                throbberLabel.setPixelSize(100, 100);
                
                baseLayoutPanel.setLayout(new FillLayout());
                baseLayoutPanel.add(throbberLabel, new FillLayoutData(HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE));
            }
            baseLayoutPanel.layout();
        }
    }

    public void onQueryFailed(String queryId) {
        if (this.queryId.equals(queryId) && this.isAttached()) {
            if (throbberLabel.isAttached()) {
                baseLayoutPanel.remove(throbberLabel);
            }
            if (mainLayoutPanel.isAttached()) {
                baseLayoutPanel.remove(fillLayoutPanel);
            }
            baseLayoutPanel.setLayout(new BorderLayout());
            baseLayoutPanel.add(mainLayoutPanel);
            baseLayoutPanel.layout();
            
        }
        
    }
}

