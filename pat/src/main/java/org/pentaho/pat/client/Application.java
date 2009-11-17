/*
 * Copyright (C) 2009 Tom Barber
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

package org.pentaho.pat.client;

import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.panels.MenuBar;
import org.pentaho.pat.client.ui.panels.WelcomePanel;
import org.pentaho.pat.client.util.dnd.FlexTableRowDragController;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.TreeImages;

/**
 * <p>
 * The Application wrapper that includes a menu bar, title and content.
 * </p>
 * <h3>CSS Style Rules</h3> <ul class="css"> <li>.Application { Applied to the
 * entire Application }</li> <li>.Application-top { The top portion of the
 * Application }</li> <li>.Application-title { The title widget }</li> <li>
 * .Application-links { The main external links }</li> <li>.Application-options
 * { The options widget }</li> <li>.Application-menu { The main menu }</li> <li>
 * .Application-content-wrapper { The element around the content }</li> </ul>
 *
 * @created Apr 23, 2009 
 * @since 0.3
 * @author Tom Barber
 * 
 */

public class Application extends Viewport {
    /**
     * Images used in the {@link Application}.
     */
    public interface ApplicationImages extends TreeImages {

        /**
         * An image indicating a leaf.
         *
         * @return a prototype of this image
         */
        @Resource("noimage.png")
        AbstractImagePrototype treeLeaf();
    }

    /**
     * A listener to handle events from the Application.
     */
    public interface ApplicationListener {

        /**
         * Fired when a menu item is selected.
         *
         * @param item
         *            the item that was selected
         */
        void onMenuItemSelected(com.google.gwt.user.client.ui.TreeItem item);
    }

    private static MainTabPanel mainTabPanel = null;

    private MenuBar menuBar = null;

    public static FlexTableRowDragController tableRowDragController;

    private static LayoutPanel rootPanel;
    /**
     * Constructor.
     */

    public Application() {
        super();
        
        rootPanel = getLayoutPanel();
        rootPanel.setLayout(new BoxLayout(Orientation.VERTICAL));

        Application.tableRowDragController = new FlexTableRowDragController(Application.getMainPanel());

        // Setup the main layout widget
        if (Pat.getApplicationState().getMode().isShowOnlyTable() == false) {

            // Add the main menu
            if (Pat.getApplicationState().getMode().isShowMenu()) {

                menuBar = new MenuBar();
                rootPanel.add(menuBar,new BoxLayoutData(FillStyle.HORIZONTAL));
            }


            if (Pat.getApplicationState().getMode().isShowWelcomePanel()) {
                MainTabPanel.displayContentWidget(new WelcomePanel(ConstantFactory.getInstance().welcome()));
            }

            mainTabPanel = new MainTabPanel();
            rootPanel.add(mainTabPanel, new BoxLayoutData(FillStyle.BOTH));
        }

        else {
            //TODO Disabled until new OlapTable implemented
            //rootPanel.add(new OlapTable(), new BoxLayoutData(FillStyle.BOTH));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.gwt.mosaic.ui.client.Viewport#getWidget()
     */
    /**
     * Gets the widget.
     *
     * @return the widget.
     */
    @Override
    protected final LayoutPanel getWidget() {
        return (LayoutPanel)super.getWidget();
    }

    // TODO remove if not needed
    public static MainTabPanel getMainTabPanel() {
        return mainTabPanel;
    }


    /**
     * Gets the main panel.
     *
     * @return the bottom panel
     */
    public static LayoutPanel getMainPanel() {
        return rootPanel;
    }


}