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
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

/**
 * This panel contains the spinning wheel, application name and logo
 * 
 * @created Aug 12, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class LogoPanel extends LayoutComposite {

    private final static String LPANEL_CSS_STYLE = "pat-LogoPanel"; //$NON-NLS-1$

    private final LayoutPanel rootPanel = getLayoutPanel();

    private static Label throbberLabel = new Label();

    /**
     * 
     * Sets the spinner spinning, or hides it.
     * 
     * @param spin
     */
    public static void spinWheel(final boolean spin) {

        if (spin) {
            throbberLabel.setVisible(true);
        } else {
            throbberLabel.setVisible(false);
        }
    }

    /**
     * Logo Panel Constructor.
     */
    public LogoPanel() {

        super();

        this.setStyleName(LPANEL_CSS_STYLE);

        final Grid logoGrid = new Grid(1, 2);
        throbberLabel.setStyleName("Throbber-loading"); //$NON-NLS-1$
        throbberLabel.addStyleName("throbber"); //$NON-NLS-1$
        throbberLabel.setSize("20px", "20px"); //$NON-NLS-1$ //$NON-NLS-2$
        throbberLabel.setPixelSize(100, 100);

        logoGrid.setWidget(0, 1, throbberLabel);
        
        logoGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        logoGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        rootPanel.add(logoGrid);

    }

}
