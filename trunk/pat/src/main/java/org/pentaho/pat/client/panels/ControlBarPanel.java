package org.pentaho.pat.client.panels;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.util.ConnectionFactory;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class ControlBarPanel extends Toolbar implements ConnectionListener,SourcesConnectionEvents {
	/*
	 * TODO The Control Bar will amonst other things display the currently
	 * selected cube for user and allow other functionality as the project
	 * progresses
	 */
	private Store store;
	private ComboBox cubeListBox;
	private ToolbarButton conButton;
	private ConnectionListenerCollection connectionListeners;
	private static RecordDef recordDef;
	private MemoryProxy proxy;
	private ArrayReader reader;
	boolean connectionEstablished = false;

	public ControlBarPanel() {
		super();

		init();
	}

	private void init() {
		cubeListBox = new ComboBox();
		cubeListBox.setFieldLabel(MessageFactory.getInstance().cube());
		populateCubeList();
		cubeListBox.setStore(store);
		cubeListBox.setDisplayField("name");
		cubeListBox.setDisabled(true);
		cubeListBox.setMode(ComboBox.LOCAL);
		cubeListBox.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onExpand(ComboBox comboBox) {
				if (isConnectionEstablished())
					getCubes(cubeListBox.getText());
			}

			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				ServiceFactory.getInstance().setCube(
						comboBox.getValueAsString(),
						GuidFactory.getGuid(), new AsyncCallback() {
							public void onSuccess(Object result) {
								setCube(cubeListBox.getText());
							}

							public void onFailure(Throwable caught) {
							}
						});
			}

		});

		conButton = new ToolbarButton("Connect");
		cubeListBox.setLabel("Cube List");
		conButton.disable();
		conButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {

				if (button.getText().equals(
						MessageFactory.getInstance().connect())) {

					//connect(ConnectionFactory.getInstance().connection_string());

				} else if (button.getText().equals(
						MessageFactory.getInstance().disconnect())) {
					disconnect();

				}
			}
		});

		this.addField(cubeListBox);
		this.addButton(conButton);

	}

	public void getCubes(final String boxText) {

		ServiceFactory.getInstance().getCubes(GuidFactory.getGuid(),
				new AsyncCallback() {
			public void onSuccess(Object result1) {
				if (result1 != null) {
					store.removeAll();
					store.commitChanges();
					String[][] cubeNames = (String[][]) result1;
					for (int i = 0; i < cubeNames.length; i++) {
						store.add(recordDef.createRecord(cubeNames[i]));
					}
					store.commitChanges();
				}
				ServiceFactory.getInstance().setCube(
						cubeListBox.getText(), GuidFactory.getGuid(),
						new AsyncCallback() {
							public void onSuccess(Object result2) {
								// populateDimensions();
							}

							public void onFailure(Throwable caught) {
							}
						});
			}

			public void onFailure(Throwable caught) {
			}
		});

	}

	public void setCube(String boxText) {
		ServiceFactory.getInstance().setCube(boxText, GuidFactory.getGuid(),
				new AsyncCallback() {
			public void onSuccess(Object result) {
				// TODO Populate the Dimensions Dialog
				DimensionPanel.populateDimensions();
			}

			public void onFailure(Throwable caught) {
			}
		});

	}

	public static String[][] getCubeData() {
		String[][] list = { new String[] { "0", "No Cubes" } };
		return list;
	}

	public void populateCubeList() {
		proxy = new MemoryProxy(getCubeData());
		recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("number"), new StringFieldDef("name") });
		reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();

	}

	/*
	 * Uncomment this lot when connect button goes away....
	 */
	/*
	 * public void onConnectionBroken(Widget sender) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onConnectionMade(Widget sender) {
	 * getCubes(cubeListBox.getText()); }
	 */

	/*
	 * Remove Below When We Don't Need The Connect Button Any More
	 */
	public void connect(String connectionStr) {
		if (!isConnectionEstablished()) {
			ServiceFactory.getInstance().connect(connectionStr,
					GuidFactory.getGuid(), new AsyncCallback() {
				public void onSuccess(Object result) {
					Boolean booleanResult = (Boolean) result;
					if (booleanResult.booleanValue()) {
						setConnectionEstablished(true);
						connectionListeners
						.fireConnectionMade(ControlBarPanel.this);
					} else {
						setConnectionEstablished(false);
						connectionListeners
						.fireConnectionBroken(ControlBarPanel.this);
					}
					conButton
					.setText(isConnectionEstablished() ? MessageFactory
							.getInstance().disconnect()
							: MessageFactory.getInstance()
							.connect());
					cubeListBox.setDisabled(!isConnectionEstablished());
				}

				public void onFailure(Throwable caught) {
					System.out.println(caught.getLocalizedMessage());
					Window.alert(MessageFactory.getInstance()
							.no_connection_param(
									caught.getLocalizedMessage()));
					setConnectionEstablished(false);
					conButton
					.setText((isConnectionEstablished() ? MessageFactory
							.getInstance().disconnect()
							: MessageFactory.getInstance()
							.connect()));
					cubeListBox.setDisabled(!isConnectionEstablished());
				}
			});

		}

	}

	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;

	}

	public void disconnect() {
		if (isConnectionEstablished()) {
			ServiceFactory.getInstance().disconnect(GuidFactory.getGuid(),
					new AsyncCallback<Object>() {
				public void onFailure(Throwable caught) {
					Window.alert(MessageFactory.getInstance()
							.no_connection_param(
									caught.getLocalizedMessage()));
					setConnectionEstablished(false);
					connectionListeners
					.fireConnectionBroken(ControlBarPanel.this);
					conButton
					.setText((isConnectionEstablished() ? MessageFactory
							.getInstance().disconnect()
							: MessageFactory.getInstance()
							.connect()));
					cubeListBox.setDisabled(!isConnectionEstablished());
				}

				public void onSuccess(Object result) {
					setConnectionEstablished(false);
					connectionListeners
					.fireConnectionBroken(ControlBarPanel.this);
					conButton
					.setText((isConnectionEstablished() ? MessageFactory
							.getInstance().disconnect()
							: MessageFactory.getInstance()
							.connect()));
					cubeListBox.setDisabled(!isConnectionEstablished());
					conButton.disable();
				}
			});
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.pat.client.listeners.SourcesConnectionEvents#
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
	 * @see
	 * org.pentaho.pat.client.listeners.SourcesConnectionEvents#removeClickListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	public void removeClickListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}
	
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		setConnectionEstablished(true);
		connectionListeners
		.fireConnectionMade(ControlBarPanel.this);
		conButton.enable();
		conButton
		.setText(isConnectionEstablished() ? MessageFactory
				.getInstance().disconnect()
				: MessageFactory.getInstance()
				.connect());
		cubeListBox.setDisabled(!isConnectionEstablished());
	}


}
