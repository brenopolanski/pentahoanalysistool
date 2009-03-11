/**
 * 
 */
package org.pentaho.pat.client.panels;

import com.google.gwt.user.client.Command;

import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.pentaho.pat.client.util.ConstantFactory;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.panels.ConnectPanel;

/**
 * PAT Toolbar
 * 
 * @author Tom Barber
 * 
 */
public class ToolBarPanel extends ToolBar implements ClickListener,
ConnectionListener, SourcesConnectionEvents {
	private PopupMenu fileBtnMenu = new PopupMenu();
	private ConnectPanel connectWindow;
	private boolean connectionEstablished = false;
	
	private MenuItem connectItem;
	private MenuItem disconnectItem;
	private ConnectionListenerCollection connectionListeners;

	public ToolBarPanel() {
		super();

		init();
	}

	// Execute command when connect button is pressed
	Command connectWindowCmd = new Command() {
		public void execute() {
			if (connectWindow == null) {
				connectWindow = new ConnectPanel();
				connectWindow.addConnectionListener(ToolBarPanel.this);
			}

			connectWindow.showModal();

		}
	};

	// Execute command when disconnect button is pressed
	Command disconnectCmd = new Command() {
		public void execute() {
			//connectWindow.disconnect();
		}
	};

	public void init() {
		createFileMenu();
		createHelpMenu();
		


	}
	
	private void createFileMenu(){
		
		ToolButton fileMenuButton = new ToolButton("File");
		fileMenuButton.setStyle(ToolButtonStyle.MENU);
		fileMenuButton.addClickListener(this);
		fileMenuButton.ensureDebugId("mosaicMenuButton-normal");

		// Create Toolbar Menu Items
		connectItem = new MenuItem(ConstantFactory.getInstance().connect(),
				connectWindowCmd);
		disconnectItem = new MenuItem(
				ConstantFactory.getInstance().disconnect(), disconnectCmd);

		// Add connect button
		fileBtnMenu.addItem(connectItem);

		// Add File menu to Toolbar
		fileMenuButton.setMenu(fileBtnMenu);
		this.add(fileMenuButton);
	}
	private void createHelpMenu(){
		PopupMenu helpBtnMenu = new PopupMenu();
		ToolButton helpMenuButton = new ToolButton("Help");
		helpMenuButton.setStyle(ToolButtonStyle.MENU);
		helpMenuButton.addClickListener(this);
		helpMenuButton.ensureDebugId("mosaicMenuButton-normal");

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
		helpBtnMenu.addItem(homeItem);
		helpBtnMenu.addItem(pentahoItem);
		// Add File menu to Toolbar
		helpMenuButton.setMenu(helpBtnMenu);
		this.add(helpMenuButton);
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
		fileBtnMenu.addItem(connectItem);
		fileBtnMenu.removeItem(disconnectItem);

	}

	public void onConnectionMade(Widget sender) {
		setConnectionEstablished(true);
		connectionListeners.fireConnectionMade(ToolBarPanel.this);
		// Alter menu
		fileBtnMenu.addItem(disconnectItem);
		fileBtnMenu.removeItem(connectItem);
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
