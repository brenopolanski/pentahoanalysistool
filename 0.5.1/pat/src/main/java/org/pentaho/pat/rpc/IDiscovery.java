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

import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Defines discovery operations methods.
 * 
 * @author Luc Boudreau
 */
public interface IDiscovery extends RemoteService {

    /**
     * Fetches a list of JDBC drivers available for use.
     * 
     * @return A list of java JDBC driver names.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String[] getDrivers() throws RpcException;

    /**
     * Returns a list of all available dimension names on a given axis. You first specify which cube you're currently
     * exploring via the Session.setCurrentCube() call.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param queryId
     * @param axis
     *            The axis for which we want the dimensions. Use null for unused dimensions.
     * @return A list object containing all available dimensions, an empty list if there are none available or null if
     *         the given axis is not present in the cube.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String[] getDimensions(String sessionId, String queryId, IAxis axis) throws RpcException;

    /**
     * Returns all the cube names available on the current connection.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param connectionId
     * @return A list of all the cube names available or an empty list if none are found.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    CubeItem[] getCubes(String sessionId, String connectionId) throws RpcException;

    /**
     * Returns a tree list of all members found within a given dimension, or null if the dimension You first specify
     * which cube you're currently exploring via the Session.setCurrentCube() call.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param The
     *            query UUID
     * @param dimensionName
     *            Name of the dimension that contains the member names we want.
     * @return A StringTree of all present members.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    StringTree getMembers(String sessionId, String queryId, String dimensionName) throws RpcException;
}
