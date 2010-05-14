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
package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.query.Selection;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.LevelProperties;
import org.pentaho.pat.rpc.dto.MemberLabelItem;
import org.pentaho.pat.rpc.dto.StringTree;
import org.springframework.security.annotation.Secured;

/**
 * Defines discovery operations methods.
 * 
 * @author Luc Boudreau
 */
public interface DiscoveryService extends Service {

    /**
     * Retreives a list of dimensions currently placed on a given dimension. For dimensions not currently used, pass a
     * null value as the Axis parameter.
     * 
     * @param userId
     *            The id of the user who requests this operation.
     * @param sessionId
     *            The id of the current session into which to perform this operation.
     * @param the
     *            query UUID where to lookup the dimensions
     * @param axis
     *            The axis for which we want the current dimensions.
     * @return A list of dimension names.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    List<String> getDimensions(String userId, String sessionId, String queryId, Axis.Standard axis)
            throws OlapException;

    /**
     * Retreives a list of dimensions currently placed on a given dimension. For dimensions not currently used, pass a
     * null value as the Axis parameter.
     * 
     * @param userId
     *            The id of the user who requests this operation.
     * @param sessionId
     *            The id of the current session into which to perform this operation.
     * @param the
     *            query UUID where to lookup the dimensions
     * @param axis
     *            The axis for which we want the current dimensions.
     * @return A list of dimension names.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    List<MemberLabelItem> getDimensionList(String userId, String sessionId, String queryId, Axis.Standard axis)
            throws OlapException;

    /**
     * Retreives a list of available cubes for the current connection. One must first create a connection via the
     * Session service.
     * 
     * @param userId
     *            The id of the user who requests this operation.
     * @param sessionId
     *            The id of the current session into which
     * @param connectionId
     *            The connection UUID to use to perform this operation.
     * @return A list of cubes.
     */
    @Secured( {"Users"})
    List<CubeItem> getCubes(String userId, String sessionId, String connectionId) throws OlapException;

    @Secured( {"Users"})
    Cube getCube(String userId, String sessionId, String connectionId, String cubeName) throws OlapException;

    /**
     * Returns a tree representation of the members included in a given dimension. The representation only includes
     * their names.
     * 
     * @param userId
     *            The id of the user who requests this operation.
     * @param sessionId
     *            The id of the current session into which to perform this operation.
     * @param queryId
     *            The id of the query to seek into.
     * @param dimensionName
     *            The name of which we want the tree of members.
     * @return A {@link StringTree} representation of the members included in a dimension.
     * @throws OlapException
     *             If anything goes sour.
     */
    @Secured( {"Users"})
    StringTree getMembers(String userId, String sessionId, String queryId, String dimensionName) throws OlapException;

    /**
     * Scans and updates if necessary the current Java classloader for registered JDBC drivers.
     * 
     * @return A list of JDBC driver class names.
     */
    @Secured( {"Users"})
    List<String> getDrivers();

    /**
     *TODO JAVADOC
     *
     * @param currentUserId
     * @param sessionId
     * @param queryId
     * @param dimensionName
     * @return
     */
    List<LevelProperties> getAllLevelProperties(String currentUserId, String sessionId, String queryId, String dimensionName) throws OlapException;

    /**
     *TODO JAVADOC
     *
     * @param currentUserId
     * @param sessionId
     * @param queryId
     * @param dimensionName
     * @param levelName
     * @return
     */
    StringTree getNamedLevelProperties(String currentUserId, String sessionId, String queryId, String dimensionName,
            String levelName) throws OlapException;

	List<MemberLabelItem> getHierarchies(String currentUserId, String sessionId,	String queryId, String dimensionName) throws OlapException;

	List<MemberLabelItem> getLevels(String currentUserId, String sessionId,
			String queryId, String dimensionName, String hierarchy) throws OlapException;
	
	List<String> getLevelMembers(String currentUserId, String sessionId,
			String queryId, String dimensionName, String hierarchyName, String levelName) throws OlapException;
	
	StringTree getSpecificMembers(String userId, String sessionId, String queryId, String dimensionName, String hierarchyName, String levelName,
			Selection.Operator selectionType) throws OlapException;

	List<MemberLabelItem> getMeasures(String currentUserId, String sessionID, String currQuery) throws OlapException;

}
