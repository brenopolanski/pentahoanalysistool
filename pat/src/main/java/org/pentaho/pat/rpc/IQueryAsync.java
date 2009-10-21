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
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.exceptions.RpcException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This interface defines the operations permitted on a query
 * object that was previously instanciated through the Session service.
 * @author Luc Boudreau
 */

public interface IQueryAsync {
	
    public void moveDimension(
            String sessionId,
            String queryId,
            IAxis axis, 
            String dimensionName,
            AsyncCallback<Object> callback);
	
	public void createSelection(
	        String sessionId,
	        String queryId,
	        String dimensionName, 
	        List<String> memberNames, 
	        String selectionType,
	        AsyncCallback<Object> callback);	
	
	public void createExclusion(
            String sessionId,
            String queryId,
            String dimensionName, 
            List <String> memberNames,
            AsyncCallback<Object> callback);    
    
	public void clearSelection(
	        String sessionId,
	        String queryId,
	        String dimensionName, 
	        List<String> memberNames,
	        AsyncCallback<Object> callback);
	
	public void clearExclusion(
            String sessionId,
            String queryId,
            String dimensionName,
            AsyncCallback<Object> callback);
	
	public void executeQuery(
	        String sessionId, 
            String queryId,
            AsyncCallback<CellDataSet> callback);
	
	public void executeMdxQuery(
	        String sessionId, 
            String connectionId, 
            String mdx,
			AsyncCallback<CellDataSet> callback);
	
	public void createNewQuery(String sessionId, String connectionId, String cubeName, AsyncCallback<String> callback);
    
    public void getQueries(String sessionId, AsyncCallback<String[]> callback);

    public void deleteQuery(String sessionId, String queryId, AsyncCallback<Object> callback);
    
    public void getMdxForQuery(
            String sessionId, 
            String queryId,
            AsyncCallback<String> callback);
    
    public void getSelection(
            String sessionId,
            String queryId,
            String dimensionName, 
            AsyncCallback<String[][]> callback);
    
    public void swapAxis(
            String sessionId, 
            String queryId,
            AsyncCallback<CellDataSet> callback);
    
   public void setSortOrder(
           String sessionId,
           String queryId,
           String dimensionName,
           String sort,
           AsyncCallback<Object> callback);
   
   public void clearSortOrder(
           String sessionId,
           String queryId,
           String dimensionName,
           AsyncCallback<Object> callback);
   
   public void getSortOrder(
           String sessionId,
           String queryId,
           String dimensionName,
           AsyncCallback<String> callback);
   
   public void setHierarchizeMode(
           String sessionId, 
           String queryId, 
           String dimensionName, 
           String mode,
           AsyncCallback<Object> callback);
   
   public void getHierarchizeMode(
           String sessionId,
           String queryId,
           String dimensionName,
           AsyncCallback<String> callback);
      
   public void drillPosition(
           String sessionId, 
           String queryId, 
           MemberCell member,
           AsyncCallback<Object> callback);
   
   public void createNewMdxQuery(String sessionId, String connectionId, String catalogName, 
           AsyncCallback<Object> callback);

   public void createNewMdxQuery(String sessionId, String connectionId, String catalogName, String mdx,
           AsyncCallback<String> callback);

   public void getMdxQueries(String sessionId,
           AsyncCallback<String[]> callback);

   public void deleteMdxQuery(String sessionId, String mdxQueryId,
           AsyncCallback<Object> callback);

   public void executeMdxQuery(String sessionId, String mdxQueryId,
           AsyncCallback<CellDataSet> callback);

   public void setMdxQuery(String sessionId, String mdxQueryId, String mdx,
           AsyncCallback<Object> callback);

   public void saveQuery(
           String sessionId,
           String queryId,
           String queryName,
           String connectionId,
           AsyncCallback<Object> callback);


   public void loadQuery(
           String sessionID, 
           String currQuery, 
           AsyncCallback asyncCallback);
    }
