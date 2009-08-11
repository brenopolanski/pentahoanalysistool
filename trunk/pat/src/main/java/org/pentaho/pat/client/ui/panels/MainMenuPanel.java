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
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 *TODO JAVADOC
 * 
 * @created Aug 5, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class MainMenuPanel extends LayoutComposite {

    private final LayoutPanel rootPanel = getLayoutPanel();

    public MainMenuPanel() {
        super();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.addStyleName("pat-MainMenu"); //$NON-NLS-1$
        final DimensionPanel dimPanel = new DimensionPanel();

        final PropertiesPanel propertiesPanel = new PropertiesPanel();

        final CaptionLayoutPanel centerPanel = new CaptionLayoutPanel(ConstantFactory.getInstance().dimensions());
        centerPanel.add(dimPanel);
        rootPanel.add(centerPanel);

        final CaptionLayoutPanel southPanel = new CaptionLayoutPanel(ConstantFactory.getInstance().properties());

        final ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES.toolCollapseDown());
        southPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);

        collapseBtn3.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                rootPanel.setCollapsed(southPanel, !rootPanel.isCollapsed(southPanel));
                rootPanel.layout();
            }
        });
        southPanel.add(propertiesPanel);
        rootPanel.add(southPanel, new BorderLayoutData(Region.SOUTH, 0.5, true));
        rootPanel.setCollapsed(southPanel, true);

    }

}
