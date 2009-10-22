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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
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
import org.pentaho.pat.client.ui.windows.SaveWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
        addLoadButton();
        rootPanel.add(logoPanel, new BoxLayoutData(FillStyle.BOTH));
        rootPanel.addStyleName("pat-menuBar"); //$NON-NLS-1$
    }

    /**
     * 
     * Adds a Connections button which controls the connection window.
     *
     */
    private void addConnectionsButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        final ToolButton connectionButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.databases(),
                ConstantFactory.getInstance().connections(), ButtonLabelType.TEXT_ON_BOTTOM));
        connectionButton.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(connectionButton.getElement(),"background", "white");

        

        connectionButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ConnectionManagerWindow.display();
            }
        });
        rootPanel.add(connectionButton, new BoxLayoutData(FillStyle.VERTICAL));
    }
    
    /**
     * 
     * Adds a button which generates the cube browser.
     *
     */
    private void addCubesButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        final ToolButton cubeButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(), ConstantFactory
                .getInstance().cubes(), ButtonLabelType.TEXT_ON_BOTTOM));
        cubeButton.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(cubeButton.getElement(),"background", "white");

        cubeButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                CubeBrowserWindow.display();
            }
        });
        rootPanel.add(cubeButton, new BoxLayoutData(FillStyle.VERTICAL));
    }

    /**
     * 
     * Adds a currently unusable save button.
     *
     */
    private void addSaveButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        final ToolButton saveButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(), ConstantFactory
                .getInstance().save(), ButtonLabelType.TEXT_ON_BOTTOM));
        saveButton.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(saveButton.getElement(),"background", "white");
        saveButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                SaveWindow.display();
            }
        });
        saveButton.setEnabled(true);
        rootPanel.add(saveButton, new BoxLayoutData(FillStyle.VERTICAL));
    }

    private void addLoadButton() {
        // TODO replace with proper icon set; connections icon(create a button widget that can be duplicated across all
        // cases)
        final ToolButton loadButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cube(), ConstantFactory
                .getInstance().load(), ButtonLabelType.TEXT_ON_BOTTOM));
        loadButton.addStyleName("pat-toolButton"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(loadButton.getElement(),"background", "white");
        loadButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                ServiceFactory.getQueryInstance().loadQuery(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<Object>(){

                    public void onFailure(Throwable arg0) {
                        MessageBox.info("failed", "boo");
                    }

                    public void onSuccess(Object arg0) {
                        MessageBox.info("Success", "yay");
                    }
                    
                });
            }
        });
        loadButton.setEnabled(true);
        rootPanel.add(loadButton, new BoxLayoutData(FillStyle.VERTICAL));
    }

}
