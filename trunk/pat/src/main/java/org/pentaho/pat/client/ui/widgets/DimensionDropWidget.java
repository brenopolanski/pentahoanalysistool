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
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.client.util.dnd.FlexTableRowDropController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.IAxis.Standard;

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
public class DimensionDropWidget extends LayoutComposite implements IQueryListener {

    private final Standard dimAxis;

    private final LayoutPanel baseLayoutPanel;

    private DimensionFlexTable dimensionTable;

    private CaptionLayoutPanel captLayoutPanel;

    private Boolean horizontal = false;

    private FlexTableRowDropController fTblRowDropCont;

    private final String query;

    private final Label spacerLabel = new Label(""); //$NON-NLS-1$

    private final FlexTableRowDragController tblRowDragCont;

    private final static String TABLE_CSS_SPACER = "spacer-label"; //$NON-NLS-1$

    private final static String TABLE_DRAG_WIDGET = "dragDimension"; //$NON-NLS-1$

    private final static String TABLE_DRAG_CELL = "dragDimensionCell"; //$NON-NLS-1$

    private final static String TABLE_DROP_ENDCELL = "dropEndCell"; //$NON-NLS-1$

    /**
     *TODO JAVADOC
     * 
     * @param tblRowDragCont
     * 
     * @param unused
     * @param string
     * 
     */
    public DimensionDropWidget(final String labelText, final Standard targetAxis,
            final FlexTableRowDragController tblRowDragCont) {
        super();
        this.tblRowDragCont = tblRowDragCont;
        this.dimAxis = targetAxis;
        query = Pat.getCurrQuery();
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

    }

    public DimensionDropWidget(final String labelText, final Standard targetAxis, final Boolean orientation,
            final FlexTableRowDragController tblRowDragCont) {
        super();
        horizontal = orientation;
        this.dimAxis = targetAxis;
        query = Pat.getCurrQuery();
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

        this.tblRowDragCont = tblRowDragCont;
    }

    private void clearDimensionTable() {
        dimensionTable.clear();
        dimensionTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        dimensionTable.setWidget(0, 0, spacerLabel);
        dimensionTable.getCellFormatter().addStyleName(0, 0, TABLE_CSS_SPACER);
    }

    /**
     *TODO JAVADOC
     * 
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

    /**
     * Initialization.
     * 
     * @param labelText
     *            the label text
     * @param targetAxis
     *            the target axis
     */
    public final void init(final String labelText, final IAxis targetAxis) {

        captLayoutPanel = new CaptionLayoutPanel(labelText);

        dimensionTable = new DimensionFlexTable(horizontal);
        dimensionTable.addStyleName("pat-dropTable"); //$NON-NLS-1$

        captLayoutPanel.add(dimensionTable);

        clearDimensionTable();
        populateDimensionTable(dimAxis);

    }

    @Override
    public void onLoad() {
        fTblRowDropCont = new FlexTableRowDropController(dimensionTable, dimAxis);
        DimensionDropWidget.this.tblRowDragCont.registerDropController(fTblRowDropCont);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onMemberMoved(com.google.gwt.user.client.ui.Widget)
     */
    public void onMemberMoved(final Widget sender) {
        populateDimensionTable(dimAxis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(final Widget sender) {
        // TODO Auto-generated method stub
        if (isAttached() && isVisible() && Pat.getCurrQuery().equals(query)) {
            populateDimensionTable(dimAxis);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String,
     * org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(final String queryId, final CellDataSet matrix) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnload() {
        tblRowDragCont.unregisterDropController(fTblRowDropCont);
    }

    private void populateDimensionTable(final IAxis targetAxis) {

        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                new AsyncCallback<String[]>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance()
                                .dimensionFetchFail());
                    }

                    public void onSuccess(final String[] arg0) {
                        clearDimensionTable();
                        for (int row = 0; row < arg0.length; row++) {
                            if(arg0[row].equals("Measures")){
                        	final MeasureFlexTable mft = new MeasureFlexTable();
                        	mft.addStyleName("pat-dropTable");
                        	mft.clear();
                        	//mft.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
                        	

                        	ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), Pat.getCurrQuery(), 
                        		"Measures", new AsyncCallback<StringTree>(){

					    public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						
					    }

					    public void onSuccess(StringTree arg0) {
						for(int i=0; i< arg0.getChildren().size();i++){
						final Label handle = new Label(arg0.getChildren().get(i).getValue());
						mft.setWidget(i, 0, handle);
						}
					    }
                        	    
                        	});
                        	mft.setHeight("100px");
                        	dimensionTable.setWidget(0, row, mft);
                            }
                            else{
                            final Label handle = new Label(arg0[row]);
                            handle.setStylePrimaryName(TABLE_DRAG_WIDGET);
                            tblRowDragCont.makeDraggable(handle);

                            if (horizontal) {
                                dimensionTable.setWidget(0, row, handle);
                                dimensionTable.getCellFormatter().setHorizontalAlignment(0, row,
                                        HasHorizontalAlignment.ALIGN_LEFT);
                                dimensionTable.getCellFormatter().setVerticalAlignment(0, row,
                                        HasVerticalAlignment.ALIGN_TOP);
                                dimensionTable.getCellFormatter().removeStyleName(0, row, TABLE_DROP_ENDCELL);
                                dimensionTable.getCellFormatter().setStylePrimaryName(0, row, TABLE_DRAG_CELL);
                                if (row == arg0.length - 1 && arg0.length - 1 > 0) {
                                    dimensionTable.getCellFormatter().addStyleName(0, row, TABLE_DROP_ENDCELL);
                                }
                            } else {
                                dimensionTable.setWidget(row, 0, handle);
                                dimensionTable.getCellFormatter().setVerticalAlignment(row, 0,
                                        HasVerticalAlignment.ALIGN_TOP);
                                dimensionTable.getCellFormatter().setStylePrimaryName(row, 0, TABLE_DRAG_CELL);
                            }

                            
                        }
                        }
                        refreshTable();
                    }
                });
    }

    private void refreshTable() {

        WidgetHelper.revalidate(dimensionTable);

    }

}
