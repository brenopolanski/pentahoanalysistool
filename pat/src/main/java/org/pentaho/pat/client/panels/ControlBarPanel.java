package org.pentaho.pat.client.panels;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.Button;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;

import org.pentaho.pat.client.util.ConnectionFactory;

public class ControlBarPanel extends Toolbar implements SourcesConnectionEvents {
	/*TODO
	 * The Control Bar will amonst other things display the currently selected cube for user and allow other functionality as the
	 * project progresses 
	 */
	private Store store;
	private ComboBox cubeListBox;
	private ToolbarButton conButton;
	ConnectionListenerCollection connectionListeners;
	private static RecordDef recordDef;
	private MemoryProxy proxy;
	private ArrayReader reader;
	boolean connectionEstablished = false;
	public ControlBarPanel() {
		super();

		init();
	}
	
	private void init(){
		cubeListBox = new ComboBox();
		cubeListBox.setFieldLabel(MessageFactory.getInstance().cube());
		populateCubeList();
		cubeListBox.setStore(store);
		cubeListBox.setDisplayField("name");
		cubeListBox.setMode(ComboBox.LOCAL);
		cubeListBox.addListener(new ComboBoxListenerAdapter() {
			public void onExpand(ComboBox comboBox){
				if (isConnectionEstablished())getCubes(cubeListBox.getText());
			}
			public void onSelect(ComboBox comboBox, Record record, int index) {
				ServiceFactory.getInstance().setCube(
						(String) comboBox.getValueAsString(),
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
		conButton.addListener(new ButtonListenerAdapter() {
			public void onClick(final Button button, final EventObject e) {
			
					if (button.getText().equals(MessageFactory.getInstance().connect())) {
				
			          connect(ConnectionFactory.getInstance().connection_string());
			         
			      
			        } else if (button.getText().equals(MessageFactory.getInstance().disconnect())) {
			          disconnect();
			         
			        }               	 
			}});
		
		this.addField(cubeListBox);
		this.addButton(conButton);
		

	}
	
/*	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		getCubes(cubeListBox.getText());
	}
*/	
	private String[][] getProxyData() {
		String[][] tom = { new String[] { "naught", "No Cubes" }};
		return tom;
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
								cubeListBox.getText(),
								GuidFactory.getGuid(), new AsyncCallback() {
									public void onSuccess(Object result2) {
										//populateDimensions();
									}

									public void onFailure(Throwable caught) {
									}
								});
					}

					public void onFailure(Throwable caught) {
					}
				});

		
	}
	
	public void setCube(String boxText){
		
		ServiceFactory.getInstance().setCube(
				boxText,
				GuidFactory.getGuid(), new AsyncCallback() {
					public void onSuccess(Object result) {
						//TODO Populate the Dimensions Dialog
					}

					public void onFailure(Throwable caught) {
					}
				});

	}
public void connect(String connectionStr) {
	    if (!isConnectionEstablished()) {
	      ServiceFactory.getInstance().connect(connectionStr, GuidFactory.getGuid(), new AsyncCallback() {
	        public void onSuccess(Object result) {
	          Boolean booleanResult = (Boolean)result;
	          if (booleanResult.booleanValue()) {
	        	  setConnectionEstablished(true);
	          } else {
	        	  setConnectionEstablished(false);
	            
	          }
	         conButton.setText(isConnectionEstablished() ? MessageFactory.getInstance().disconnect() : MessageFactory.getInstance().connect());
	        
	        }
	        public void onFailure(Throwable caught) {
	          System.out.println(caught.getLocalizedMessage());
	        	Window.alert(MessageFactory.getInstance().no_connection_param(caught.getLocalizedMessage()));
	        	setConnectionEstablished(false);
	         conButton.setText((isConnectionEstablished() ? MessageFactory.getInstance().disconnect() : MessageFactory.getInstance().connect()));
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
	      ServiceFactory.getInstance().disconnect(GuidFactory.getGuid(), new AsyncCallback<Object>() {
	        public void onFailure(Throwable caught) {
	          Window.alert(MessageFactory.getInstance().no_connection_param(caught.getLocalizedMessage()));
	          setConnectionEstablished(false);
	          //connectionListeners.fireConnectionBroken(ControlBarPanel.this);
	         // conStr = (isConnectionEstablished() ? MessageFactory.getInstance().disconnect() : MessageFactory.getInstance().connect());
	        }
	        public void onSuccess(Object result) {
	        	setConnectionEstablished(false);
	         // connectionListeners.fireConnectionBroken(ControlBar.this);
	         // conStr = (isConnectionEstablished() ? MessageFactory.getInstance().disconnect() : MessageFactory.getInstance().connect());
	        }       
	      });
	    }
	    
	  }
	  
	  
	  /* (non-Javadoc)
	   * @see org.pentaho.halogen.client.listeners.SourcesConnectionEvents#addConnectionListener(org.pentaho.halogen.client.listeners.ConnectionListener)
	   */
	  public void addConnectionListener(ConnectionListener listener) {
	    if (connectionListeners == null) {
	      connectionListeners = new ConnectionListenerCollection();
	    }
	    connectionListeners.add(listener);
	  }

	  /* (non-Javadoc)
	   * @see org.pentaho.halogen.client.listeners.SourcesConnectionEvents#removeClickListener(org.pentaho.halogen.client.listeners.ConnectionListener)
	   */
	  public void removeClickListener(ConnectionListener listener) {
	    if (connectionListeners != null) {
	      connectionListeners.remove(listener);
	    }
	  }
	  
		public static String[][] getCubeData() {
			String[][] list = { new String[] { "naught", "No Cubes" }};
			return list;
		}


		public void populateCubeList(){
			proxy = new MemoryProxy(getCubeData());
			recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("number"),
					new StringFieldDef("name") });
			reader = new ArrayReader(recordDef);
			store = new Store(proxy, reader);
			store.load();
			
		}

}
