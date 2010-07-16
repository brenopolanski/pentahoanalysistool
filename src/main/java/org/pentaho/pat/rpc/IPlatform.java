/*
 * Copyright (C) 2009 Paul Stoellberger
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

import org.pentaho.pat.rpc.exceptions.RpcException;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * This interface defines the operations permitted related to the Pentaho BI Platform
 * 
 * @author Paul Stoellberger
 */
public interface IPlatform extends RemoteService {
    
    @Secured( {"Users"})
    void saveQueryToSolution(String sessionId, String queryId, String connectionId, String solution, String path, String name, String localizedFileName)throws RpcException;
    
    @Secured( {"Users"})
    void saveQueryAsCda(String sessionId, String queryId, String connectionId, String solution,String path, String name, String localizedFileName) throws RpcException;
}
