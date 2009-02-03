package org.pentaho.pat.server.services.impl;

import java.util.ArrayList;
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
import org.pentaho.pat.Constants;
import org.pentaho.pat.client.util.StringTree;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.OlapUtil;
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
	
	
	public String[][] getDrivers() {
		// TODO implement this.
		return null;
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
		String currentQuery = this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		Query query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		// The UNUSED axis is not used by default...
		Axis targetAxis = null;
	    if (!axis.name().equalsIgnoreCase(Axis.UNUSED.name())) { //$NON-NLS-1$
	      targetAxis = axis;
	    }
		
	    List<QueryDimension> dimList = query.getAxes().get(targetAxis).getDimensions();
	    List<String> dimNames = new ArrayList<String>();
	    for (QueryDimension dim : dimList) {
	      dimNames.add(dim.getName());
	    }
	    return dimNames;
	}

	public StringTree getMembers(String userId, String sessionId, String dimensionName) 
		throws OlapException 
	{
		
		String currentQuery = this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		Query query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		List<String> uniqueNameList = new ArrayList<String>();
	    
		NamedList<Level> levels = query.getDimension(dimensionName).getDimension()
	    	.getHierarchies().get(dimensionName).getLevels();
	    
	    for (Level level : levels) {
	      try {
	        List<Member> levelMembers = level.getMembers();
	        for (Member member : levelMembers) {
	          uniqueNameList.add(member.getUniqueName());
	        }
	      } catch (OlapException e) {
	        e.printStackTrace();
	      }
	    }
	    
	    StringTree result = new StringTree(dimensionName, null);
	    for (int i = 0; i < uniqueNameList.size(); i++) {
	      String[] memberNames = uniqueNameList.get(i).split("\\."); //$NON-NLS-1$
	      for (int j = 0; j < memberNames.length; j++) { // Trim off the brackets
	        memberNames[j] = memberNames[j].substring(1, memberNames[j].length() - 1);
	      }
	      result = OlapUtil.parseMembers(memberNames, result);
	    }

	    return result;
		
	}
}
