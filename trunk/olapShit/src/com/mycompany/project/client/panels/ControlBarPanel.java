package com.mycompany.project.client.panels;

/**
 * @author Tom Barber
 *
 */
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Toolbar;
import com.mycompany.project.client.listeners.ConnectionListener;

public class ControlBarPanel extends Toolbar implements ConnectionListener {
	/*TODO
	 * The Control Bar will amonst other things display the currently selected cube for user and allow other functionality as the
	 * project progresses 
	 */
	public ControlBarPanel() {
		super();

		init();
	}
	
	public void init(){
		
	}
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}

}
