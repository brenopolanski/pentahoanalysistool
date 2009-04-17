package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Member;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.olap4j.query.Selection.Operator;
import org.pentaho.pat.rpc.beans.OlapData;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.OlapUtil;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.springframework.util.Assert;

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
		Assert.notNull(this.discoveryService);
		Assert.notNull(this.sessionService);
	}
	
	
	public void clearSelection(String userId, String sessionId, 
			String dimensionName, List<String> memberNames) 
	{
	    this.sessionService.validateSession(userId, sessionId);
	    
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		Query query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
	    QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
	    String path = OlapUtil.normalizeMemberNames((String[])memberNames.toArray());
	    Selection selection = OlapUtil.findSelection(path, qDim);
	    qDim.getSelections().remove(selection);
	}

	public void createSelection(String userId, String sessionId,
            String dimensionName, List<String> memberNames,
            Operator selectionType) throws OlapException
    {
	    this.sessionService.validateSession(userId, sessionId);

        String currentQuery = (String) this.sessionService
                .getUserSessionVariable(userId, sessionId,
                        Constants.CURRENT_QUERY_NAME);
        String currentCube = (String) this.sessionService
                .getUserSessionVariable(userId, sessionId,
                        Constants.CURRENT_CUBE_NAME);

        Query query = this.sessionService.getQuery(userId, sessionId,
                currentQuery);
        Cube cube = this.discoveryService.getCube(userId, sessionId,
                currentCube);

        Member member = cube.lookupMember((String[]) memberNames
                .toArray(new String[] {}));
        QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
        Selection.Operator selectionMode = Selection.Operator.values()[selectionType
                .ordinal()];
        Selection selection = qDim.createSelection(member, selectionMode);
        qDim.getSelections().add(selection);
    }

	public void moveDimension(String userId, String sessionId, Axis.Standard axis, String dimensionName) 
	{
	    this.sessionService.validateSession(userId, sessionId);

		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		
		Query query = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
	    query.getAxes().get(axis).getDimensions()
	    	.add(query.getDimension(dimensionName));
	}


	public OlapData executeQuery(String userId, String sessionId) throws OlapException 
	{
	    this.sessionService.validateSession(userId, sessionId);
	    
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);

		Query mdx = this.sessionService.getQuery(userId, sessionId, currentQuery);
		
		return OlapUtil.cellSet2OlapData(mdx.execute());
	}
}
