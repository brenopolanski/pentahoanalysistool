/*
 * Copyright 2009 Thomas Barber.  All rights reserved. 
 * This software was developed by Thomas Barber and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * Analysis Tool.  The Initial Developer is Thomas Barber.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * PAT Toolbar.
 *
 * @author Tom Barber
 */
public class ToolBarPanel extends MenuBar implements ClickListener, ConnectionListener {

    /**
     * 
     * @author tom(at)wamonline.org.uk
     *
     */
    private static final class ThemeMenuItemCommand implements Command {
	    private String theme;
	        public ThemeMenuItemCommand(String element2) {
	    	this.theme = element2;
	        }

	        public void execute() {
	    	Pat.CUR_THEME = this.theme;
	    	// Load the new style sheets
	    	Pat.updateStyleSheets();
	        }

	    }

    /**
     * 
     * @author tom(at)wamonline.org.uk
     *
     */
	    private final static class ThemeMenuItem extends MenuItem {
	        private String theme;
	        public ThemeMenuItem(String element2, Command command){
	    	super(element2, command);
	    	this.theme = element2;
	        }

	        public String getTheme(){
	    	return theme;
	        }
	    }


	/** The Connection Dialog. */
	private ConnectionWindow connectWindow;

	/** Connection Established. */
	private boolean connectionEstablished = false;

	/** Connect Item. */
	private MenuItem connectItem;

	/**
	 * Constructor.
	 */
	public ToolBarPanel() {
		super();

		init();
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
						connectWindow.addConnectionListener(ToolBarPanel.this);
					}
					connectWindow.emptyForms();
					connectWindow.showModal();
					// connectWindow.show();
				} else {
					ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), new AsyncCallback<Object>() {
						public void onFailure(final Throwable arg0) {
							MessageBox.error(ConstantFactory.getInstance().error(), arg0.getLocalizedMessage());
						}

						public void onSuccess(final Object o) {
							setConnectionEstablished(false);
							GlobalConnectionFactory.getInstance().connectionListeners.fireConnectionBroken(ToolBarPanel.this);
						}
					});
				}
			}
		});

		// Add connect button
		fileMenuBar.addItem(connectItem);

		// Add File menu to Toolbar
		this.addItem(new MenuItem(ConstantFactory.getInstance().file(), fileMenuBar));

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
		this.addItem(new MenuItem("Help", helpMenu)); //$NON-NLS-1$
	}

	/**
	 * TODO JAVADOC.
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
		viewMenu.addItem(ConstantFactory.getInstance().theme(), styleSheetMenu); //$NON-NLS-1$

		this.addItem(new MenuItem("View", viewMenu)); //$NON-NLS-1$
	}

	/**
	 * TODO JAVADOC.
	 */
	public final void init() {
		GlobalConnectionFactory.getInstance().addConnectionListener(ToolBarPanel.this);
		createFileMenu();
		createViewMenu();
		createHelpMenu();

	}

	/**
	 * Checks connection status.
	 *
	 * @return true, if checks if is connection established
	 */
	private final boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
	 */
	/**
	 * Fires on click
	 * @param sender the sender
	 */
	public void onClick(final Widget sender) {

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