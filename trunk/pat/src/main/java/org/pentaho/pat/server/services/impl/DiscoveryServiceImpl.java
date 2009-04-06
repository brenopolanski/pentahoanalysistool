package org.pentaho.pat.server.services.impl;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;
import org.olap4j.Axis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.pentaho.pat.rpc.beans.StringTree;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.OlapUtil;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.util.JdbcDriverFinder;
import org.springframework.util.Assert;

/**
 * Simple service implementation as a Spring bean.
 * @author Luc Boudreau
 */
public class DiscoveryServiceImpl extends AbstractService 
	implements DiscoveryService
{

	private SessionService sessionService = null;
	
	private JdbcDriverFinder driverFinder = null;

	Logger log = Logger.getLogger(this.getClass());

	
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(sessionService);
		Assert.notNull(driverFinder);
	}
	
	
	public List<String> getDrivers() 
	{
		
		this.driverFinder.registerDrivers();
		
		// An enumeration is a very unpractical thing, so let's convert it to a List.
		// We can't even know it's size... what a shameful object.
		Enumeration<Driver> driversEnum = DriverManager.getDrivers();
		List<String> drivers = new ArrayList<String>();
		while (driversEnum.hasMoreElements()) 
			{ drivers.add(driversEnum.nextElement().getClass().getName()); }
		
		return drivers;
	}
	
	
	public List<String> getCubes(String userId, String sessionId) 
	    throws OlapException
	{

        List<String> list = new ArrayList<String>();

        OlapConnection conn = this.sessionService.getConnection(userId,
                sessionId);

        if (conn == null)
            return list;

        NamedList<Cube> cubes = conn.getSchema().getCubes();

        for (int i = 0; i < cubes.size(); i++) {
            list.add(cubes.get(i).getName());
        }

        return list;
    }

	
	public Cube getCube(String userId, String sessionId, String name) {
		try {
			OlapConnection conn = this.sessionService.getConnection(userId,sessionId);
			return conn.getSchema().getCubes().get(name);
		} catch (Exception e) {}
		return null;
	}
	
	
	public List<String> getDimensions(String userId, String sessionId, 
		Axis axis) throws OlapException
	{
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		Query query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		Axis targetAxis = null;
		if (axis!=Axis.UNUSED)
			targetAxis=axis;
		
	    List<QueryDimension> dimList = query.getAxes().get(targetAxis).getDimensions();
	    List<String> dimNames = new ArrayList<String>();
	    for (QueryDimension dim : dimList) {
	      dimNames.add(dim.getName());
	    }
	    return dimNames;
	}

	public StringTree getMembers(String userId, String sessionId,
            String dimensionName) throws OlapException {

        String currentQuery = (String) this.sessionService
                .getUserSessionVariable(userId, sessionId,
                        Constants.CURRENT_QUERY_NAME);
        Query query = this.sessionService.getQuery(userId, sessionId,
                currentQuery);

        List<String> uniqueNameList = new ArrayList<String>();

        NamedList<Level> levels = query.getDimension(dimensionName)
                .getDimension().getHierarchies().get(dimensionName).getLevels();

        for (Level level : levels) {
            List<Member> levelMembers = level.getMembers();
            for (Member member : levelMembers) {
                uniqueNameList.add(member.getUniqueName());
            }
        }

        StringTree result = new StringTree(dimensionName, null);
        for (int i = 0; i < uniqueNameList.size(); i++) {
            String[] memberNames = uniqueNameList.get(i).split("\\."); //$NON-NLS-1$
            for (int j = 0; j < memberNames.length; j++) { // Trim off the
                                                           // brackets
                memberNames[j] = memberNames[j].substring(1, memberNames[j]
                        .length() - 1);
            }
            result = OlapUtil.parseMembers(memberNames, result);
        }

        return result;
    }



    public void setDriverFinder(JdbcDriverFinder driverFinder) {
        this.driverFinder = driverFinder;
    }
}
