package com.mycompany.project.client.panels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;
import com.mycompany.project.client.events.SourcesConnectionEvents;
import com.mycompany.project.client.listeners.ConnectionListener;
import com.mycompany.project.client.listeners.ConnectionListenerCollection;
import com.mycompany.project.client.util.GuidFactory;
import com.mycompany.project.client.util.MessageFactory;
import com.mycompany.project.client.util.ServiceFactory;

public class ConnectPanel extends Window implements SourcesConnectionEvents {
	FormPanel formPanel;
	TextArea connectionText;
	TextField serverText;
	TextField portText;
	TextField usernameText;
	TextField passwordText;
	TextField cubeText;
	TextField databaseText;
	Button connectBtn;

	static String queryTypeGroup = "QUERY_TYPE"; //$NON-NLS-1$
	boolean connectionEstablished = false;
	ConnectionListenerCollection connectionListeners;

	public ConnectPanel() {
		super();

		init();
	}

	private void init() {
		this.setTitle("Connect");
		this.setMaximizable(false);
		this.setResizable(true);
		this.setLayout(new FitLayout());
		this.setWidth(500);
		this.setModal(false);
		this.setShadow(true);
		FlexTable flex = new FlexTable();
		Panel panel = new FormPanel();

		serverText = new TextField();
		serverText.setLabel("Server");
		panel.add(serverText);

		portText = new TextField();
		portText.setLabel("Port");
		panel.add(portText);

		usernameText = new TextField();
		usernameText.setLabel("Username");
		panel.add(usernameText);

		passwordText = new TextField();
		passwordText.setLabel("Password");
		panel.add(passwordText);

		databaseText = new TextField();
		databaseText.setLabel("Database");
		panel.add(databaseText);

		cubeText = new TextField();
		cubeText.setLabel("Schema");
		cubeText.setInputType("file");
		panel.add(cubeText);

		connectBtn = new Button(MessageFactory.getInstance().connect());
		connectBtn.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (connectBtn.getText().equals(
						MessageFactory.getInstance().connect())) {
					String cStr = "jdbc:mondrian:Jdbc=jdbc:mysql://"
							+ getServerText() + ":" + getPortText() + "/"
							+ getDatabaseText() + "?user=" + getUsernameText()
							+ "&password=" + getPasswordText() + ";Catalog="
							+ getCubeText() + ";";
					connect(cStr);

				} else if (connectBtn.getText().equals(
						MessageFactory.getInstance().disconnect())) {
					disconnect();
				}
			}
		});

		flex.setWidget(0, 1, connectBtn);
		this.add(flex);

		flex.setWidget(0, 0, panel);

	}

	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	public void connect(String connectionStr) {
		if (!isConnectionEstablished()) {
			ServiceFactory.getInstance().connect(connectionStr,
					GuidFactory.getGuid(), new AsyncCallback() {
						public void onSuccess(Object result) {
							Boolean booleanResult = (Boolean) result;
							if (booleanResult.booleanValue()) {
								setConnectionEstablished(true);
								connectionListeners
										.fireConnectionMade(ConnectPanel.this);

							} else {
								setConnectionEstablished(false);
								connectionListeners
										.fireConnectionBroken(ConnectPanel.this);
							}
							connectBtn
									.setText(isConnectionEstablished() ? MessageFactory
											.getInstance().disconnect()
											: MessageFactory.getInstance()
													.connect());
							destroy();
						}

						public void onFailure(Throwable caught) {
							/*
							 * Window.alert(MessageFactory.getInstance().
							 * no_connection_param
							 * (caught.getLocalizedMessage()));
							 * setConnectionEstablished(false);
							 */
							connectBtn
									.setText(isConnectionEstablished() ? MessageFactory
											.getInstance().disconnect()
											: MessageFactory.getInstance()
													.connect());
						}
					});
		}
	}

	public void disconnect() {
		if (isConnectionEstablished()) {
			ServiceFactory.getInstance().disconnect(GuidFactory.getGuid(),
					new AsyncCallback() {
						public void onFailure(Throwable caught) {
							// Window.alert(MessageFactory.getInstance().
							// no_connection_param
							// (caught.getLocalizedMessage()));
							setConnectionEstablished(false);
							connectionListeners
									.fireConnectionBroken(ConnectPanel.this);
							connectBtn
									.setText(isConnectionEstablished() ? MessageFactory
											.getInstance().disconnect()
											: MessageFactory.getInstance()
													.connect());
						}

						public void onSuccess(Object result) {
							setConnectionEstablished(false);
							connectionListeners
									.fireConnectionBroken(ConnectPanel.this);
							connectBtn
									.setText(isConnectionEstablished() ? MessageFactory
											.getInstance().disconnect()
											: MessageFactory.getInstance()
													.connect());
						}
					});
		}
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
	public void removeClickListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}

	/**
	 * @return the serverText
	 */
	private String getServerText() {
		return serverText.getText();
	}

	/**
	 * @return the portText
	 */
	private String getPortText() {
		return portText.getText();
	}

	/**
	 * @return the usernameText
	 */
	private String getUsernameText() {
		return usernameText.getText();
	}

	/**
	 * @return the passwordText
	 */
	private String getPasswordText() {
		return passwordText.getText();
	}

	/**
	 * @return the cubeText
	 */
	private String getCubeText() {
		return cubeText.getText();
	}

	/**
	 * @return the cubeText
	 */
	private String getDatabaseText() {
		return databaseText.getText();
	}

	/**
	 * @param serverText
	 *            the serverText to set
	 */
	private void setServerText(String serverText) {
		this.serverText.setValue(serverText);
	}

	/**
	 * @param portText
	 *            the portText to set
	 */
	private void setPortText(String portText) {
		this.portText.setValue(portText);
	}

	/**
	 * @param usernameText
	 *            the usernameText to set
	 */
	private void setUsernameText(String usernameText) {
		this.usernameText.setValue(usernameText);
	}

	/**
	 * @param passwordText
	 *            the passwordText to set
	 */
	private void setPasswordText(String passwordText) {
		this.passwordText.setValue(passwordText);
	}

	/**
	 * @param cubeText
	 *            the cubeText to set
	 */
	private void setCubeText(String cubeText) {
		this.cubeText.setValue(cubeText);
	}

	/**
	 * @param cubeText
	 *            the cubeText to set
	 */
	private void setDatabaseText(String databaseText) {
		this.databaseText.setValue(databaseText);
	}
}
