package org.pentaho.pat.client.panels;

import java.util.ArrayList;

import org.gwt.mosaic.ui.client.ToolBar;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.ServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;


public class ControlBarPanel extends ToolBar implements ConnectionListener, SourcesConnectionEvents{
	private ListBox cubeListBox; 
	private boolean connectionEstablished;
	private ConnectionListenerCollection connectionListeners;



	public ControlBarPanel(){
		super();
		
		init();
	}
	
	
	public void init(){
	
	    cubeListBox = new ListBox();
	    cubeListBox.addStyleName("demo-ListBox");
	    
	    cubeListBox.addChangeListener(new ChangeListener(){  
	    	

	    	public void onChange(Widget sender) { 
	    	final int selected = cubeListBox.getSelectedIndex();
	    	 if (cubeListBox.getItemCount()>0){
			ServiceFactory.getInstance().setCube(
					cubeListBox.getItemText(selected), GuidFactory.getGuid(), new AsyncCallback() {
						public void onSuccess(Object result) {
							System.out.println("Cube Set");
						}

						public void onFailure(Throwable caught) {
							System.out.println("Cube Failed");
						}
					});
		}
	    	}
	    });

	    cubeListBox.addClickListener(new ClickListener(){
	    	 public void onClick(Widget sender) {
	    		 if (cubeListBox.getItemCount()>0){
	    		 final int selected = cubeListBox.getSelectedIndex();
	 			ServiceFactory.getInstance().setCube(
	 					cubeListBox.getItemText(selected), GuidFactory.getGuid(), new AsyncCallback() {
	 						public void onSuccess(Object result) {
	 							System.out.println("Cube Set");
	 						}

	 						public void onFailure(Throwable caught) {
	 							System.out.println("Cube Failed");
	 						}
	 					});
	    	      }
	    	 }
	    });
	   

	    this.add(cubeListBox);



	
	  }


	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;

	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		setConnectionEstablished(false);
		

	}


	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		setConnectionEstablished(true);
		
		getCubes();

	}


	private void getCubes() {

		ServiceFactory.getInstance().getCubes(GuidFactory.getGuid(),
				new AsyncCallback() {
			public void onSuccess(Object result1) {
				if (result1 != null) {
					ArrayList cubeListArray = new ArrayList();
					String[][] cubeNames = (String[][]) result1;
					for (int i = 0; i < cubeNames.length; i++) {
						
						cubeListBox.clear();
						cubeListBox.addItem(cubeNames[i][1]);
						
					}
					
				}
				int cubeIndex = cubeListBox.getSelectedIndex();
				ServiceFactory.getInstance().setCube(
						cubeListBox.getItemText(cubeIndex), GuidFactory.getGuid(),
						new AsyncCallback() {
							public void onSuccess(Object result2) {
								// populateDimensions();
								System.out.println("CubeSet");
							}

							public void onFailure(Throwable caught) {
							}
						});
			}

			public void onFailure(Throwable caught) {
			}
		});


	}


	public void addConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub
		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);

	}


	public void removeConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}

	}

}
