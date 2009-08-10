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

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.Axis.Standard;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionDropWidget extends LayoutComposite {

    private Standard dimAxis;
    private LayoutPanel baseLayoutPanel;
    private DimensionFlexTable dimensionTable;
    /**
     *TODO JAVADOC
     * @param unused 
     * @param string 
     *
     */
    public DimensionDropWidget(String labelText, Standard targetAxis) {
        this.dimAxis = targetAxis;
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);

    }
   
    /**
     * Initialization.
     *
     * @param labelText the label text
     * @param targetAxis the target axis
     */
    public final void init(final String labelText, final Axis targetAxis) {
           
            LayoutPanel scrollLayoutPanel = new ScrollLayoutPanel();
            
            dimensionTable = new DimensionFlexTable(/*DimensionPanel.getTableRowDragController()*/);
            dimensionTable.setWidth("100%"); //$NON-NLS-1$
            final Label dropLabel = new Label(labelText);
            dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
            dimensionTable.setStyleName("dropTable"); //$NON-NLS-1$
            
            Grid dimensionGrid = new Grid(2,1);
            dimensionGrid.setWidget(0, 0, dropLabel);

            dimensionGrid.setWidget(1, 0, dimensionTable);
            dimensionGrid.setWidth("100%"); //$NON-NLS-1$
            
            scrollLayoutPanel.add(dimensionGrid);
            
            baseLayoutPanel.add(scrollLayoutPanel);
            
            dimensionTable.populateDimensionTable(dimAxis);
            }

}
