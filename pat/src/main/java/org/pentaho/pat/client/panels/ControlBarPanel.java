package org.pentaho.pat.client.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

public class ControlBarPanel extends HLayout implements ConnectionListener,SourcesConnectionEvents {
	/*
	 * TODO The Control Bar will amonst other things display the currently
	 * selected cube for user and allow other functionality as the project
	 * progresses
	 */

	private ComboBoxItem cubeListBox;
	private ConnectionListenerCollection connectionListeners;
	boolean connectionEstablished = false;
	ArrayList departments;
	public ControlBarPanel() {
		super();

		init();
	}

	private void init() {
		final DynamicForm form = new DynamicForm();  
		cubeListBox = new ComboBoxItem();
		cubeListBox.setName("cubeListBox");
		departments = new ArrayList();  
		populateCubeList();
		
		cubeListBox.addChangeHandler(new ChangeHandler() { 
			public void onChange(ChangeEvent event) {
				//if (isConnectionEstablished())getCubes();
				 //form.getField("cubeListBox").setValueMap(departments);
				ServiceFactory.getInstance().setCube(
						(String)cubeListBox.getValue(),
						GuidFactory.getGuid(), new AsyncCallback() {
							public void onSuccess(Object result) {
								setCube((String)cubeListBox.getValue());
							}

							public void onFailure(Throwable caught) {
							}
						});
			}
		});

		form.setFields(cubeListBox);
		this.addMember(form);
	}

	public void getCubes() {

		ServiceFactory.getInstance().getCubes(GuidFactory.getGuid(),
				new AsyncCallback() {
			public void onSuccess(Object result1) {
				if (result1 != null) {
					
				
					
					String[][] cubeNames = (String[][]) result1;
					for (int i = 0; i < cubeNames.length; i++) {
						
						cubeListBox.setValueMap(cubeNames[i]);
					}
					
				}
				/*ServiceFactory.getInstance().setCube(
						cubeListBox.getText(), GuidFactory.getGuid(),
						new AsyncCallback() {
							public void onSuccess(Object result2) {
								// populateDimensions();
							}

							public void onFailure(Throwable caught) {
							}
						});*/
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
//				DimensionPanel.populateDimensions();
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
		/*proxy = new MemoryProxy(getCubeData());
		recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("number"), new StringFieldDef("name") });
		reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();
*/
	}

	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;

	}

	
	private void emptyStore(){
			/*store.removeAll();
			store.commitChanges();
			cubeListBox.setValue(null);*/
		
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
		getCubes();
		cubeListBox.setDisabled(false);
		
		
	}


}
