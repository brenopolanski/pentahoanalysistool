/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */


package org.pentaho.pat.client.ui.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * The Interface SelectionModeImageBundle.
 * 
 * @created May 8, 2009
 * @author tom(at)wamonline.org.uk
 */
public interface ISelectionModeImageBundle extends ImageBundle {
	
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
