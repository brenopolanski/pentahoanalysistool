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

	private ConnectPanel connectWindow;
	private boolean connectionEstablished = false;
	private PopupMenu menuBtnMenu = new PopupMenu();
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
		// Create Toolbar Menu
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
		menuBtnMenu.addItem(connectItem);

		// Add File menu to Toolbar
		fileMenuButton.setMenu(menuBtnMenu);
		this.add(fileMenuButton);

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
		menuBtnMenu.addItem(connectItem);
		menuBtnMenu.removeItem(disconnectItem);

	}

	public void onConnectionMade(Widget sender) {
		setConnectionEstablished(true);
		connectionListeners.fireConnectionMade(ToolBarPanel.this);
		// Alter menu
		menuBtnMenu.addItem(disconnectItem);
		menuBtnMenu.removeItem(connectItem);
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
