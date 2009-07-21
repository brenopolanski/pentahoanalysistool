/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009
 * @author Tom Barber
 */

package org.pentaho.pat.client;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.panels.MainMenu;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.panels.WelcomePanel;
import org.pentaho.pat.client.ui.widgets.OlapTable;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
 * @author tom(at)wamonline.org.uk
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




	/** The Application Main Panel. */
	private static MainMenu menuPanel = null;


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
		if (Pat.getInitialState().getMode().isShowOnlyTable() == false) {
			
	
			mainTabPanel = new MainTabPanel();
			mainPanel.add(mainTabPanel);
			
			// Add the main menu
			if (Pat.getInitialState().getMode().isShowMenu()) {
				final ImageButton collapseBtn = new ImageButton(Caption.IMAGES.toolCollapseLeft());
				final CaptionLayoutPanel menuWrapperPanel = new CaptionLayoutPanel();
				menuWrapperPanel.getHeader().add(collapseBtn, CaptionRegion.RIGHT);
		
				menuPanel = new MainMenu();
				menuWrapperPanel.add(menuPanel);
		
				collapseBtn.addClickHandler(new ClickHandler() {
					public void onClick(final ClickEvent event) {
						mainPanel.setCollapsed(menuWrapperPanel, true);
						mainPanel.layout();
					}
				});
		
				mainPanel.add(menuWrapperPanel, new BorderLayoutData(Region.WEST, 200, 10, 250, true));
				rootPanel.add(mainPanel, new BoxLayoutData(FillStyle.BOTH));
			}
			
			
			
			if (Pat.getInitialState().getMode().isShowWelcomePanel()) {
				MainTabPanel.displayContentWidget(new WelcomePanel(ConstantFactory.getInstance().welcome()));
			}
			
			
		}
		else {
			mainPanel.add(new OlapTable());
			
			rootPanel.add(mainPanel, new BoxLayoutData(FillStyle.BOTH));
			
		}
	}

	public static MainMenu getMenuPanel() {
		return menuPanel;
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
	// TODO is it ok to cast to LayoutPanel here? Yes
	@Override
	protected final LayoutPanel getWidget() {
		return (LayoutPanel)super.getWidget();
	}

	public static MainTabPanel getMainTabPanel() {
		return mainTabPanel;
	}


	/**
	 * Gets the bottom panel.
	 *
	 * @return the bottom panel
	 */
	public static LayoutPanel getMainPanel() {
		return mainPanel;
	}


}
