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
import java.util.Map;
import java.util.Set;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.Axis.Standard;
import org.olap4j.query.Query;
import org.olap4j.query.Selection;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.enums.SelectionType;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.dto.query.PatQueryAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.data.pojo.SavedQuery;
import org.pentaho.pat.server.util.MdxQuery;
import org.springframework.security.annotation.Secured;

/**
 * This interface defines the operations permitted on a query object.
 * 
 * @author Luc Boudreau
 */
public interface QueryService extends Service {

    /**
     * Creates a new query for a given session.
     * 
     * @param userId
     *            The owner of the query to create.
     * @param sessionId
     *            The session id into which we want to create a new query.
     * @param connectionId
     *            The target connection
     * @param cubeName
     *            The target cube for the query
     * @return A unique query identification number.
     * @throws OlapException
     *             If creating the query fails.
     */
    @Secured( {"Users"})
    String createNewQuery(String userId, String sessionId, String connectionId, String cubeName) throws OlapException;

    /**
     * 
     * Returns a query object
     * 
     * @param userId
     * @param sessionId
     * @param queryId
     * @return
     */
    @Secured( {"Users"})
    Query getQuery(String userId, String sessionId, String queryId);

    /**
     * Returns a list of the currently created queries inside a given session.
     * 
     * @param userId
     *            The owner of the session and queries.
     * @param sessionId
     *            The unique id of the session for which we want the current opened queries.
     * @return A list of query names.
     */
    @Secured( {"Users"})
    List<String> getQueries(String userId, String sessionId);

    /**
     * Releases and closes a query inside a given session.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The unique session id for which we want to close and delete a query.
     * @param queryId
     *            The unique id of the query to close and release.
     */
    @Secured( {"Users"})
    void releaseQuery(String userId, String sessionId, String queryId);

    /**
     * Moves a dimension from an axis to another. You must first specify onto which query to perform the operation via
     * Session.setCurrentQuery().
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param axis
     *            The axis into which we want the dimension to be put. Null to remove it from the current query.
     * @param dimensionName
     *            The name of the dimension to move.
     */
    @Secured( {"Users"})
    void moveDimension(String userId, String sessionId, String queryId, Axis.Standard axis, String dimensionName);

    /**
     * Creates a selection of members on a given dimension. You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param dimensionName
     *            The name of the dimension onto which to apply the selection.
     * @param uniqueName
     *            A list of member names to select.
     * @param selectionType
     *            The type of selection to perform.
     * @throws OlapException
     *             If something goes sour.
     */
   


    @Secured( {"Users"})
    List<String> createSelection(String userId, String sessionId, String queryId,
            String uniqueName, ObjectType type, SelectionType selectionType) throws OlapException;

    @Secured( {"Users"})
    StringTree getSpecificMembers(String userId, String sessionId, String queryId, String dimensionName,
            ObjectType type, Selection.Operator selectionType) throws OlapException;

    /**
     * Creates a selection of members on a given dimension. You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param dimensionName
     *            The name of the dimension onto which to apply the selection.
     * @param memberNames
     *            A list of member names to select.
     * @param selectionType
     *            The type of selection to perform.
     * @throws OlapException
     *             If something goes sour.
     */
    @Deprecated
    @Secured( {"Users"})
    void createSelection(String userId, String sessionId, String queryId, String dimensionName,
            Selection.Operator selectionType) throws OlapException;

    /**
     * Creates a exclusion of members on a given dimension. You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param dimensionName
     *            The name of the dimension onto which to apply the selection.
     * @param memberNames
     *            A list of member names to select.
     * @param selectionType
     *            The type of selection to perform.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void createExclusion(String userId, String sessionId, String queryId, String dimensionName, List<String> memberNames)
            throws OlapException;

    /**
     * Unselects members from a dimension inside a query. You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param dimensionName
     *            The name of the dimension that contains the members to deselect.
     * @param currentSelections 
     */
    @Secured( {"Users"})
    int clearSelection(String userId, String sessionId, String queryId, String dimensionName, List<String> currentSelections);

    /**
     * Unselects members from a dimension inside a query. You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param dimensionName
     *            The name of the dimension that contains the members to deselect.
     * @param memberNames
     *            A list of member names to deselect.
     */
    @Secured( {"Users"})
    void clearExclusion(String userId, String sessionId, String queryId, String dimensionName);

