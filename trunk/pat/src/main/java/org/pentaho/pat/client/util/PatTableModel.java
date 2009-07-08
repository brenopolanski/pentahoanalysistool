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

	public void getColumnHeaders(){

		for (int y = 0; y < olapMatrix.getMatrixHeight(); y++) {
            for (int x = 0; x < olapMatrix.getMatrixWidth(); x++) {
            	
            }
		}
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
	    BaseCell[][] values = new BaseCell[olapMatrix.getMatrixHeight()][olapMatrix.getMatrixWidth()];
	    for (int y = 0; y < olapMatrix.getMatrixHeight(); y++) {
                for (int x = 0; x < olapMatrix.getMatrixWidth(); x++) {
                    final BaseCell cell = olapMatrix.get(x, y);
                    values[y][x] = cell;
                }
	    }
		//List myList = new ArrayList(olapMatrix.getMap().values());
		return values;
	}


}
