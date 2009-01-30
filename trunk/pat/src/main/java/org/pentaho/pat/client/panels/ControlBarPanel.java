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

		
		cubeListBox.setLabel("Cube List");
		
		

		this.addField(cubeListBox);
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

	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;

	}

	
	private void emptyStore(){
			store.removeAll();
			store.commitChanges();
			cubeListBox.setValue(null);
		
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
		setConnectionEstablished(false);
		connectionListeners.fireConnectionBroken(ControlBarPanel.this);
		emptyStore();
		cubeListBox.setDisabled(true);
		
		

		
	}

	public void onConnectionMade(Widget sender) {
		setConnectionEstablished(true);
		connectionListeners.fireConnectionMade(ControlBarPanel.this);
		cubeListBox.setDisabled(false);
		
		
	}


}
