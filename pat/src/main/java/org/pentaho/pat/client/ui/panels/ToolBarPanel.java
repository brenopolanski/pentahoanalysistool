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
import org.pentaho.pat.client.util.GlobalConnectionListeners;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.anotherbigidea.flash.movie.Button;
import com.google.gwt.user.client.Command;
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
	
    private static final class ThemeMenuItemCommand implements Command {
	    private String theme;
	        public ThemeMenuItemCommand(String element2) {
	    	// TODO Auto-generated constructor stub
	    	this.theme = element2;
	        }

	        public void execute() {
	    	// TODO Auto-generated method stub
	    	Pat.CUR_THEME = this.theme;
	    	// Load the new style sheets
	    	Pat.updateStyleSheets();
	        }

	    }

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
			final ThemeMenuItem button = new ThemeMenuItem(element2, new ThemeMenuItemCommand(element2));
			
			styleSheetMenu.addItem(button);
		}
		viewMenu.addItem("Blah", styleSheetMenu); //$NON-NLS-1$

		this.addItem(new MenuItem("View", viewMenu)); //$NON-NLS-1$
	}

	/**
	 * TODO JAVADOC.
	 */
	public void init() {
		GlobalConnectionFactory.getInstance().addConnectionListener(ToolBarPanel.this);
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