package org.pentaho.pat.client.panels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.Button;

import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.core.EventObject;
import com.google.gwt.user.client.ui.Widget;

import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;


import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.panels.services.ControlBar;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;

import org.pentaho.pat.client.util.ConnectionFactory;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;

public class ControlBarPanel extends Toolbar implements ConnectionListener {
	/*TODO
	 * The Control Bar will amonst other things display the currently selected cube for user and allow other functionality as the
	 * project progresses 
	 */
	private Store store;
	private ComboBox cubeListBox;
	private ToolbarButton conButton;
	private ControlBar obj1 = new ControlBar();
	
	public ControlBarPanel() {
		super();

		init();
	}
	
	private void init(){
		
		cubeListBox = new ComboBox();
		cubeListBox.setFieldLabel(MessageFactory.getInstance().cube());
		store = ControlBar.populateCubeList();
		cubeListBox.setStore(store);
		cubeListBox.setDisplayField("name");
		cubeListBox.setMode(ComboBox.LOCAL);
		cubeListBox.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				ServiceFactory.getInstance().setCube(
						(String) comboBox.getValueAsString(),
						GuidFactory.getGuid(), new AsyncCallback() {
							public void onSuccess(Object result) {
								obj1.setCube(cubeListBox.getText());
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
				
			          obj1.connect(ConnectionFactory.getInstance().connection_string());
			          if (obj1.isConnectionEstablished() == false)conButton.setText("Disconnect");
			        } else if (button.getText().equals(MessageFactory.getInstance().disconnect())) {
			          obj1.disconnect();
			          if (obj1.isConnectionEstablished() == true)conButton.setText("Connect");
			        }               
			      
			}});
		
		this.addField(cubeListBox);
		this.addButton(conButton);
		

	}
	
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		obj1.getCubes(cubeListBox.getText());
	}


}
