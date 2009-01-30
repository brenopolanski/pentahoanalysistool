package org.pentaho.pat.client.panels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
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


import com.gwtext.client.data.SimpleStore;  
import com.gwtext.client.data.Store;  

public class ConnectPanel extends Window implements SourcesConnectionEvents {
	FormPanel formPanel;
	TextField connectionText;
	TextField serverText;
	TextField portText;
	TextField usernameText;
	TextField passwordText;
	TextField cubeText;
	TextField usernameText2;
	TextField passwordText2;
	TextField cubeText2;
	TextField databaseText;
	Button connectBtn2;
	Button connectBtn;
	ComboBox supportedDriverCombo;
	ComboBox DriverCombo;
	Label debuglabel;
	
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
		supportedJDBC.setHeight(250);
		

		genericJDBC = new Panel();  
		genericJDBC.setAutoScroll(true);  
		genericJDBC.setTitle("Generic JDBC");  
		genericJDBC.setIconCls("tab-icon");  
		genericJDBC.setHeight(250);

		xmla = new Panel();  
		xmla.setAutoScroll(true);  
		xmla.setTitle("XMLA");  
		xmla.setIconCls("tab-icon");  

		this.setWidth(500);
		this.setShadow(true);
		Panel fpanel1= new FormPanel();
		fpanel1.setHeight(250);
		fpanel1.setPaddings(5);
		
		supportedDriverCombo = new ComboBox("Select a DB Type");
		supportedDriverCombo.setLabel("Driver");
		Object[][] drivers = 	new Object[][]{  
			                 	new Object[]{"org.hsqldb.jdbcDriver", "HSQLDB"},  
			                 	new Object[]{"com.mysql.jdbc.Driver", "MySQL"},  
			                 	  
				};  
		final Store driversStore = new SimpleStore(new String[]{"driver", "name"}, drivers);  
		driversStore.load();  
		supportedDriverCombo.setStore(driversStore);
		supportedDriverCombo.setDisplayField("name");  
		supportedDriverCombo.setMode(ComboBox.LOCAL);  
		supportedDriverCombo.setTriggerAction(ComboBox.ALL);  
		supportedDriverCombo.setForceSelection(true);  
		supportedDriverCombo.setValueField("driver");  
		supportedDriverCombo.setReadOnly(true);  
		fpanel1.add(supportedDriverCombo);
		
		
		serverText = new TextField();
		serverText.setLabel("Server");
		serverText.setAllowBlank(false);
		fpanel1.add(serverText);

		portText = new TextField();
		portText.setLabel("Port");
		serverText.setAllowBlank(true);
		fpanel1.add(portText);

		databaseText = new TextField();
		databaseText.setLabel("Database");
		databaseText.setAllowBlank(false);
		fpanel1.add(databaseText);
		
		usernameText = new TextField();
		usernameText.setLabel("Username");
		usernameText.setAllowBlank(false);
		fpanel1.add(usernameText);

		passwordText = new TextField();
		passwordText.setInputType( "password" );
		passwordText.setLabel("Password");
		passwordText.setAllowBlank(true);
		fpanel1.add(passwordText);

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
					String cStr = "jdbc:mondrian:Jdbc=";
							
							if (supportedDriverCombo.getValueAsString().equals("com.mysql.jdbc.Driver")) {
							//jdbc:mysql://localhost:3306/sampledata
							cStr = cStr + "jdbc:mysql://" + serverText.getText() + ":" + portText.getText() + "/" + databaseText.getText();
							cStr = cStr + "?user=" + usernameText.getText() 
							+ "&password=" + passwordText.getText();
							}
							if (supportedDriverCombo.getValueAsString().equals("org.hsqldb.jdbcDriver")) {
								//jdbc:hsqldb:hsql://localhost:9001/hibernate
								cStr = cStr + "jdbc:hsqldb:hsql://" + serverText.getText() + ":" + portText.getText() + "/" + databaseText.getText();
								cStr = cStr + ";username=" + usernameText.getText() 
								+ ";password=" + passwordText.getText(); 
									
							}
							
