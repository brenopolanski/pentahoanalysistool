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
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.CubeMenu;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.panels.MdxPanel;
import org.pentaho.pat.client.ui.panels.OlapPanel;
import org.pentaho.pat.client.ui.widgets.CubeTreeItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Lists all Connections and Cubes and allows creation of new queries.
 * 
 * @created Aug 7, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class CubeBrowserWindow extends WindowPanel {

    /** The Window Title. */
    private static final String TITLE = Pat.CONSTANTS.titleCubeBrowser();

    private final static CubeMenu CUBEMENUPANEL = new CubeMenu();

    private final LayoutPanel winContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

    private final static ToolButton QMQUERYBUTTON = new ToolButton(Pat.CONSTANTS.newQuery());

    private final static ToolButton MDXQUERYBUTTON = new ToolButton(Pat.CONSTANTS.newMdxQuery());

    private final ToolButton cancelButton = new ToolButton(Pat.CONSTANTS.cancel());

    private final static CubeBrowserWindow CBW = new CubeBrowserWindow();

    /**
     * Display.
     */
    public static void display() {
        display(true);
    }

    /**
     * Display.
     * @param connectionId
     */
    public static void display(final String connectionId) {
        CUBEMENUPANEL.loadCubes(connectionId);
        display(false);
    }

    /**
     * Enable MDX Query.
     * @param enabled
     */
    public static void enableMdxQuery(final boolean enabled) {
        MDXQUERYBUTTON.setEnabled(enabled);
    }

    /**
     * Enable Query Model Query.
     * @param enabled
     */
    public static void enableQmQuery(final boolean enabled) {
        QMQUERYBUTTON.setEnabled(enabled);
    }

    /**
     * Display with refresh.
     * @param refreshCubes
     */
    private static void display(final boolean refreshCubes) {
        QMQUERYBUTTON.setEnabled(false);
        MDXQUERYBUTTON.setEnabled(false);
        CBW.setSize("450px", "300px"); //$NON-NLS-1$ //$NON-NLS-2$
        if (refreshCubes) {
            CUBEMENUPANEL.loadCubes();
        }
        CBW.showModal(false);
        CBW.layout();
    }

    /**
     * Cube Browser Window Constructor.
     */
    public CubeBrowserWindow() {
        super(TITLE);
        winContentpanel.add(CUBEMENUPANEL, new BoxLayoutData(FillStyle.BOTH));
        final LayoutPanel newQryButPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        setupQueryButtons();

        newQryButPanel.add(QMQUERYBUTTON, new BoxLayoutData(FillStyle.HORIZONTAL));
        newQryButPanel.add(MDXQUERYBUTTON, new BoxLayoutData(FillStyle.HORIZONTAL));
        newQryButPanel.add(cancelButton, new BoxLayoutData(FillStyle.HORIZONTAL));
        winContentpanel.add(newQryButPanel, new BoxLayoutData(FillStyle.VERTICAL));
        this.setWidget(winContentpanel);
        this.layout();

    }

    /**
     * Setup query buttons.
     */
    private void setupQueryButtons() {
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {

                CBW.hide();

            }
        });

        QMQUERYBUTTON.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {
                if (CUBEMENUPANEL.getCubeTree().getSelectedItem() != null) {
                    final CubeTreeItem selected = (CubeTreeItem) CUBEMENUPANEL.getCubeTree().getSelectedItem();
                    if (selected.getType() == CubeTreeItem.ItemType.CUBE) {
                        LogoPanel.spinWheel(true);
                        final OlapPanel olappanel = new OlapPanel(selected.getCubeItem(), selected.getConnection());
                        MainTabPanel.displayContentWidget(olappanel);
                    }
                    CBW.hide();
                }
            }
        });
        QMQUERYBUTTON.setEnabled(false);

        MDXQUERYBUTTON.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent arg0) {
                if (CUBEMENUPANEL.getCubeTree().getSelectedItem() != null) {
                    final CubeTreeItem selected = (CubeTreeItem) CUBEMENUPANEL.getCubeTree().getSelectedItem();
                    if (selected.getType() == CubeTreeItem.ItemType.CUBE) {
                        LogoPanel.spinWheel(true);
                        final MdxPanel mdxPanel = new MdxPanel(selected.getCubeItem(), selected.getConnection());
                        MainTabPanel.displayContentWidget(mdxPanel);
                    }
                    CBW.hide();
                }
            }
        });
        MDXQUERYBUTTON.setEnabled(false);
    }

}
