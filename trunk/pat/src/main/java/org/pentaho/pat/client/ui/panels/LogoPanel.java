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
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.jfree.ui.Align;
import org.pentaho.pat.client.Pat;

import com.google.gwt.user.client.ui.Image;
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

    private final static String LOGOPANEL_CSS_STYLE = "logoPanel"; //$NON-NLS-1$

    private final LayoutPanel rootPanel = getLayoutPanel();

    private static LayoutPanel mainPanel;

    static Label test = new Label("argh!"); //$NON-NLS-1$

    /**
     * 
     * Sets the spinner spinning, or hides it.
     *
     * @param spin
     */
    public static void spinWheel(final boolean spin) {
 
        if (spin)
            test.setVisible(true);
        else
            test.setVisible(false);
    }

    /**
     *  Logo Panel Constructor.
     */
    public LogoPanel() {

        mainPanel = new LayoutPanel();

        final GridLayout mainLayout = new GridLayout(2,1);
        //mainLayout.setLeftToRight(false);
        mainPanel.setLayout(mainLayout);
        //mainLayout.setAlignment(Alignment.END);
        this.setStylePrimaryName(LOGOPANEL_CSS_STYLE);

        test.setStylePrimaryName("Throbber-loading"); //$NON-NLS-1$
        test.addStyleName("throbber"); //$NON-NLS-1$
        test.setSize("100px", "100px");
        GridLayoutData gl = new GridLayoutData(1, 3, true);
        gl.setHorizontalAlignment(GridLayoutData.ALIGN_RIGHT);
        gl.setVerticalAlignment(GridLayoutData.ALIGN_MIDDLE);

        final Image patlogo = Pat.IMAGES.pat_orange_banner().createImage();
        mainPanel.add(test);
        
        mainPanel.add(patlogo);
        
        rootPanel.add(mainPanel);

    }

}
