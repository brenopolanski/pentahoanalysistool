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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;
import org.pentaho.pat.rpc.IQuery;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.QuerySaveModel;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;
import org.pentaho.pat.rpc.dto.enums.QueryType;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.data.pojo.SavedQuery;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.util.MdxQuery;

import com.thoughtworks.xstream.XStream;

/**
 * @author luc Boudreau
 * 
 */
public class QueryServlet extends AbstractServlet implements IQuery {

    private static final long serialVersionUID = 1L;

    private QueryService queryService;

    private final static Logger LOG = Logger.getLogger(QueryServlet.class);

    // THIS IS DIRTY AND JUST A QUICK HACK
    private static final List<String> bootstrapQueries = new ArrayList<String>();

    public void addBootstrapQuery(String queryId) {
        bootstrapQueries.add(queryId);
    }

    public List<String> getBootstrapQueries() {
        return bootstrapQueries;
    }


    public void init() throws ServletException {
        super.init();
        queryService = (QueryService) applicationContext.getBean("queryService"); //$NON-NLS-1$
        final SessionService sessionService = (SessionService) applicationContext.getBean("sessionService"); //$NON-NLS-1$
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
    public void clearSelection(final String sessionId, final String queryId, final String dimensionName,
            final List<String> memberNames) throws RpcException {
        this.queryService.clearSelection(getCurrentUserId(), sessionId, queryId, dimensionName, memberNames);
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
    public void createSelection(final String sessionId, final String queryId, final String dimensionName,
            final List<String> memberNames, final String selectionType) throws RpcException {
        try {
            this.queryService.createSelection(getCurrentUserId(), sessionId, queryId, dimensionName, memberNames,
                    org.olap4j.query.Selection.Operator.valueOf(selectionType));
        } catch (OlapException e) {
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
    public void createSelection(final String sessionId, final String queryId, final String dimensionName,
            final String selectionType) throws RpcException {
        try {
            this.queryService.createSelection(getCurrentUserId(), sessionId, queryId, dimensionName,
                    org.olap4j.query.Selection.Operator.valueOf(selectionType));
        } catch (OlapException e) {
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
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantSelectMembers"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
    }

    public void alterCell(final String sessionId, final String queryId, final String connectionId, final String scenarioId,  
            final String newCellValue)throws RpcException {
        try {
            this.queryService.alterCell(getCurrentUserId(), queryId, sessionId, scenarioId, connectionId, newCellValue);
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        this.queryService.moveDimension(getCurrentUserId(), sessionId, queryId,
                (axis.equals(IAxis.UNUSED)) ? null : org.olap4j.Axis.Standard.valueOf(axis.name()), dimensionName);
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
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
        } catch (OlapException e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
        }
    }

    public String[][] drillThrough(final String sessionId, final String queryId, final List<Integer> coordinates) throws RpcException {
        try {
            ResultSet rs = this.queryService.drillThrough(getCurrentUserId(), sessionId, queryId, coordinates);
            Integer width = 0;
            Integer height = 0;
            String[] header = null;
            List<String[]> rows = new ArrayList<String[]>();
//            System.out.println("DATASET");
            while(rs.next()) {
                if (height == 0) {
                    width = rs.getMetaData().getColumnCount();
                    header = new String[width];
                    for (int s = 0;s<width;s++) {
                        header[s] =rs.getMetaData().getColumnName(s+1);
//                        System.out.print(" |\t" + header[s]);
                    }
                    if (width > 0) {
                        rows.add(header);
//                        System.out.println(" |");
                    }
                }
                String[] row = new String[width];
                for (int i = 0;i<width;i++) {
                    
                        row[i] = rs.getString(header[i]);
                        if (row[i] == null )
                            row[i] = "";
//                        if(height < 10) {
//                            System.out.print(" |\t" + row[i]);
//                        }
                    
                }
//                if(height < 10) {
//                    System.out.println(" |");
                    rows.add(row);

//                }
                height++;
            }
            height = 11;
            String[][] result = rows.toArray(new String[height][width]);
//            for (int i = 0; i<rows.size();i++) {
//                String[] row = rows.get(i);
//                result[i] = row;
//            }
            LOG.debug("Drill-Through Result: Rows : " + rows.size() + " Width: " + width);
            return result;
            
        } catch (SQLException e) {
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
            if (qm != null) {
                sc = this.convert((Object)qm, queryId, queryName, connectionId, cube, cubeName);
                sc.setQueryType(QueryType.QM);
            }
            else if (mdxq != null) {
                sc = this.convert((Object)mdxq,queryId, queryName, connectionId, cube, cubeName);
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
    private SavedQuery convert(final Object cc,final String queryId, final String queryName, final String connectionId, final CubeItem cube,
            final String cubeName) {
        final XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        xstream.alias("query", org.olap4j.query.Query.class); //$NON-NLS-1$
        xstream.alias("axis", org.olap4j.query.QueryAxis.class); //$NON-NLS-1$
        xstream.alias("axisstandard", org.olap4j.Axis.Standard.class); //$NON-NLS-1$
        xstream.alias("querydimension", org.olap4j.query.QueryDimension.class); //$NON-NLS-1$
        xstream.alias("selection", org.olap4j.query.Selection.class); //$NON-NLS-1$
        xstream.setClassLoader(org.olap4j.query.Query.class.getClassLoader());
        final String xml = xstream.toXML(cc);
        final SavedQuery sc = new SavedQuery(cc.toString());
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

            final Iterator<SavedQuery> it = ssc.iterator();
            final List<QuerySaveModel> results = new ArrayList<QuerySaveModel>();
            while (it.hasNext()) {
                // Get element
                final SavedQuery element = (SavedQuery) it.next();

                results.add(new QuerySaveModel(element.getId(), element.getQueryId(), element.getName(), element.getConnectionId(), element
                        .getCube(), element.getCubeName(), element.getUpdatedDate(), element.getQueryType()));

            }

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
                    if(bootstrapQueries.get(k).equals(results.get(i).getQueryId())) {
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
    public QuerySaveModel loadQuery(final String sessionId, final String queryId) throws RpcException {
        try {

            final SavedQuery sc = this.queryService.loadQuery(getCurrentUserId(), sessionId, queryId);

            final XStream xstream = new XStream();

            xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
            xstream.alias("query", org.olap4j.query.Query.class); //$NON-NLS-1$
            xstream.alias("axis", org.olap4j.query.QueryAxis.class); //$NON-NLS-1$
            xstream.alias("axisstandard", org.olap4j.Axis.Standard.class); //$NON-NLS-1$
            xstream.alias("querydimension", org.olap4j.query.QueryDimension.class); //$NON-NLS-1$
            xstream.alias("selection", org.olap4j.query.Selection.class); //$NON-NLS-1$
            xstream.setClassLoader(org.olap4j.query.Query.class.getClassLoader());
            Object oQuery = xstream.fromXML(sc.getXml());
            String createdQueryId = null;
            if (oQuery instanceof Query) {
                createdQueryId = this.queryService.createSavedQuery(getCurrentUserId(), sessionId, 
                        sc.getConnectionId(), (Query) oQuery);

            }
            if (oQuery instanceof MdxQuery) {
                createdQueryId = this.queryService.createSavedQuery(getCurrentUserId(), sessionId, sc.getConnectionId(),(MdxQuery) oQuery);
            }


            return new QuerySaveModel(createdQueryId, sc.getQueryId(), sc.getName(), sc.getConnectionId(), sc.getCube(), sc.getCubeName(), sc.getUpdatedDate(), sc.getQueryType());

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


}
