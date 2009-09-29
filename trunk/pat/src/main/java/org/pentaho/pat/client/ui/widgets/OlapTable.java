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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.LiveTable;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.windows.DimensionBrowserWindow;
import org.pentaho.pat.client.util.PatColDef;
import org.pentaho.pat.client.util.PatTableModel;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.IterableTableModel;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates the data table, using a hashmap matrix converted to an array and inserted into a GWT Mosaic Live Table, which
 * is a lazy loading scrolltable.
 * 
 * @author tom (at) wamonline.org.uk
 * 
 */
public class OlapTable extends LayoutComposite implements QueryListener {

    Image cross = Pat.IMAGES.cross().createImage();

    private CellDataSet olapData;

    private int offset;

    PatTableModel patTableModel;

    TableModel<BaseCell[]> tableModel;

    LiveTable<BaseCell[]> table;

    private static String CELLBUTTON = "cellButton"; //$NON-NLS-1$
    
    final LayoutPanel layoutPanel = getLayoutPanel();

    public OlapTable() {
        super();
        this.setSize("100%", "100%"); //$NON-NLS-1$//$NON-NLS-2$
        GlobalConnectionFactory.getQueryInstance().addQueryListener(OlapTable.this);
    }

    /**
     * 
     * Initialize the Live Table.
     * 
     */
    public void initTable() {
        // Not sure what the effect of this is, but at least something is happening now. Not just frozen
        layoutPanel.clear();
        patTableModel = new PatTableModel(olapData);
        final List<BaseCell[]> data = Arrays.asList(patTableModel.getRowData());
        offset = patTableModel.getOffset();
        // tableModel = null;
        tableModel = new IterableTableModel<BaseCell[]>(data) {
            @Override
            public int getRowCount() {
                return data.size();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void requestRows(final Request request, final Callback<BaseCell[]> callback) {
                final int numRows = Math.min(request.getNumRows(), data.size() - request.getStartRow());

                final List<BaseCell[]> list = new ArrayList<BaseCell[]>();
                for (int i = 0, n = numRows; i < n; i++)
                    list.add(data.get(request.getStartRow() + i));
                final SerializableResponse response = new SerializableResponse(list);
                callback.onRowsReady(request, response);
            }
        };

        table = new LiveTable<BaseCell[]>(tableModel, createTableDefinition());
        table.addDoubleClickHandler(new DoubleClickHandler() {
            public void onDoubleClick(final DoubleClickEvent event) {
                Window.alert(event.getSource().getClass().getName());
            }
        });

        layoutPanel.add(table);
        layoutPanel.layout();

    }

    /**
     * Fire on query changing.
     */
    public void onQueryChange(final Widget sender) {


    }

    /**
     * Fire when the query is executed.
     */
    public void onQueryExecuted(final String queryId, final CellDataSet olapData) {
        if (Pat.getApplicationState().getMode().isShowOnlyTable())
            setData(olapData);
    }

    /**
     * 
     * Setup the current table.
     * 
     * @param olapData
     */
    public void setData(final CellDataSet olapData) {
        this.olapData = olapData;
        initTable();
        // can't see any effect with that
        table.reload();
        table.redraw();
        this.layout();
    }
    
    /**
     * 
     * @param headers
     * @return cellPanel
     */
    private HorizontalPanel createCell(final BaseCell headers) {
        final HorizontalPanel cellPanel = new HorizontalPanel();

        if (headers.getRawValue() != null && headers instanceof MemberCell) {
            final Image cellButton = Pat.IMAGES.cross().createImage();
            cellButton.addClickHandler(new ClickHandler() {

                public void onClick(final ClickEvent arg0) {
                    DimensionBrowserWindow.displayDimension(Pat.getCurrQuery(), headers.getParentDimension());
                }

            });
            
            Image drillButton = null;
            if(((MemberCell)headers).getChildMemberCount()>0){
            drillButton = Pat.IMAGES.cube().createImage();
            drillButton.addClickHandler(new ClickHandler() {

                public void onClick(final ClickEvent arg0) {
                    ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(), (MemberCell)headers, new AsyncCallback<Object>(){

                        public void onFailure(Throwable arg0) {
                            MessageBox.alert("Failed", "failed");
                        }

                        public void onSuccess(Object arg0) {
                            ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<CellDataSet>(){

                            public void onFailure(Throwable arg0) {
                                
                                MessageBox.alert("Failed", "failed");    
                                
                            }

                            public void onSuccess(CellDataSet arg0) {
                                GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
                                        OlapTable.this, Pat.getCurrQuery(), arg0);                        
                                
                            }
                             
                         });
                        }
                        
                    });
                }

            });
            }
            final Label cellLabel = new Label(headers.formattedValue);
            cellPanel.add(cellLabel);
            
            cellPanel.setWidth("100%"); //$NON-NLS-1$
            
            cellButton.addStyleName(CELLBUTTON);
            
            if(!headers.getRawValue().equals("")){ //$NON-NLS-1$
            cellPanel.add(cellButton);
            if(drillButton!=null)
            cellPanel.add(drillButton);
            }
        }

        else {
            final Label blanklabel = new Label(headers.getFormattedValue());
            cellPanel.add(blanklabel);
        }
        return cellPanel;
    }

    /**
     * Create the Live Table Column Definitions.
     * 
     * @return tableDef
     */
    private TableDefinition<BaseCell[]> createTableDefinition() {
        BaseCell[] group = null;
        final DefaultTableDefinition<BaseCell[]> tableDef = new DefaultTableDefinition<BaseCell[]>();
        final List<BaseCell[]> colData = Arrays.asList(patTableModel.getColumnHeaders());
        for (int i = 0; i < olapData.getWidth(); i++) {
            final BaseCell[] headers = colData.get(offset - 1);
            // work in progress
            if (offset > 1)
                group = colData.get(offset - 2);

            final int cell = i;

            final HorizontalPanel cellPanel = createCell(headers[i]);

            final PatColDef<BaseCell[], Widget> colDef0 = new PatColDef<BaseCell[], Widget>(cellPanel) {
                @Override
                public Widget getCellValue(final BaseCell[] rowValue) {
                    if (rowValue[cell] == null) {
                        final Label testLabel = new Label(""); //$NON-NLS-1$
                        return testLabel;
                    } else {

                        final HorizontalPanel cellPanel = createCell(rowValue[cell]);
                        return cellPanel;
                    }
                }                
            };

            if (group != null) {
                HorizontalPanel groupPanel = null;
                if (group[i].formattedValue != null) {
                    groupPanel = createCell(group[i]);
                    colDef0.setHeader(1, groupPanel);
                } else
                    colDef0.setHeader(1, groupPanel);
            }
            
            colDef0.setHeaderTruncatable(false);
            colDef0.setColumnSortable(false);
            colDef0.setColumnTruncatable(false);
            tableDef.addColumnDefinition(colDef0);
        }
        return tableDef;
    }
}