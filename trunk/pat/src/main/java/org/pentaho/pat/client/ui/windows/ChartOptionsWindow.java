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
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.ChartOptionsPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

/**
 * Allows users to adjust chart options.
 *
 * @author tom(at)wamonline.org.uk
 *
 */
public class ChartOptionsWindow extends WindowPanel {

    private static final String TITLE = ConstantFactory.getInstance().chartOptions();
    
    private final LayoutPanel windowContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    
    private final static ChartOptionsPanel chartOptionsPanel = new ChartOptionsPanel();

    private final static ChartOptionsWindow cow = new ChartOptionsWindow();
    
    /**
     * 
     * Dislay the window.
     *
     */
    public static void display() {
        cow.setSize("450px", "300px"); //$NON-NLS-1$ //$NON-NLS-2$
        cow.showModal(true);
        cow.layout();
    }

    /**
     * 
     * Chart Options Window Constructor.
     *
     */
    public ChartOptionsWindow(){
        super(TITLE);
        windowContentpanel.add(chartOptionsPanel, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(windowContentpanel);
        this.layout();
    }
    
}
