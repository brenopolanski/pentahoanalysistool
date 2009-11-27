/*
 * Copyright (C) 2009 Tom Barber
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
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.ui.FlexTable;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionFlexTable extends FlexTable {

    private Boolean horizontal = false;

    private final static String TABLE_CSS_NAME = "dropFlexTable"; //$NON-NLS-1$

    /**
     *TODO JAVADOC
     * 
     */
    public DimensionFlexTable() {
        super();

    }

    public DimensionFlexTable(final Boolean orientation) {
        super();
        addStyleName(TABLE_CSS_NAME);
        // FIXME remove that and use style
        DOM.setStyleAttribute(getElement(), "background", "#EEEEEE"); //$NON-NLS-1$ //$NON-NLS-2$

        horizontal = orientation;

    }

    /**
     *TODO JAVADOC
     * 
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

}
