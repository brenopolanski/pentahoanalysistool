package org.pentaho.pat.client.panels;

/**
 * @author Tom Barber
 *
 */
import org.pentaho.pat.client.listeners.ConnectionListener;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Toolbar;

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
