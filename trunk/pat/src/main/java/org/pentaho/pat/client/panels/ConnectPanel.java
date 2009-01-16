package org.pentaho.pat.client.panels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;  
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ComboBox;  
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.TabPanel;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;

public class ConnectPanel extends Window implements SourcesConnectionEvents {
	FormPanel formPanel;
	TextField connectionText;
	TextField serverText;
	TextField portText;
	TextField usernameText;
	TextField passwordText;
	TextField cubeText;
	TextField databaseText;
	Button connectBtn;
	ComboBox supportedDriverCombo;
	
	private TabPanel tabs;
	private Panel supportedJDBC;
	private Panel genericJDBC;
	private Panel xmla;
	


	static String queryTypeGroup = "QUERY_TYPE"; //$NON-NLS-1$
	boolean connectionEstablished = false;
	ConnectionListenerCollection connectionListeners;

	public ConnectPanel() {
		super();

		init();
	}

	private void init() {
		this.setTitle("Register new Mondrian Connection");
		
		tabs = new TabPanel();
		tabs.setTabPosition(Position.TOP);  
		tabs.setPaddings(10);
		
		supportedJDBC = new Panel();  
		supportedJDBC.setAutoScroll(true);  
		supportedJDBC.setTitle("Supported JDBC");  
		supportedJDBC.setIconCls("tab-icon");  
		//supportedJDBC.setLayout(new FitLayout());
		

		genericJDBC = new Panel();  
		genericJDBC.setAutoScroll(true);  
		genericJDBC.setTitle("Generic JDBC");  
		genericJDBC.setIconCls("tab-icon");  

		xmla = new Panel();  
		xmla.setAutoScroll(true);  
		xmla.setTitle("XMLA");  
		xmla.setIconCls("tab-icon");  

		this.setWidth(500);
		this.setShadow(true);
		Panel fpanel1= new FormPanel();
		fpanel1.setPaddings(5);
		
		supportedDriverCombo = new ComboBox("Select a DB Type");
		supportedDriverCombo.setLabel("Driver");
		fpanel1.add(supportedDriverCombo);
		
		serverText = new TextField();
		serverText.setLabel("Server");
		fpanel1.add(serverText);

		portText = new TextField();
		portText.setLabel("Port");
		
		fpanel1.add(portText);

		usernameText = new TextField();
		usernameText.setLabel("Username");
		fpanel1.add(usernameText);

		passwordText = new TextField();
		passwordText.setLabel("Password");
		fpanel1.add(passwordText);

		databaseText = new TextField();
		databaseText.setLabel("Database");
		fpanel1.add(databaseText);

		cubeText = new TextField();
		cubeText.setLabel("Schema");
		cubeText.setInputType("file");
		fpanel1.add(cubeText);

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
		
		supportedJDBC.add(fpanel1);
		
		Panel fpanel2= new FormPanel();
		fpanel2.setPaddings(5);
		
		supportedDriverCombo = new ComboBox("Select a Driver");
		supportedDriverCombo.setLabel("Driver");
		fpanel2.add(supportedDriverCombo);
		
		connectionText = new TextField();
		connectionText.setLabel("Connection String");
		fpanel2.add(connectionText);

		usernameText = new TextField();
		usernameText.setLabel("Username");
		fpanel2.add(usernameText);

		passwordText = new TextField();
		passwordText.setLabel("Password");
		fpanel2.add(passwordText);

		cubeText = new TextField();
		cubeText.setLabel("Schema");
		cubeText.setInputType("file");
		fpanel2.add(cubeText);
		
		genericJDBC.add(fpanel2);
		
		tabs.add(supportedJDBC);
		tabs.add(genericJDBC);
		tabs.add(xmla);
		tabs.activate(0);
		this.add(tabs);
		this.setPaddings(10);
		this.add(connectBtn);

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
