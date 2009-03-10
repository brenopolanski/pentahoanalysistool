package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.mdx.SelectNode;
import org.olap4j.metadata.Cube;
import org.olap4j.query.Selection.Operator;
import org.pentaho.pat.client.util.OlapData;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.OlapUtil;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;

/**
 * Simple service implementation as a Spring bean.
 * @author Luc Boudreau
 */
public class QueryServiceImpl extends AbstractService 
	implements QueryService 
{
	private SessionService sessionService = null;

	private DiscoveryService discoveryService = null;
	
	
	public void setDiscoveryService(DiscoveryService discoveryService) {
		this.discoveryService = discoveryService;
	}


	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}

	
	public void afterPropertiesSet() throws Exception {
		if (this.sessionService == null)
			throw new Exception("A sessionService is required.");
		if (this.discoveryService == null)
			throw new Exception("A discoveryService is required.");
	}
	
	
	public Boolean clearSelection(String userId, String sessionId, 
			String dimensionName, List<String> memberNames) 
	{
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		SelectNode query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		// FIXME IMPLEMENT ME
		throw new UnsupportedOperationException();

//	    QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
//	    String path = OlapUtil.normalizeMemberNames((String[])memberNames.toArray());
//	    Selection selection = OlapUtil.findSelection(path, qDim);
//	    if (selection == null) {
//	      return new Boolean(false);
//	    }
//	    qDim.getSelections().remove(selection);
//	    return new Boolean(true);
	}

	public Boolean createSelection(String userId, String sessionId, String dimensionName,
			List<String> memberNames, Operator selectionType) {
		
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		String currentCube = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_CUBE_NAME);
		
		SelectNode query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		Cube cube = this.discoveryService.getCube(userId, sessionId, currentCube);
		
		// FIXME IMPLEMENT ME
		throw new UnsupportedOperationException();
		
//		try {
//		      Member member = cube.lookupMember((String[])memberNames.toArray());
//		      QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
//		      Selection.Operator selectionMode = Selection.Operator.values()[selectionType.ordinal()];
//		      Selection selection = qDim.createSelection(member, selectionMode);
//		      qDim.getSelections().add(selection);
//	    } catch (OlapException e) {
//	      e.printStackTrace();
//	      return new Boolean(false);
//	    }
//	    return new Boolean(true);
	}

	public Boolean moveDimension(String userId, String sessionId, Axis axis, String dimensionName) {

		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		
		SelectNode query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		// FIXME IMPLEMENT ME
		throw new UnsupportedOperationException();

//	    query.getAxes().get(axis).getDimensions()
//	    	.add(query.getDimension(dimensionName));
	}


	public OlapData executeQuery(String userId, String sessionId) throws OlapException 
	{
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);

		SelectNode mdx = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
        OlapStatement olapStatement = this.sessionService.getConnection(userId, sessionId).createStatement();
        
		return OlapUtil.cellSet2OlapData(olapStatement.executeOlapQuery(mdx));
	}
}
