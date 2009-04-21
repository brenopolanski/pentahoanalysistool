/**
 * 
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.pentaho.pat.client.listeners.ConnectionListener;

import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class NorthPanel.
 * 
 * @author root
 */
/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class NorthPanel extends CaptionLayoutPanel implements ConnectionListener {

	/**
	 * TODO JAVADOC.
	 * 
	 * @param text the text
	 */
	public NorthPanel(final String text) {
		super(text);
		// this(null, false);
		init();
	}

	/**
	 * TODO JAVADOC.
	 */
	public void init() {

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(final Widget sender) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(final Widget sender) {
		// TODO Auto-generated method stub

	}
}
