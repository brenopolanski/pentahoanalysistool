/**
 * 
 */
package org.pentaho.pat.client.ui.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Command;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.ConnectPanel;
import org.pentaho.pat.client.ui.widgets.ConnectMondrianPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;

/**
 * PAT Toolbar
 * 
 * @author Tom Barber
 * 
 */
public class ToolBarPanel extends MenuBar implements ClickListener,
ConnectionListener, SourcesConnectionEvents {
	private ConnectMondrianPanel connectWindow;
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
		      addStyleName("sc-ThemeButton-" + theme);
		      
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
		fileMenuBar.ensureDebugId("mosaicMenuButton-normal");

		// Create Toolbar Menu Items
		
		connectItem = new MenuItem(ConstantFactory.getInstance().connect(), new Command(){ 
			public void execute() {
				if (!connectionEstablished){
				if (connectWindow == null) {
					connectWindow = new ConnectMondrianPanel();
					connectWindow.addConnectionListener(ToolBarPanel.this);
				}
				connectWindow.showModal();
				}
				else{
					
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
	

		 for (int i = 0; i < ConstantFactory.getInstance().STYLE_THEMES.length; i++) {
		      final ThemeMenu button = new ThemeMenu(
		          ConstantFactory.getInstance().STYLE_THEMES[i], new Command(){
		        	  
		        	  public void execute() {
		    	  
			          // Update the current theme
//		    	   	 Pat.CUR_THEME = button.getTheme();

			          // Load the new style sheets
			         Pat.updateStyleSheets();
		       }});
		      
		     styleSheetMenu.addItem(button);
		    }
		 viewMenu.addItem("Blah", styleSheetMenu);
		 
		 this.addItem(new MenuItem("View",viewMenu));
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
		this.addItem(new MenuItem("Help", helpMenu));
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
		connectItem.setText("Connect");
	}

	public void onConnectionMade(Widget sender) {
		setConnectionEstablished(true);
		connectionListeners.fireConnectionMade(ToolBarPanel.this);
		// Alter menu
	
		connectItem.setText("Disconnect");
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
