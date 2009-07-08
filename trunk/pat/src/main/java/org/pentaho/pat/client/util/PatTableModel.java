package org.pentaho.pat.client.util;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.rpc.dto.Matrix;


public class PatTableModel {

	Matrix olapMatrix = null;
	
	public PatTableModel(){



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

	public List getRowData(){
		List myList = new ArrayList(olapMatrix.getMap().values());
		return myList;
	}


}
