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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.olap4j.AllocationPolicy;
import org.olap4j.Axis;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.Axis.Standard;
import org.olap4j.mdx.ParseTreeWriter;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.olap4j.query.SortOrder;
import org.olap4j.query.QueryDimension.HierarchizeMode;
import org.olap4j.query.Selection.Operator;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.enums.SelectionType;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.dto.query.PatQueryAxis;
import org.pentaho.pat.rpc.dto.query.PatQueryDimension;
import org.pentaho.pat.rpc.dto.query.PatQuerySelection;
import org.pentaho.pat.server.data.pojo.SavedQuery;
import org.pentaho.pat.server.data.pojo.User;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.util.MdxQuery;
import org.pentaho.pat.server.util.OlapUtil;
import org.springframework.util.Assert;

/**
 * Simple service implementation as a Spring bean.
 * 
 * @author Luc Boudreau
 */
public class QueryServiceImpl extends AbstractService implements QueryService {
    private SessionService sessionService = null;

    private DiscoveryService discoveryService = null;

    QueryDimension queryDimension = null;

    private final static Logger LOG = Logger.getLogger(QueryServiceImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.discoveryService);
        Assert.notNull(this.sessionService);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#clearSelection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.util.List)
     */
    public void clearExclusion(final String userId, final String sessionId, final String queryId,
            final String dimensionName) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);

        qDim.getExclusions().clear();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#clearSelection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.util.List)
     */
    public void clearSelection(final String userId, final String sessionId, final String queryId,
            final String uniqueName, final List<String> currentSelections) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        String[] named = QueryDimension.getNameParts(uniqueName);
        final QueryDimension qDim = OlapUtil.getQueryDimension(query, getDimensionName(named));
        // final String path = OlapUtil.normalizeMemberNames(memberNames.toArray(new String[memberNames.size()]));
        for (int i = 0; i < currentSelections.size(); i++) {

            final Selection selection = OlapUtil.findSelection(currentSelections.get(i), qDim);
            if (selection != null) {
                qDim.getInclusions().remove(selection);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#clearSortOrder(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public void clearSortOrder(final String userId, final String sessionId, final String queryId,
            final String dimensionName) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        query.getDimension(dimensionName).clearSort();
    }

    public void createExclusion(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final List<String> memberNames) throws OlapException {
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
                // more than one hierarchy in a given dimension will require
                // this
                // format anyways.
                final List<String> completeMemberNames = new ArrayList<String>();
                completeMemberNames.add(dimensionName.concat(".").concat(memberNames.get(0))); //$NON-NLS-1$
                completeMemberNames.addAll(memberNames.subList(1, memberNames.size()));
                member = cube.lookupMember(completeMemberNames.toArray(new String[completeMemberNames.size()]));

                if (member == null) {
                    // We failed to find the member.
                    String lookup = "";
                    for (String namepart : memberNames) {
                        lookup = lookup + namepart;
                    }

                    throw new OlapException(Messages.getString("Services.Query.Selection.CannotFindMember", lookup));//$NON-NLS-1$
                }
            }
        }

        final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);

        qDim.exclude(member);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#createNewQuery(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String createNewQuery(final String userId, final String sessionId, final String connectionId,
            final String cubeName) throws OlapException {

        this.sessionService.validateSession(userId, sessionId);

        if (cubeName == null) {
            throw new OlapException(Messages.getString("Services.Session.NoCubeSelected")); //$NON-NLS-1$
        }
        final Cube cube = this.getCube4Guid(userId, sessionId, connectionId, cubeName);
        final String generatedId = String.valueOf(UUID.randomUUID());
        Query newQuery;
        try {
            newQuery = new Query(generatedId, cube);
            newQuery.getAxis(Axis.COLUMNS).setNonEmpty(true);
            newQuery.getAxis(Axis.ROWS).setNonEmpty(true);
        } catch (final SQLException e) {
            throw new OlapException(Messages.getString("Services.Session.CreateQueryException"), //$NON-NLS-1$
                    e);
        }

        sessionService.getSession(userId, sessionId).getQueries().put(generatedId, newQuery);

        return generatedId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#createNewMdxQuery(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String createNewMdxQuery(final String userId, final String sessionId, final String connectionId,
            final String catalogName) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final OlapConnection connection = sessionService.getNativeConnection(userId, sessionId, connectionId);

        if (connection == null) {
            throw new OlapException(Messages.getString("Services.Session.NoSuchConnectionId")); //$NON-NLS-1$
        }
        final String generatedId = String.valueOf(UUID.randomUUID());
        MdxQuery newMdxQuery;
        newMdxQuery = new MdxQuery(generatedId, connection, catalogName);

        sessionService.getSession(userId, sessionId).getMdxQueries().put(generatedId, newMdxQuery);

        return generatedId;
    }

    public void alterCell(final String userId, final String sessionId, final String queryId, final String connectionId,
            final String scenarioId, final String newCellValue) {

        this.sessionService.validateSession(userId, sessionId);

        final Query query = this.getQuery(userId, sessionId, queryId);
        query.getAxes().get(Axis.FILTER).getDimensions().add(query.getDimension("Scenario")); //$NON-NLS-1$
        List<String> selection = new ArrayList<String>();
        selection.add("0"); //$NON-NLS-1$
        try {
            createSelection(userId, sessionId, queryId, "Scenario", selection, Operator.MEMBER); //$NON-NLS-1$
        } catch (OlapException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            executeQuery(userId, sessionId, queryId);
        } catch (OlapException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // final OlapConnection connection = sessionService.getNativeConnection(userId, sessionId, connectionId);

        CellSet cellData = OlapUtil.getCellSet(queryId);

        final Cell cell = cellData.getCell(Arrays.asList(0, 0));

        cell.setValue(123.00, AllocationPolicy.EQUAL_ALLOCATION);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#createNewMdxQuery(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public String createNewMdxQuery(final String userId, final String sessionId, final String connectionId,
            final String catalogName, final String mdx) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final OlapConnection connection = sessionService.getNativeConnection(userId, sessionId, connectionId);

        if (connection == null) {
            throw new OlapException(Messages.getString("Services.Session.NoSuchConnectionId")); //$NON-NLS-1$
        }
        final String generatedId = String.valueOf(UUID.randomUUID());
        MdxQuery newMdxQuery;
        newMdxQuery = new MdxQuery(generatedId, connection, catalogName, mdx);

        sessionService.getSession(userId, sessionId).getMdxQueries().put(generatedId, newMdxQuery);

        return generatedId;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#setMdxQuery(java.lang.String , java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public void setMdxQuery(String userId, String sessionId, String mdxQueryId, String mdx) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final MdxQuery mdxQuery = this.getMdxQuery(userId, sessionId, mdxQueryId);
        if (mdxQuery == null) {
            throw new OlapException(Messages.getString("Servlet.Query.CantSetMdx")); //$NON-NLS-1$
        }
        mdxQuery.setMdx(mdx);
        sessionService.getSession(userId, sessionId).getMdxQueries().remove(mdxQueryId);
        sessionService.getSession(userId, sessionId).getMdxQueries().put(mdxQueryId, mdxQuery);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#createSelection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.util.List, org.olap4j.query.Selection.Operator)
     */
    @Deprecated
    public void createSelection(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final List<String> memberNames, final Selection.Operator selectionType)
    throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final Query query = this.getQuery(userId, sessionId, queryId);
        final Cube cube = query.getCube();

        if (memberNames != null) {
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
                    // more than one hierarchy in a given dimension will require
                    // this
                    // format anyways.
                    final List<String> completeMemberNames = new ArrayList<String>();
                    completeMemberNames.add(dimensionName.concat(".").concat(memberNames.get(0))); //$NON-NLS-1$
                    completeMemberNames.addAll(memberNames.subList(1, memberNames.size()));
                    member = cube.lookupMember(completeMemberNames.toArray(new String[completeMemberNames.size()]));

                    if (member == null) {
                        // We failed to find the member.
                        throw new OlapException(Messages.getString("Services.Query.Selection.CannotFindMember"));//$NON-NLS-1$
                    }
                }
            }

            final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
            final Selection.Operator selectionMode = Selection.Operator.values()[selectionType.ordinal()];
            qDim.include(selectionMode, member);
        } else {

            final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
            final Selection.Operator selectionMode = Selection.Operator.values()[selectionType.ordinal()];
            qDim.include(selectionMode, cube.getDimensions().get(dimensionName).getHierarchies().get(0)
                    .getDefaultMember());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#createSelection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.util.List, org.olap4j.query.Selection.Operator)
     */
    public List<String> createSelection(String userId, String sessionId, String queryId, String uniqueName,
            ObjectType type, SelectionType selectionType) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        String[] memberNames = QueryDimension.getNameParts(uniqueName);
        final Query query = this.getQuery(userId, sessionId, queryId);
        final Cube cube = query.getCube();
        QueryDimension qDim = OlapUtil.getQueryDimension(query, getDimensionName(memberNames));
        final Selection.Operator selectionMode = Selection.Operator.values()[selectionType.ordinal()];
        if (type.equals(ObjectType.DIMENSION)) {

            qDim.include(selectionMode, cube.getDimensions().get(memberNames[0]).getHierarchies().get(0)
                    .getDefaultMember());
            List<String> memberNameList = new ArrayList<String>();
            String name = cube.getDimensions().get(memberNames[0]).getHierarchies().get(0).getDefaultMember()
            .getUniqueName();
            memberNameList.add(name);
            
            return memberNameList;
            
        } else if (type.equals(ObjectType.HIERARCHY)) {
            Hierarchy h = cube.getHierarchies().get(memberNames[0]);
            List<String> memberNameList = new ArrayList<String>();
            for (int i = 0; i < h.getRootMembers().size(); i++) {
                Member m = h.getRootMembers().get(i);
                qDim.include(selectionMode, m);
                String name = h.getRootMembers().get(i).getUniqueName();
                memberNameList.add(name);
            }

            return memberNameList;
        } else if (type.equals(ObjectType.LEVEL)) {
            List<Member> members = cube.getHierarchies().get(memberNames[0]).getLevels().get(memberNames[1])
            .getMembers();
            List<String> memberNameList = new ArrayList<String>();
            for (Member member : members) {
                qDim.include(selectionMode, member);
                String name = member.getUniqueName();

                memberNameList.add(name);
            }
            return memberNameList;
        } else if (type.equals(ObjectType.MEMBER)) {
            List<String> memberNameList = new ArrayList<String>();
            Member member = cube.lookupMember(memberNames);
            qDim.include(selectionMode, member);
            memberNameList.add(member.getUniqueName());
            /*
             * List<Member> members = cube.getHierarchies()
             * .get(memberNames[0]).getLevels().get(memberNames[1]).getMembers();
             * 
             * for (Member member : members) { if (member.getName().equals(memberNames[3])) {
             * qDim.include(selectionMode, member);
             * 
             * String name = member.getUniqueName();
             * 
             * memberNameList.add(name); } }
             */
            return memberNameList;
        } else if (type.equals(ObjectType.MEASURE)) {
            List<String> memberNameList = new ArrayList<String>();
            qDim = OlapUtil.getQueryDimension(query, "Measures");
            if (memberNames.length == 1) {
                List<Measure> members = cube.getMeasures();
                for (Member member : members) {
                    qDim.include(selectionMode, member);
                    String name = member.getUniqueName();

                    memberNameList.add(name);
                }

            } else {
                Member measure = cube.lookupMember(memberNames);
                qDim.include(selectionMode, measure);

                memberNameList.add(measure.getUniqueName());
            }

            return memberNameList;
        }

        return null;
        // return discoveryService.getSpecificMembers(userId, sessionId, queryId, memberNames.get(0), hierarchyName,
        // levelName, selectionMode);

    }

    public StringTree getSpecificMembers(final String userId, final String sessionId, final String queryId,
            String uniqueName, final ObjectType type, final Selection.Operator selectionType) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final Selection.Operator selectionMode = Selection.Operator.values()[selectionType.ordinal()];

        return discoveryService.getSpecificMembers(userId, sessionId, queryId, uniqueName, type, selectionMode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#createSelection(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, java.util.List, org.olap4j.query.Selection.Operator)
     */
    @Deprecated
    public void createSelection(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final Selection.Operator selectionType) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final Query query = this.getQuery(userId, sessionId, queryId);
        final Cube cube = query.getCube();

        NamedList<Level> levels = query.getDimension(dimensionName).getDimension().getHierarchies().get(0).getLevels();
        Member member = null;
        if (levels != null && levels.size() >= 1 && levels.get(0) != null && levels.get(0).getMembers().size() >= 1) {
            member = levels.get(0).getMembers().get(0);
        }

        if (member == null) {
            // Let's try with only the dimension name in front.
            final List<String> dimPlusMemberNames = new ArrayList<String>();
            dimPlusMemberNames.add(dimensionName);
            // dimPlusMemberNames.addAll(memberNames);
            member = cube.lookupMember(dimPlusMemberNames.toArray(new String[dimPlusMemberNames.size()]));

            if (member == null) {
                // Sometimes we need to find it in a different name format.
                // To make sure we find the member, the first element
                // will be sent as DimensionName.HierarchyName. Cubes which have
                // more than one hierarchy in a given dimension will require
                // this
                // format anyways.
                final List<String> completeMemberNames = new ArrayList<String>();
                //  completeMemberNames.add(dimensionName.concat(".").concat(memberNames.get(0))); //$NON-NLS-1$
                // completeMemberNames.addAll(memberNames.subList(1, memberNames.size()));
                member = cube.lookupMember(completeMemberNames.toArray(new String[completeMemberNames.size()]));

                if (member == null) {
                    // We failed to find the member.
                    throw new OlapException(Messages.getString("Services.Query.Selection.CannotFindMember"));//$NON-NLS-1$
                }
            }
        }

        final QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
        final Selection.Operator selectionMode = Selection.Operator.values()[selectionType.ordinal()];
        qDim.include(selectionMode, member);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#drillPosition(java.lang. String, java.lang.String,
     * java.lang.String, org.pentaho.pat.rpc.dto.celltypes.MemberCell)
     */
    public StringTree drillPosition(final String userId, final String sessionId, final String queryId,
            DrillType drillType, final MemberCell member) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = getQuery(userId, sessionId, queryId);
        final CellSet cellSet = OlapUtil.getCellSet(queryId);

        // CellSetFormatter formatter = new TraditionalCellSetFormatter();
        // formatter.format(cellSet,new PrintWriter(System.out, true));
        // System.out.flush();
        queryDimension = OlapUtil.getQueryDimension(query, member.getParentDimension());
        final Member memberFetched = OlapUtil.getMember(query, queryDimension, member, cellSet);
        // LOG.error("children: " + memberFetched.getChildMemberCount() + " member expanded:" + member.isExpanded() );
        HierarchizeMode hm = queryDimension.getHierarchizeMode();
        if (hm == null) {
            queryDimension.setHierarchizeMode(HierarchizeMode.PRE);
        }
        // Top Down Drilling
        if (memberFetched.getChildMemberCount() > 0 && ( (drillType.equals(DrillType.POSITION)) || (drillType.equals(DrillType.REPLACE)))) {
            if (!member.isExpanded()) {
                // If its the left most dimension don't do anything apart from
                // include.
                if (member.getRightOf() == null) {
                    switch (drillType) {
                    case POSITION:
                        // queryDimension.getInclusions().remove(selection);
                        // queryDimension.include(Selection.Operator.INCLUDE_CHILDREN, memberFetched);
                        queryDimension.include(Selection.Operator.CHILDREN, memberFetched);
                        break;
                    case REPLACE:
                        queryDimension.clearInclusions();
                        queryDimension.include(Selection.Operator.CHILDREN, memberFetched);
                        break;
                    default:
                        break;
                    }

                }
                else {
                    // Get the drilling member
                    MemberCell memberdrill = member;

                    if (drillType == DrillType.REPLACE) {
                        queryDimension.clearInclusions();
                        final Selection selection = queryDimension.include(Selection.Operator.CHILDREN, memberFetched);
                        // Test to see if there are populated cells to its left
                        while (memberdrill.getRightOf() != null) {

                            // If yes test to make sure its not name isn't null and
                            // it hasn't just skipped back to its all member level
                            if (memberdrill.getRightOf().getUniqueName() != null
                                    && !memberdrill.getRightOf().getUniqueName().equals(memberdrill.getParentMember())) {

                                // Get dimension to the left of the current member.
                                final QueryDimension queryDimension2 = OlapUtil.getQueryDimension(query, memberdrill
                                        .getRightOfDimension());

                                // Get the Olap4J member.
                                final Member memberFetched2 = OlapUtil.getMember(query, queryDimension2, memberdrill
                                        .getRightOf(), cellSet);

                                selection.addContext(queryDimension2.createSelection(memberFetched2));

                            }

                            // Get next member.
                            memberdrill = memberdrill.getRightOf();
                        }

                    }
                    if (drillType == DrillType.POSITION) {
                        final Selection selection = queryDimension.include(Selection.Operator.CHILDREN, memberFetched);
                        // Test to see if there are populated cells to its left
                        while (memberdrill.getRightOf() != null) {

                            // If yes test to make sure its not name isn't null and
                            // it hasn't just skipped back to its all member level
                            if (memberdrill.getRightOf().getUniqueName() != null
                                    && !memberdrill.getRightOf().getUniqueName().equals(memberdrill.getParentMember())) {

                                // Get dimension to the left of the current member.
                                final QueryDimension queryDimension2 = OlapUtil.getQueryDimension(query, memberdrill
                                        .getRightOfDimension());

                                // Get the Olap4J member.
                                final Member memberFetched2 = OlapUtil.getMember(query, queryDimension2, memberdrill
                                        .getRightOf(), cellSet);

                                selection.addContext(queryDimension2.createSelection(memberFetched2));

                            }

                            // Get next member.
                            memberdrill = memberdrill.getRightOf();
                        }

                    }
                }
            } else {

                if (drillType.equals(DrillType.POSITION)) {
                    
                    final Selection collapseSelChildren = queryDimension.createSelection(Selection.Operator.CHILDREN,
                            memberFetched);
                    final Selection collapseSelIncludeChildren = queryDimension.createSelection(
                            Selection.Operator.INCLUDE_CHILDREN, memberFetched);
                    MemberCell collapsemember = member;
                    while (collapsemember.getRightOf() != null) {
                        if (collapsemember.getRightOf().getUniqueName() != null
                                && !collapsemember.getRightOf().getUniqueName().equals(collapsemember.getParentMember())) {

                            // Get dimension to the left of the current member.
                            final QueryDimension queryDimension2 = OlapUtil.getQueryDimension(query, collapsemember
                                    .getRightOfDimension());

                            // Get the Olap4J member.
                            final Member memberFetched2 = OlapUtil.getMember(query, queryDimension2, collapsemember
                                    .getRightOf(), cellSet);

                            collapseSelChildren.addContext(queryDimension2.createSelection(memberFetched2));
                            collapseSelIncludeChildren.addContext(queryDimension2.createSelection(memberFetched2));

                        }

                        // Get next member.
                        collapsemember = collapsemember.getRightOf();
                    }

                    List<Selection> childSelections = OlapUtil.findChildrenSelections(queryDimension, collapseSelChildren);
                    queryDimension.getInclusions().removeAll(childSelections);

                    List<Selection> childSelections2 = OlapUtil.findChildrenSelections(queryDimension,
                            collapseSelIncludeChildren);
                    queryDimension.getInclusions().removeAll(childSelections2);

                    Integer index = queryDimension.getAxis().getDimensions().indexOf(queryDimension);
                    Integer size = queryDimension.getAxis().getDimensions().size();
                    for (int i = index + 1; index >= 0 && i < size; i++) {
                        QueryDimension qDim = queryDimension.getAxis().getDimensions().get(i);
                        for (Selection possibleContext : childSelections) {
                            List<Selection> findResult = OlapUtil.findSelectionsByContext(qDim, possibleContext
                                    .getSelectionContext());
                            qDim.getInclusions().removeAll(findResult);
                        }
                        for (Selection possibleContext : childSelections2) {
                            List<Selection> findResult = OlapUtil.findSelectionsByContext(qDim, possibleContext
                                    .getSelectionContext());
                            qDim.getInclusions().removeAll(findResult);
                        }
                        if (member.getRightOf() == null) {
                            List<Selection> findResult2 = OlapUtil.findSelectionByParent(qDim, collapseSelChildren
                                    .getMember());
                            qDim.getInclusions().removeAll(findResult2);
                        }

                    }

                }
            }
        }

        if (drillType.equals(DrillType.UP) && memberFetched.getParentMember() != null) {

            if (memberFetched != null && memberFetched.getParentMember() != null) {
                queryDimension.clearInclusions();
                final Selection selection = queryDimension.include(Selection.Operator.MEMBER, memberFetched.getParentMember());
                MemberCell memberdrill = member;
                
                final Selection collapseSelChildren = queryDimension.createSelection(Selection.Operator.CHILDREN,
                        memberFetched);
                final Selection collapseSelIncludeChildren = queryDimension.createSelection(
                        Selection.Operator.INCLUDE_CHILDREN, memberFetched);
                
                // Test to see if there are populated cells to its left
                while (memberdrill.getRightOf() != null) {

                    // If yes test to make sure its not name isn't null and
                    // it hasn't just skipped back to its all member level
                    if (memberdrill.getRightOf().getUniqueName() != null
                            && !memberdrill.getRightOf().getUniqueName().equals(memberdrill.getParentMember())) {

                        // Get dimension to the left of the current member.
                        final QueryDimension queryDimension2 = OlapUtil.getQueryDimension(query, memberdrill
                                .getRightOfDimension());

                        // Get the Olap4J member.
                        final Member memberFetched2 = OlapUtil.getMember(query, queryDimension2, memberdrill
                                .getRightOf(), cellSet);

                        selection.addContext(queryDimension2.createSelection(memberFetched2));
                        collapseSelChildren.addContext(queryDimension2.createSelection(memberFetched2));
                        collapseSelIncludeChildren.addContext(queryDimension2.createSelection(memberFetched2));


                    }

                    // Get next member.
                    memberdrill = memberdrill.getRightOf();
                }
                
                List<Selection> childSelections = OlapUtil.findChildrenSelections(queryDimension, collapseSelChildren);
                queryDimension.getInclusions().removeAll(childSelections);

                List<Selection> childSelections2 = OlapUtil.findChildrenSelections(queryDimension,
                        collapseSelIncludeChildren);
                queryDimension.getInclusions().removeAll(childSelections2);

                Integer index = queryDimension.getAxis().getDimensions().indexOf(queryDimension);
                Integer size = queryDimension.getAxis().getDimensions().size();
                for (int i = index + 1; index >= 0 && i < size; i++) {
                    QueryDimension qDim = queryDimension.getAxis().getDimensions().get(i);
                    for (Selection possibleContext : childSelections) {
                        List<Selection> findResult = OlapUtil.findSelectionsByContext(qDim, possibleContext
                                .getSelectionContext());
                        qDim.getInclusions().removeAll(findResult);
                    }
                    for (Selection possibleContext : childSelections2) {
                        List<Selection> findResult = OlapUtil.findSelectionsByContext(qDim, possibleContext
                                .getSelectionContext());
                        qDim.getInclusions().removeAll(findResult);
                    }
                    if (member.getRightOf() == null) {
                        List<Selection> findResult2 = OlapUtil.findSelectionByParent(qDim, collapseSelChildren
                                .getMember());
                        qDim.getInclusions().removeAll(findResult2);
                    }

                }
            }


        }
        return null;
    }

    public String[][] drillThrough(final String userId, final String sessionId, final String queryId,
            final List<Integer> coordinates) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final CellSet cellSet = OlapUtil.getCellSet(queryId);
        Cell cell = cellSet.getCell(coordinates);

        if (cell == null) {
            // We failed to find the member.
            throw new OlapException("Cannot find cell for Drillthrough");//$NON-NLS-1$
        }

        final ResultSet rs = cell.drillThrough();
        try {
            Integer width = 0;
            Integer height = 0;
            String[] header = null;
            List<String[]> rows = new ArrayList<String[]>();
            // System.out.println("DATASET");
            while (rs.next()) {
                if (height == 0) {
                    width = rs.getMetaData().getColumnCount();
                    header = new String[width];
                    for (int s = 0; s < width; s++) {
                        header[s] = rs.getMetaData().getColumnName(s + 1);

                        // System.out.print(" |\t" + header[s]);
                    }
                    if (width > 0) {
                        rows.add(header);
                        // System.out.println(" |");
                    }
                }
                String[] row = new String[width];
                for (int i = 0; i < width; i++) {

                    row[i] = rs.getString(i + 1);
                    if (row[i] == null)
                        row[i] = "";
                    // if(height < 10) {
                    // System.out.print(" |\t" + row[i]);
                    // }

                }
                // if(height < 10) {
                // System.out.println(" |");
                rows.add(row);

                // }
                height++;
            }
            height = 11;
            String[][] result = rows.toArray(new String[height][width]);
            // for (int i = 0; i<rows.size();i++) {
            // String[] row = rows.get(i);
            // result[i] = row;
            // }
            LOG.debug("Drill-Through Result: Rows : " + rows.size() + " Width: " + width);

            return result;

        } catch (SQLException e) {
            LOG.error(Messages.getString("Servlet.Query.CantExecuteQuery"), e); //$NON-NLS-1$
            throw new OlapException(e.getMessage(), e); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getMdxQuery(java.lang.String , java.lang.String,
     * java.lang.String)
     */

    public MdxQuery getMdxQuery(String userId, String sessionId, String mdxQueryId) {
        sessionService.validateSession(userId, sessionId);
        return sessionService.getSession(userId, sessionId).getMdxQueries().get(mdxQueryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getMdxQueries(java.lang. String, java.lang.String)
     */
    public List<String> getMdxQueries(String userId, String sessionId) {
        sessionService.validateSession(userId, sessionId);
        final List<String> names = new ArrayList<String>();
        final Set<Entry<String, MdxQuery>> entries = sessionService.getSession(userId, sessionId).getMdxQueries()
        .entrySet();
        for (final Entry<String, MdxQuery> entry : entries) {
            names.add(entry.getKey());
        }
        return names;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#executeMdxQuery(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Deprecated
    public CellDataSet executeMdxQuery(final String userId, final String sessionId, final String connectionId,
            final String mdx) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final OlapConnection con = this.sessionService.getNativeConnection(userId, sessionId, connectionId);
        final OlapStatement stmt = con.createStatement();
        return OlapUtil.cellSet2Matrix(stmt.executeOlapQuery(mdx));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#executeMdxQuery(java.lang .String, java.lang.String,
     * java.lang.String)
     */
    public CellDataSet executeMdxQuery(final String userId, final String sessionId, final String mdxQueryId)
    throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final MdxQuery mdxQuery = this.getMdxQuery(userId, sessionId, mdxQueryId);
        final CellSet cellSet = mdxQuery.execute();

        OlapUtil.storeCellSet(mdxQueryId, cellSet);

        return OlapUtil.cellSet2Matrix(cellSet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#releaseMdxQuery(java.lang .String, java.lang.String,
     * java.lang.String)
     */
    public void releaseMdxQuery(final String userId, final String sessionId, final String mdxQueryId) {
        sessionService.validateSession(userId, sessionId);

        OlapUtil.deleteCellSet(mdxQueryId);

        sessionService.getSession(userId, sessionId).getMdxQueries().remove(mdxQueryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#executeQuery(java.lang.String , java.lang.String,
     * java.lang.String)
     */
    public CellDataSet executeQuery(final String userId, final String sessionId, final String queryId)
    throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query mdx = this.getQuery(userId, sessionId, queryId);
        mdx.validate();
        final Writer writer = new StringWriter();
        mdx.getSelect().unparse(new ParseTreeWriter(new PrintWriter(writer)));
        Long start = (new Date()).getTime();
        final CellSet cellSet = mdx.execute();
        Long exec = (new Date()).getTime();

        OlapUtil.storeCellSet(queryId, cellSet);

        CellDataSet result = OlapUtil.cellSet2Matrix(cellSet);
        Long format = (new Date()).getTime();
        LOG.info("Size: " + result.getWidth() + "/" + result.getHeight() + "\tExecute:\t" + (exec - start)
                + "ms\tFormat:\t" + (format - exec) + "ms\t Total: " + (format - start) + "ms");
        return result;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getHierarchizeMode(java. lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getMdxForQuery(java.lang .String, java.lang.String,
     * java.lang.String)
     */
    public String getMdxForQuery(final String userId, final String sessionId, final String queryId)
    throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query q = this.getQuery(userId, sessionId, queryId);
        if (q == null)
            throw new OlapException(Messages.getString("Services.Query.NoSuchQuery")); //$NON-NLS-1$

        return q.getSelect().toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getQueries(java.lang.String, java.lang.String)
     */
    public List<String> getQueries(final String userId, final String sessionId) {
        sessionService.validateSession(userId, sessionId);
        final List<String> names = new ArrayList<String>();
        final Set<Entry<String, Query>> entries = sessionService.getSession(userId, sessionId).getQueries().entrySet();
        for (final Entry<String, Query> entry : entries) {
            names.add(entry.getKey());
        }
        return names;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    public Query getQuery(final String userId, final String sessionId, final String queryId) {
        sessionService.validateSession(userId, sessionId);
        return sessionService.getSession(userId, sessionId).getQueries().get(queryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getSelection(java.lang.String , java.lang.String,
     * java.lang.String, java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getSortOrder(java.lang.String , java.lang.String,
     * java.lang.String, java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#moveDimension(java.lang. String, java.lang.String,
     * java.lang.String, org.olap4j.Axis.Standard, java.lang.String)
     */
    public void moveDimension(final String userId, final String sessionId, final String queryId,
            final Axis.Standard axis, final String dimensionName) {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);
        String[] named = QueryDimension.getNameParts(dimensionName);

        QueryDimension dim = query.getDimension(getDimensionName(named));
        if(!query.getAxes().get(axis).getDimensions().contains(dim)){
        	query.getAxes().get(axis).getDimensions().add(dim);
        }
    }

    private String getDimensionName(String[] named) {
        String dimName;
        if (named[0].contains(".")) {
            int index = named[0].indexOf(".");
            dimName = named[0].substring(0, index);
        } else {
            dimName = named[0];
        }

        return dimName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#releaseQuery(java.lang.String , java.lang.String,
     * java.lang.String)
     */
    public void releaseQuery(final String userId, final String sessionId, final String queryId) {
        sessionService.validateSession(userId, sessionId);

        OlapUtil.deleteCellSet(queryId);

        sessionService.getSession(userId, sessionId).getQueries().remove(queryId);
    }

    public void setDiscoveryService(final DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#setHierachizeMode(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, org.olap4j.query.QueryDimension.HierarchizeMode)
     */
    public void setHierarchizeMode(final String userId, final String sessionId, final String queryId,
            final String dimensionName, final HierarchizeMode hierachizeMode) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        final Query query = this.getQuery(userId, sessionId, queryId);

        query.getDimension(dimensionName).setHierarchizeMode(hierachizeMode);
    }

    /**
     * 
     * TODO JAVADOC
     * 
     * @param service
     */
    public void setSessionService(final SessionService service) {
        this.sessionService = service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#setSortOrder(java.lang.String , java.lang.String,
     * java.lang.String, java.lang.String, org.olap4j.query.SortOrder)
     */
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
        if (q == null) {
            throw new OlapException(Messages.getString("Services.Query.NoSuchQuery")); //$NON-NLS-1$
        } else {
            q.swapAxes();
        }

        return executeQuery(userId, sessionId, queryId);

    }

    /**
     * 
     * TODO JAVADOC
     * 
     * @param userId
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @return
     * @throws OlapException
     */
    private Cube getCube4Guid(final String userId, final String sessionId, final String connectionId,
            final String cubeName) throws OlapException {
        final OlapConnection connection = sessionService.getNativeConnection(userId, sessionId, connectionId);

        Cube cube = null;
        final NamedList<Catalog> catalogs = connection.getCatalogs();
        for (int k = 0; k < catalogs.size(); k++) {
            try {
                connection.setCatalog(catalogs.get(k).getName());
            } catch (SQLException e) {
                throw new OlapException("Error setting catalog: " + catalogs.get(k).getName(), e);

            }
            final NamedList<Schema> schemas = catalogs.get(k).getSchemas();
            for (int j = 0; j < schemas.size(); j++) {
                final NamedList<Cube> cubes = schemas.get(j).getCubes();
                final Iterator<Cube> iter = cubes.iterator();
                while (iter.hasNext() && cube == null) {
                    final Cube testCube = iter.next();
                    if (cubeName.equals(testCube.getName())) {
                        cube = testCube;
                    }
                }
            }
        }
        // if (catalog != null) {
        // try {
        // connection.setCatalog(catalog.getName());
        // } catch (SQLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        if (cube != null)
            return cube;

        throw new OlapException(Messages.getString("Services.Session.CubeNameNotValid")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#saveQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    public void saveQuery(String userId, String sessionId, SavedQuery query) {
        this.sessionService.validateSession(userId, sessionId);

        final User user = this.userManager.getUser(userId);
        user.getSavedQueries().remove(query);
        user.getSavedQueries().add(query);
        this.userManager.updateUser(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#deleteQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    public void deleteQuery(String userId, String sessionId, String queryName) {
        this.sessionService.validateSession(userId, sessionId);

        final User user = this.userManager.getUser(userId);
        SavedQuery dummy = new SavedQuery();
        dummy.setName(queryName);
        user.getSavedQueries().remove(dummy);
        this.userManager.updateUser(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#loadQuery(java.lang.String, java.lang.String, java.lang.String)
     */
    public SavedQuery loadQuery(String userId, String sessionId, String savedQueryName) {
        this.sessionService.validateSession(userId, sessionId);

        return this.userManager.getSavedQuery(userId, savedQueryName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#getSavedQueries(java.lang .String, java.lang.String)
     */
    public Set<SavedQuery> getSavedQueries(String userId, String sessionId) {
        this.sessionService.validateSession(userId, sessionId);

        final User user = this.userManager.getUser(userId);

        return user.getSavedQueries();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#createSavedQuery(java.lang .String, java.lang.String,
     * java.lang.String, java.lang.String, org.olap4j.query.Query)
     */
    public String createSavedQuery(String userId, String sessionId, String connectionId, /* String cubeName, */
            Query newQuery) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        // if (cubeName == null)
        //    throw new OlapException(Messages.getString("Services.Session.NoCubeSelected")); //$NON-NLS-1$

        // final Cube cube = this.getCube4Guid(userId, sessionId, connectionId,
        // cubeName);
        final String generatedId = String.valueOf(UUID.randomUUID());

        sessionService.getSession(userId, sessionId).getQueries().put(generatedId, newQuery);

        return generatedId;

    }

    public String createSavedQuery(String userId, String sessionId, String connectionId, /* String cubeName, */
            MdxQuery newMdxQuery) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final String generatedId = String.valueOf(UUID.randomUUID());

        sessionService.getSession(userId, sessionId).getMdxQueries().put(generatedId, newMdxQuery);

        return generatedId;

    }

    public CellDataSet setNonEmpty(String userId, String sessionId, String queryId, boolean flag) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        final Query q = this.getQuery(userId, sessionId, queryId);

        q.getAxis(Axis.COLUMNS).setNonEmpty(flag);
        q.getAxis(Axis.ROWS).setNonEmpty(flag);
        return executeQuery(userId, sessionId, queryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.QueryService#setProperty(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public void setProperty(String userId, String sessionId, String queryId, String dimensionName, String levelName,
            String propertyName, Boolean enabled) {
        this.sessionService.validateSession(userId, sessionId);
        // Query query = this.getQuery(userId, sessionId, queryId);

    }

    public void pullUpDimension(String currentUserId, String sessionID, String queryId, Axis iaxis,
            int currentposition, int newposition) {
        this.sessionService.validateSession(currentUserId, sessionID);

        Query query = this.getQuery(currentUserId, sessionID, queryId);

        QueryAxis axis = query.getAxis(iaxis);
        int difference = currentposition - newposition;
        for (int i = 0; i < difference; i++) {
            axis.pullUp(currentposition);
            currentposition--;
        }
    }

    public void pushDownDimension(String currentUserId, String sessionID, String queryId, Axis iaxis,
            int currentposition, int newposition) {
        this.sessionService.validateSession(currentUserId, sessionID);

        Query query = this.getQuery(currentUserId, sessionID, queryId);

        QueryAxis axis = query.getAxis(iaxis);

        int difference = newposition - currentposition;
        for (int i = 0; i < difference; i++) {
            axis.pushDown(currentposition);
            currentposition++;
        }
    }


	public void pullUpMeasureMember(String currentUserId, String sessionID,
			String queryId, Standard standard, int currentposition,
			int newposition) {
		this.sessionService.validateSession(currentUserId, sessionID);

        Query query = this.getQuery(currentUserId, sessionID, queryId);
        
        List<Selection> sellist =query.getDimension("Measures").getInclusions();
        
        Selection selection = sellist.get(currentposition-1);
        
        sellist.remove(currentposition-1);
        
        sellist.add(newposition-1, selection);

		
	}

	public void pushDownMeasureMember(String currentUserId, String sessionID,
			String queryId, Standard standard, int currentposition,
			int newposition) {
		this.sessionService.validateSession(currentUserId, sessionID);

        Query query = this.getQuery(currentUserId, sessionID, queryId);
        
        List<Selection> sellist = query.getDimension("Measures").getInclusions();
        
        Selection selection = sellist.get(currentposition-1);
        
        sellist.remove(currentposition-1);
        
        sellist.add(newposition-1, selection);

		
	}

    public Map<IAxis, PatQueryAxis> getSelections(String currentUserId, String sessionId, String queryId) {
        this.sessionService.validateSession(currentUserId, sessionId);
        final Query query = this.getQuery(currentUserId, sessionId, queryId);
        
        Map<IAxis,PatQueryAxis> axes = new HashMap<IAxis,PatQueryAxis>();
        if (query != null) {
            if (query.getAxes().get(Axis.ROWS) != null) {
                QueryAxis olap4jAxis = query.getAxes().get(Axis.ROWS);
                PatQueryAxis axis = populateAxis(new PatQueryAxis(IAxis.ROWS),olap4jAxis);
                axes.put(IAxis.ROWS, axis);
            }
            if (query.getAxes().get(Axis.COLUMNS) != null) {
                QueryAxis olap4jAxis = query.getAxes().get(Axis.COLUMNS);
                PatQueryAxis axis = populateAxis(new PatQueryAxis(IAxis.COLUMNS),olap4jAxis);
                axes.put(IAxis.COLUMNS, axis);
            }
            if (query.getAxes().get(Axis.FILTER) != null) {
                QueryAxis olap4jAxis = query.getAxes().get(Axis.FILTER);
                PatQueryAxis axis = populateAxis(new PatQueryAxis(IAxis.FILTER),olap4jAxis);
                axes.put(IAxis.FILTER, axis);
            }

        }
        return axes;
    }
    
    private static PatQueryAxis populateAxis(PatQueryAxis patAxis, QueryAxis olap4jAxis) {
        for (QueryDimension qDim : olap4jAxis.getDimensions()) {
            PatQueryDimension dimension = new PatQueryDimension(qDim.getName());
            
            for (Selection selection : qDim.getInclusions()) {
                String caption = selection.getMember().getCaption(null);
                String uniqueName = selection.getMember().getUniqueName();
                SelectionType operator = SelectionType.MEMBER;
                
                Operator olap4jOperator = selection.getOperator();
                if (Operator.ANCESTORS.equals(olap4jOperator)) {
                    operator = SelectionType.ANCESTORS;
                } else if (Operator.CHILDREN.equals(olap4jOperator)) {
                    operator = SelectionType.CHILDREN;
                } else if (Operator.DESCENDANTS.equals(olap4jOperator)) {
                    operator = SelectionType.DESCENDANTS;
                } else if (Operator.INCLUDE_CHILDREN.equals(olap4jOperator)) {
                    operator = SelectionType.INCLUDE_CHILDREN;
                } else if (Operator.MEMBER.equals(olap4jOperator)) {
                    operator = SelectionType.MEMBER;
                } else if (Operator.SIBLINGS.equals(olap4jOperator)) {
                    operator = SelectionType.SIBLINGS;
                } 
                
                PatQuerySelection patSelection = new PatQuerySelection(uniqueName, caption, operator);
                dimension.addInclusion(patSelection);
            }
            
            patAxis.addDimension(dimension);
        }
        return patAxis;
    }
    
}
