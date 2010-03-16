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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

/**
 * The MainMenuPanel is the holder for the properties, and dimension panels.
 *
 * @created Aug 5, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 *
 */
public class MainMenuPanel extends LayoutComposite {

    private final LayoutPanel rootPanel = getLayoutPanel();

    /**
     *
     * MainMenuPanel Constructor.
     *
     * @param dPanel
     *
     */
    public MainMenuPanel(final DataPanel dPanel) {
        super();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.addStyleName("pat-MainMenu"); //$NON-NLS-1$
        // FIXME remove that and use style
        DOM.setStyleAttribute(rootPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        final DimensionPanel dimPanel = new DimensionPanel();
        // FIXME remove that and use style
        DOM.setStyleAttribute(dimPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        final CaptionLayoutPanel centerPanel = new CaptionLayoutPanel(ConstantFactory.getInstance().dimensions());
        // FIXME remove that and use style
        DOM.setStyleAttribute(centerPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        centerPanel.add(dimPanel);
        rootPanel.add(centerPanel);
    }

}

