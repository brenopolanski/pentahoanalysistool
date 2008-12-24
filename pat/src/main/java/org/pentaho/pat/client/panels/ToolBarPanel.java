/**
 * 
 */
package org.pentaho.pat.client.panels;

import org.pentaho.pat.client.util.MessageFactory;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListener;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;
import com.gwtext.client.core.EventObject;

import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.panels.ConnectPanel;


/**
 * @author Tom Barber
 *
 */
public class ToolBarPanel extends Toolbar implements ConnectionListener  {

	ConnectPanel connectWindow;
	
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
				connectWindow.show();
					}
				};
			
		
		Item connect = new Item(MessageFactory.getInstance().connect());
		connect.addListener(listener);

		Menu submenu = new Menu();
		submenu.addItem(connect);
		connect.addListener(listener);
		

		final ToolbarMenuButton fileToolbarMenuButton = new ToolbarMenuButton(
				MessageFactory.getInstance().file());
		fileToolbarMenuButton.setMenu(submenu);
		this.setAutoWidth(true);
		this.addButton(fileToolbarMenuButton);

	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}
}
