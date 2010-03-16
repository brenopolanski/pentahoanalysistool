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

import org.pentaho.pat.client.ui.widgets.DataCellPanel;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

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

    private MemberCell parentRowMember = null;

    public Number getRawNumber() {
        return rawNumber;
    }

    public void setRawNumber(final Number rawNumber) {
        this.rawNumber = rawNumber;
    }

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
    public DataCell(final boolean right, final boolean sameAsPrev) {
        super();
        this.right = right;
        this.sameAsPrev = sameAsPrev;
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
    public HorizontalPanel getLabel() {
        final DataCellPanel cellPanel = new DataCellPanel(parentColMember, parentRowMember, rawNumber);

        final Label cellLabel = new Label(getFormattedValue());
        cellLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        cellPanel.add(cellLabel);

        cellPanel.setWidth("100%"); //$NON-NLS-1$

        return cellPanel;

    }
}
