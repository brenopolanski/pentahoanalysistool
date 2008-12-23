package org.pentaho.pat.client.panels.services;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.panels.ControlBarPanel;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Toolbar;

public class ControlBar implements SourcesConnectionEvents {
	
	private static Store store;
	private static MemoryProxy proxy;
	private static RecordDef recordDef;
	private static ArrayReader reader;
	public static final String AXIS_NONE = "none"; //$NON-NLS-1$
	public static final String AXIS_UNUSED = "UNUSED"; //$NON-NLS-1$
	public static final String AXIS_FILTER = "FILTER"; //$NON-NLS-1$
	public static final String AXIS_COLUMNS = "COLUMNS"; //$NON-NLS-1$
	public static final String AXIS_ROWS = "ROWS"; //$NON-NLS-1$
	public static final String AXIS_PAGES = "PAGES"; //$NON-NLS-1$
	public static final String AXIS_CHAPTERS = "CHAPTERS"; //$NON-NLS-1$
	public static final String AXIS_SECTIONS = "SECTIONS"; //$NON-NLS-1$
	//Need deleting once connection dialog is written...
	static String queryTypeGroup = "QUERY_TYPE"; //$NON-NLS-1$
	boolean connectionEstablished = false;
	ConnectionListenerCollection connectionListeners;
	  

	  
	public ControlBar() {
		 super();

		 init();

	}


	private void init() {
		// TODO Auto-generated method stub
		
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
						setCube(boxText);
										}

					public void onFailure(Throwable caught) {
					}
				});

	}

	
	public static String[][] getCubeData() {
		String[][] list = { new String[] { "naught", "No Cubes" }};
		return list;
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
	public static Store populateCubeList(){
		proxy = new MemoryProxy(getCubeData());
		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("number"),
				new StringFieldDef("name") });
		reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();
		return store;
	}
	
	
	
	
	
	  public boolean isConnectionEstablished() {
		    return connectionEstablished;
		  }

		  public void setConnectionEstablished(boolean connectionEstablished) {
		    this.connectionEstablished = connectionEstablished;
		    
		  }
		  
		  
	public void connect(String connectionStr) {
		
	    if (!isConnectionEstablished()) {
	      ServiceFactory.getInstance().connect(connectionStr, GuidFactory.getGuid(), new AsyncCallback() {
	        public void onSuccess(Object result) {
	          Boolean booleanResult = (Boolean)result;
	          if (booleanResult.booleanValue()) {
	            setConnectionEstablished(true);
	           // connectionListeners.fireConnectionMade();
	          } else {
	            setConnectionEstablished(false);
	           // connectionListeners.fireConnectionBroken();
	          }
	         // conStr= isConnectionEstablished() ? MessageFactory.getInstance().disconnect() : MessageFactory.getInstance().connect();
	          
	        }
	        public void onFailure(Throwable caught) {
	          System.out.println(caught.getLocalizedMessage());
	        	Window.alert(MessageFactory.getInstance().no_connection_param(caught.getLocalizedMessage()));
	          setConnectionEstablished(false);
	         // conStr= (isConnectionEstablished() ? MessageFactory.getInstance().disconnect() : MessageFactory.getInstance().connect());
	        }      
	      });
	      
	    }
	    
	    
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
	          //connectionListeners.fireConnectionBroken(ControlBar.this);
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




}
