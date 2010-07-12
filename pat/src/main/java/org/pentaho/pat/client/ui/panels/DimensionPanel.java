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
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.ui.widgets.DimensionTreeWidget;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.factory.EventFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * The dimension panel creates the axis dimension lists and facilitates the drag and drop of those widgets
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionPanel extends LayoutComposite {

	private final ToolButton uniqueNameBtn = new ToolButton("Show Unique Names");
	
    private final SimplePanelDragControllerImpl tRDragController = Application.INSTANCE.getSimplePanelDrgCont();

    private final static String ROOT_STYLE_NAME = "pat-DimensionPanel"; //$NON-NLS-1$

    /**
     * Returns the drag controller.
     * 
     * @return tableRowDragController
     */
    public SimplePanelDragControllerImpl getTableRowDragController() {
        return tRDragController;
    }

    /**
     * Creates a dimension panel this lists all the available dimensions on the various available axis.
     * 
     */
    public DimensionPanel() {
        super();
         final LayoutPanel rootPanel = getLayoutPanel();

        final ScrollLayoutPanel mainPanel = new ScrollLayoutPanel();

        mainPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        mainPanel.addStyleName(ROOT_STYLE_NAME);

        
        uniqueNameBtn.setStyle(ToolButtonStyle.CHECKBOX);
        
        uniqueNameBtn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {
                EventFactory.getLabelInstance().getLabelListeners()
                    .fireUniqueNameChange(DimensionPanel.this, uniqueNameBtn.isChecked());

            }
        });
        final DimensionTreeWidget dimDropUnused = new DimensionTreeWidget(tRDragController, this);
        mainPanel.add(uniqueNameBtn);
        mainPanel.add(dimDropUnused, new BoxLayoutData(FillStyle.BOTH, true));
        
        rootPanel.add(mainPanel);

    }
    
    public boolean isUniqueNameLabel(){
    	return uniqueNameBtn.isChecked();
    }


}
