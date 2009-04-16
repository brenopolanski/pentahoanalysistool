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
public class ConnectionWindow extends WindowPanel implements SourcesConnectionEvents, ConnectionListener {

	/**
	 *TODO JAVADOC
	 */
	private static final String HEIGHT = "330px"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String WIDTH = "660px"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String TITLE = ConstantFactory.getInstance().register_new_connection();
	/**
	 *TODO JAVADOC
	 */
	private final ConnectMondrianPanel connectMondrian;
	/**
	 *TODO JAVADOC
	 */
	private final ConnectXmlaPanel connectXmla;
	/**
	 *TODO JAVADOC
	 */
	private boolean connectionEstablished = false;
	/**
	 *TODO JAVADOC
	 */
	private ConnectionListenerCollection connectionListeners;
	/**
	 *TODO JAVADOC
	 */
	final TabLayoutPanel tabPanel = new TabLayoutPanel();

	/**
	 *TODO JAVADOC
	 *
	 */
	public ConnectionWindow() {
		super(TITLE);
		this.setHeight(HEIGHT);
		this.setWidth(WIDTH);

		connectMondrian = new ConnectMondrianPanel();
		connectXmla = new ConnectXmlaPanel();
		this.add(onInitialize());
	}

	/* (non-Javadoc)
	 * @see org.gwt.mosaic.ui.client.WindowPanel#onLoad()
	 */
	@Override
	protected void onLoad() {
		tabPanel.selectTab(0);
		super.onLoad();
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	protected LayoutPanel onInitialize() {
		LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

		tabPanel.setPadding(5);
		tabPanel.add(connectMondrian, ConstantFactory.getInstance().mondrian());
		tabPanel.add(connectXmla, ConstantFactory.getInstance().xmla());
		connectMondrian.addConnectionListener(ConnectionWindow.this);
		connectXmla.addConnectionListener(ConnectionWindow.this);

		tabPanel.addTabListener(new TabListener() {
			public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
				return true;
			}

			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				// FormLayout.getPreferredSize() needs to be improved so that
				// pack()
				// works like expected. But you can try it.
				// pack();
			}
		});
		layoutPanel.add(tabPanel, new BoxLayoutData(FillStyle.BOTH));
		return layoutPanel;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param connectionEstablished
	 */
	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(Widget sender) {
		setConnectionEstablished(false);
		connectionListeners.fireConnectionBroken(ConnectionWindow.this);
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
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
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#addConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
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
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#removeConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}

	/**
	 *TODO JAVADOC
	 *
	 */
	public void emptyForms() {
		connectMondrian.emptyForm();
		connectXmla.emptyForm();
	}

}
