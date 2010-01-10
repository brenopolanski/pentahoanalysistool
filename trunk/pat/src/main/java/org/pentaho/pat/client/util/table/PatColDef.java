/*
 * Copyright (C) 2009 Thomas Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 *
 */
package org.pentaho.pat.client.util.table;

import org.gwt.mosaic.ui.client.table.DefaultColumnDefinition;

import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @author bugg
 * 
 */
public class PatColDef<RowType, ColType> extends AbstractColumnDefinition<RowType, ColType> implements
        HasHorizontalAlignment {

    /**
     * Construct a new {@link DefaultColumnDefinition}.
     * 
     * @param header
     *            the name of the column.
     */
    public PatColDef(final Widget header) {
        super();
        setHeader(0, header);
    }

    @Override
    public void setCellValue(final RowType rowValue, final ColType cellValue) {
        // Ignore
    }

    @Override
    public ColType getCellValue(final RowType rowValue) {
        // TODO Auto-generated method stub
        return null;
    }

    public HorizontalAlignmentConstant getHorizontalAlignment() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setHorizontalAlignment(final HorizontalAlignmentConstant align) {
        // TODO Auto-generated method stub

    }

    
}