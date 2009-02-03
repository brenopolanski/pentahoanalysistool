/**
 * 
 */
package org.pentaho.pat.client.panels;

import org.pentaho.pat.client.util.ConnectionFactory;
import org.pentaho.pat.client.util.MessageFactory;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListener;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.core.EventObject;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.panels.ConnectPanel;
import com.gwtext.client.widgets.ToolbarButton;


/**
 * @author Tom Barber
 *
 */
public class ToolBarPanel extends Toolbar implements ConnectionListener,SourcesConnectionEvents  {

	ConnectPanel connectWindow;
	ToolbarButton fileToolbarMenuButton;
	boolean connectionEstablished = false;

	
	private ConnectionListenerCollection connectionListeners;
	
	public ToolBarPanel(){
		super();
		
		init();
	}
	
	public void init(){
		final BaseItemListenerAdapter listener = new BaseItemListenerAdapter() {
			public void onClick(BaseItem connect, EventObject e) {
				
				connectWindow = new ConnectPanel();
				// ADD WHEN IMPLEMENTED: connectWindow.addConnectionListener(dimensionPanel);
				// ADD WHEN IMPLEMENTED: connectWindow.addConnectionListener(cBar);
				connectWindow.addConnectionListener(ToolBarPanel.this);
				
				
				
				connectWindow.show();
					}
				};
			
		
		//Item connect = new Item(MessageFactory.getInstance().connect());
		

		//Menu submenu = new Menu();
		//submenu.addItem(connect);
		//connect.addListener(listener);
		

		fileToolbarMenuButton = new ToolbarButton(MessageFactory.getInstance().connect());
		
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

	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		connectionListeners.fireConnectionMade(ToolBarPanel.this);
		fileToolbarMenuButton.setText(MessageFactory.getInstance().disconnect());
		
		
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
