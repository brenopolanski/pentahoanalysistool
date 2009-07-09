package org.pentaho.pat.client.util;

import org.pentaho.pat.rpc.dto.Matrix;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;


public class PatTableModel {

	Matrix olapMatrix = null;
	
	public PatTableModel(Matrix olapMatrix){
	    this.olapMatrix = olapMatrix;


	}

	/**
	 * 
	 *Get the amount of columns in the dataset
	 *
	 * @return olapData.getCellData().getAcrossCount();
	 */
	public int getColumnCount(){
		return olapMatrix.getMatrixWidth();
	}

	public BaseCell[][] getColumnHeaders(){
		BaseCell[][] values = new BaseCell[olapMatrix.getMatrixHeight()][olapMatrix.getMatrixWidth()];
	    for (int y = 0; y < getOffset(); y++) {
                for (int x = 0; x < olapMatrix.getMatrixWidth(); x++) {
                    final BaseCell cell = olapMatrix.get(x, y);
                    values[y][x] = cell;
                }
	    }
		//List myList = new ArrayList(olapMatrix.getMap().values());
		return values;
		
	}

	/**
	 * 
	 * Get the amount of data rows
	 * 
	 * @return olapData.getCellData().getDownCount();
	 */
	public int getRowCount(){
		return olapMatrix.getMatrixHeight();
	}

	public BaseCell[][] getRowData(){
		int z = 0;
	    BaseCell[][] values = new BaseCell[olapMatrix.getMatrixHeight()-getOffset()+2][olapMatrix.getMatrixWidth()];
	    for (int y = getOffset(); y < olapMatrix.getMatrixHeight(); y++) {
               
	    	for (int x = 0; x < olapMatrix.getMatrixWidth(); x++) {
                    final BaseCell cell = olapMatrix.get(x, y);
                    values[z][x] = cell;
                }
	    	z++;
	    }
		//List myList = new ArrayList(olapMatrix.getMap().values());
		return values;
	}

	public int getOffset() {
		
		return olapMatrix.getOffset();
	}


}
