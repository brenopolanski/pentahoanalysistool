/**
 * 
 */
package org.pentaho.pat.client.ui;

import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.hibernate.jdbc.ConnectionManager;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.ui.panels.ToolBarPanel;
import org.pentaho.pat.client.ui.widgets.ConnectMondrianPanel;
import org.pentaho.pat.client.ui.widgets.ConnectXmlaPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author pstoellberger
 *
 */
public class ConnectionWindow extends WindowPanel implements SourcesConnectionEvents,ConnectionListener {
	
	private static final String HEIGHT = "300";
	private static final String WIDTH = "700";
	private static final String TITLE = ConstantFactory.getInstance().register_new_connection();
	private final ConnectMondrianPanel connectMondrian;
	private final ConnectXmlaPanel connectXmla;
	private boolean connectionEstablished = false;
	private ConnectionListenerCollection connectionListeners;
	
	
	public ConnectionWindow() {
		super();
		this.setTitle(TITLE);
		
		
		connectMondrian = new ConnectMondrianPanel();
		connectXmla = new ConnectXmlaPanel();
		
		this.setWidget(onInitialize());
	}	
		
	  protected Widget onInitialize() {
	    // Create a layout panel to align the widgets
	    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());
	    layoutPanel.setHeight(HEIGHT);
	    layoutPanel.setWidth(WIDTH);
	    final TabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
	    tabPanel.setPadding(5);
	    
	    tabPanel.add(connectMondrian,ConstantFactory.getInstance().mondrian());
	    tabPanel.add(connectXmla,ConstantFactory.getInstance().xmla());
	    tabPanel.selectTab(0);
	    connectXmla.addConnectionListener(ConnectionWindow.this);
		connectMondrian.addConnectionListener(ConnectionWindow.this);
	    
	    layoutPanel.add(tabPanel);
	    
	    return layoutPanel;
	  }

	  public boolean isConnectionEstablished() {
			return connectionEstablished;
		}

		public void setConnectionEstablished(boolean connectionEstablished) {
			this.connectionEstablished = connectionEstablished;
		}

		public void onConnectionBroken(Widget sender) {
			setConnectionEstablished(false);
			connectionListeners.fireConnectionBroken(ConnectionWindow.this);
		}

		public void onConnectionMade(Widget sender) {
			setConnectionEstablished(true);
			connectionListeners.fireConnectionMade(ConnectionWindow.this);
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
		
		public void emptyForms() {
			connectMondrian.emptyForm();
			connectXmla.emptyForm();
		}

}
