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
package org.pentaho.pat.client.util.table;

import org.pentaho.pat.rpc.dto.TableDataSet;

/**
 * 
 * Creates the needed components of an resultset table model for the PAT Resultset Live Table.
 * 
 * @author Paul Stoellberger
 * @version 0.7
 * @since 06/04/2010
 * 
 */
public class ResultSetTableModel {

    private TableDataSet tableMatrix = null;

    /**
     * 
     * Constructor.
     * 
     * @param olapMatrix
     */
    public ResultSetTableModel(final TableDataSet tableMatrix) {
        this.tableMatrix = tableMatrix;

    }

    /**
     * 
     *Get the number of columns in the dataset.
     * 
     * @return olapData.getCellData().getAcrossCount();
     */
    public int getColumnCount() {
        return tableMatrix.getWidth();
    }

    /**
     * 
     * Returns the column headers for the current dataset.
     * 
     * @return values
     */
    public String[] getColumnHeader() {
        return tableMatrix.getTableHeader();
    }

    /**
     * 
     * Get the amount of data rows
     * 
     * @return olapData.getCellData().getDownCount();
     */
    public int getRowCount() {
        return tableMatrix.getHeight();
    }

    /**
     * 
     * Return the current dataset's row data.
     * 
     * @return values
     */
    public String[][] getRowData() {
        return tableMatrix.getTableBody();
    }

}
