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
import java.util.List;

import org.pentaho.pat.client.ui.widgets.DataCellPanel;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class CellInfo.
 * 
 * @author wseyler
 */
public class DataCell extends AbstractBaseCell implements Serializable, IsSerializable {

    private static final long serialVersionUID = 1L;

    /** The color value. */
    private String colorValue = null; // Color held as hex String

    private Number rawNumber = null;

    private MemberCell parentColMember = null;

    private MemberCell parentRowMember = null;

    private List<Integer> coordinates = null;
    
    private int ordinal;
    //private HashMap<String,String> properties = new HashMap<String, String>();
    
    
    /**
     * 
     * Blank constructor for serialization purposes, don't use it.
     * 
     */
    public DataCell() {
        super();
    }

    /**
     * 
     * Construct a Data Cell containing olap data.
     * 
     * @param b
     * @param c
     */
    public DataCell(final boolean right, final boolean sameAsPrev, List<Integer> coordinates, int ordinal) {
        super();
        this.right = right;
        this.sameAsPrev = sameAsPrev;
        this.coordinates = coordinates;
        this.ordinal = ordinal;
    }
    
    
    public MemberCell getParentColMember() {
        return parentColMember;
    }

    public void setParentColMember(final MemberCell parentColMember) {
        this.parentColMember = parentColMember;
    }

    public MemberCell getParentRowMember() {
        return parentRowMember;
    }

    public void setParentRowMember(final MemberCell parentRowMember) {
        this.parentRowMember = parentRowMember;
    }


    public Number getRawNumber() {
        return rawNumber;
    }

    public void setRawNumber(final Number rawNumber) {
        this.rawNumber = rawNumber;
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
    public Widget getLabel() {
        //final Label cellLabel = new Label(getFormattedValue());
        DataCellPanel cellPanel = new DataCellPanel(this.ordinal, DataCell.this);

            final Label cellLabel = new Label(getFormattedValue());
            cellLabel.setStyleName("pat-MemberCellLabel");
            cellLabel.setWidth("100%");
            cellPanel.add(cellLabel);
            cellPanel.setCellWidth(cellLabel, "100%");
            cellPanel.setWidth("100%"); //$NON-NLS-1$

            	return cellPanel;

    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Integer> coordinates) {
        this.coordinates = coordinates;
    }

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public int getOrdinal() {
		return ordinal;
	}
    
/*    public void setProperty(String name, String value){
        properties.put(name, value);
    }
    
    public HashMap<String, String> getProperties(){
        return properties;
    }
    
    public String getProperty(String name){
        return properties.get(name);
    }
  */  
}
