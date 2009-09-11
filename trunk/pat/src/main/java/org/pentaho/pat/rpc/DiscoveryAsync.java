/*
 * Copyright (C) 2009 Luc Boudreau
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
package org.pentaho.pat.rpc;


import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines discovery operations methods.
 * @author Luc Boudreau
 */
public interface DiscoveryAsync {
	
	public void getDrivers(AsyncCallback<String[]> callback);

	public void getDimensions(String sessionId, String queryId, Axis axis, AsyncCallback<String []> callback);
	
	public void getCubes(String sessionId, String connectionId, AsyncCallback<String []> callback);
	
	public void getMembers(String sessionId, String queryId, String dimensionName, AsyncCallback<StringTree> callback);
}
