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
import org.pentaho.pat.client.util.ConnectionFactory;
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
import com.gwtext.client.widgets.ToolbarButton;


/**
 * @author Tom Barber
 *
 */
public class ToolBarPanel extends ToolBar implements ClickListener,ConnectionListener,SourcesConnectionEvents  {

	ConnectPanel connectWindow;
	ToolbarButton fileToolbarMenuButton;
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
		
		
		/*final BaseItemListenerAdapter listener = new BaseItemListenerAdapter() {
			public void onClick(BaseItem connect, EventObject e) {
				
				connectWindow = new ConnectPanel();
				// ADD WHEN IMPLEMENTED: connectWindow.addConnectionListener(dimensionPanel);
				// ADD WHEN IMPLEMENTED: connectWindow.addConnectionListener(cBar);
				connectWindow.addConnectionListener(ToolBarPanel.this);
				
				
				
				connectWindow.show();
					}
				};
			*/
		
		//Item connect = new Item(MessageFactory.getInstance().connect());
		

		//Menu submenu = new Menu();
		//submenu.addItem(connect);
		//connect.addListener(listener);
		

		/*fileToolbarMenuButton = new ToolbarButton(MessageFactory.getInstance().connect());
		
		fileToolbarMenuButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				if (button.getText().equals(
						MessageFactory.getInstance().connect())) {
					connectWindow = new ConnectPanel();
					// ADD WHEN IMPLEMENTED: connectWindow.addConnectionListener(dimensionPanel);
					// ADD WHEN IMPLEMENTED: connectWindow.addConnectionListener(cBar);
					connectWindow.addConnectionListener(ToolBarPanel.this);
					connectWindow.show();
					//connect(ConnectionFactory.getInstance().connection_string());

				} else if (button.getText().equals(
						MessageFactory.getInstance().disconnect())) {
					connectionListeners.fireConnectionBroken(ToolBarPanel.this);
					fileToolbarMenuButton.setText(MessageFactory.getInstance().connect());
				}
			}
		});
		this.setAutoWidth(true);
		this.addButton(fileToolbarMenuButton);
*/
	}
	 public void onClick(Widget sender) {
		    final Button btn = (Button) sender;
		    
		    InfoPanel.show(btn.getHTML(), "Clicked!");
		  }
	 
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		menuBtnMenu.addItem(connectItem);
		menuBtnMenu.removeItem(disconnectItem);
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		//connectionListeners.fireConnectionMade(ToolBarPanel.this);
		//fileToolbarMenuButton.setText(MessageFactory.getInstance().disconnect());
		//menuBtnMenu.addItem(MessageFactory.getInstance().disconnect(), cmd1);
		menuBtnMenu.addItem(disconnectItem);
		menuBtnMenu.removeItem(connectItem);
	}
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
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pentaho.pat.client.listeners.SourcesConnectionEvents#removeClickListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	public void removeClickListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}
}
