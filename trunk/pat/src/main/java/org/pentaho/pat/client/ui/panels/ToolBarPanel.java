/*
 * 
 */
package org.pentaho.pat.client.ui.panels;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.i18n.PatConstants;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.ui.ConnectionWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * PAT Toolbar.
 * 
 * @author Tom Barber
 */
public class ToolBarPanel extends MenuBar implements ClickListener, ConnectionListener, SourcesConnectionEvents {
	
	/**
	 * TODO JAVADOC.
	 * 
	 * @author bugg
	 */
	private static final class ThemeMenu extends MenuItem {
		
		/** The all buttons. */
		private static List<ThemeMenu> allButtons = null;

		/** The theme. */
		private final String theme;

		/**
		 * Instantiates a new theme menu.
		 * 
		 * @param theme the theme
		 * @param cmd the cmd
		 */
		public ThemeMenu(final String theme, final Command cmd) {
			super(theme, cmd);
			this.theme = theme;
			addStyleName("sc-ThemeButton-" + theme); //$NON-NLS-1$

			// Add this button to the static list
			if (allButtons == null) {
				allButtons = new ArrayList<ThemeMenu>();
				// setDown(true);

			}
			allButtons.add(this);
		}

		/**
		 * Gets the theme.
		 * 
		 * @return the theme
		 */
		public String getTheme() {
			return theme;
		}

	}
	
	/** TODO JAVADOC. */
	private ConnectionWindow connectWindow;
	
	/** TODO JAVADOC. */
	private boolean connectionEstablished = false;
	
	/** TODO JAVADOC. */
	private MenuItem connectItem;

	/** TODO JAVADOC. */
	private ConnectionListenerCollection connectionListeners;

	/**
	 * TODO JAVADOC.
	 */
	public ToolBarPanel() {
		super();

		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pentaho.pat.client.listeners.SourcesConnectionEvents#removeClickListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#addConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void addConnectionListener(final ConnectionListener listener) {
		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);
	}

	/**
	 * TODO JAVADOC.
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
							ToolBarPanel.this.onConnectionBroken(ToolBarPanel.this);
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
	 * TODO JAVADOC.
	 */
	private void createHelpMenu() {

		final MenuBar helpMenu = new MenuBar(true);
		helpMenu.setAnimationEnabled(true);

		// Create Toolbar Menu Items

		final MenuItem homeItem = new MenuItem(ConstantFactory.getInstance().mainLinkPat(), new Command() {
			public void execute() {
				//	System.out.print(ConstantFactory.getInstance().pathomepage());
				//TODO Webpage
			}

		});

		final MenuItem pentahoItem = new MenuItem(ConstantFactory.getInstance().mainLinkHomepage(), new Command() {
			public void execute() {
				//	System.out.print(ConstantFactory.getInstance().pentahohomepage());
				//TODO Webpage
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
			final ThemeMenu button = new ThemeMenu(element2, new Command() {

				public void execute() {

					// Update the current theme
					// Pat.CUR_THEME = button.getTheme();

					// Load the new style sheets
					Pat.updateStyleSheets();
				}
			});

			styleSheetMenu.addItem(button);
		}
		viewMenu.addItem("Blah", styleSheetMenu); //$NON-NLS-1$

		this.addItem(new MenuItem("View", viewMenu)); //$NON-NLS-1$
	}

	/**
	 * TODO JAVADOC.
	 */
	public void init() {
		createFileMenu();
		createViewMenu();
		createHelpMenu();

	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @return true, if checks if is connection established
	 */
	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	// Inherited on click method
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
	 */
	public void onClick(final Widget sender) {

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(final Widget sender) {
		setConnectionEstablished(false);
		connectionListeners.fireConnectionBroken(ToolBarPanel.this);
		// Alter menu
		connectItem.setText("Connect"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(final Widget sender) {
		setConnectionEstablished(true);
		// Alter menu

		connectItem.setText("Disconnect"); //$NON-NLS-1$

		connectionListeners.fireConnectionMade(ToolBarPanel.this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pentaho.pat.client.listeners.SourcesConnectionEvents#removeClickListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#removeConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void removeConnectionListener(final ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @param connectionEstablished the connection established
	 */
	public void setConnectionEstablished(final boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}
}
