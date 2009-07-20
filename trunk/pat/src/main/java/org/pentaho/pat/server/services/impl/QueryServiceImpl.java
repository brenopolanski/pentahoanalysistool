package org.pentaho.pat.server.services.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.olap4j.Axis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapStatement;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.query.Query;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;
import org.olap4j.query.Selection.Operator;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.messages.Messages;
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
	
	public String createNewQuery(String userId, String sessionId)
        throws OlapException 
    {
    
	    sessionService.validateUser(userId);
	    
        // We need to verify if the user has selected a cube.
        String cubeName = (String) sessionService.getUserSessionVariable(
            userId, sessionId, Constants.CURRENT_CUBE_NAME);

        if (cubeName == null)
            throw new OlapException(Messages
                    .getString("Services.Session.NoCubeSelected")); //$NON-NLS-1$

        Cube cube = this.getCube4Guid(userId, sessionId, cubeName);
        String generatedId = String.valueOf(UUID.randomUUID());
        Query newQuery;
        try {
            newQuery = new Query(generatedId, cube);
        } catch (SQLException e) {
            throw new OlapException(Messages
                    .getString("Services.Session.CreateQueryException"), //$NON-NLS-1$
                    e);
        }

        sessionService.getSession(userId, sessionId).getQueries().put(generatedId,newQuery);

        return generatedId;
    }

	public String createNewQuery(String userId, String sessionId, String mdx) 
	    throws OlapException 
	{
	    // Validate the user.
	    sessionService.validateUser(userId);
	    
	    // Generate a unique query name.
	    String generatedId = String.valueOf(UUID.randomUUID());
	    
	    
	    Query query = null;
	    
	    //query = Query.parse(mdx);
	    // FIXME Not implemented yet.
	    if (true)
	        throw new UnsupportedOperationException();
	    
	    sessionService.getSession(userId, sessionId).getQueries().put(generatedId,query);
	    
	    return generatedId;
	}
	
    private Cube getCube4Guid(String userId, String sessionId, String cubeName)
            throws OlapException {
        OlapConnection connection = sessionService.getConnection(userId, sessionId);
        NamedList<Cube> cubes = connection.getSchema().getCubes();
        Cube cube = null;
        Iterator<Cube> iter = cubes.iterator();
        while (iter.hasNext() && cube == null) {
            Cube testCube = iter.next();
            if (cubeName.equals(testCube.getName())) {
                cube = testCube;
            }
        }
        if (cube != null) {
            return cube;
        }

        throw new OlapException(Messages
                .getString("Services.Session.CubeNameNotValid")); //$NON-NLS-1$
    }

    public Query getQuery(String userId, String sessionId, String queryId) {
        sessionService.validateSession(userId, sessionId);
        return sessionService.getSession(userId, sessionId).getQueries().get(queryId);
    }

    public List<String> getQueries(String userId, String sessionId) {
        sessionService.validateSession(userId, sessionId);
        List<String> names = new ArrayList<String>();
        Set<Entry<String, Query>> entries = sessionService.getSession(userId, sessionId)
                .getQueries().entrySet();
        for (Entry<String, Query> entry : entries) {
            names.add(entry.getKey());
        }
        return names;
    }

    public void releaseQuery(String userId, String sessionId, String queryId) {
        sessionService.validateSession(userId, sessionId);
        sessionService.getSession(userId, sessionId).getQueries().remove(queryId);
    }
	
	@SuppressWarnings("deprecation")
	public void clearSelection(String userId, String sessionId, 
			String dimensionName, List<String> memberNames) 
	{
	    this.sessionService.validateSession(userId, sessionId);
	    
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		Query query = this.getQuery(userId, sessionId, currentQuery);
		
	    QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
	    String path = OlapUtil.normalizeMemberNames(memberNames.toArray(new String[memberNames.size()]));
	    Selection selection = OlapUtil.findSelection(path, qDim);
	    qDim.getSelections().remove(selection);
	}

	@SuppressWarnings("deprecation")
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

        Query query = this.getQuery(userId, sessionId,
                currentQuery);
        Cube cube = this.discoveryService.getCube(userId, sessionId,
                currentCube);

        // First try to resolve the member quick and dirty.
        Member member = cube.lookupMember(memberNames.toArray(new String[memberNames.size()]));
        
        if (member==null)
        {
            // Let's try with only the dimension name in front.
            List<String> dimPlusMemberNames = new ArrayList<String>();
            dimPlusMemberNames.add(dimensionName);
            dimPlusMemberNames.addAll(memberNames);
            member = cube.lookupMember(dimPlusMemberNames.toArray(new String[dimPlusMemberNames.size()]));

            if (member==null)
            {
                // Sometimes we need to find it in a different name format.
                // To make sure we find the member, the first element
                // will be sent as DimensionName.HierarchyName. Cubes which have
                // more than one hierarchy in a given dimension will require this
                // format anyways.
                List<String> completeMemberNames = new ArrayList<String>();
                completeMemberNames.add(dimensionName.concat(".").concat(memberNames.get(0))); //$NON-NLS-1$
                completeMemberNames.addAll(memberNames.subList(1, memberNames.size()));
                member = cube.lookupMember(completeMemberNames.toArray(new String[completeMemberNames.size()]));
    
                if (member==null)
                {
                    // We failed to find the member.
                    throw new OlapException(Messages.getString("Services.Query.Selection.CannotFindMember"));//$NON-NLS-1$
                }
            }
        }

        QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
        Selection.Operator selectionMode = Selection.Operator.values()[selectionType
                .ordinal()];
        // TODO is this a fix? 
         //Selection selection = qDim.createSelection(member, selectionMode);
        //        qDim.getSelections().add(selection);
        qDim.clearSelection();
        qDim.include(selectionMode, member);
        
    }

	public void moveDimension(String userId, String sessionId, Axis.Standard axis, String dimensionName) 
	{
	    this.sessionService.validateSession(userId, sessionId);

		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);
		
		Query query = this.getQuery(userId, sessionId, currentQuery);
		
	    query.getAxes().get(axis).getDimensions()
	    	.add(query.getDimension(dimensionName));
	}

	
	public CellDataSet executeQuery(String userId, String sessionId) throws OlapException 
	{
	    this.sessionService.validateSession(userId, sessionId);
	    
		String currentQuery = (String)this.sessionService.getUserSessionVariable(userId, 
				sessionId, Constants.CURRENT_QUERY_NAME);

		Query mdx = this.getQuery(userId, sessionId, currentQuery);
		
		return OlapUtil.cellSet2Matrix(mdx.execute());
	}

	// TODO is this the way we want mdx to work?
	public CellDataSet executeMdxQuery(String userId, String sessionId,
			String mdx) throws OlapException {
		this.sessionService.validateSession(userId, sessionId);
		
		OlapConnection con = this.sessionService.getConnection(userId, sessionId);
		OlapStatement stmt = con.createStatement();
		return OlapUtil.cellSet2Matrix(stmt.executeOlapQuery(mdx));
	}
	
	
}
