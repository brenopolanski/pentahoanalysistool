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

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.olap4j.Axis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;
import org.olap4j.metadata.Schema;
import org.olap4j.metadata.Property.StandardMemberProperty;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.LevelProperties;
import org.pentaho.pat.rpc.dto.MemberItem;
import org.pentaho.pat.rpc.dto.MemberLabelItem;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.util.JdbcDriverFinder;
import org.pentaho.pat.server.util.OlapUtil;
import org.springframework.util.Assert;

/**
 * Simple service implementation as a Spring bean.
 * 
 * @author Luc Boudreau
 */
public class DiscoveryServiceImpl extends AbstractService implements DiscoveryService {

    private SessionService sessionService = null;

    private QueryService queryService = null;

    private JdbcDriverFinder driverFinder = null;

    private Locale loc = Locale.getDefault();

    public void setSessionService(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setQueryService(final QueryService queryService) {
        this.queryService = queryService;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(sessionService);
        Assert.notNull(queryService);
        Assert.notNull(driverFinder);
    }

    public List<String> getDrivers() {
        this.driverFinder.registerDrivers();

        // An enumeration is a very unpractical thing, so let's convert it to a List.
        // We can't even know it's size... what a shameful object.
        final Enumeration<Driver> driversEnum = DriverManager.getDrivers();
        final List<String> drivers = new ArrayList<String>();
        while (driversEnum.hasMoreElements()) {
            drivers.add(driversEnum.nextElement().getClass().getName());
        }
        Collections.sort(drivers);

        return drivers;
    }

    public List<CubeItem> getCubes(final String userId, final String sessionId, final String connectionId)
            throws OlapException {

        this.sessionService.validateSession(userId, sessionId);

        final List<CubeItem> list = new ArrayList<CubeItem>();

        final OlapConnection conn = this.sessionService.getNativeConnection(userId, sessionId, connectionId);

        if (conn == null) {
            return list;
        }
        // OlapDatabaseMetaData olapDbMeta = conn.getMetaData();
        // try {
        // ResultSet cubesResult = olapDbMeta.getCubes(null, null, null);
        //                
        // while(cubesResult.next()) {
        //        
        // list.add(new CubeItem(cubesResult.getString("CUBE_NAME"),cubesResult.getString("CATALOG_NAME"),
        // cubesResult.getString("SCHEMA_NAME")));
        //
        // }
        // } catch (SQLException e) {
        // throw new OlapException(e.getMessage(),e);
        // }

        for (Catalog cat : conn.getCatalogs()) {
            for (Schema schem : cat.getSchemas()) {
                for (Cube cub : schem.getCubes()) {
                    list.add(new CubeItem(cub.getName(), cat.getName(), schem.getName()));
                }
            }
        }
        return list;
    }

    public Cube getCube(String userId, String sessionId, String connectionId, String cubeName) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        OlapConnection conn = this.sessionService.getNativeConnection(userId, sessionId, connectionId);
        return conn.getSchema().getCubes().get(cubeName);
    }