							cStr = cStr + ";Catalog="  + cubeText.getText() 
							+ ";JdbcDrivers=" + supportedDriverCombo.getValueAsString();

					connect(cStr);
					//debuglabel.setText(cStr);

				} else if (connectBtn.getText().equals(
						MessageFactory.getInstance().disconnect())) {
					disconnect();
				}
			}
		});
		fpanel1.add(connectBtn);
		//debuglabel = new Label("Example: HSQLDB/Server:localhost/Port:9001/DB:sampledata/username:hibuser/password:password");
		//fpanel1.add(debuglabel);
		
		
		supportedJDBC.add(fpanel1);
		
		Panel fpanel2= new FormPanel();
		fpanel2.setHeight(200);
		fpanel2.setPaddings(5);
		
		DriverCombo = new ComboBox("Select a Driver");
		DriverCombo.setLabel("Driver");
		Object[][] drivers2 = 	new Object[][]{  
			                 	new Object[]{"org.hsqldb.jdbcDriver", "HSQLDB"},  
			                 	new Object[]{"com.mysql.jdbc.Driver", "MySQL"},  
			                 	  
				};  
		final Store driversStore2 = new SimpleStore(new String[]{"driver", "name"}, drivers2);  
		driversStore2.load();  
		DriverCombo.setStore(driversStore2);
		DriverCombo.setDisplayField("name");  
		DriverCombo.setMode(ComboBox.LOCAL);  
		DriverCombo.setTriggerAction(ComboBox.ALL);  
		DriverCombo.setForceSelection(true);  
		DriverCombo.setValueField("driver");  
		DriverCombo.setReadOnly(true);  
		
		fpanel2.add(DriverCombo);
		
		connectionText = new TextField();
		connectionText.setLabel("ConnectString");
		fpanel2.add(connectionText);

		usernameText2 = new TextField();
		usernameText2.setLabel("Username");
		fpanel2.add(usernameText2);

		passwordText2 = new TextField();
		passwordText2.setLabel("Password");
		fpanel2.add(passwordText2);

		cubeText2 = new TextField();
		cubeText2.setLabel("Schema");
		cubeText2.setInputType("file");
		fpanel2.add(cubeText2);
		
		connectBtn2 = new Button(MessageFactory.getInstance().connect());
		connectBtn2.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (connectBtn2.getText().equals(
						MessageFactory.getInstance().connect())) {
					String cStr = "jdbc:mondrian:Jdbc="
							+ connectionText.getText();
							if (DriverCombo.getValueAsString().equals("com.mysql.jdbc.Driver")) {
								
							cStr = cStr + "?user=" + usernameText2.getText() 
							+ "&password=" + passwordText2.getText();
							}
							if (DriverCombo.getValueAsString().equals("org.hsqldb.jdbcDriver")) {
								cStr = cStr + ";username=" + usernameText2.getText() 
								+ ";password=" + passwordText2.getText(); 
									
							}
							
							cStr = cStr + ";Catalog="  + cubeText2.getText() 
							+ ";JdbcDrivers=" + DriverCombo.getValueAsString();

					connect(cStr);
					//debuglabel.setText(cStr);

				} else if (connectBtn2.getText().equals(
						MessageFactory.getInstance().disconnect())) {
					disconnect();
				}
			}
		});
		fpanel2.add(connectBtn2);
		//debuglabel = new Label("empty");
		//fpanel2.add(debuglabel);
		genericJDBC.add(fpanel2);
		
		tabs.add(supportedJDBC);
		//tabs.add(genericJDBC);
		//tabs.add(xmla);
		tabs.activate(0);
		this.add(tabs);
		this.setPaddings(10);
		


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
					ConnectPanel.this.close();
					
					
				}

				public void onFailure(Throwable caught) {
					System.out.println(caught.getLocalizedMessage());
					com.google.gwt.user.client.Window.alert(MessageFactory.getInstance()
							.no_connection_param(
									caught.getLocalizedMessage()));
					setConnectionEstablished(false);
					connectBtn
					.setText((isConnectionEstablished() ? MessageFactory
							.getInstance().disconnect()
							: MessageFactory.getInstance()
							.connect()));
					
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
