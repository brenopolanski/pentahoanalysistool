/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.beans.CubeConnection;
import org.pentaho.pat.rpc.beans.CubeConnection.ConnectionType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author pstoellberger
 * 
 */

public class ConnectXmlaPanel extends LayoutPanel implements SourcesConnectionEvents {

	// TODO Finish this Widget

	/**
	 *TODO JAVADOC
	 */
	private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String HEIGHT = "280px"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String WIDTH = "620px"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private final TextBox urlTextBox;
	// private final TextBox catalogTextBox;
	/**
	 *TODO JAVADOC
	 */
	private final TextBox userTextBox;
	/**
	 *TODO JAVADOC
	 */
	private final PasswordTextBox passwordTextBox;
	/**
	 *TODO JAVADOC
	 */
	private final Button connectButton;
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
	 *
	 */
	public ConnectXmlaPanel() {
		super();
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		connectButton = new Button(ConstantFactory.getInstance().connect());
		// catalogTextBox = new TextBox();
		urlTextBox = new TextBox();
		userTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		this.setLayout(new BorderLayout());
		onInitialize();
		// this.add(onInitialize());
	}

	/**
	 *TODO JAVADOC
	 *
	 */
	private void onInitialize() {

		// final FormPanel formPanel = new FormPanel();
		// formPanel.setWidth(WIDTH);
		// formPanel.setHeight(HEIGHT);

		final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
				+ "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
				// "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
				"p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
		final PanelBuilder builder = new PanelBuilder(layout);

		builder.addLabel(ConstantFactory.getInstance().xmla_url() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
		builder.add(urlTextBox, CellConstraints.xyw(3, 1, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
		builder.add(userTextBox, CellConstraints.xy(3, 3));
		builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 3));
		builder.add(passwordTextBox, CellConstraints.xy(7, 3));
		// builder.addLabel(ConstantFactory.getInstance().catalog() +
		// LABEL_SUFFIX, CellConstraints.xy(1, 8));
		// builder.add(catalogTextBox, CellConstraints.xyw(3,8,5));

		connectButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new AsyncCallback<Object>() {
					public void onSuccess(Object o) {
						MessageBox.info(ConstantFactory.getInstance().success(), ConstantFactory.getInstance().connection_established());
						setConnectionEstablished(true);
						connectionListeners.fireConnectionMade(ConnectXmlaPanel.this);
					}

					public void onFailure(Throwable arg0) {
						MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().no_connection_param(arg0.getLocalizedMessage()));
						connectButton.setEnabled(true);
					}
				});
			}
		});

		builder.add(connectButton, CellConstraints.xyw(3, 9, 5));

		LayoutPanel layoutPanel = builder.getPanel();
		layoutPanel.setPadding(15);
		this.add(layoutPanel);
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

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	private CubeConnection getCubeConnection() {
		final CubeConnection cc = new CubeConnection(ConnectionType.XMLA);
		cc.setUrl(urlTextBox.getText());
		if (userTextBox.getText() != null && userTextBox.getText().length() > 0) {
			cc.setUsername(userTextBox.getText());
		} else {
			cc.setUsername(null);
		}
		if (passwordTextBox.getText() != null && passwordTextBox.getText().length() > 0) {
			cc.setPassword(passwordTextBox.getText());
		} else {
			cc.setPassword(null);
		}
		// if(catalogTextBox.getText() != null &&
		// catalogTextBox.getText().length() > 0) {
		// cc.setCatalog(catalogTextBox.getText());
		// }
		// else {
		// cc.setCatalog(null);
		// }

		return cc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * addConnectionListener
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
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * removeClickListener
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
	public void emptyForm() {
		urlTextBox.setText(""); //$NON-NLS-1$
		userTextBox.setText(""); //$NON-NLS-1$
		passwordTextBox.setText(""); //$NON-NLS-1$
		// catalogTextbox.setText("");
	}

}
