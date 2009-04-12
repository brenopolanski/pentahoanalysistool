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
public class ToolBarPanel extends MenuBar implements ClickListener,
ConnectionListener, SourcesConnectionEvents {
	private ConnectionWindow connectWindow;
	private boolean connectionEstablished = false;
	private MenuItem connectItem;
	private ConnectionListenerCollection connectionListeners;


	public ToolBarPanel() {
		super();

		init();
	}

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
				//setDown(true);

			}
			allButtons.add(this);
		}
		
		public String getTheme() {
			return theme;
		}



	}


	public void init() {
		createFileMenu();
		createViewMenu();
		createHelpMenu();



	}

	private void createFileMenu(){

		MenuBar fileMenuBar = new MenuBar(true);
		fileMenuBar.setAnimationEnabled(true);
		fileMenuBar.ensureDebugId("patMenuButton-normal"); //$NON-NLS-1$

		// Create Toolbar Menu Items

		connectItem = new MenuItem(ConstantFactory.getInstance().connect(), new Command(){ 
			public void execute() {
				if (!connectionEstablished){
					if (connectWindow == null) {
						connectWindow = new ConnectionWindow();
						connectWindow.addConnectionListener(ToolBarPanel.this);
					}
					connectWindow.emptyForms();
					connectWindow.showModal();
				}
				else{
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

	private void createViewMenu(){
		MenuBar viewMenu = new MenuBar(true);
		viewMenu.setAnimationEnabled(true);
		MenuBar styleSheetMenu = new MenuBar(true);


		ConstantFactory.getInstance();
		for (int i = 0; i < PatConstants.STYLE_THEMES.length; i++) {
			ConstantFactory.getInstance();
			final ThemeMenu button = new ThemeMenu(
					PatConstants.STYLE_THEMES[i], new Command(){

						public void execute() {

							// Update the current theme
							//		    	   	 Pat.CUR_THEME = button.getTheme();

							// Load the new style sheets
							Pat.updateStyleSheets();
						}});

			styleSheetMenu.addItem(button);
		}
		viewMenu.addItem("Blah", styleSheetMenu); //$NON-NLS-1$

		this.addItem(new MenuItem("View",viewMenu)); //$NON-NLS-1$
	}

	private void createHelpMenu(){

		MenuBar helpMenu = new MenuBar(true);
		helpMenu.setAnimationEnabled(true);

		// Create Toolbar Menu Items

		MenuItem homeItem = new MenuItem(ConstantFactory.getInstance().mainLinkPat(),
				new Command()
		{
			public void execute(){
				System.out.print(ConstantFactory.getInstance().pat_homepage());
			}

		});

		MenuItem pentahoItem = new MenuItem(
				ConstantFactory.getInstance().mainLinkHomepage(),new Command()
				{
					public void execute(){
						System.out.print(ConstantFactory.getInstance().pentaho_homepage());
					}

				});

		// Add connect button
		helpMenu.addItem(homeItem);
		helpMenu.addItem(pentahoItem);
		this.addItem(new MenuItem("Help", helpMenu)); //$NON-NLS-1$
	}
	// Inherited on click method
	public void onClick(Widget sender) {

	}

	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	public void onConnectionBroken(Widget sender) {
		setConnectionEstablished(false);
		connectionListeners.fireConnectionBroken(ToolBarPanel.this);
		// Alter menu
		connectItem.setText("Connect"); //$NON-NLS-1$
	}

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
	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}
}
