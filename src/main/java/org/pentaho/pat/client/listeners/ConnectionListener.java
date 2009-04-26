/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * @created Jan 31, 2008
 * @author wseyler
 */


package org.pentaho.pat.client.listeners;

import java.util.EventListener;

import com.google.gwt.user.client.ui.Widget;


/**
 * The Interface ConnectionListener.
 * 
 * @author wseyler
 */
public interface ConnectionListener extends EventListener {
	
	/**
	 * On connection broken.
	 * 
	 * @param sender the sender
	 */
	public void onConnectionBroken(Widget sender);
	
	/**
	 * On connection made.
	 * 
	 * @param sender the sender
	 */
	public void onConnectionMade(Widget sender);
}
