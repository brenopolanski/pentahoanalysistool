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
import org.pentaho.pat.client.util.StringTree;
import org.pentaho.pat.server.data.ConnectionManager;
import org.pentaho.pat.server.data.QueryManager;
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

	private SessionService sessionService;
	
	private ConnectionManager connectionManager;
	
	private QueryManager queryManager;
	
	Logger log = Logger.getLogger(this.getClass());

	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}
	
	public void setConnectionManager(ConnectionManager manager) {
		this.connectionManager = manager;
	}
	
	public void setQueryManager(QueryManager manager) {
		this.queryManager = manager;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (this.sessionService == null)
			throw new Exception("A sessionService is required.");
		if (this.connectionManager == null)
			throw new Exception("A connectionManager is required.");
		if (this.queryManager == null)
			throw new Exception("A queryManager is required.");
	}
	
	public List<String> getCubes(String guid) {
		
		try {
			
			List<String> list = new ArrayList<String>();
			
			OlapConnection conn = this.connectionManager.getConnection(guid);
			
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

	
	public Cube getCube(String guid, String name) {
		try {
			OlapConnection conn = this.connectionManager.getConnection(guid);
			return conn.getSchema().getCubes().get(name);
		} catch (Exception e) {}
		return null;
	}
	
	
	public List<String> getDimensions(String guid, Axis axis) 
		throws OlapException
	{
		Query query = this.queryManager.get(guid,
				this.sessionService.getCurrentQuery(guid));
	    List<QueryDimension> dimList = query.getAxes().get(axis).getDimensions();
	    List<String> dimNames = new ArrayList<String>();
	    for (QueryDimension dim : dimList) {
	      dimNames.add(dim.getName());
	    }
	    return dimNames;
	}

	public StringTree getMembers(String guid, String dimensionName) 
		throws OlapException 
	{
		
		Query query = this.queryManager.get(guid,
				this.sessionService.getCurrentQuery(guid));
		
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
