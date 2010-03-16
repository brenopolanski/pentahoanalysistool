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

import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.windows.LoadMenuPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * The load query window.
 * 
 * @created Oct 21, 2009
 * @since 0.5.1
 * @author tom (at) wamonline.org.uk
 */
public class LoadWindow extends WindowPanel {

    private static final String LOAD_TITLE = ConstantFactory.getInstance().load();

    private final static LoadMenuPanel LOADMENUPANEL = new LoadMenuPanel();

    private final LayoutPanel winContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

    private final static LoadWindow CBW = new LoadWindow();

    /**
     * Display window.
     */
    public static void display() {
        CBW.setSize("450px", "300px"); //$NON-NLS-1$ //$NON-NLS-2$
        LOADMENUPANEL.loadSavedQueries();
        CBW.showModal(false);
        CBW.layout();
    }

    /**
     * Create the load window.
     */
    public LoadWindow() {
        super(LOAD_TITLE);

        winContentpanel.add(LOADMENUPANEL, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(winContentpanel);
        this.setFooter(buttonBar());
        this.layout();
    }

    /**
     * Create the button bar.
     * @return
     */
    public LayoutPanel buttonBar() {
        final Button okButton = new Button(ConstantFactory.getInstance().ok());
        okButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {

                LOADMENUPANEL.load();
                CBW.hide();
            }

        });
        final Button cancelButton = new Button(ConstantFactory.getInstance().cancel());
        cancelButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                CBW.hide();

            }

        });
        return ButtonBarFactory.buildOKCancelBar(okButton, cancelButton);
    }
}