    /**
     * Executes a query. You must first specify onto which query to perform the operation via Session.setCurrentQuery().
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @return The resultset of the query as a OlapData object.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    CellDataSet executeQuery(String userId, String sessionId, String queryId) throws OlapException;

    /**
     * Returns the MDX coresponding to a query.
     * 
     * @param userId
     *            The owner of the query
     * @param sessionId
     *            The window session id
     * @param queryId
     *            The query id for which we want the MDX code.
     * @return An MDX String.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    String getMdxForQuery(String userId, String sessionId, String queryId) throws OlapException;

    /**
     * 
     *Returns the selection of the given dimension.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension to perform the action on.
     * @return An array of members and selections.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String[][] getSelection(String userId, String sessionId, String queryId, String dimensionName) throws OlapException;

    /**
     * Swaps the axis that the dimensions are on and then executes the query.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @return CellDataSet The data for the executed query
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    CellDataSet swapAxis(String userId, String sessionId, String queryId) throws OlapException;

    /**
     * 
     * Sets the sort order for the selected dimension.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension to perform the action on.
     * @param sortOrder
     *            The sort order selected.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void setSortOrder(String userId, String sessionId, String queryId, String dimensionName, SortOrder sortOrder)
            throws OlapException;

    /**
     * 
     *Clears the sort order for the selected dimension.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension to perform the action on.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void clearSortOrder(String userId, String sessionId, String queryId, String dimensionName) throws OlapException;

    /**
     * 
     *Gets the sort order for the selected dimension.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension to perform the action on.
     * @return String The name of the sort order.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String getSortOrder(String userId, String sessionId, String queryId, String dimensionName) throws OlapException;

    /**
     * 
     * Sets the Hierarchize Mode for the selected dimension.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension to perform the action on.
     * @param hierachizeMode
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void setHierarchizeMode(String userId, String sessionId, String queryId, String dimensionName,
            HierarchizeMode hierachizeMode) throws OlapException;

    /**
     * 
     * Returns the string of whichever hierarchize mode the selected dimension is in.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension to perform the action on.
     * @return String The string of the selected hierarchize mode.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String getHierarchizeMode(String userId, String sessionId, String queryId, String dimensionName)
            throws OlapException;

    /**
     * 
     * Carries out a drill position drill type expanding the current member in place to show its child members.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param drillType
     * @param member
     *            The member being drilled.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    StringTree drillPosition(String userId, String sessionId, String queryId, DrillType drillType, MemberCell member)
            throws OlapException;

    /**
     * 
     * Carries out a drillthrough on the current member in 
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param queryId
     *            The query id.
     * @param member
     *            The member being drilled.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String[][] drillThrough(String userId, String sessionId, String queryId, List<Integer> coordinates)
            throws OlapException;

    
    /**
     *TODO JAVADOC
     * 
     * @param currentUserId
     * @param sessionId
     * @param queryId
     * @param connectionId
     * @param queryName
     */
    @Secured( {"Users"})
    void saveQuery(String currentUserId, String sessionId, SavedQuery queryId);

    /**
     *TODO JAVADOC
     * 
     * @param currentUserId
     * @param sessionId
     * @param queryId
     * @param connectionId
     * @param queryName
     */
    @Secured( {"Users"})
    void deleteQuery(String currentUserId, String sessionId, String queryName);

    
    /**
     * Creates a new mdx query for a given session.
     * 
     * @param userId
     *            The owner of the query to create.
     * @param sessionId
     *            The session id into which we want to create a new query.
     * @param connectionId
     *            The target connection
     * @return A unique mdx query identification number.
     * @throws OlapException
     *             If creating the query fails.
     */
    @Secured( {"Users"})
    String createNewMdxQuery(String userId, String sessionId, String connectionId, String catalogName)
            throws OlapException;

    /**
     * Creates a new mdx query for a given session.
     * 
     * @param userId
     *            The owner of the query to create.
     * @param sessionId
     *            The session id into which we want to create a new query.
     * @param connectionId
     *            The target connection
     * @param mdx
     *            The initial mdx statement for the mdx query
     * @return A unique mdx query identification number.
     * @throws OlapException
     *             If creating the query fails.
     */
    @Secured( {"Users"})
    String createNewMdxQuery(String userId, String sessionId, String connectionId, String catalogName, String mdx)
            throws OlapException;

    /**
     * 
     * Returns a mdx query
     * 
     * @param userId
     * @param sessionId
     * @param queryId
     * @return
     */
    @Secured( {"Users"})
    MdxQuery getMdxQuery(String userId, String sessionId, String queryId);

    /**
     * Returns a list of the currently created mdx queries inside a given session.
     * 
     * @param userId
     *            The owner of the session and queries.
     * @param sessionId
     *            The unique id of the session for which we want the current opened queries.
     * @return A list of query names.
     */
    @Secured( {"Users"})
    List<String> getMdxQueries(String userId, String sessionId);

