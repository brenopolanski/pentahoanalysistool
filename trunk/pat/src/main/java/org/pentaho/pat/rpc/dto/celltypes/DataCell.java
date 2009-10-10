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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class CellInfo.
 * 
 * @author wseyler
 */
public class DataCell extends BaseCell implements Serializable, IsSerializable {

	private static final long serialVersionUID = 1L;

	/** The color value. */
	String colorValue = null; // Color held as hex String

	/**
	 * 
	 * Blank constructor for serialization purposes, don't use it.
	 * 
	 */
	public DataCell() {
	}

	/**
	 * 
	 * Construct a Data Cell containing olap data.
	 * 
	 * @param b
	 * @param c
	 */
	public DataCell(final boolean b, final boolean c) {
		this.right = b;
		this.sameAsPrev = c;
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
	 * Sets the color value.
	 * 
	 * @param colorValue
	 *            the new color value
	 */
	public void setColorValue(final String colorValue) {
		this.colorValue = colorValue;
	}

	@Override
	public HorizontalPanel getLabel(){
	    final HorizontalPanel cellPanel = new HorizontalPanel();
	    
	    final Label cellLabel = new Label(getFormattedValue());
	    
	    cellPanel.add(cellLabel);

        cellPanel.setWidth("100%"); //$NON-NLS-1$

        return cellPanel;
	    
	}
}
