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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;
import org.pentaho.pat.rpc.IQuery;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.QuerySaveModel;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.enums.QueryType;
import org.pentaho.pat.rpc.dto.enums.SelectionType;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.dto.query.PatQueryAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.SavedQuery;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.util.MdxQuery;
import org.pentaho.pat.server.util.serializer.MdxQuery2;
import org.pentaho.pat.server.util.serializer.PatQuery;
import org.pentaho.pat.server.util.serializer.QmQuery;
import org.pentaho.pat.server.util.serializer.QueryDeserializer;
import org.pentaho.pat.server.util.serializer.QuerySerializer;

/**
 * @author Paul Stoellberger, Tom Barber
 * 
 */
public class QueryServlet extends AbstractServlet implements IQuery {

    private static final long serialVersionUID = 1L;

    private QueryService queryService;
    
    private SessionService sessionService; 

    private final static Logger LOG = Logger.getLogger(QueryServlet.class);

    // THIS IS DIRTY AND JUST A QUICK HACK
    private static final List<String> bootstrapQueries = new ArrayList<String>();

    public void addBootstrapQuery(String sessionId, String connectionId, String queryName,  String queryXml) throws Exception {
        
        OlapConnection con = this.sessionService.getNativeConnection(getCurrentUserId(), sessionId, connectionId);
        PatQuery qs2 = QueryDeserializer.unparse(queryXml, con);
        SavedQuery sq = new SavedQuery();
        if (qs2 instanceof QmQuery && qs2.getQuery() != null) {
            sq.setConnectionId(connectionId);
            sq.setQueryId(qs2.getQuery().getName());
            sq.setCubeName(qs2.getCubeName());
            sq.setCube(new CubeItem(qs2.getCubeName(),qs2.getCatalogName(), qs2.getCube().getSchema().getName()));
            sq.setName(queryName);
            sq.setUsername(getCurrentUserId());
            sq.setXml(queryXml);
            sq.setUpdatedDate(new Date());
            sq.setQueryType(QueryType.QM);
            
        }
        else if (qs2 instanceof MdxQuery2) {
            MdxQuery mdx = new MdxQuery(qs2.getName(),con, qs2.getCatalogName(),qs2.getMdx());
            sq.setConnectionId(connectionId);
            sq.setQueryId(mdx.getId());
            sq.setCubeName("");
            sq.setCube(new CubeItem("",qs2.getCatalogName(), ""));
            sq.setName(queryName);
            sq.setUsername(getCurrentUserId());
            sq.setXml(queryXml);
            sq.setUpdatedDate(new Date());
            sq.setQueryType(QueryType.MDX);
        }
        
        if (sq != null ) {
            this.queryService.saveQuery(getCurrentUserId(), sessionId, sq);
        }
        
        bootstrapQueries.add(sq.getName());
    }

    public List<String> getBootstrapQueries() {
        return bootstrapQueries;
    }


