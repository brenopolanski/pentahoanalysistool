/*
 * Copyright 2009 Thomas Barber.  All rights reserved. 
 * This software was developed by Thomas Barber and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * Analysis Tool.  The Initial Developer is Thomas Barber.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
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
