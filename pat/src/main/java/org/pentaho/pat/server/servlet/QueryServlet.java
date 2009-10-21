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
package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;
import org.pentaho.pat.rpc.IQuery;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.data.pojo.SavedQuery;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;

import com.thoughtworks.xstream.XStream;


/**
 * @author luc Boudreau
 *
 */
public class QueryServlet extends AbstractServlet implements IQuery {

    private static final long serialVersionUID = 1L;

    private transient QueryService queryService;

    private transient SessionService sessionService;

    private transient Logger log = Logger.getLogger(QueryServlet.class);

    public void init() throws ServletException {
        super.init();
        queryService = (QueryService)applicationContext.getBean("queryService"); //$NON-NLS-1$
        sessionService = (SessionService)applicationContext.getBean("sessionService"); //$NON-NLS-1$
        if (queryService==null)
            throw new ServletException(Messages.getString("Servlet.QueryServiceNotFound")); //$NON-NLS-1$
        if (sessionService==null)
            throw new ServletException(Messages.getString("Servlet.SessionServiceNotFound")); //$NON-NLS-1$
    }

    public String createNewQuery(String sessionId, String connectionId, String cubeName) throws RpcException
    {
        try {
            return queryService.createNewQuery(getCurrentUserId(), sessionId, connectionId, cubeName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantCreateQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    public void deleteQuery(String sessionId, String queryId) throws RpcException
    {
        queryService.releaseQuery(getCurrentUserId(), sessionId, queryId);
    }

    public String[] getQueries(String sessionId) throws RpcException
    {
        List<String> list = queryService.getQueries(getCurrentUserId(), sessionId);
        return list.toArray(new String[list.size()]);
    }

    public void clearSelection(
            String sessionId,
            String queryId,
            String dimensionName, 
            List<String> memberNames) throws RpcException
            {
        this.queryService.clearSelection(getCurrentUserId(), 
                sessionId, queryId, dimensionName, memberNames);
            }

    public void clearExclusion(
            String sessionId,
            String queryId,
            String dimensionName) throws RpcException
            {
        this.queryService.clearExclusion(getCurrentUserId(), 
                sessionId, queryId, dimensionName);
            }

    public void createSelection(
            String sessionId,
            String queryId,
            String dimensionName, 
            List<String> memberNames, 
            String selectionType) throws RpcException
            {
        try {
            this.queryService.createSelection(getCurrentUserId(), sessionId, 
                    queryId, dimensionName, memberNames, org.olap4j.query.Selection.Operator.valueOf(selectionType));
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantSelectMembers"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
            }

    public void createExclusion(
            String sessionId,
            String queryId,
            String dimensionName, 
            List<String> memberNames) throws RpcException
            {
        try {
            this.queryService.createExclusion(getCurrentUserId(), sessionId, 
                    queryId, dimensionName, memberNames);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantSelectMembers"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
            }

    public String[][] getSelection(
            String sessionId,
            String queryId,
            String dimensionName) throws RpcException
            {
        try {
            return this.queryService.getSelection(getCurrentUserId(), sessionId, 
                    queryId, dimensionName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantGetMembers"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantGetMembers")); //$NON-NLS-1$
        }

            }
    public void moveDimension(
            String sessionId,
            String queryId,
            IAxis axis, 
            String dimensionName) throws RpcException
            {
        this.queryService.moveDimension(
                getCurrentUserId(), 
                sessionId, 
                queryId,
                (axis.equals(IAxis.UNUSED))?null:org.olap4j.Axis.Standard.valueOf(axis.name()), 
                        dimensionName);
            }
    public void setSortOrder(String sessionId, String queryId, String dimensionName, String sort) throws RpcException
    {
        SortOrder sortValue = null;
        for(SortOrder v : SortOrder.values()){
            if( v.name().equals(sort)){
                sortValue = v;
            }
        }

        try {
            this.queryService.setSortOrder(getCurrentUserId(), sessionId, queryId, dimensionName, sortValue);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantSort"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSort")); //$NON-NLS-1$
        }
    }

    public void clearSortOrder(String sessionId, String queryId, String dimensionName) throws RpcException
    {
        try {
            this.queryService.clearSortOrder(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantClearSortOrder"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantClearSortOrder")); //$NON-NLS-1$
        }
    }

    public String getSortOrder(String sessionId, String queryId, String dimensionName) throws RpcException
    {
        try {
            return this.queryService.getSortOrder(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantGetSortOrder"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantGetSortOrder")); //$NON-NLS-1$
        }

    }