    public void init() throws ServletException {
        super.init();
        queryService = (QueryService) applicationContext.getBean("queryService"); //$NON-NLS-1$
        sessionService = (SessionService) applicationContext.getBean("sessionService"); //$NON-NLS-1$
        if (queryService == null) {
            throw new ServletException(Messages.getString("Servlet.QueryServiceNotFound")); //$NON-NLS-1$
        }
        if (sessionService == null) {
            throw new ServletException(Messages.getString("Servlet.SessionServiceNotFound")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#createNewQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    public String createNewQuery(final String sessionId, final String connectionId, final String cubeName)
    throws RpcException {
        try {
            return queryService.createNewQuery(getCurrentUserId(), sessionId, connectionId, cubeName);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#deleteQuery(java.lang.String, java.lang.String)
     */
    public void deleteQuery(final String sessionId, final String queryId) throws RpcException {
        queryService.releaseQuery(getCurrentUserId(), sessionId, queryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#getQueries(java.lang.String)
     */
    public String[] getQueries(final String sessionId) throws RpcException {
        final List<String> list = queryService.getQueries(getCurrentUserId(), sessionId);
        return list.toArray(new String[list.size()]);
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#clearSelection(java.lang.String, java.lang.String, java.lang.String,
     * java.util.List)
     */
    public void clearSelection(final String sessionId, final String queryId, final String dimensionName, List<String> currentSelections) throws RpcException {
        this.queryService.clearSelection(getCurrentUserId(), sessionId, queryId, dimensionName, currentSelections);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#clearExclusion(java.lang.String, java.lang.String, java.lang.String)
     */
    public void clearExclusion(final String sessionId, final String queryId, final String dimensionName)
    throws RpcException {
        this.queryService.clearExclusion(getCurrentUserId(), sessionId, queryId, dimensionName);
    }



    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#createSelection(java.lang.String, java.lang.String, java.lang.String,
     * java.util.List, java.lang.String)
     */
    public List<String> createSelection(final String sessionId, final String queryId, String uniqueName,
            ObjectType type, final SelectionType selectionType) throws RpcException {
        try {
            
            return this.queryService.createSelection(getCurrentUserId(), sessionId, queryId, uniqueName, type,
                    selectionType);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantSelectMembers"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
    }


    
    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#createSelection(java.lang.String, java.lang.String, java.lang.String,
     * java.util.List, java.lang.String)
     */
    public StringTree getSpecificMembers(final String sessionId, final String queryId, String uniqueName,
            ObjectType type, final SelectionType selectionType) throws RpcException {
        try {
            return this.queryService.getSpecificMembers(getCurrentUserId(), sessionId, queryId, uniqueName, type,
                    org.olap4j.query.Selection.Operator.valueOf(selectionType.toString()));
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantSelectMembers"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
    }

    

    
    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#createExclusion(java.lang.String, java.lang.String, java.lang.String,
     * java.util.List)
     */
    public void createExclusion(final String sessionId, final String queryId, final String dimensionName,
            final List<String> memberNames) throws RpcException {
        try {
            this.queryService.createExclusion(getCurrentUserId(), sessionId, queryId, dimensionName, memberNames);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantSelectMembers"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
    }

    public void alterCell(final String sessionId, final String queryId, final String connectionId, final String scenarioId,  
            final String newCellValue)throws RpcException {
        try {
            this.queryService.alterCell(getCurrentUserId(), queryId, sessionId, scenarioId, connectionId, newCellValue);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#getSelection(java.lang.String, java.lang.String, java.lang.String)
     */
    public String[][] getSelection(final String sessionId, final String queryId, final String dimensionName)
    throws RpcException {
        try {
            return this.queryService.getSelection(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantGetMembers"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantGetMembers")); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#moveDimension(java.lang.String, java.lang.String, org.pentaho.pat.rpc.dto.IAxis,
     * java.lang.String)
     */
    public void moveDimension(final String sessionId, final String queryId, final IAxis axis, final String dimensionName)
    throws RpcException {
        try {
            this.queryService.moveDimension(getCurrentUserId(), sessionId, queryId,
                    (axis.equals(IAxis.UNUSED)) ? null : org.olap4j.Axis.Standard.valueOf(axis.name()), dimensionName);

        } catch (Exception e) {
            // TODO add localization
            LOG.error(e.getMessage(), e); //$NON-NLS-1$
            throw new RpcException(e.getMessage()); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#setSortOrder(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public void setSortOrder(final String sessionId, final String queryId, final String dimensionName, final String sort)
    throws RpcException {
        SortOrder sortValue = null;
        for (SortOrder v : SortOrder.values()) {
            if (v.name().equals(sort)) {
                sortValue = v;
            }
        }

        try {
            this.queryService.setSortOrder(getCurrentUserId(), sessionId, queryId, dimensionName, sortValue);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantSort"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSort")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#clearSortOrder(java.lang.String, java.lang.String, java.lang.String)
     */
    public void clearSortOrder(final String sessionId, final String queryId, final String dimensionName)
    throws RpcException {
        try {
            this.queryService.clearSortOrder(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantClearSortOrder"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantClearSortOrder")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#getSortOrder(java.lang.String, java.lang.String, java.lang.String)
     */
    public String getSortOrder(final String sessionId, final String queryId, final String dimensionName)
    throws RpcException {
        try {
            return this.queryService.getSortOrder(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantGetSortOrder"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantGetSortOrder")); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#setHierarchizeMode(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public void setHierarchizeMode(final String sessionId, final String queryId, final String dimensionName,
            final String mode) throws RpcException {
        HierarchizeMode hMode = null;
        for (HierarchizeMode v : HierarchizeMode.values()) {
            if (v.name().equals(mode)) {
                hMode = v;
            }
        }

        try {
            this.queryService.setHierarchizeMode(getCurrentUserId(), sessionId, queryId, dimensionName, hMode);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantSetHierarchizeMode"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSethierarchizeMode")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#getHierarchizeMode(java.lang.String, java.lang.String, java.lang.String)
     */

    public String getHierarchizeMode(final String sessionId, final String queryId, final String dimensionName)
    throws RpcException {
        try {
            return this.queryService.getHierarchizeMode(getCurrentUserId(), sessionId, queryId, dimensionName);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantGetHierarchizeMode"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantGetHierarchizeMode")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#swapAxis(java.lang.String, java.lang.String)
     */
    public CellDataSet swapAxis(final String sessionId, final String queryId) throws RpcException {

        try {
            return this.queryService.swapAxis(getCurrentUserId(), sessionId, queryId);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantSwapAxis"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSwapAxis")); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#swapAxis(java.lang.String, java.lang.String)
     */
    public CellDataSet setNonEmpty(final String sessionId, final String queryId, final boolean flag)
    throws RpcException {

        try {
            return this.queryService.setNonEmpty(getCurrentUserId(), sessionId, queryId, flag);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantSwapAxis"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSwapAxis")); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#executeQuery(java.lang.String, java.lang.String)
     */
    public CellDataSet executeQuery(final String sessionId, final String queryId) throws RpcException {
        try {
            CellDataSet cd = this.queryService.executeQuery(getCurrentUserId(), sessionId, queryId);
            // TODO remove this later
            ExportController.exportResult = cd;
            return cd;
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#drillPosition(java.lang.String, java.lang.String,
     * org.pentaho.pat.rpc.dto.celltypes.MemberCell)
     */
    public void drillPosition(final String sessionId, final String queryId, final DrillType drillType,
            final MemberCell member) throws RpcException {
        try {
            this.queryService.drillPosition(getCurrentUserId(), sessionId, queryId, drillType, member);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }

    public String[][] drillThrough(final String sessionId, final String queryId, final List<Integer> coordinates) throws RpcException {
        try {
            return this.queryService.drillThrough(getCurrentUserId(), sessionId, queryId, coordinates);
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#createNewMdxQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    public String createNewMdxQuery(final String sessionId, final String connectionId, final String catalogName)
    throws RpcException {
        try {
            return queryService.createNewMdxQuery(getCurrentUserId(), sessionId, connectionId, catalogName);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#createNewMdxQuery(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public String createNewMdxQuery(final String sessionId, final String connectionId, final String catalogName,
            final String mdx) throws RpcException {
        try {
            return queryService.createNewMdxQuery(getCurrentUserId(), sessionId, connectionId, catalogName, mdx);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#getMdxQueries(java.lang.String)
     */
    public String[] getMdxQueries(final String sessionId) throws RpcException {
        final List<String> list = queryService.getMdxQueries(getCurrentUserId(), sessionId);
        return list.toArray(new String[list.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#deleteMdxQuery(java.lang.String, java.lang.String)
     */
    public void deleteMdxQuery(final String sessionId, final String mdxQueryId) throws RpcException {
        queryService.releaseMdxQuery(getCurrentUserId(), sessionId, mdxQueryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#executeMdxQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    @Deprecated
    public CellDataSet executeMdxQuery(final String sessionId, final String connectionId, final String mdx)
    throws RpcException {
        try {
            return this.queryService.executeMdxQuery(getCurrentUserId(), sessionId, connectionId, mdx);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#executeMdxQuery(java.lang.String, java.lang.String)
     */
    public CellDataSet executeMdxQuery(final String sessionId, final String mdxQueryId) throws RpcException {
        try {
            return this.queryService.executeMdxQuery(getCurrentUserId(), sessionId, mdxQueryId);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#setMdxQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    public void setMdxQuery(final String sessionId, final String mdxQueryId, final String mdx) throws RpcException {
        try {
            this.queryService.setMdxQuery(getCurrentUserId(), sessionId, mdxQueryId, mdx);
        } catch (OlapException e) {
            LOG.error("can't set mdx query", e); //$NON-NLS-1$
            throw new RpcException("can't set mdx query"); //$NON-NLS-1$

        }
    }


    public String getMdxQuery(final String sessionId, final String mdxQueryId) throws RpcException {

        MdxQuery mq = this.queryService.getMdxQuery(getCurrentUserId(), sessionId, mdxQueryId);
        if (mq != null) {
            return mq.getMdx();
        }

        LOG.error("can't get mdx query"); //$NON-NLS-1$
        throw new RpcException("can't get mdx query"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#getMdxForQuery(java.lang.String, java.lang.String)
     */
    public String getMdxForQuery(final String sessionId, final String queryId) throws RpcException {
        try {
            return this.queryService.getMdxForQuery(getCurrentUserId(), sessionId, queryId);
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantFetchMdx"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantFetchMdx")); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#saveQuery(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * org.pentaho.pat.rpc.dto.CubeItem, java.lang.String)
     */
    public void saveQuery(final String sessionId, final String queryId, final String queryName,
            final String connectionId, final CubeItem cube, final String cubeName) throws RpcException {
        try {
            final Query qm = this.queryService.getQuery(getCurrentUserId(), sessionId, queryId);
            final MdxQuery mdxq = this.queryService.getMdxQuery(getCurrentUserId(), sessionId, queryId);
            SavedQuery sc = null;
            OlapConnection con = this.sessionService.getNativeConnection(getCurrentUserId(), sessionId, connectionId);
            if (con == null)
                throw new Exception("Cannot find open connection with ID:" + connectionId);
            
            if (qm != null) {
                
                PatQuery qs = new QmQuery(con,qm);
                sc = this.convert(qs, queryId, queryName, connectionId, cube, cubeName);
                sc.setQueryType(QueryType.QM);

            }
            else if (mdxq != null) {
                MdxQuery2 mdx2 = new MdxQuery2(con, mdxq.getCatalogName());
                mdx2.setMdx(mdxq.getMdx());
                sc = this.convert(mdx2,queryId, queryName, connectionId, cube, cubeName);
                sc.setQueryType(QueryType.MDX);
            }
            if (sc != null ) {
                this.queryService.saveQuery(getCurrentUserId(), sessionId, sc);
            }
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.QuerySaveError"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.QuerySaveError"), e); //$NON-NLS-1$
        }
    }
    
    public void deleteSavedQuery(final String sessionId, final String queryName) throws RpcException {
        try {
            if (queryName != null ) {
                this.queryService.deleteQuery(getCurrentUserId(), sessionId, queryName);
            }
        } catch (Exception e) {
            LOG.error(Messages.getString("Services.Query.QueryDeleteError"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Services.Query.QueryDeleteError"), e); //$NON-NLS-1$
        }
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param cc
     * @param queryName
     * @param connectionId
     * @param cube
     * @param cubeName
     * @return
     */
    protected SavedQuery convert(final PatQuery cc,final String queryId, final String queryName, final String connectionId, final CubeItem cube,
            final String cubeName) {
//        final XStream xstream = new XStream();
//        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
//        xstream.alias("query", org.olap4j.query.Query.class); //$NON-NLS-1$
//        xstream.alias("axis", org.olap4j.query.QueryAxis.class); //$NON-NLS-1$
//        xstream.alias("axisstandard", org.olap4j.Axis.Standard.class); //$NON-NLS-1$
//        xstream.alias("querydimension", org.olap4j.query.QueryDimension.class); //$NON-NLS-1$
//        xstream.alias("selection", org.olap4j.query.Selection.class); //$NON-NLS-1$
//        xstream.setClassLoader(org.olap4j.query.Query.class.getClassLoader());
//        final String xml = xstream.toXML(cc);
        QuerySerializer qser = new QuerySerializer(cc);
        String xml = null;
        try {
            xml = qser.createXML();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final SavedQuery sc = new SavedQuery();
        sc.setQueryId(queryId);
        sc.setCubeName(cubeName);
        sc.setCube(cube);
        sc.setName(queryName);
        sc.setUsername(getCurrentUserId());
        sc.setXml(xml);
        sc.setConnectionId(connectionId);
        sc.setUpdatedDate(new Date());


        /*String file = "file.txt";
        try{
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(xml);
                out.close();
        }catch(IOException ex){
                ex.printStackTrace();
        }*/

        return sc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#getSavedQueries(java.lang.String)
     */
    public List<QuerySaveModel> getSavedQueries(final String sessionId) throws RpcException {
        try {
            final Set<SavedQuery> ssc = this.queryService.getSavedQueries(getCurrentUserId(), sessionId);

            final List<QuerySaveModel> results = new ArrayList<QuerySaveModel>();
            for (SavedQuery savedQuery : ssc) {
                SavedConnection sc = sessionService.getConnection(getCurrentUserId(), savedQuery.getConnectionId());
                if (sc == null) {
                    throw new Exception("Connection for query doesn't exist");
                }
                
                results.add(new QuerySaveModel(savedQuery.getId(), savedQuery.getQueryId(), savedQuery.getName(), SessionServlet.convert(sc),savedQuery
                        .getCube(), savedQuery.getCubeName(), savedQuery.getUpdatedDate(), savedQuery.getQueryType()));

            }
            Collections.sort(results);
            return results;
        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.GetSavedQueriesError"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.GetSavedQueriesError"), e); //$NON-NLS-1$
        }
    }

    public List<QuerySaveModel> getActiveQueries(final String sessionId) throws RpcException {
        final List<QuerySaveModel> activeQueries = new ArrayList<QuerySaveModel>();
        try {
            final List<QuerySaveModel> results = getSavedQueries(sessionId);
            for (int i=0 ; i<results.size();i++) {
                for (int k = 0;k<bootstrapQueries.size();k++) {
                    if(bootstrapQueries.get(k).equals(results.get(i).getName())) {
                        activeQueries.add(results.get(i));
                    }
                }
            }
            bootstrapQueries.clear();
            return activeQueries;
        }
        catch (Exception e) {
            LOG.error("Failed get Active queries", e); //$NON-NLS-1$
            throw new RpcException("Failed get Active queries", e); //$NON-NLS-1$
        }


    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.rpc.IQuery#loadQuery(java.lang.String, java.lang.String)
     */
    public QuerySaveModel loadQuery(final String sessionId, final String savedQueryName) throws RpcException {
        try {

            final SavedQuery sc = this.queryService.loadQuery(getCurrentUserId(), sessionId, savedQueryName);

            if (sc == null)
                throw new Exception("Couldn't load Query with Name: " + savedQueryName);
            
            OlapConnection con = this.sessionService.getNativeConnection(getCurrentUserId(), sessionId, sc.getConnectionId());
            if (con == null) {
                this.sessionService.connect(getCurrentUserId(), sessionId, sc.getConnectionId());
                con = this.sessionService.getNativeConnection(getCurrentUserId(), sessionId, sc.getConnectionId());
            }
            if (con == null)
                throw new Exception("Cannot find connection with ID:" + sc.getConnectionId() + ". Tried to connect but failed!");
            
            PatQuery qs2 = QueryDeserializer.unparse(sc.getXml(), con);
            String createdQueryId = null;
            if (qs2 instanceof QmQuery && qs2.getQuery() != null) {
                createdQueryId = this.queryService.createSavedQuery(getCurrentUserId(), sessionId, 
                        sc.getConnectionId(), qs2.getQuery());

            }
            else if (qs2 instanceof MdxQuery2) {
                MdxQuery mdx = new MdxQuery(qs2.getName(),con, qs2.getCatalogName(),qs2.getMdx());
                createdQueryId = this.queryService.createSavedQuery(getCurrentUserId(), sessionId, sc.getConnectionId(),mdx);
            }

            if (createdQueryId == null) {
                throw new Exception("Query could not be loaded. Query Creation Failed");
            }
            SavedConnection svc = sessionService.getConnection(getCurrentUserId(), sc.getConnectionId());
            if (svc == null) {
                throw new Exception("Connection for query doesn't exist");
            }

            return new QuerySaveModel(createdQueryId, createdQueryId, sc.getName(), SessionServlet.convert(svc), sc.getCube(), sc.getCubeName(), sc.getUpdatedDate(), sc.getQueryType());

        } catch (Exception e) {
            LOG.error(Messages.getString("Servlet.Query.LoadQueryError"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.LoadQueryError"), e); //$NON-NLS-1$
        }

    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.rpc.IQuery#addProperty(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addProperty(String sessionID, String queryId, String dimensionName, String levelName, String propertyName, Boolean enabled) throws RpcException {

        this.queryService.setProperty(getCurrentUserId(), sessionID, queryId, dimensionName, levelName, propertyName, enabled);

    }



    
    public void pushDownDimension(String sessionID, String queryId, IAxis axis, int currentposition, int newposition){
    	this.queryService.pushDownDimension(getCurrentUserId(), sessionID, queryId, 
    			(axis.equals(IAxis.UNUSED)) ? null : org.olap4j.Axis.Standard.valueOf(axis.name()), currentposition, newposition);
    }
    
    
    public void pullUpDimension(String sessionID, String queryId, IAxis axis, int currentposition, int newposition){
    	this.queryService.pullUpDimension(getCurrentUserId(), sessionID, queryId, 
    			(axis.equals(IAxis.UNUSED)) ? null : org.olap4j.Axis.Standard.valueOf(axis.name()), currentposition, newposition);
    	
    }

    
	public void pullUpMeasember(String sessionID, String queryId, IAxis axis,
			int currentposition, int newposition) throws RpcException {
		this.queryService.pullUpMeasember(getCurrentUserId(), sessionID, queryId, 
    			(axis.equals(IAxis.UNUSED)) ? null : org.olap4j.Axis.Standard.valueOf(axis.name()), currentposition, newposition);
		
	}

	public void pushDownMeasember(String sessionID, String currQuery,
			IAxis axis, int currentposition, int newposition) throws RpcException {
		this.queryService.pushDownMeasember(getCurrentUserId(), sessionID, currQuery, 
    			(axis.equals(IAxis.UNUSED)) ? null : org.olap4j.Axis.Standard.valueOf(axis.name()), currentposition, newposition);
		
		
	}

    public Map<IAxis, PatQueryAxis> getSelections(String sessionId, String queryId) throws RpcException {
        return this.queryService.getSelections(getCurrentUserId(),sessionId, queryId);

    }

  






}
