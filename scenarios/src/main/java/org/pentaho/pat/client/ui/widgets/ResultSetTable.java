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
package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.LiveTable;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.table.DefaultColumnDefinition;
import org.pentaho.pat.client.util.table.ResultSetTableModel;
import org.pentaho.pat.rpc.dto.TableDataSet;

import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.IterableTableModel;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;

/**
 * Creates the data table, using a hashmap matrix converted to an array and
 * inserted into a GWT Mosaic Live Table, which is a lazy loading scrolltable.
 * 
 * @author Paul Stoellberger
 * @version 0.7
 * @since 07/04/2010
 * 
 */
public class ResultSetTable extends LayoutComposite {

    private TableDataSet tableData;

    private ResultSetTableModel patTableModel;

    private LiveTable<String[]> table;

    private final LayoutPanel layoutPanel = getLayoutPanel();

    /**
     * Create an OLAPTable.
     */
    public ResultSetTable() {
        super();

    }

    /**
     * 
     * Initialize the Live Table.
     * 
     */
    private void initTable() {
        // Not sure what the effect of this is, but at least something is
        // happening now. Not just frozen
        layoutPanel.clear();
        patTableModel = new ResultSetTableModel(tableData);
        final List<String[]> data = Arrays.asList(patTableModel.getRowData());
        

        final TableModel<String[]> tableModel = new IterableTableModel<String[]>(data) {
            @Override
            public int getRowCount() {
                return data.size();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void requestRows(final Request request,
                    final Callback<String[]> callback) {
                final int numRows = Math.min(request.getNumRows(), data.size()
                        - request.getStartRow());

                final List<String[]> list = new ArrayList<String[]>();
                // in the first row is just the header of the data table
                // this might be a dirty hack but works for now, until we have a proper resultset object
                for (int i = 0, n = numRows - 1; i < n; i++) {
                    list.add(data.get(request.getStartRow() + 1 + i));
                }
                final SerializableResponse response = new SerializableResponse(
                        list);
                callback.onRowsReady(request, response);
            }
        };

        table = new LiveTable<String[]>(tableModel,createTableDefinition());

        layoutPanel.add(table);
        table.fillWidth();
        layoutPanel.layout();

    }


    /**
     * 
     * Setup the current table.
     * 
     * @param olapData
     */
    public void setData(final TableDataSet tableData) {
        this.tableData = tableData;
        initTable();

        table.reload();
        table.redraw();
        table.fillWidth();
        this.layout();
    }

    /**
     * Create the Live Table Column Definitions.
     * 
     * @return tableDef
     */
    private TableDefinition<String[]> createTableDefinition() {
        final DefaultTableDefinition<String[]> tableDef = new DefaultTableDefinition<String[]>();
        final List<String> colData = Arrays.asList(patTableModel.getColumnHeader());
        for (int i = 0; i < tableData.getWidth(); i++) {
            final String headers = colData.get(i);
            final Integer cell = i;
            final DefaultColumnDefinition<String[],String> colDef0 = new DefaultColumnDefinition<String[],String>(headers) {
                @Override
                public String getCellValue(String[] rowValue) {
                    return (rowValue == null  || rowValue[cell] == null || rowValue[cell].equals("null") ? "" : rowValue[cell]);
                    
                }
              };

            
            colDef0.setHeaderTruncatable(false);
            colDef0.setColumnSortable(false);
            colDef0.setColumnTruncatable(false);

            tableDef.addColumnDefinition(colDef0);
        }
        return tableDef;
    }
}
