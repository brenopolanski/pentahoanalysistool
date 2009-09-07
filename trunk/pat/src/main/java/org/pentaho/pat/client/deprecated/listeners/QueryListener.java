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


package org.pentaho.pat.client.deprecated.listeners;

import java.util.EventListener;

import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.user.client.ui.Widget;


/**
 * The Interface ConnectionListener.
 * 
 * @author tom(at)wamonline.org.uk
 */
public interface QueryListener extends EventListener {
	
	public void onQueryChange(Widget sender);
	
	public void onQueryExecuted(String queryId, CellDataSet matrix);
}
