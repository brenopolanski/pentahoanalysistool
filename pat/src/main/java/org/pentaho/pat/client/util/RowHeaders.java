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

package org.pentaho.pat.client.util;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class RowHeaders.
 * 
 * @author wseyler
 */
public class RowHeaders implements IOlapDataStructure, IsSerializable, Serializable {
	
	/** The row header members. */
	CellInfo[][] rowHeaderMembers;

	/**
	 * Instantiates a new row headers.
	 */
	public RowHeaders() {
		super();
	}

	/**
	 * Instantiates a new row headers.
	 * 
	 * @param rowHeaderMembers the row header members
	 */
	public RowHeaders(final CellInfo[][] rowHeaderMembers) {
		this();
		setRowHeaderMembers(rowHeaderMembers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pentaho.halogen.client.util.IOlapDataStructure#normalize()
	 */
	/**
	 * Equalize.
	 */
	public void equalize() {
		if (rowHeaderMembers != null) {
			for (int r = 0; r < getDownCount(); r++) {
				for (int c = 0; c < getAcrossCount(); c++) {
					final CellInfo cell = getCell(r, c);
					if (cell == null && c > 0) {
						rowHeaderMembers[r][c] = getCell(r, c - 1);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.IOlapDataStructure#getAcrossCount()
	 */
	public int getAcrossCount() {
		if (rowHeaderMembers == null) {
			return 0;
		}
		return rowHeaderMembers[0].length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pentaho.halogen.client.util.IOlapDataStructure#getCell(int, int)
	 */
	public CellInfo getCell(final int row, final int column) {
		return rowHeaderMembers == null ? null : rowHeaderMembers[row][column];
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.IOlapDataStructure#getDownCount()
	 */
	public int getDownCount() {
		if (rowHeaderMembers == null) {
			return 0;
		}
		return rowHeaderMembers.length;
	}

	/**
	 * Gets the row header members.
	 * 
	 * @return the row header members
	 */
	public CellInfo[][] getRowHeaderMembers() {
		return rowHeaderMembers;
	}

	/**
	 * Sets the row header members.
	 * 
	 * @param rowHeaderMembers the new row header members
	 */
	public void setRowHeaderMembers(final CellInfo[][] rowHeaderMembers) {
		this.rowHeaderMembers = rowHeaderMembers;
		equalize();
	}

}
