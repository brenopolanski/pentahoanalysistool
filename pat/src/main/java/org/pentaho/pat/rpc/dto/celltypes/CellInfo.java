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
 * @created Jan 3, 2008
 * @author wseyler
 */

package org.pentaho.pat.rpc.dto.celltypes;

import java.io.Serializable;


import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class CellInfo.
 * 
 * @author wseyler
 */
public class CellInfo extends BaseCell implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	/** The is column header. */
	boolean isColumnHeader = false;
	
	/** The is row header. */
	boolean isRowHeader = false;
	
	/** The color value. */
	String colorValue = null; // Color held as hex String

	public CellInfo(){
	    
	}
	
	public CellInfo(boolean b, boolean c) {
	    this.right=b;
	    this.sameAsPrev=c;
	}


	/**
	 * Gets the color value.
	 * 
	 * @return the color value
	 */
	public String getColorValue() {
		return colorValue;
	}


	/**
	 * Checks if is column header.
	 * 
	 * @return true, if is column header
	 */
	public boolean isColumnHeader() {
		return isColumnHeader;
	}

	/**
	 * Checks if is header.
	 * 
	 * @return true, if is header
	 */
	public boolean isHeader() {
		return isRowHeader() || isColumnHeader();
	}

	/**
	 * Checks if is row header.
	 * 
	 * @return true, if is row header
	 */
	public boolean isRowHeader() {
		return isRowHeader;
	}

	/**
	 * Sets the color value.
	 * 
	 * @param colorValue the new color value
	 */
	public void setColorValue(final String colorValue) {
		this.colorValue = colorValue;
	}

	/**
	 * Sets the column header.
	 * 
	 * @param isColumnHeader the new column header
	 */
	public void setColumnHeader(final boolean isColumnHeader) {
		this.isColumnHeader = isColumnHeader;
	}


	/**
	 * Sets the row header.
	 * 
	 * @param isRowHeader the new row header
	 */
	public void setRowHeader(final boolean isRowHeader) {
		this.isRowHeader = isRowHeader;
	}
}
