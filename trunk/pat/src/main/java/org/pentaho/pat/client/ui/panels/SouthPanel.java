/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Tom Barber
 */

package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.pentaho.pat.client.listeners.ConnectionListener;

import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class SouthPanel.
 * 
 * @author root
 */
public class SouthPanel extends CaptionLayoutPanel implements ConnectionListener {

	/**
	 * TODO JAVADOC.
	 * 
	 * @param text the text
	 */
	public SouthPanel(final String text) {
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
