package org.pentaho.pat.client.panels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.panels.services.ControlBar;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
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
	private ControlBar controlBar;
	
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
								ControlBar.setCube(cubeListBox.getText());
							}

							public void onFailure(Throwable caught) {
							}
						});
			}

		});

		this.addField(cubeListBox);

	}
	
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		controlBar.getCubes(cubeListBox.getText());
	}


}
