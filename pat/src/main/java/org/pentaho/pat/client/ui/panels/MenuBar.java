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
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.ui.windows.CubeBrowserWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Menu Bar
 * 
 * @created Jul 29, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class MenuBar extends LayoutComposite {

    private final LayoutPanel rootPanel = getLayoutPanel();

    /**
     * Initializes the Menu Bar contents
     */
    public MenuBar() {
        super();
        final LogoPanel logoPanel = new LogoPanel();
        rootPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));
        rootPanel.setWidth("100%"); //$NON-NLS-1$
        addConnectionsButton();
        addCubesButton();
        addSaveButton();
        rootPanel.add(logoPanel, new BoxLayoutData(FillStyle.BOTH));
        rootPanel.addStyleName("pat-menuBar"); //$NON-NLS-1$
    }

    private void addConnectionsButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        final ToolButton connectionButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.databases(),
                ConstantFactory.getInstance().connections(), ButtonLabelType.TEXT_ON_BOTTOM));
        connectionButton.addStyleName("pat-toolButton"); //$NON-NLS-1$

        connectionButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ConnectionManagerWindow.display();
            }
        });
        rootPanel.add(connectionButton, new BoxLayoutData(FillStyle.VERTICAL));
    }

    private void addCubesButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        final ToolButton cubeButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(), ConstantFactory
                .getInstance().cubes(), ButtonLabelType.TEXT_ON_BOTTOM));
        cubeButton.addStyleName("pat-toolButton"); //$NON-NLS-1$

        cubeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                CubeBrowserWindow.display();
            }
        });
        rootPanel.add(cubeButton, new BoxLayoutData(FillStyle.VERTICAL));
    }

    private void addSaveButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        final ToolButton saveButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(), ConstantFactory
                .getInstance().save(), ButtonLabelType.TEXT_ON_BOTTOM));
        saveButton.addStyleName("pat-toolButton"); //$NON-NLS-1$

        saveButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {

            }
        });
        saveButton.setEnabled(false);
        rootPanel.add(saveButton, new BoxLayoutData(FillStyle.VERTICAL));
    }
}
