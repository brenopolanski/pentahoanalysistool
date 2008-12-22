/**
 * 
 */
package org.pentaho.pat.client.panels;

import org.pentaho.pat.client.util.MessageFactory;

import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.core.EventObject;

/**
 * @author Tom Barber
 *
 */
public class ToolBarPanel extends Toolbar {

	public ToolBarPanel(){
		super();
		
		init();
	}
	
	public void init(){
		final BaseItemListenerAdapter listener = new BaseItemListenerAdapter() {
			public void onClick(BaseItem connect, EventObject e) {
				
			}
		};
		Item connect = new Item(MessageFactory.getInstance().connect());
		connect.addListener(listener);

		Menu submenu = new Menu();
		submenu.addItem(connect);

		final ToolbarMenuButton fileToolbarMenuButton = new ToolbarMenuButton(
				MessageFactory.getInstance().file());
		fileToolbarMenuButton.setMenu(submenu);
		this.setAutoWidth(true);
		this.addButton(fileToolbarMenuButton);

	}
}
