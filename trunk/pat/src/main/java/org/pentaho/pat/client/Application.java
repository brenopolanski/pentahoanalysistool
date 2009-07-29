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
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.deprecated.ui.panels.MainTabPanel;
import org.pentaho.pat.client.deprecated.ui.panels.WelcomePanel;
import org.pentaho.pat.client.deprecated.ui.widgets.OlapTable;
import org.pentaho.pat.client.deprecated.util.factory.ConstantFactory;

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

    /** The bottom Panel for the Application, contains the main panels. */
    private static LayoutPanel mainPanel;

    private static MainTabPanel mainTabPanel = null;

    /**
     * Constructor.
     */

    public Application() {
        super();
        final LayoutPanel rootPanel = getLayoutPanel();
        rootPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        mainPanel = new LayoutPanel(new BorderLayout());
        // Setup the main layout widget
        if (Pat.getApplicationState().getMode().isShowOnlyTable() == false) {
            mainTabPanel = new MainTabPanel();
            mainPanel.add(mainTabPanel);

            // Add the main menu
            if (Pat.getApplicationState().getMode().isShowMenu()) {
                // TODO replace menuPanel with new dimPanel
//                menuPanel = new MainMenu();
//                mainPanel.add(menuPanel, new BorderLayoutData(Region.EAST, 200, 10, 250, true));

                rootPanel.add(mainPanel, new BoxLayoutData(FillStyle.BOTH));
            }

            if (Pat.getApplicationState().getMode().isShowWelcomePanel()) {
                MainTabPanel.displayContentWidget(new WelcomePanel(ConstantFactory.getInstance().welcome()));
            }
        }
        else {
            mainPanel.add(new OlapTable());
            rootPanel.add(mainPanel, new BoxLayoutData(FillStyle.BOTH));
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


    // TODO remove if not needed
    /**
     * Gets the main panel.
     *
     * @return the bottom panel
     */
    public static LayoutPanel getMainPanel() {
        return mainPanel;
    }


}
