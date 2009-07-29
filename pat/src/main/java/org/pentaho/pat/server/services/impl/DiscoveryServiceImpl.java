package org.pentaho.pat.server.services.impl;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.OlapUtil;
import org.pentaho.pat.server.services.QueryService;
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
	
	private QueryService queryService = null;
	
	private JdbcDriverFinder driverFinder = null;


	
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	
	
	public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }



    public void afterPropertiesSet() throws Exception {
		Assert.notNull(sessionService);
		Assert.notNull(queryService);
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
	
	
	public List<String> getCubes(String userId, String sessionId, String connectionId) 
	    throws OlapException
	{

	    this.sessionService.validateSession(userId, sessionId);
	    
        List<String> list = new ArrayList<String>();

        OlapConnection conn = 
            this.sessionService.getNativeConnection(userId, sessionId, connectionId);

        if (conn == null)
            return list;

        NamedList<Cube> cubes = conn.getSchema().getCubes();

        for (int i = 0; i < cubes.size(); i++) {
            list.add(cubes.get(i).getName());
        }

        return list;
    }

	
	public Cube getCube(
	        String userId, 
	        String sessionId,
	        String connectionId,
	        String cubeName) throws OlapException 
	{
	    this.sessionService.validateSession(userId, sessionId);
	    OlapConnection conn = this.sessionService.getNativeConnection(userId,sessionId,connectionId);
		return conn.getSchema().getCubes().get(cubeName);
	}
	
	
	public List<String> getDimensions(String userId, String sessionId, 
            String queryId, Axis.Standard axis) throws OlapException
	{
	    this.sessionService.validateSession(userId, sessionId);
	    
		Query query = this.queryService.getQuery(userId, sessionId, queryId);
		
		Axis targetAxis = null;
		if (axis!=null)
			targetAxis=axis;
		
	    List<QueryDimension> dimList = query.getAxes().get(targetAxis).getDimensions();
	    List<String> dimNames = new ArrayList<String>();
	    for (QueryDimension dim : dimList) {
	      dimNames.add(dim.getName());
	    }
	    return dimNames;
	}

	public StringTree getMembers(String userId, String sessionId,
            String queryId, String dimensionName) throws OlapException 
    {
	    this.sessionService.validateSession(userId, sessionId);

        Query query = 
            this.queryService.getQuery(userId, sessionId, queryId);

        List<String> uniqueNameList = new ArrayList<String>();

        // FIXME Only uses the first hierarchy for now.
        NamedList<Level> levels = query.getDimension(dimensionName)
                .getDimension().getHierarchies().get(0).getLevels();

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
                memberNames[j] = memberNames[j]
                  .replaceAll("\\[", "") //$NON-NLS-1$ //$NON-NLS-2$
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
