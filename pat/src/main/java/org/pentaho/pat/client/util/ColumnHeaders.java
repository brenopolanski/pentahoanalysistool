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
 * The Class ColumnHeaders.
 * 
 * @author wseyler
 */
public class ColumnHeaders implements IOlapDataStructure, IsSerializable, Serializable {
	
	/** The column header members. */
	CellInfo[][] columnHeaderMembers;

	/**
	 * Instantiates a new column headers.
	 */
	public ColumnHeaders() {
		super();
	}

	/**
	 * Instantiates a new column headers.
	 * 
	 * @param columnHeaderMembers the column header members
	 */
	public ColumnHeaders(final CellInfo[][] columnHeaderMembers) {
		this();
		setColumnHeaderMembers(columnHeaderMembers);
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.IOlapDataStructure#getAcrossCount()
	 */
	public int getAcrossCount() {
		if (columnHeaderMembers == null) {
			return 0;
		}
		return columnHeaderMembers[0].length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pentaho.halogen.client.util.IOlapDataStructure#getCell(int, int)
	 */
	public CellInfo getCell(final int row, final int column) {
		return columnHeaderMembers == null ? null : columnHeaderMembers[row][column];
	}

	/**
	 * Gets the column header members.
	 * 
	 * @return the column header members
	 */
	public CellInfo[][] getColumnHeaderMembers() {
		return columnHeaderMembers;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.util.IOlapDataStructure#getDownCount()
	 */
	public int getDownCount() {
		if (columnHeaderMembers == null) {
			return 0;
		}
		return columnHeaderMembers.length;
	}

	/**
	 * Normalize.
	 */
	public void normalize() {
		if (columnHeaderMembers != null) {
			for (int r = 0; r < getDownCount(); r++) {
				for (int c = 0; c < getAcrossCount(); c++) {
					final CellInfo cell = getCell(r, c);
					if (cell == null && r > 0) {
						columnHeaderMembers[r][c] = getCell(r - 1, c);
					}
				}
			}
		}
	}

	/**
	 * Sets the column header members.
	 * 
	 * @param columnHeaderMembers the new column header members
	 */
	public void setColumnHeaderMembers(final CellInfo[][] columnHeaderMembers) {
		this.columnHeaderMembers = columnHeaderMembers;
		normalize();
	}

}
