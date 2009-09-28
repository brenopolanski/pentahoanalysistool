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
package org.pentaho.pat.server.services.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;
import org.olap4j.query.Selection.Operator;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.OlapUtil;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.springframework.util.Assert;

/**
 * Simple service implementation as a Spring bean.
 * 
 * @author Luc Boudreau
 */
public class QueryServiceImpl extends AbstractService implements QueryService {
    private SessionService sessionService = null;

    private DiscoveryService discoveryService = null;

    QueryDimension qd = null;
    
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.discoveryService);
        Assert.notNull(this.sessionService);
    }

    public void clearSelection(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final List<String> memberNames) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
        final String path = OlapUtil.normalizeMemberNames(memberNames.toArray(new String[memberNames.size()]));
        final Selection selection = OlapUtil.findSelection(path, qDim);
        qDim.getInclusions().remove(selection);

    }

    public void clearSortOrder(final String userId, final String sessionId, final String queryId,
            final String dimensionName) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        query.getDimension(dimensionName).clearSort();
    }

    public String createNewQuery(final String userId, final String sessionId, final String connectionId,
            final String cubeName) throws OlapException {

        sessionService.validateUser(userId);

        if (cubeName == null)
            throw new OlapException(Messages.getString("Services.Session.NoCubeSelected")); //$NON-NLS-1$

        final Cube cube = this.getCube4Guid(userId, sessionId, connectionId, cubeName);
        final String generatedId = String.valueOf(UUID.randomUUID());
        Query newQuery;
        try {
            newQuery = new Query(generatedId, cube);
        } catch (final SQLException e) {
            throw new OlapException(Messages.getString("Services.Session.CreateQueryException"), //$NON-NLS-1$
                    e);
        }

        sessionService.getSession(userId, sessionId).getQueries().put(generatedId, newQuery);

        return generatedId;
    }

    public void createSelection(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final List<String> memberNames, final Selection.Operator selectionType)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final Query query = this.getQuery(userId, sessionId, queryId);
        final Cube cube = query.getCube();

        // First try to resolve the member quick and dirty.
        Member member = cube.lookupMember(memberNames.toArray(new String[memberNames.size()]));

        if (member == null) {
            // Let's try with only the dimension name in front.
            final List<String> dimPlusMemberNames = new ArrayList<String>();
            dimPlusMemberNames.add(dimensionName);
            dimPlusMemberNames.addAll(memberNames);
            member = cube.lookupMember(dimPlusMemberNames.toArray(new String[dimPlusMemberNames.size()]));

            if (member == null) {
                // Sometimes we need to find it in a different name format.
                // To make sure we find the member, the first element
                // will be sent as DimensionName.HierarchyName. Cubes which have
                // more than one hierarchy in a given dimension will require this
                // format anyways.
                final List<String> completeMemberNames = new ArrayList<String>();
                completeMemberNames.add(dimensionName.concat(".").concat(memberNames.get(0))); //$NON-NLS-1$
                completeMemberNames.addAll(memberNames.subList(1, memberNames.size()));
                member = cube.lookupMember(completeMemberNames.toArray(new String[completeMemberNames.size()]));

                if (member == null)
                    // We failed to find the member.
                    throw new OlapException(Messages.getString("Services.Query.Selection.CannotFindMember"));//$NON-NLS-1$
            }
        }

        final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
        final Selection.Operator selectionMode = Selection.Operator.values()[selectionType.ordinal()];
        qDim.include(selectionMode, member);
    }

    // TODO is this the way we want mdx to work?
    public CellDataSet executeMdxQuery(final String userId, final String sessionId, final String connectionId,
            final String mdx) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final OlapConnection con = this.sessionService.getNativeConnection(userId, sessionId, connectionId);
        final OlapStatement stmt = con.createStatement();
        return OlapUtil.cellSet2Matrix(stmt.executeOlapQuery(mdx));
    }

    public CellDataSet executeQuery(final String userId, final String sessionId, final String queryId)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query mdx = this.getQuery(userId, sessionId, queryId);
        CellSet cellSet = mdx.execute();
        
        OlapUtil.storeCellSet(queryId, cellSet);
        
        return OlapUtil.cellSet2Matrix(cellSet);
        
    }

    public String getHierarchizeMode(final String userId, final String sessionId, final String queryId,
            final String dimensionName) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        final HierarchizeMode hm = query.getDimension(dimensionName).getHierarchizeMode();
        String str = null;
        if (hm != null)
            str = hm.name();

        return str;

    }

    public String getMdxForQuery(final String userId, final String sessionId, final String queryId)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query q = this.getQuery(userId, sessionId, queryId);
        if (q == null)
            throw new OlapException(Messages.getString("Services.Query.NoSuchQuery")); //$NON-NLS-1$
        return q.getSelect().toString();
    }

    public List<String> getQueries(final String userId, final String sessionId) {
        sessionService.validateSession(userId, sessionId);
        final List<String> names = new ArrayList<String>();
        final Set<Entry<String, Query>> entries = sessionService.getSession(userId, sessionId).getQueries().entrySet();
        for (final Entry<String, Query> entry : entries)
            names.add(entry.getKey());
        return names;
    }

    public Query getQuery(final String userId, final String sessionId, final String queryId) {
        sessionService.validateSession(userId, sessionId);
        return sessionService.getSession(userId, sessionId).getQueries().get(queryId);
    }

    public String[][] getSelection(final String userId, final String sessionId, final String queryId,
            final String dimensionName) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final Query query = this.getQuery(userId, sessionId, queryId);

        final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);

        final List<Selection> selList = qDim.getInclusions();
        int i = 0;
        final String[][] selectionList = new String[selList.size()][2];
        for (final Selection selection : selList) {

            final Selection sel = selection;
            selectionList[i][0] = sel.getMember().getName();
            selectionList[i][1] = sel.getOperator().name();
            i++;
        }
        return selectionList;

    }

    public String getSortOrder(final String userId, final String sessionId, final String queryId,
            final String dimensionName) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        final SortOrder so = query.getDimension(dimensionName).getSortOrder();
        String str = null;
        if (so != null)
            str = so.name();

        return str;
    }

    public void moveDimension(final String userId, final String sessionId, final String queryId,
            final Axis.Standard axis, final String dimensionName) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        query.getAxes().get(axis).getDimensions().add(query.getDimension(dimensionName));
    }

    public void releaseQuery(final String userId, final String sessionId, final String queryId) {
        sessionService.validateSession(userId, sessionId);
        sessionService.getSession(userId, sessionId).getQueries().remove(queryId);
    }

    public void setDiscoveryService(final DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#setHierachizeMode(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, org.olap4j.query.QueryDimension.HierarchizeMode)
     */
    public void setHierarchizeMode(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final HierarchizeMode hierachizeMode) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);

        query.getDimension(dimensionName).setHierarchizeMode(hierachizeMode);
    }

    public void setSessionService(final SessionService service) {
        this.sessionService = service;
    }

    public void setSortOrder(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final SortOrder sortOrder) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        query.getDimension(dimensionName).sort(sortOrder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#swapAxis(java.lang.String, java.lang.String)
     */
    public CellDataSet swapAxis(final String userId, final String sessionId, final String queryId) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final Query q = this.getQuery(userId, sessionId, queryId);
        if (q == null)
            throw new OlapException(Messages.getString("Services.Query.NoSuchQuery")); //$NON-NLS-1$
        else
            q.swapAxes();

        return executeQuery(userId, sessionId, queryId);

    }

    private Cube getCube4Guid(final String userId, final String sessionId, final String connectionId,
            final String cubeName) throws OlapException {
        final OlapConnection connection = sessionService.getNativeConnection(userId, sessionId, connectionId);
        final NamedList<Cube> cubes = connection.getSchema().getCubes();
        Cube cube = null;
        final Iterator<Cube> iter = cubes.iterator();
        while (iter.hasNext() && cube == null) {
            final Cube testCube = iter.next();
            if (cubeName.equals(testCube.getName()))
                cube = testCube;
        }
        if (cube != null)
            return cube;

        throw new OlapException(Messages.getString("Services.Session.CubeNameNotValid")); //$NON-NLS-1$
    }
    
    public void drillPosition(final String userId, final String sessionId, String queryId, MemberCell member) {
        this.sessionService.validateSession(userId, sessionId);
        Query q = getQuery(userId, sessionId, queryId);
        CellSet cellSet = OlapUtil.getCellSet(queryId);
        qd = OlapUtil.getQueryDimension(q, member.getParentDimension());
        Member memberFetched = OlapUtil.getMember(q, qd, member, cellSet);
           NamedList<? extends Member> childmembers = null;
            try {
                childmembers = memberFetched.getChildMembers();
            } catch (OlapException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            if (childmembers!=null){
               if(!member.isExpanded())
                for (int i = 0; i<childmembers.size(); i++){
                    qd.include(childmembers.get(i));

                }
               else
                   for (int i = 0; i<childmembers.size(); i++){
                       try {
                        removeChildren(childmembers.get(i).getChildMembers());
                        
                    } catch (OlapException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    qd.exclude(childmembers.get(i));
                   }
            }
        }
    
    private void removeChildren(NamedList<? extends Member> list){
        for (int i = 0; i<list.size(); i++){
            qd.exclude(list.get(i));
            try {
                removeChildren(list.get(i).getChildMembers());
            } catch (OlapException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    

}
