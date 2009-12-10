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

import java.util.List;

import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.QuerySaveModel;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * This interface defines the operations permitted on a query object that was previously instanciated through the
 * Session service.
 * 
 * @author Luc Boudreau
 */
public interface IQuery extends RemoteService {

    /**
     * Tells the server that we want to create a new query. It will create a new query against the currently selected
     * cube. It returns a unique identification string to identify the query created.
     * 
     * @param sessionId
     *            The window session id.
     * @param connectionId
     * @param cubeName
     * @return The unique ame of the created query.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String createNewQuery(String sessionId, String connectionId, String cubeName) throws RpcException;

    /**
     * Returns the list of currently created queries.
     * 
     * @param sessionId
     *            The window session id.
     * @return An array of query unique names.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    String[] getQueries(String sessionId) throws RpcException;

    /**
     * Closes and releases a given query.
     * 
     * @param sessionId
     *            The window session id.
     * @param queryId
     *            The query we want to close and release.
     * @return True if all is good.
     */
    @Secured( {"Users"})
    void deleteQuery(String sessionId, String queryId) throws RpcException;

    /**
     * Moves a dimension to a different axis.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param queryId
     *            Identifies on which query to perform the operation.
     * @param axis
     *            The destination axis.
     * @param dimensionName
     *            The name of the dimension to move.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void moveDimension(String sessionId, String queryId, IAxis axis, String dimensionName) throws RpcException;

    /**
     * Performs a selection of certain members in a dimension.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param queryId
     *            Identifies on which query to perform the operation.
     * @param dimensionName
     *            The name of the dimension on which we want to select members.
     * @param memberNames
     *            The actual names of the members to perform a selection on.
     * @param selectionType
     *            The type of selection to perform.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void createSelection(String sessionId, String queryId, String dimensionName, List<String> memberNames,
            String selectionType) throws RpcException;

    /**
     * Performs a selection of certain members in a dimension.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param queryId
     *            Identifies on which query to perform the operation.
     * @param dimensionName
     *            The name of the dimension on which we want to select members.
     * @param memberNames
     *            The actual names of the members to perform a selection on.
     * @param selectionType
     *            The type of selection to perform.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void createExclusion(String sessionId, String queryId, String dimensionName, List<String> memberNames)
            throws RpcException;

    /**
     * Removes the selection status of members inside a given dimension.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param queryId
     *            Identifies on which query to perform the operation.
     * @param dimensionName
     *            Name of the dimension that includes the members to remove selection status.
     * @param memberNames
     *            The actual member names of which we want to remove the selection status.
     * @return True if all is well.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void clearSelection(String sessionId, String queryId, String dimensionName, List<String> memberNames)
            throws RpcException;

    /**
     * Removes the selection status of members inside a given dimension.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param queryId
     *            Identifies on which query to perform the operation.
     * @param dimensionName
     *            Name of the dimension that includes the members to remove selection status.
     * @param memberNames
     *            The actual member names of which we want to remove the selection status.
     * @return True if all is well.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    void clearExclusion(String sessionId, String queryId, String dimensionName) throws RpcException;

    /**
     * Executes the current query.
     * 
     * @param sessionId
     *            Identifies the window session id that requested the operation.
     * @param queryId
     *            Identifies on which query to perform the operation.
     * @return The result of the query execution.
     * @throws RpcException
     *             If something goes sour.
     */
    @Secured( {"Users"})
    CellDataSet executeQuery(String sessionId, String queryId) throws RpcException;

