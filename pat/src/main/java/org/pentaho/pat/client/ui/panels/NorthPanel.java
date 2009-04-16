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
public class NorthPanel extends CaptionLayoutPanel implements ConnectionListener {

	public NorthPanel(String text) {
		super(text);
		// this(null, false);
		init();
	}

	public void init() {

	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub

	}
}
