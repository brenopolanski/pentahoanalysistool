/**
 * 
 */
package org.pentaho.pat.client.ui;

import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.ui.widgets.ConnectMondrianPanel;
import org.pentaho.pat.client.ui.widgets.ConnectXmlaPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author pstoellberger
 *
 */
public class ConnectionWindow extends WindowPanel implements SourcesConnectionEvents,ConnectionListener {

	private static final String HEIGHT = "400px"; //$NON-NLS-1$
	private static final String WIDTH = "800px"; //$NON-NLS-1$
	private static final String TITLE = ConstantFactory.getInstance().register_new_connection();
	private final ConnectMondrianPanel connectMondrian;
	private final ConnectXmlaPanel connectXmla;
	private boolean connectionEstablished = false;
	private ConnectionListenerCollection connectionListeners;
	final TabLayoutPanel tabPanel= new TabLayoutPanel();

	public ConnectionWindow() {
		super(TITLE);
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);


		connectMondrian = new ConnectMondrianPanel();
		connectXmla = new ConnectXmlaPanel();
		this.add(onInitialize());	
	}	

	@Override
	protected void onLoad() {
		tabPanel.selectTab(0);
		super.onLoad();
	} 

	protected LayoutPanel onInitialize() {
		LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
				Orientation.VERTICAL));

		tabPanel.setPadding(5);	    
		tabPanel.add(connectMondrian,ConstantFactory.getInstance().mondrian());
		tabPanel.add(connectXmla,ConstantFactory.getInstance().xmla());
		connectMondrian.addConnectionListener(ConnectionWindow.this);
		connectXmla.addConnectionListener(ConnectionWindow.this);
		
		tabPanel.addTabListener(new TabListener() {
			public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) 
			{
				return true;
			}

			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				// FormLayout.getPreferredSize() needs to be improved so that pack()
				// works like expected. But you can try it.
				//           pack();
			}
		});
		layoutPanel.add(tabPanel, new BoxLayoutData(FillStyle.BOTH)); 
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
		ConnectionWindow.this.hide();
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
