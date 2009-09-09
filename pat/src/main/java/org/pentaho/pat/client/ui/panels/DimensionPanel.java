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

package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.pentaho.pat.client.ui.widgets.DimensionDropWidget;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.rpc.dto.Axis;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionPanel extends LayoutComposite {

    private final DimensionDropWidget dimDropUnused;

    private final DimensionDropWidget dimDropRow;

    private final DimensionDropWidget dimDropCol;

    private final DimensionDropWidget dimDropFilter;

    private static FlexTableRowDragController tableRowDragController;
    
    private final static String ROOT_STYLE_NAME = "pat-DimensionPanel"; //$NON-NLS-1$

    /**
     *TODO JAVADOC
     * 
     */
    public DimensionPanel() {
        setTableRowDragController(new FlexTableRowDragController(Application.getMainPanel()));
        
        final LayoutPanel rootPanel = getLayoutPanel();

        final ScrollLayoutPanel mainPanel = new ScrollLayoutPanel();

        mainPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        mainPanel.addStyleName(ROOT_STYLE_NAME);
        
        
        dimDropUnused = new DimensionDropWidget(ConstantFactory.getInstance().unused(), Axis.UNUSED);
        dimDropRow = new DimensionDropWidget(ConstantFactory.getInstance().rows(), Axis.ROWS);
        dimDropCol = new DimensionDropWidget(ConstantFactory.getInstance().columns(), Axis.COLUMNS);
        dimDropFilter = new DimensionDropWidget(ConstantFactory.getInstance().filter(), Axis.FILTER);
        mainPanel.add(dimDropUnused, new BoxLayoutData(1, -1));
        
        mainPanel.add(dimDropRow, new BoxLayoutData(1, -1));
        mainPanel.add(dimDropCol, new BoxLayoutData(1, -1));
        mainPanel.add(dimDropFilter, new BoxLayoutData(1, -1));

        rootPanel.add(mainPanel);
        
        

    }


    /**
     * Returns the drag controller.
     * 
     * @return tableRowDragController
     */
    public static FlexTableRowDragController getTableRowDragController() {
            return tableRowDragController;
    }

    /**
     * Set the drag controller.
     * 
     * @param tableRowDragController
     *            Accepts a FlexTableRowDragController
     */
    private static void setTableRowDragController(final FlexTableRowDragController tableRowDragController) {
            DimensionPanel.tableRowDragController = tableRowDragController;
    }
    
}
