/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.Iterator;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.WindowPanel;
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
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author pstoellberger
 *
 */

public class ConnectXmlaPanel extends LayoutPanel implements
SourcesConnectionEvents {

	// TODO Finish this Widget

	private static final String HEIGHT = "300";
	private static final String WIDTH = "700";
	private static final String TITLE = ConstantFactory.getInstance().register_new_xmla_connection();
	private static final String LABEL_SUFFIX = ":";
	private static final String LABEL_REQUIRED_SUFFIX = "*";

	private final TextBox urlTextBox;
	// private final TextBox catalogTextBox;
	private final TextBox userTextBox;
	private final PasswordTextBox passwordTextBox;
	private final Button connectButton;
	private boolean connectionEstablished = false;
	private ConnectionListenerCollection connectionListeners;

	public ConnectXmlaPanel() {
		super();
		this.setTitle(TITLE);
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		connectButton = new Button(ConstantFactory.getInstance().connect());
		// catalogTextBox = new TextBox();
		urlTextBox = new TextBox();
		userTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();

		this.add(onInitialize());
	}


	private Widget onInitialize() {

		final FormPanel formPanel;
		formPanel = new FormPanel();

		final FormLayout layout = new FormLayout(
				"right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
				+ "right:[40dlu,pref], 3dlu, 70dlu",
		"12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");

		final PanelBuilder builder = new PanelBuilder(layout);

		builder.addLabel(ConstantFactory.getInstance().xmla_url() + LABEL_SUFFIX, CellConstraints.xy(1, 4));
		builder.add(urlTextBox, CellConstraints.xyw(3, 4, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 6));
		builder.add(userTextBox, CellConstraints.xy(3, 6));
		builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 6));
		builder.add(passwordTextBox, CellConstraints.xy(7, 6));
		// builder.addLabel(ConstantFactory.getInstance().catalog() + LABEL_SUFFIX, CellConstraints.xy(1, 8));
		// builder.add(catalogTextBox, CellConstraints.xyw(3,8,5));

		connectButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
					ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new AsyncCallback<Object>() {
					public void onSuccess(Object o) {
						MessageBox.info(ConstantFactory.getInstance().success(),MessageFactory.getInstance().connection_established());
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

		builder.add(connectButton, CellConstraints.xyw(3,12,5));

		formPanel.add(builder.getPanel());
		return formPanel;
	}

	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	private CubeConnection getCubeConnection() {
		final CubeConnection cc = new CubeConnection(ConnectionType.XMLA);
		cc.setUrl(urlTextBox.getText());
		if(userTextBox.getText() != null && userTextBox.getText().length() > 0) {
			cc.setUsername(userTextBox.getText());
		}
		else {
			cc.setUsername(null);
		}
		if(passwordTextBox.getText() != null && passwordTextBox.getText().length() > 0) {
			cc.setPassword(passwordTextBox.getText());	
		}
		else {
			cc.setPassword(null);
		}
		//if(catalogTextBox.getText() != null && catalogTextBox.getText().length() > 0) {
		//	cc.setCatalog(catalogTextBox.getText());	
		//}
		//else {
		//	cc.setCatalog(null);
		//}

		return cc;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * addConnectionListener
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
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * removeClickListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}
	
	public void emptyForm() {
		urlTextBox.setText("");
		userTextBox.setText("");
		passwordTextBox.setText("");
		//catalogTextbox.setText("");
	}


	
}
