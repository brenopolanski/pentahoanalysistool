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

import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
@SuppressWarnings("unchecked")
public interface ISessionAsync {
	
	public void createSession(AsyncCallback<String> callback);
	
	public void closeSession(String sessionId, AsyncCallback callback);
	
	public void connect(String sessionId, String connectionId, AsyncCallback callback);
	
	public void disconnect(String sessionId, String connectionId, AsyncCallback callback);	
	
	public void getConnection(String sessionId, String connectionId, AsyncCallback<CubeConnection> callback);
	
	public void getConnections(String sessionId, AsyncCallback<CubeConnection[]> callback);
	
	public void getActiveConnections(String sessionId, AsyncCallback<CubeConnection[]> callback);
	
	public void saveConnection(String sessionId, CubeConnection connection, AsyncCallback<String> callback);
	
	public void deleteConnection(String sessionId, String connectionId, AsyncCallback callback);
	
}
