/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Jul 10, 2009 
 * @author Tom Barber
 */
package org.pentaho.pat.client.util.table;

import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;

/**
 * 
 * Creates the needed components of an olap table model 
 * for the PAT Live Table.
 *
 * @author tom (at) wamonline.org.uk
 *
 */
public class PatTableModel {

	CellDataSet olapMatrix = null;

	/**
	 * 
	 * Constructor.
	 *
	 * @param olapMatrix
	 */
	public PatTableModel(final CellDataSet olapMatrix) {
		this.olapMatrix = olapMatrix;

	}

	/**
	 * 
	 *Get the number of columns in the dataset.
	 * 
	 * @return olapData.getCellData().getAcrossCount();
	 */
	public int getColumnCount() {
		return olapMatrix.getWidth();
	}

	/**
	 * 
	 * Returns the column headers for the current dataset.
	 *
	 * @return values
	 */
	public BaseCell[][] getColumnHeaders() {
		/*final BaseCell[][] values = new BaseCell[olapMatrix.getMatrixHeight()][olapMatrix.getMatrixWidth()];
		for (int y = 0; y < getOffset(); y++) {
			for (int x = 0; x < olapMatrix.getMatrixWidth(); x++) {
				values[y][x] = olapMatrix.get(x, y);
			}
		}*/
		return olapMatrix.getCellSetHeaders();

	}

	/**
	 * 
	 * Return the column header offset.
	 *
	 * @return offset
	 */
	public int getOffset() {
		return olapMatrix.getOffset();
	}

	/**
	 * 
	 * Get the amount of data rows
	 * 
	 * @return olapData.getCellData().getDownCount();
	 */
	public int getRowCount() {
		return olapMatrix.getHeight();
	}

	/**
	 * 
	 * Return the current dataset's row data.
	 *
	 * @return values
	 */
	public BaseCell[][] getRowData() {
		/*int z = 0;
		final BaseCell[][] values = new BaseCell[olapMatrix.getMatrixHeight() - getOffset() + 2][olapMatrix.getMatrixWidth()];
		for (int y = getOffset(); y < olapMatrix.getMatrixHeight(); y++) {

			for (int x = 0; x < olapMatrix.getMatrixWidth(); x++) {
				values[z][x] = olapMatrix.get(x, y);
			}
			z++;
		}*/
	    
	    
		return olapMatrix.getCellSetBody();
	}

}
