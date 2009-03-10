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
import org.olap4j.mdx.SelectNode;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;
import org.pentaho.pat.client.util.StringTree;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;

/**
 * Simple service implementation as a Spring bean.
 * @author Luc Boudreau
 */
public class DiscoveryServiceImpl extends AbstractService 
	implements DiscoveryService
{

	private SessionService sessionService = null;
	
	

	Logger log = Logger.getLogger(this.getClass());

	
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	
	
	public void afterPropertiesSet() throws Exception {
		if (this.sessionService == null)
			throw new Exception("A sessionService is required.");
	}
	
	
	public String[] getDrivers() 
	{
		
		// TODO we need to scan a folder for JDBC drivers and register them prior to what follows here.
		
		// An enumeration is a very unpractical thing, so let's convert it to a List.
		// We can't even know it's size... what a shameful object.
		Enumeration<Driver> driversEnum = DriverManager.getDrivers();
		List<Driver> drivers = new ArrayList<Driver>();
		while (driversEnum.hasMoreElements()) 
			{ drivers.add(driversEnum.nextElement()); }
		
		// Now we can instanciate the string array properly.
		String[] result = new String[drivers.size()];
		
		for (int cpt = 0; cpt < drivers.size(); cpt++) {
			result[cpt] = drivers.get(cpt).getClass().getName();
		}
		
		return result;
	}
	
	
	public List<String> getCubes(String userId, String sessionId) {
		
		try {
			
			List<String> list = new ArrayList<String>();
			
			OlapConnection conn = this.sessionService.getConnection(userId, sessionId);
			
			if (conn == null)
				return list;
			
			NamedList<Cube> cubes = conn.getSchema().getCubes();
			
			for (int i = 0; i < cubes.size(); i++) {
				list.add(cubes.get(i).getName());
			}
			
			if (log.isDebugEnabled())
				log.debug("Found the following cubes:"+list.toString());
			
			return list;
			
		} catch (OlapException e) {
			log.error("Communications error while retrieving the cubes list.", e);
		}
		
		return null;
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
		SelectNode query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		// FIXME IMPLEMENT ME
		throw new UnsupportedOperationException();
		
//		// The UNUSED axis is not used by default...
//		Axis targetAxis = null;
//	    if (!axis.name().equalsIgnoreCase(Axis.UNUSED.name())) { //$NON-NLS-1$
//	      targetAxis = axis;
//	    }
//		
//	    List<QueryDimension> dimList = query.getAxes().get(targetAxis).getDimensions();
//	    List<String> dimNames = new ArrayList<String>();
//	    for (QueryDimension dim : dimList) {
//	      dimNames.add(dim.getName());
//	    }
//	    return dimNames;
	}

	public StringTree getMembers(String userId, String sessionId, String dimensionName) 
		throws OlapException 
	{
		
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		SelectNode query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		// FIXME IMPLEMENT ME
		throw new UnsupportedOperationException();
		
//		List<String> uniqueNameList = new ArrayList<String>();
//	    
//		NamedList<Level> levels = query.getDimension(dimensionName).getDimension()
//	    	.getHierarchies().get(dimensionName).getLevels();
//	    
//	    for (Level level : levels) {
//	      try {
//	        List<Member> levelMembers = level.getMembers();
//	        for (Member member : levelMembers) {
//	          uniqueNameList.add(member.getUniqueName());
//	        }
//	      } catch (OlapException e) {
//	        e.printStackTrace();
//	      }
//	    }
//	    
//	    StringTree result = new StringTree(dimensionName, null);
//	    for (int i = 0; i < uniqueNameList.size(); i++) {
//	      String[] memberNames = uniqueNameList.get(i).split("\\."); //$NON-NLS-1$
//	      for (int j = 0; j < memberNames.length; j++) { // Trim off the brackets
//	        memberNames[j] = memberNames[j].substring(1, memberNames[j].length() - 1);
//	      }
//	      result = OlapUtil.parseMembers(memberNames, result);
//	    }
//
//	    return result;
	}
}