    /**
     * Executes a temporary volatile mdx query. You must first create a connection via Session.createConnection()
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param connectionId
     *            The connction ID onto which to execute the ad-hoc query
     * @param mdx
     *            The mdx query.
     * @return The resultset of the query as a OlapData object.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    @Deprecated
    CellDataSet executeMdxQuery(String userId, String sessionId, String connectionId, String mdx) throws OlapException;

    /**
     * Executes a mdx query. You must first create a connection via Session.createConnection()
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param mdxQueryId
     *            The Id of the mdx query we want to execute
     * @param mdx
     *            The mdx query.
     * @return The resultset of the query as a OlapData object.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    CellDataSet executeMdxQuery(String userId, String sessionId, String mdxQueryId) throws OlapException;

    /**
     * Sets a mdx query. You must first create a connection via Session.createConnection() You must first create a mdx
     * query via QueryService.createNewMdxQuery()
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The session id into which the query is stored.
     * @param mdxQueryId
     *            The Id of the mdx query we want to set
     * @param mdx
     *            The mdx query.
     * @return The resultset of the query as a OlapData object.
     * @throws OlapException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void setMdxQuery(String userId, String sessionId, String mdxQueryId, String mdx) throws OlapException;

    /**
     * Releases and closes a mdx query inside a given session.
     * 
     * @param userId
     *            The owner of the query.
     * @param sessionId
     *            The unique session id for which we want to close and delete a query.
     * @param mdxQueryId
     *            The unique id of the query to close and release.
     */
    @Secured( {"Users"})
    void releaseMdxQuery(final String userId, final String sessionId, final String mdxQueryId);

    /**
     *TODO JAVADOC
     * 
     * @param currentUserId
     * @param sessioinId
     * @param queryName
     */
    @Secured( {"Users"})
    SavedQuery loadQuery(String currentUserId, String sessioinId, String savedQueryName);

    /**
     *TODO JAVADOC
     * 
     * @param currentUserId
     * @param sessionId
     */
    @Secured( {"Users"})
    Set<SavedQuery> getSavedQueries(String currentUserId, String sessionId);

    /**
     *TODO JAVADOC
     * 
     * @param currentUserId
     * @param sessioinId
     * @param connectionId
     * @param cubeName
     * @param newQuery
     */
    @Secured( {"Users"})
    String createSavedQuery(String currentUserId, String sessioinId, String connectionId, /* String cubeName, */
    Query newQuery) throws OlapException;

    /**
     *TODO JAVADOC
     * 
     * @param currentUserId
     * @param sessioinId
     * @param connectionId
     * @param cubeName
     * @param newMdxQuery
     */
    @Secured( {"Users"})
    String createSavedQuery(String currentUserId, String sessioinId, String connectionId, /* String cubeName, */
    MdxQuery newMdxQuery) throws OlapException;
    
    /**
     * 
     * @param userId
     * @param sessionId
     * @param queryId
     * @param flag
     * @return
     * @throws OlapException
     */
    @Secured( {"Users"})
    CellDataSet setNonEmpty(String userId, String sessionId, String queryId, boolean flag) throws OlapException;
    
    @Secured( {"Users"})
    void alterCell(final String userId, final String queryId, final String sessionId, final String scenarioId, final String connectionId, 
	    final String newCellValue) throws OlapException;

    /**
     *TODO JAVADOC
     *
     * @param currentUserId
     * @param sessionID
     * @param queryId
     * @param levelName
     * @param propertyName
     */
    @Secured( {"Users"})
    void setProperty(String currentUserId, String sessionID, String queryId, String dimensionName, String levelName, String propertyName, Boolean enabled);
    
    @Secured( {"Users"})
    void pushDownDimension(String currentUserId, String sessionID, String queryId, Axis axis, int position, int newposition);
    
    @Secured( {"Users"})
    void pullUpDimension(String currentUserId, String sessionID, String queryId, Axis axis, int position, int newposition);

    @Secured( {"Users"})
	void pullUpMeasureMember(String currentUserId, String sessionID,
			String queryId, Standard standard, String dimension, int currentposition,
			int newposition);
    
    @Secured( {"Users"})
	void pushDownMeasureMember(String currentUserId, String sessionID,
			String currQuery, Standard standard, String dimension, int currentposition,
			int newposition);

    @Secured( {"Users"})
    Map<IAxis, PatQueryAxis> getSelections(String currentUserId, String sessionId, String queryId);
}
