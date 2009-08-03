/*
 * Copyright (C) 2009 Paul Stoellberger
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
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Menu Bar
 * @created Jul 29, 2009 
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class MenuBar extends LayoutComposite {

    private LayoutPanel rootPanel = getLayoutPanel();
    /**
     * Initializes the Menu Bar contents  
     */
    public MenuBar() {
        super();
        rootPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));
        addConnectionsButton();
       
    }
    
    private void addConnectionsButton() {
        // TODO replace with proper icon set; connections icon
        ToolButton connectionButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.databases(),"Connections",ButtonLabelType.TEXT_ON_BOTTOM));

        connectionButton.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                ConnectionManagerWindow.display();
                
            }
        });
        rootPanel.add(connectionButton);
    }

}
