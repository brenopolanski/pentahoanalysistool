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
import org.pentaho.pat.rpc.dto.CellDataSet;
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
	
	public String createNewQuery(String userId, String sessionId,
	        String connectionId, String cubeName) throws OlapException 
    {
    
	    sessionService.validateUser(userId);

        if (cubeName == null)
            throw new OlapException(Messages
                    .getString("Services.Session.NoCubeSelected")); //$NON-NLS-1$

        Cube cube = this.getCube4Guid(userId, sessionId, connectionId, cubeName);
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
	
    private Cube getCube4Guid(String userId, String sessionId, String connectionId, String cubeName)
            throws OlapException 
    {
        OlapConnection connection = sessionService.getNativeConnection(userId, sessionId, connectionId);
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
	
    public void clearSelection(
            String userId, 
            String sessionId,
            String queryId,
            String dimensionName, 
            List<String> memberNames) 
	{
	    this.sessionService.validateSession(userId, sessionId);
		Query query = this.getQuery(userId, sessionId, queryId);
	    QueryDimension qDim = OlapUtil.getQueryDimension(query, dimensionName);
	    String path = OlapUtil.normalizeMemberNames(memberNames.toArray(new String[memberNames.size()]));
	    Selection selection = OlapUtil.findSelection(path, qDim);
	    qDim.getInclusions().remove(selection);
	}

    public void createSelection(
            String userId, 
            String sessionId,
            String queryId,
            String dimensionName, 
            List<String> memberNames, 
            Selection.Operator selectionType) throws OlapException
    {
	    this.sessionService.validateSession(userId, sessionId);

        Query query = this.getQuery(userId, sessionId, queryId);
        Cube cube = query.getCube();

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

        QueryDimension qDim = 
            OlapUtil.getQueryDimension(query, dimensionName);
        Selection.Operator selectionMode = 
            Selection.Operator.values()[selectionType.ordinal()];
        qDim.include(selectionMode, member);
    }

    public String[][] getSelection(
            String userId, 
            String sessionId,
            String queryId,
            String dimensionName) throws OlapException
    {
	    this.sessionService.validateSession(userId, sessionId);

        Query query = this.getQuery(userId, sessionId, queryId);

        QueryDimension qDim = 
            OlapUtil.getQueryDimension(query, dimensionName);
        
        List<Selection> selList = qDim.getInclusions();
        int i=0;
        String[][] selectionList = new String[selList.size()][2];
        for(Iterator<Selection> iter = selList.iterator(); iter.hasNext();){ 
            
            Selection sel = (Selection) iter.next();
            selectionList[i][0] = sel.getMember().getName();
            selectionList[i][1]= sel.getOperator().name();
            i++;
        }
	return selectionList;
        
    }
    
    public void moveDimension(
            String userId, 
            String sessionId,
            String queryId,
            Axis.Standard axis, 
            String dimensionName) 
	{
	    this.sessionService.validateSession(userId, sessionId);
		Query query = this.getQuery(userId, sessionId, queryId);
	    query.getAxes().get(axis).getDimensions()
	    	.add(query.getDimension(dimensionName));
	}

	
	public CellDataSet executeQuery(String userId, String sessionId, String queryId) throws OlapException 
	{
	    this.sessionService.validateSession(userId, sessionId);
		Query mdx = this.getQuery(userId, sessionId, queryId);
		return OlapUtil.cellSet2Matrix(mdx.execute());
	}

	// TODO is this the way we want mdx to work?
	public CellDataSet executeMdxQuery(String userId, String sessionId,
			String connectionId, String mdx) throws OlapException {
		this.sessionService.validateSession(userId, sessionId);
		OlapConnection con = this.sessionService.getNativeConnection(userId, sessionId, connectionId);
		OlapStatement stmt = con.createStatement();
		return OlapUtil.cellSet2Matrix(stmt.executeOlapQuery(mdx));
	}


    public String getMdxForQuery(String userId, String sessionId, String queryId)
            throws OlapException {
        this.sessionService.validateSession(userId, sessionId);
        Query q = this.getQuery(userId, sessionId, queryId);
        if (q == null) {
            throw new OlapException(Messages.getString("Services.Query.NoSuchQuery")); //$NON-NLS-1$
        }
        return q.getSelect().toString();
    }
	
	
}
