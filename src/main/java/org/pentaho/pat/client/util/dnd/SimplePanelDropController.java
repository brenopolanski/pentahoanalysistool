/*
 * Copyright (C) 2010 Thomas Barber
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
package org.pentaho.pat.client.util.dnd;

import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @created Frb 25, 2010
 * @since 0.6.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public interface SimplePanelDropController {
	 

	  public abstract void setWidgetDropController(SimplePanel dropTarget);
}