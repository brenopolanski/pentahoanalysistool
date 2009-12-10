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
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This interface defines the operations permitted on a query object that was previously instanciated through the
 * Session service.
 * 
 * @author Luc Boudreau
 */

public interface IQueryAsync {

    void moveDimension(String sessionId, String queryId, IAxis axis, String dimensionName,
            AsyncCallback<Object> callback);

    void createSelection(String sessionId, String queryId, String dimensionName, List<String> memberNames,
            String selectionType, AsyncCallback<Object> callback);

    void createExclusion(String sessionId, String queryId, String dimensionName, List<String> memberNames,
            AsyncCallback<Object> callback);

    void clearSelection(String sessionId, String queryId, String dimensionName, List<String> memberNames,
            AsyncCallback<Object> callback);

    void clearExclusion(String sessionId, String queryId, String dimensionName, AsyncCallback<Object> callback);

    void executeQuery(String sessionId, String queryId, AsyncCallback<CellDataSet> callback);

    void executeMdxQuery(String sessionId, String connectionId, String mdx, AsyncCallback<CellDataSet> callback);

    void createNewQuery(String sessionId, String connectionId, String cubeName, AsyncCallback<String> callback);

    void getQueries(String sessionId, AsyncCallback<String[]> callback);

    void deleteQuery(String sessionId, String queryId, AsyncCallback<Object> callback);

    void getMdxForQuery(String sessionId, String queryId, AsyncCallback<String> callback);

    void getSelection(String sessionId, String queryId, String dimensionName, AsyncCallback<String[][]> callback);

    void swapAxis(String sessionId, String queryId, AsyncCallback<CellDataSet> callback);

    void setSortOrder(String sessionId, String queryId, String dimensionName, String sort,
            AsyncCallback<Object> callback);

    void clearSortOrder(String sessionId, String queryId, String dimensionName, AsyncCallback<Object> callback);

    void getSortOrder(String sessionId, String queryId, String dimensionName, AsyncCallback<String> callback);

    void setHierarchizeMode(String sessionId, String queryId, String dimensionName, String mode,
            AsyncCallback<Object> callback);

    void getHierarchizeMode(String sessionId, String queryId, String dimensionName, AsyncCallback<String> callback);

    void drillPosition(String sessionId, String queryId, DrillType drillType, MemberCell member,
            AsyncCallback<Object> callback);

    void createNewMdxQuery(String sessionId, String connectionId, String catalogName, AsyncCallback<String> callback);

    void createNewMdxQuery(String sessionId, String connectionId, String catalogName, String mdx,
            AsyncCallback<String> callback);

    void getMdxQueries(String sessionId, AsyncCallback<String[]> callback);

    void deleteMdxQuery(String sessionId, String mdxQueryId, AsyncCallback<Object> callback);

    void executeMdxQuery(String sessionId, String mdxQueryId, AsyncCallback<CellDataSet> callback);

    void setMdxQuery(String sessionId, String mdxQueryId, String mdx, AsyncCallback<Object> callback);
    
    void getMdxQuery(String sessionId, String mdxQueryId, AsyncCallback<String> callback);

    void saveQuery(String sessionId, String queryId, String queryName, String connectionId, CubeItem cubeItem,
            String cubeName, AsyncCallback<Object> callback);

    void loadQuery(String sessionID, String currQuery, AsyncCallback<QuerySaveModel> asyncCallback);

    void getSavedQueries(String sessionId, AsyncCallback<List<QuerySaveModel>> callback);

    void setNonEmpty(String sessionId, String queryId, boolean flag, AsyncCallback<CellDataSet> callback);
    
    void alterCell(String sessionId, String queryId, String connectionId, String scenarioId, 
	    String newCellValue, AsyncCallback<CellDataSet> callback);
}