    public void setHierarchizeMode(String sessionId, String queryId, String dimensionName, String mode) throws RpcException
    {
        HierarchizeMode hMode = null;
        for(HierarchizeMode v : HierarchizeMode.values()){
            if( v.name().equals(mode)){
                hMode = v;
            }
        }

        try {
            this.queryService.setHierarchizeMode(getCurrentUserId(), sessionId, queryId, dimensionName, hMode);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantSetHierarchizeMode"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSethierarchizeMode")); //$NON-NLS-1$
        }
    }

    public String getHierarchizeMode(String sessionId, String queryId, String dimensionName) throws RpcException
    {
        try {
            return this.queryService.getHierarchizeMode(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantGetHierarchizeMode"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantGetHierarchizeMode")); //$NON-NLS-1$
        }
    }

    public CellDataSet swapAxis(String sessionId, String queryId) throws RpcException{

        try {
            return this.queryService.swapAxis(getCurrentUserId(), sessionId, queryId);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantSwapAxis"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSwapAxis")); //$NON-NLS-1$
        }



    }
    public CellDataSet executeQuery(String sessionId, String queryId) throws RpcException
    {
        try {
            return this.queryService.executeQuery(getCurrentUserId(), sessionId, queryId);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantExecuteQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }

    public void drillPosition(String sessionId, String queryId, MemberCell member) throws RpcException
    {
        try {
            this.queryService.drillPosition(getCurrentUserId(), sessionId, queryId, member);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantExecuteQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }
    
    public String createNewMdxQuery(String sessionId, String connectionId, String catalogName) throws RpcException
    {
        try {
            return queryService.createNewMdxQuery(getCurrentUserId(), sessionId, connectionId, catalogName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantCreateQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    public String createNewMdxQuery(String sessionId, String connectionId, String catalogName, String mdx) throws RpcException
    {
        try {
            return queryService.createNewMdxQuery(getCurrentUserId(), sessionId, connectionId, catalogName, mdx);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantCreateQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    public String[] getMdxQueries(String sessionId) throws RpcException
    {
        List<String> list = queryService.getMdxQueries(getCurrentUserId(), sessionId);
        return list.toArray(new String[list.size()]);
    }
    
    public void deleteMdxQuery(String sessionId, String mdxQueryId) throws RpcException
    {
        queryService.releaseMdxQuery(getCurrentUserId(), sessionId, mdxQueryId);
    }
    
    @Deprecated
    public CellDataSet executeMdxQuery(String sessionId, String connectionId, String mdx) throws RpcException
    {
        try {
            return this.queryService.executeMdxQuery(getCurrentUserId(), sessionId, connectionId, mdx);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantExecuteQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }
    
    public CellDataSet executeMdxQuery(String sessionId, String mdxQueryId) throws RpcException 
    {
        try {
            return this.queryService.executeMdxQuery(getCurrentUserId(), sessionId, mdxQueryId);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantExecuteQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }
    
    public void setMdxQuery(String sessionId, String mdxQueryId, String mdx) throws RpcException
    {
            try {
                this.queryService.setMdxQuery(getCurrentUserId(), sessionId, mdxQueryId, mdx);
            } catch (OlapException e) {
                log.error("can't set mdx query",e); //$NON-NLS-1$
                throw new RpcException("can't set mdx query"); //$NON-NLS-1$

            }
    }

    public String getMdxForQuery(String sessionId, String queryId)
    throws RpcException {
        try {
            return this.queryService.getMdxForQuery(getCurrentUserId(), sessionId, queryId);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantFetchMdx"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantFetchMdx")); //$NON-NLS-1$
        }

    }

    public void saveQuery(String sessionId, String queryId, String queryName, String connectionId) throws RpcException{
        try{
        Query qm = this.queryService.getQuery(getCurrentUserId(), sessionId, queryId);
        
        SavedQuery sc = this.convert(qm, queryName, connectionId);
        this.queryService.saveQuery(getCurrentUserId(), sessionId, sc);
        } catch (Exception e) {
            log.error(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"),e); //$NON-NLS-1$
        }
    }
    
    private SavedQuery convert(Query cc, String queryName, String connectionId) {
        XStream xstream = new XStream(); 
        
        String xml = xstream.toXML(cc);
        SavedQuery sc = new SavedQuery(cc.toString());
        
        sc.setName(queryName);
        sc.setUsername(getCurrentUserId());
        sc.setXml(xml);
        sc.setConnectionId(connectionId);
        return sc;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.rpc.IQuery#loadQuery(java.lang.String, java.lang.String)
     */
    public void loadQuery(String sessioinId, String queryId) throws RpcException {
        try{
            Query qm = this.queryService.getQuery(getCurrentUserId(), sessioinId, queryId);
            
             
             SavedQuery sc = this.queryService.loadQuery(getCurrentUserId(), sessioinId, qm.toString());
        
             XStream xstream = new XStream(); 
             
             Query newQuery = (Query)xstream.fromXML(sc.getXml());
             
            } catch (Exception e) {
                log.error(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"),e); //$NON-NLS-1$
                throw new RpcException(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"),e); //$NON-NLS-1$
            }
        
    }
}