    /**
     * Returns the MDX coresponding to a query.
     * 
     * @param sessionId
     *            The window session id
     * @param queryId
     *            The query id for which we want the MDX code.
     * @return An MDX String.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    String getMdxForQuery(String sessionId, String queryId) throws RpcException;

    /**
     * 
     * Executes an mdx based query.
     * 
     * @param sessionId
     *            The current session id.
     * @param connectionId
     *            The current connectionId.
     * @param mdx
     *            The mdx code.
     * @return A cell data set.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    @Deprecated
    CellDataSet executeMdxQuery(String sessionId, String connectionId, String mdx) throws RpcException;

    /**
     * 
     * Gets the selection status for a dimension.
     * 
     * @param sessionId
     *            The current session id.
     * @param queryId
     *            The current query id.
     * @param dimensionName
     *            The dimension in question.
     * @return A String Array.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    String[][] getSelection(String sessionId, String queryId, String dimensionName) throws RpcException;

    /**
     * 
     * Swaps the axis of the data set.
     * 
     * @param sessionId
     *            The current session id.
     * @param queryId
     *            The current query id.
     * @return A CellDataSet
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    CellDataSet swapAxis(String sessionId, String queryId) throws RpcException;

    /**
     * 
     * Sets the sort order of a dimension.
     * 
     * @param sessionId
     *            The current session id.
     * @param queryId
     *            The current query id.
     * @param dimensionName
     *            The dimension in question.
     * @param sort
     *            The sort type.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    void setSortOrder(String sessionId, String queryId, String dimensionName, String sort) throws RpcException;

    /**
     * 
     * Clears the sort order on a dimension.
     * 
     * @param sessionId
     *            The session id.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension in question.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    void clearSortOrder(String sessionId, String queryId, String dimensionName) throws RpcException;

    /**
     * 
     * Gets the sort order for a dimension.
     * 
     * @param sessionId
     *            The session id.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension in question.
     * @return A String.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    String getSortOrder(String sessionId, String queryId, String dimensionName) throws RpcException;

    /**
     * 
     * Set the hierarchize mode for a dimension.
     * 
     * @param sessionId
     *            The session id.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension in question.
     * @param mode
     *            The hierarchize mode.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    void setHierarchizeMode(String sessionId, String queryId, String dimensionName, String mode) throws RpcException;

    /**
     * 
     * Gets the hierarchize mode for a dimension
     * 
     * @param sessionId
     *            The session id.
     * @param queryId
     *            The query id.
     * @param dimensionName
     *            The dimension in question.
     * @return A String
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    String getHierarchizeMode(String sessionId, String queryId, String dimensionName) throws RpcException;

    /**
     * 
     * Perform a drill position drill on a member.
     * 
     * @param sessionId
     *            The session id.
     * @param queryId
     *            The query id.
     * @param member
     *            The member cell.
     * @throws RpcException
     *             If something turns sour.
     */
    @Secured( {"Users"})
    void drillPosition(String sessionId, String queryId, DrillType drillType, MemberCell member) throws RpcException;

    @Secured( {"Users"})
    String createNewMdxQuery(String sessionId, String connectionId, String catalogName) throws RpcException;

    @Secured( {"Users"})
    String createNewMdxQuery(String sessionId, String connectionId, String catalogName, String mdx) throws RpcException;

    @Secured( {"Users"})
    String[] getMdxQueries(String sessionId) throws RpcException;

    @Secured( {"Users"})
    void deleteMdxQuery(String sessionId, String mdxQueryId) throws RpcException;

    @Secured( {"Users"})
    CellDataSet executeMdxQuery(String sessionId, String mdxQueryId) throws RpcException;

    @Secured( {"Users"})
    String getMdxQuery(String sessionId, String mdxQueryId) throws RpcException;
    
    @Secured( {"Users"})
    void setMdxQuery(String sessionId, String mdxQueryId, String mdx) throws RpcException;

    @Secured( {"Users"})
    void saveQuery(String sessionId, String queryId, String queryName, String connectionId, CubeItem cube,
            String cubeName) throws RpcException;

    @Secured( {"Users"})
    QuerySaveModel loadQuery(String sessioinId, String queryId) throws RpcException;

    @Secured( {"Users"})
    List<QuerySaveModel> getSavedQueries(String sessionId) throws RpcException;

    @Secured( {"Users"})
    CellDataSet setNonEmpty(String sessionId, String queryId, boolean flag) throws RpcException;
    
    @Secured( {"Users"})
    CellDataSet alterCell(String sessionId, String queryId, String connectionId, String scenarioId, 
	    String newCellValue)throws RpcException;
}