    public List<String> getDimensions(String userId, String sessionId, String queryId, Axis.Standard axis)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);

        Axis targetAxis = null;
        if (axis != null) {
            targetAxis = axis;
        }
        List<QueryDimension> dimList = query.getAxes().get(targetAxis).getDimensions();
        List<String> dimNames = new ArrayList<String>();
        for (QueryDimension dim : dimList) {
            dimNames.add(dim.getName());
        }
        return dimNames;
    }

    public List<MemberLabelItem> getDimensionList(String userId, String sessionId, String queryId, Axis.Standard axis)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);

        Axis targetAxis = null;
        if (axis != null) {
            targetAxis = axis;
        }
        List<QueryDimension> dimList = query.getAxes().get(targetAxis).getDimensions();
        List<MemberLabelItem> dimNames = new ArrayList<MemberLabelItem>();
        for (QueryDimension dim : dimList) {
            dimNames.add(new MemberLabelItem(dim.getName(), dim.getName(), null));
        }
        return dimNames;
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

    private String getHierarchyName(String[] named) {
        String hierarchyName;
        if (named[0].contains(".")) {
            int index = named[0].indexOf(".");
            hierarchyName = named[0].substring(index + 1, named[0].length());
        } else {
            hierarchyName = named[0];
        }

        return hierarchyName;
    }

    public StringTree getSpecificMembers(String userId, String sessionId, String queryId, String uniqueName,
            ObjectType type, Selection.Operator selectionType) throws OlapException {

        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);
        String[] parts = QueryDimension.getNameParts(uniqueName);

        StringTree st = null;
        if (parts[0].equals("Measures")) {
            List<Measure> measures = query.getCube().getMeasures();
            st = new StringTree("", "", null);

            for (Measure mem : measures) {
                new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
            }
        } else if (selectionType == (Selection.Operator.MEMBER)) {
            if (type.equals(ObjectType.DIMENSION)) {
                NamedList<? extends Member> members = query.getDimension(parts[0]).getDimension().getDefaultHierarchy()
                        .getRootMembers();
                st = new StringTree(null, null, null);
                for (Member mem : members) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            } else if (type.equals(ObjectType.HIERARCHY)) {
                NamedList<? extends Member> members = query.getDimension(getDimensionName(parts)).getDimension()
                        .getHierarchies().get(parts[0]).getRootMembers();
                st = new StringTree(null, null, null);
                for (Member mem : members) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            } else {
                Member members = query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(
                        getHierarchyName(parts)).getLevels().get(parts[1]).getMembers().get(0);
                st = new StringTree(members.getUniqueName(), members.getCaption(loc), null);
            }
        } else if (selectionType.equals(Selection.Operator.CHILDREN)) {
            if (type.equals(ObjectType.DIMENSION)) {
                Member parentMember = query.getDimension(getDimensionName(parts)).getDimension().getDefaultHierarchy()
                        .getDefaultMember();
                NamedList<? extends Member> members = parentMember.getChildMembers();
                st = new StringTree(null, null, null);
                for (Member mem : members) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            } else if (type.equals(ObjectType.HIERARCHY)) {
                Member parentMember = query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(
                        parts[0]).getDefaultMember();
                NamedList<? extends Member> members = parentMember.getChildMembers();
                st = new StringTree(null, null, null);
                for (Member mem : members) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            } else {
                List<Member> parentMember = query.getDimension(getDimensionName(parts)).getDimension().getHierarchies()
                        .get(parts[0]).getLevels().get(parts[1]).getMembers();
                // NamedList<? extends Member> members = parentMember.getChildMembers();
                st = new StringTree(null, null, null);
                for (Member mem : parentMember) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            }
        } else if (selectionType.equals(Selection.Operator.INCLUDE_CHILDREN)) {
            if (type.equals(ObjectType.DIMENSION)) {
                Member parentMember = query.getDimension(getDimensionName(parts)).getDimension().getDefaultHierarchy()
                        .getDefaultMember();
                NamedList<? extends Member> members = parentMember.getChildMembers();
                st = new StringTree(parentMember.getUniqueName(), parentMember.getCaption(loc), null);
                for (Member mem : members) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            } else if (type.equals(ObjectType.HIERARCHY)) {
                Member parentMember = query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(
                        parts[0]).getDefaultMember();
                NamedList<? extends Member> members = parentMember.getChildMembers();
                st = new StringTree(parentMember.getUniqueName(), parentMember.getCaption(loc), null);
                for (Member mem : members) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            } else {
                List<Member> parentMember = query.getDimension(getDimensionName(parts)).getDimension().getHierarchies()
                        .get(parts[0]).getLevels().get(parts[1]).getMembers();
                // NamedList<? extends Member> members = parentMember.getChildMembers();
                st = new StringTree("", "", null);
                for (Member mem : parentMember) {
                    new StringTree(mem.getUniqueName(), mem.getCaption(loc), st);
                }
            }
        } else if (selectionType.equals(Selection.Operator.DESCENDANTS)) {
            if (type.equals(ObjectType.DIMENSION)) {
                query.getDimension(getDimensionName(parts)).getDimension().getDefaultHierarchy().getDefaultMember()
                        .getName();
            } else if (type.equals(ObjectType.HIERARCHY)) {
                query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(parts[0])
                        .getDefaultMember().getName();
            } else {
                query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(parts[0]).getLevels()
                        .get(parts[1]);
            }
        } else if (selectionType.equals(Selection.Operator.SIBLINGS)) {
            if (type.equals(ObjectType.HIERARCHY)) {
                query.getDimension(getDimensionName(parts)).getDimension().getDefaultHierarchy().getDefaultMember()
                        .getName();
            } else if (type.equals(ObjectType.HIERARCHY)) {
                query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(parts[0])
                        .getDefaultMember().getName();
            } else {
                query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(parts[0]).getLevels()
                        .get(parts[1]);
            }
        } else if (selectionType.equals(Selection.Operator.ANCESTORS)) {
            if (type.equals(ObjectType.HIERARCHY)) {
                query.getDimension(getDimensionName(parts)).getDimension().getDefaultHierarchy().getDefaultMember()
                        .getName();
            } else if (type.equals(ObjectType.HIERARCHY)) {
                query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(parts[0])
                        .getDefaultMember().getName();
            } else {
                query.getDimension(getDimensionName(parts)).getDimension().getHierarchies().get(parts[0]).getLevels()
                        .get(parts[1]);
            }
        }
        return st;
    }

    @Deprecated
    public StringTree getMembers(String userId, String sessionId, String queryId, String dimensionName)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);

        List<MemberItem> uniqueNameList = new ArrayList<MemberItem>();

        // FIXME Only uses the first hierarchy for now.
        NamedList<Level> levels = query.getDimension(dimensionName).getDimension().getHierarchies().get(0).getLevels();

        for (Level level : levels) {
            List<Member> levelMembers = level.getMembers();
            for (Member member : levelMembers) {
                uniqueNameList.add(new MemberItem(member.getUniqueName(), member.getCaption(loc)));
            }
        }

        StringTree result = new StringTree(dimensionName, null);
        for (int i = 0; i < uniqueNameList.size(); i++) {
            String[] memberNames = uniqueNameList.get(i).getLevelName().split("\\]\\.\\["); //$NON-NLS-1$
            String captionNames = uniqueNameList.get(i).getPropertyName();
            for (int j = 0; j < memberNames.length; j++) { // Trim off the
                // brackets
                memberNames[j] = memberNames[j].replaceAll("\\[", "") //$NON-NLS-1$ //$NON-NLS-2$
                        .replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$
            }
            result = OlapUtil.parseMembers(memberNames, captionNames, result);
        }

        return result;
    }

    public void setDriverFinder(JdbcDriverFinder driverFinder) {
        this.driverFinder = driverFinder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.DiscoveryService#getAllLevelProperties(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public List<LevelProperties> getAllLevelProperties(String userId, String sessionId, String queryId,
            String dimensionName) throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);

        List<LevelProperties> propertiesReturnList = new ArrayList<LevelProperties>();
        NamedList<Level> levels = query.getDimension(dimensionName).getDimension().getHierarchies().get(0).getLevels();
        Set<String> propertyExclusions = getPropertyExclusions();
        for (Level level : levels) {
            List<Property> levelProperties = level.getProperties();
            for (Property property : levelProperties) {
                if (!propertyExclusions.contains(property.getUniqueName())) {
                    propertiesReturnList.add(new LevelProperties(level.getName(), property.getCaption(null)));
                }

            }
        }

        return propertiesReturnList;

    }

    private static Set<String> getPropertyExclusions() {
        Set<String> propertyElementExclusions = new HashSet<String>();
        StandardMemberProperty[] test = Property.StandardMemberProperty.values();
        for (int i = 0; i < test.length; i++) {
            propertyElementExclusions.add(test[i].getCaption(null));
        }
        return propertyElementExclusions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.pat.server.services.DiscoveryService#getNamedLevelProperties(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public StringTree getNamedLevelProperties(String currentUserId, String sessionId, String queryId,
            String dimensionName, String levelName) throws OlapException {
        return null;
    }

    public List<MemberLabelItem> getHierarchies(String userId, String sessionId, String queryId, String dimensionName)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);

        List<Hierarchy> hierarchyList = query.getCube().getDimensions().get(dimensionName).getHierarchies();

        List<MemberLabelItem> hNames = new ArrayList<MemberLabelItem>();
        Locale loc = Locale.getDefault();
        for (Hierarchy dim : hierarchyList) {
            List<String> lst = new ArrayList<String>();
            String dimunique = dim.getUniqueName().replaceAll("\\[", "") //$NON-NLS-1$ //$NON-NLS-2$
                    .replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$
            lst.add(dim.getDimension().getName());
            lst.add(dimunique);
            hNames.add(new MemberLabelItem(dim.getUniqueName(), dim.getCaption(loc), lst));
        }
        return hNames;

    }

    public List<MemberLabelItem> getLevels(String userId, String sessionId, String queryId, String uniqueName)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);

        String[] parts = QueryDimension.getNameParts(uniqueName);
        List<MemberLabelItem> levelNames = new ArrayList<MemberLabelItem>();
        NamedList<Hierarchy> hierarchies = query.getCube().getHierarchies();
        if (hierarchies != null && hierarchies.size() > 0) {
            Hierarchy hierarchy = hierarchies.get(parts[0]);
            if (hierarchy == null) {
                hierarchy = hierarchies.get(uniqueName);
                if (hierarchy == null) {
                    throw new OlapException("Hierarchy not found: " + uniqueName);
                }
            }
            
            List<Level> levelList = hierarchy.getLevels();
            
            for (Level dim : levelList) {
                List<String> lst = new ArrayList<String>();
                String hierarchyunique = dim.getHierarchy().getUniqueName().replaceAll("\\[", "") //$NON-NLS-1$ //$NON-NLS-2$
                    .replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$
                lst.add(dim.getDimension().getName());
                lst.add(hierarchyunique);
                lst.add(dim.getName());
                levelNames.add(new MemberLabelItem(dim.getUniqueName(), dim.getCaption(loc), lst));
            }
        }
        return levelNames;
    }

    public List<MemberLabelItem> getLevelMembers(String userId, String sessionId, String queryId, String uniqueName)
    throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);
        String[] parts = QueryDimension.getNameParts(uniqueName);

        List<MemberLabelItem> levelNames = new ArrayList<MemberLabelItem>();
        NamedList<Hierarchy> hierarchies = query.getCube().getHierarchies();
        if (hierarchies != null && hierarchies.size() > 0) {
            Hierarchy hierarchy = hierarchies.get(parts[0]);
            if (hierarchy == null) {
                hierarchy = hierarchies.get("[" + parts[0] + "]");
                if (hierarchy == null) {
                    throw new OlapException("Hierarchy not found: " + parts[0]);
                }

                if (parts.length >= 2) {
                    Level level = hierarchy.getLevels().get(parts[1]);
                    if (level == null) {
                        level = hierarchy.getLevels().get("[" + parts[1] + "]");
                        if (level == null) {
                            throw new OlapException("Level not found" + uniqueName);
                        }
                    }
                    List<Member> levelList = level.getMembers();


                    for (Member dim : levelList) {
                        List<String> lst = new ArrayList<String>();
                        String hierarchyunique = dim.getHierarchy().getUniqueName().replaceAll("\\[", "") //$NON-NLS-1$ //$NON-NLS-2$
                        .replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$
                        lst.add(dim.getDimension().getName());
                        lst.add(hierarchyunique);
                        // lst.add(levelName);
                        lst.add(dim.getName());
                        levelNames.add(new MemberLabelItem(dim.getUniqueName(), dim.getCaption(loc), lst));
                    }
                    Collections.sort(levelNames);
                }
            }

        }


        return levelNames;

    }

    public List<MemberLabelItem> getMeasures(String currentUserId, String sessionID, String currQuery)
            throws OlapException {
        this.sessionService.validateSession(currentUserId, sessionID);

        Query query = this.queryService.getQuery(currentUserId, sessionID, currQuery);

        List<Measure> measureList = query.getCube().getMeasures();
        List<MemberLabelItem> measureNames = new ArrayList<MemberLabelItem>();
        for (Measure measure : measureList) {
            measureNames.add(new MemberLabelItem(measure.getUniqueName(), measure.getCaption(loc), null));
        }
        Collections.sort(measureNames);
        return measureNames;
    }
}
