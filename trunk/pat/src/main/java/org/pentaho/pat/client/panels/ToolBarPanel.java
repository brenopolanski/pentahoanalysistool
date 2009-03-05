/**
 * 
 */
package org.pentaho.pat.client.panels;
import com.google.gwt.user.client.Command;


import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;


import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.panels.ConnectPanel;


/**
 * @author Tom Barber
 *
 */
public class ToolBarPanel extends ToolBar implements ClickListener,ConnectionListener,SourcesConnectionEvents  {

	ConnectPanel connectWindow;

	boolean connectionEstablished = false;
	PopupMenu menuBtnMenu = new PopupMenu();
	MenuItem connectItem;
	MenuItem disconnectItem;
	private ConnectionListenerCollection connectionListeners;
	
	public ToolBarPanel(){
		super();
		
		init();
	}
	
	// Make a command that we will execute from all menu items.
    Command connectWindowCmd = new Command() {
      public void execute() {
    	  if (connectWindow == null) {
	          connectWindow = new ConnectPanel();
	          connectWindow.addConnectionListener(ToolBarPanel.this);
	          
	        }
	        connectWindow.showModal();
        
      }
    };
    
    Command disconnectCmd = new Command() {
        public void execute() {
      	 //connectWindow.disconnect(); 
        	connectWindow.disconnect();
        }
      };

	public void init(){
	    // Add a menu button
		  // Add a menu button
	    ToolButton menuButton = new ToolButton("File");
	    menuButton.setStyle(ToolButtonStyle.MENU);
	    menuButton.addClickListener(this);
	    menuButton.ensureDebugId("mosaicMenuButton-normal");
	    
	    
		connectItem = new MenuItem(MessageFactory.getInstance().connect(), connectWindowCmd);
		disconnectItem = new MenuItem(MessageFactory.getInstance().disconnect(), disconnectCmd);
		menuBtnMenu.addItem(connectItem);
		
		
		menuButton.setMenu(menuBtnMenu);
		this.add(menuButton);
	
	}
	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}
	
	
	 public void onClick(Widget sender) {
		    final Button btn = (Button) sender;
		    
		    InfoPanel.show(btn.getHTML(), "Clicked!");
		  }
	 
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		setConnectionEstablished(false);
		connectionListeners.fireConnectionBroken(ToolBarPanel.this);
		menuBtnMenu.addItem(connectItem);
		menuBtnMenu.removeItem(disconnectItem);
		
	}

	public void onConnectionMade(Widget sender) {
		setConnectionEstablished(true);
		connectionListeners.fireConnectionMade(ToolBarPanel.this);
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
