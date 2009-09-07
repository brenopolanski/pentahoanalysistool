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
 * @created Jan 22, 2008
 * @author wseyler
 */


package org.pentaho.pat.client.ui.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * The Interface SelectionModeImageBundle.
 * 
 * @author wseyler
 */
public interface SelectionModeImageBundle extends ImageBundle {
	
	/**
	 * Children select icon.
	 * 
	 * @return the abstract image prototype
	 */
	public AbstractImagePrototype childrenSelectIcon();
	
	/**
	 * Include children select icon.
	 * 
	 * @return the abstract image prototype
	 */
	public AbstractImagePrototype includeChildrenSelectIcon();
	
	/**
	 * Member select icon.
	 * 
	 * @return the abstract image prototype
	 */
	public AbstractImagePrototype memberSelectIcon();
	
	/**
	 * Siblings select icon.
	 * 
	 * @return the abstract image prototype
	 */
	public AbstractImagePrototype siblingsSelectIcon();
}
