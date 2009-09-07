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
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.Axis.Standard;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionDropWidget extends LayoutComposite implements QueryListener{

    private Standard dimAxis;

    private LayoutPanel baseLayoutPanel;

    private DimensionFlexTable dimensionTable;

    private CaptionLayoutPanel captionLayoutPanel;

    private Boolean horizontal = false;

    private FlexTableRowDropController flexTableRowDropController1;
    
    private String query = Pat.getCurrQuery();

    private FlexTableRowDragController trdc = null;

    private final Label spacerLabel = new Label(""); //$NON-NLS-1$

    private final static String TABLE_CSS_SPACER = "spacer-label"; //$NON-NLS-1$
    
    private final static String TABLE_DRAG_WIDGET = "dragDimension";  //$NON-NLS-1$
    
    private final static String TABLE_DRAG_CELL = "dragDimensionCell";  //$NON-NLS-1$
    
    private final static String TABLE_DROP_ENDCELL = "dropEndCell";  //$NON-NLS-1$
    /**
     *TODO JAVADOC
     * 
     * @param unused
     * @param string
     *  
     */
    public DimensionDropWidget(final String labelText, final Standard targetAxis) {
        this.dimAxis = targetAxis;
        this.trdc = DimensionPanel.getTableRowDragController();
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captionLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

    }

    public DimensionDropWidget(final String labelText, final Standard targetAxis, final Boolean orientation) {
        super();
        horizontal = orientation;
        this.dimAxis = targetAxis;
        this.trdc = DimensionPanel.getTableRowDragController();
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captionLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

    }

    /**
     * Initialization.
     * 
     * @param labelText
     *            the label text
     * @param targetAxis
     *            the target axis
     */
    public final void init(final String labelText, final Axis targetAxis) {

        captionLayoutPanel = new CaptionLayoutPanel(labelText);

        dimensionTable = new DimensionFlexTable(DimensionPanel.getTableRowDragController(), horizontal);
        dimensionTable.addStyleName("pat-dropTable"); //$NON-NLS-1$

        captionLayoutPanel.add(dimensionTable);

        clearDimensionTable();
        populateDimensionTable(dimAxis);
        
        
    }

    @Override
    public void onLoad() {
        flexTableRowDropController1 = new FlexTableRowDropController(dimensionTable, dimAxis);
        DimensionPanel.getTableRowDragController().registerDropController(flexTableRowDropController1);

    }

    @Override
    public void onUnload() {
        DimensionPanel.getTableRowDragController().unregisterDropController(flexTableRowDropController1);
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onMemberMoved(com.google.gwt.user.client.ui.Widget)
     */
    public void onMemberMoved(Widget sender) {
        populateDimensionTable(dimAxis);
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(Widget sender) {
        // TODO Auto-generated method stub
        if(isAttached() && isVisible() && Pat.getCurrQuery().equals(query))
        populateDimensionTable(dimAxis);
   }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String, org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(String queryId, CellDataSet matrix) {
        // TODO Auto-generated method stub
        
    }

    /**
     *TODO JAVADOC
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

    public void populateDimensionTable(final Axis targetAxis) {

        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                new AsyncCallback<String[]>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().dimensionFetchFail());
                    }

                    public void onSuccess(final String[] arg0) {
                        clearDimensionTable();
                        for (int row = 0; row < arg0.length; row++) {
                            final Label handle = new Label(arg0[row]);
                            handle.setStylePrimaryName(TABLE_DRAG_WIDGET);
                            trdc.makeDraggable(handle);
                            
                            if (!horizontal) {
                                dimensionTable.setWidget(row, 0, handle);
                                dimensionTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
                                dimensionTable.getCellFormatter().setStylePrimaryName(row, 0, TABLE_DRAG_CELL);
                            } else {
                                dimensionTable.setWidget(0, row, handle);
                                dimensionTable.getCellFormatter().setHorizontalAlignment(0, row, HasHorizontalAlignment.ALIGN_LEFT);
                                dimensionTable.getCellFormatter().setVerticalAlignment(0, row, HasVerticalAlignment.ALIGN_TOP);
                                dimensionTable.getCellFormatter().removeStyleName(0, row, TABLE_DROP_ENDCELL);
                                dimensionTable.getCellFormatter().setStylePrimaryName(0, row, TABLE_DRAG_CELL);
                                if (row == arg0.length - 1 && arg0.length - 1 > 0)
                                    dimensionTable.getCellFormatter().addStyleName(0, row, TABLE_DROP_ENDCELL);
                            }
                            
                            refreshTable();    
                        }
                                   
                    }
                });
    }
    
    private void refreshTable(){
        

        WidgetHelper.revalidate(dimensionTable);
        
        }

    public void clearDimensionTable() {
        dimensionTable.clear();
        dimensionTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        dimensionTable.setWidget(0, 0, spacerLabel);
        dimensionTable.getCellFormatter().addStyleName(0, 0,TABLE_CSS_SPACER);
    }

}
