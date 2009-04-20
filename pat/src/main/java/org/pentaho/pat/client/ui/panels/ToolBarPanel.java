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

/**
 * PAT Toolbar
 * 
 * @author Tom Barber
 * 
 */
public class ToolBarPanel extends MenuBar implements ClickListener, ConnectionListener, SourcesConnectionEvents {
	/**
	 *TODO JAVADOC
	 */
	private ConnectionWindow connectWindow;
	/**
	 *TODO JAVADOC
	 */
	private boolean connectionEstablished = false;
	/**
	 *TODO JAVADOC
	 */
	private MenuItem connectItem;
	/**
	 *TODO JAVADOC
	 */
	private ConnectionListenerCollection connectionListeners;

	/**
	 *TODO JAVADOC
	 *
	 */
	public ToolBarPanel() {
		super();

		init();
	}

	/**
	 *TODO JAVADOC
	 *
	 * @author bugg
	 *
	 */
	private static final class ThemeMenu extends MenuItem {
		private static List<ThemeMenu> allButtons = null;

		private String theme;

		public ThemeMenu(String theme, Command cmd) {
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

		public String getTheme() {
			return theme;
		}

	}

	/**
	 *TODO JAVADOC
	 *
	 */
	public void init() {
		createFileMenu();
		createViewMenu();
		createHelpMenu();

	}

	/**
	 *TODO JAVADOC
	 *
	 */
	private void createFileMenu() {

		MenuBar fileMenuBar = new MenuBar(true);
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
						public void onSuccess(Object o) {
							setConnectionEstablished(false);
							ToolBarPanel.this.onConnectionBroken(ToolBarPanel.this);
						}

						public void onFailure(Throwable arg0) {
							MessageBox.error(ConstantFactory.getInstance().error(), arg0.getLocalizedMessage());
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
	 *TODO JAVADOC
	 *
	 */
	private void createViewMenu() {
		MenuBar viewMenu = new MenuBar(true);
		viewMenu.setAnimationEnabled(true);
		MenuBar styleSheetMenu = new MenuBar(true);

		ConstantFactory.getInstance();
		for (int i = 0; i < PatConstants.STYLE_THEMES.length; i++) {
			ConstantFactory.getInstance();
			final ThemeMenu button = new ThemeMenu(PatConstants.STYLE_THEMES[i], new Command() {

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
	 *TODO JAVADOC
	 *
	 */
	private void createHelpMenu() {

		MenuBar helpMenu = new MenuBar(true);
		helpMenu.setAnimationEnabled(true);

		// Create Toolbar Menu Items

		MenuItem homeItem = new MenuItem(ConstantFactory.getInstance().mainLinkPat(), new Command() {
			public void execute() {
			//	System.out.print(ConstantFactory.getInstance().pathomepage());
				//TODO Webpage
			}

		});

		MenuItem pentahoItem = new MenuItem(ConstantFactory.getInstance().mainLinkHomepage(), new Command() {
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

	// Inherited on click method
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
	 */
	public void onClick(Widget sender) {

	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param connectionEstablished
	 */
	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(Widget sender) {
		setConnectionEstablished(false);
		connectionListeners.fireConnectionBroken(ToolBarPanel.this);
		// Alter menu
		connectItem.setText("Connect"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(Widget sender) {
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
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#addConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void addConnectionListener(ConnectionListener listener) {
		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);
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
	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}
}
