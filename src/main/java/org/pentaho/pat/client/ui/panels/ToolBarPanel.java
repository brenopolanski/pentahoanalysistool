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

package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.i18n.PatConstants;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.ConnectionWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * PAT Toolbar.
 *
 * @author tom(at)wamonline.org.uk
 */
public class ToolBarPanel extends Composite implements ClickHandler, ConnectionListener {

	/**
	 * 
	 * @author tom(at)wamonline.org.uk
	 *
	 */
	private final static class ThemeMenuItem extends MenuItem {
		private final String theme;
		public ThemeMenuItem(final String element2, final Command command){
			super(element2, command);
			this.theme = element2;
		}

		public String getTheme(){
			return theme;
		}
	}

	private static final class ThemeMenuItemCommand implements Command {
		private final String theme;
		public ThemeMenuItemCommand(final String element2) {
			this.theme = element2;
		}

		public void execute() {
			Pat.CUR_THEME = this.theme;
			// Load the new style sheets
			Pat.updateStyleSheets();
		}

	}

	private final MenuBar menuBar;


	/** The Connection Dialog. */
	public static ConnectionWindow connectWindow;

	/** Connection Established. */
	private boolean connectionEstablished = false;

	/** Connect Item. */
	private MenuItem connectItem;

	/**
	 * Constructor.
	 */
	public ToolBarPanel() {
		super();
		menuBar= new MenuBar();
		//LayoutPanel baseLayoutPanel = getLayoutPanel();
		initWidget(menuBar);
		init();
		//baseLayoutPanel.add(menuBar);
		//this.setHeight("100px");
		//this.invalidate();
		//this.layout();
	}


	/**
	 * Create the file menu.
	 */
	private void createFileMenu() {

		final MenuBar fileMenuBar = new MenuBar(true);
		fileMenuBar.setAnimationEnabled(true);
		fileMenuBar.ensureDebugId("patMenuButton-normal"); //$NON-NLS-1$

		// Create Toolbar Menu Items

		connectItem = new MenuItem(ConstantFactory.getInstance().connect(), new Command() {
			public void execute() {
				if (!connectionEstablished) {
					if (connectWindow == null) {
						connectWindow = new ConnectionWindow();
						
					}
					connectWindow.emptyForms();
					connectWindow.showModal(true);
				} else {
					ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), new AsyncCallback<Object>() {
						public void onFailure(final Throwable arg0) {
							MessageBox.error(ConstantFactory.getInstance().error(), arg0.getLocalizedMessage());
						}

						public void onSuccess(final Object o) {
							setConnectionEstablished(false);
							GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionBroken(ToolBarPanel.this);
						}
					});
				}
			}
		});

		// Add connect button
		fileMenuBar.addItem(connectItem);

		// Add File menu to Toolbar
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().file(), fileMenuBar));

	}

	/**
	 * Create the help menu.
	 */
	private void createHelpMenu() {

		final MenuBar helpMenu = new MenuBar(true);
		helpMenu.setAnimationEnabled(true);

		// Create Toolbar Menu Items

		final MenuItem homeItem = new MenuItem(ConstantFactory.getInstance().mainLinkPat(), new Command() {
			public void execute() {

				Window.open("http://code.google.com/p/pentahoanalysistool/", "_blank", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			}

		});

		final MenuItem pentahoItem = new MenuItem(ConstantFactory.getInstance().mainLinkHomepage(), new Command() {
			public void execute() {

				Window.open("http://www.pentaho.com", "_blank", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			}

		});

		// Add connect button
		helpMenu.addItem(homeItem);
		helpMenu.addItem(pentahoItem);
		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().help(), helpMenu)); //$NON-NLS-1$
	}

	/**
	 * Create the view Menu.
	 */
	private void createViewMenu() {
		final MenuBar viewMenu = new MenuBar(true);
		viewMenu.setAnimationEnabled(true);
		final MenuBar styleSheetMenu = new MenuBar(true);

		ConstantFactory.getInstance();
		for (final String element2 : PatConstants.STYLE_THEMES) {
			ConstantFactory.getInstance();
			final ThemeMenuItem button = new ThemeMenuItem(element2, new ThemeMenuItemCommand(element2));

			styleSheetMenu.addItem(button);
		}
		viewMenu.addItem(ConstantFactory.getInstance().theme(), styleSheetMenu);

		menuBar.addItem(new MenuItem(ConstantFactory.getInstance().view(), viewMenu)); //$NON-NLS-1$
	}

	/**
	 * Initialize the panel.
	 */
	public final void init() {
		GlobalConnectionFactory.getInstance().addConnectionListener(ToolBarPanel.this);
		createFileMenu();
		createViewMenu();
		createHelpMenu();

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires on click
	 * @param event the event
	 */
	public void onClick(final ClickEvent event) {

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when the database connection is broken.
	 * @param sender the sender
	 */
	public void onConnectionBroken(final Widget sender) {
		setConnectionEstablished(false);
		// Alter menu
		connectItem.setText("Connect"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires when a database connection is established.
	 * @param sender the sender
	 */
	public void onConnectionMade(final Widget sender) {
		setConnectionEstablished(true);
		// Alter menu

		connectItem.setText("Disconnect"); //$NON-NLS-1$
	}

	/**
	 * Sets the connection status
	 * 
	 * @param connectionEstablished the connection established
	 */
	private final void setConnectionEstablished(final boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}



}