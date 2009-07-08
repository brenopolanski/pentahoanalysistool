/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * @created Feb 1, 2008
 * @author wseyler
 */

package org.pentaho.pat.rpc.dto;

import java.io.Serializable;

import org.pentaho.pat.rpc.dto.celltypes.CellInfo;


import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class CellData.
 * 
 * @author wseyler
 */
public class CellData implements IOlapDataStructure, Serializable, IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	/** The olap data cells. */
	CellInfo[][] olapDataCells;

	/**
	 * Instantiates a new cell data.
	 */
	public CellData() {
		super();
	}

	/**
	 * Instantiates a new cell data.
	 * 
	 * @param olapDataCells the olap data cells
	 */
	public CellData(final CellInfo[][] olapDataCells) {
		this();
		this.olapDataCells = olapDataCells;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.IOlapDataStructure#getAcrossCount()
	 */
	public int getAcrossCount() {
		if (olapDataCells == null) {
			return 0;
		}
		return olapDataCells[0].length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pentaho.halogen.client.util.IOlapDataStructure#getCell(int, int)
	 */
	public CellInfo getCell(final int row, final int column) {
		return olapDataCells == null ? null : olapDataCells[row][column];
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.IOlapDataStructure#getDownCount()
	 */
	public int getDownCount() {
		if (olapDataCells == null) {
			return 0;
		}
		return olapDataCells.length;
	}

	/**
	 * Gets the olap data cells.
	 * 
	 * @return the olap data cells
	 */
	public CellInfo[][] getOlapDataCells() {
		return olapDataCells;
	}

	/**
	 * Sets the olap cells.
	 * 
	 * @param olapDataCells the new olap cells
	 */
	public void setOlapCells(final CellInfo[][] olapDataCells) {
		this.olapDataCells = olapDataCells;
	}

}
