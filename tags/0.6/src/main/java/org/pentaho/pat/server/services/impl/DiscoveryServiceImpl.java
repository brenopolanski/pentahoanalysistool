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
import java.util.Enumeration;
import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Schema;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.StringTree;
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
        // ResultSet cubesResult = olapDbMeta.getCubes(conn.getCatalog(), null, null);
        //        
        // while(cubesResult.next()) {
        //
        // list.add(cubesResult.getString("CUBE_NAME"));
        // }
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }

        final NamedList<Catalog> catalogs = conn.getCatalogs();
        for (int k = 0; k < catalogs.size(); k++) {
            NamedList<Schema> schemas = catalogs.get(k).getSchemas();
            for (int j = 0; j < schemas.size(); j++) {
                NamedList<Cube> cubes = schemas.get(j).getCubes();

                for (int i = 0; i < cubes.size(); i++) {
                    list.add(new CubeItem(cubes.get(i).getName(), cubes.get(i).getSchema().getCatalog().getName(),
                            cubes.get(i).getSchema().getName()));
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

    public StringTree getMembers(String userId, String sessionId, String queryId, String dimensionName)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);

        Query query = this.queryService.getQuery(userId, sessionId, queryId);

        List<String> uniqueNameList = new ArrayList<String>();

        // FIXME Only uses the first hierarchy for now.
        NamedList<Level> levels = query.getDimension(dimensionName).getDimension().getHierarchies().get(0).getLevels();

        for (Level level : levels) {
            List<Member> levelMembers = level.getMembers();
            for (Member member : levelMembers) {
                uniqueNameList.add(member.getUniqueName());
            }
        }

        StringTree result = new StringTree(dimensionName, null);
        for (int i = 0; i < uniqueNameList.size(); i++) {
            String[] memberNames = uniqueNameList.get(i).split("\\]\\.\\["); //$NON-NLS-1$
            for (int j = 0; j < memberNames.length; j++) { // Trim off the
                // brackets
                memberNames[j] = memberNames[j].replaceAll("\\[", "") //$NON-NLS-1$ //$NON-NLS-2$
                        .replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$
            }
            result = OlapUtil.parseMembers(memberNames, result);
        }

        return result;
    }

    public void setDriverFinder(JdbcDriverFinder driverFinder) {
        this.driverFinder = driverFinder;
    }
}
