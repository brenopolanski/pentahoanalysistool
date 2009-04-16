/**
 * 
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.pentaho.pat.client.listeners.ConnectionListener;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author root
 * 
 */
public class SouthPanel extends CaptionLayoutPanel implements ConnectionListener {

	/**
	 *TODO JAVADOC
	 *
	 * @param text
	 */
	public SouthPanel(String text) {
		super(text);
		// this(null, false);
		init();
	}

	/**
	 *TODO JAVADOC
	 *
	 */
	public void init() {

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub

	}

}
