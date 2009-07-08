package org.pentaho.pat.server.services.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.mdx.ParseTreeWriter;
import org.olap4j.query.Query;
import org.olap4j.query.Selection;
import org.pentaho.pat.client.i18n.PatConstants;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;

public class QueryServiceImplTest extends AbstractServiceTest {

	private SessionService sessionService;
	private QueryService queryService;
	private DiscoveryService discoveryService;
	
	public void testCreateQuery() throws Exception 
    {
        final String userId = "admin"; //$NON-NLS-1$
        initTest();
        
        // Create a single session.
        String sessionId = this.sessionService.createNewSession(userId); 
        
        // Create a connection.
        createConnection(this.sessionService, userId, sessionId); 
        
        // Creating a query without selecting a cube should fail
        try {
            this.queryService.createNewQuery(userId, sessionId); 
            fail();
        } catch (OlapException e) {
            // ignore
        }
        
        // Create a query object.
        String cubeName = this.discoveryService.getCubes(userId, sessionId).get(0); 
        assertNotNull(cubeName);
        this.sessionService.saveUserSessionVariable(userId, sessionId, PatConstants.CURRENT_CUBE_NAME, cubeName); 
        String queryId = this.queryService.createNewQuery(userId, sessionId); 
        assertNotNull(queryId);
        this.sessionService.saveUserSessionVariable(userId, sessionId, PatConstants.CURRENT_QUERY_NAME, queryId); 
        assertNotNull(this.queryService.getQuery(userId, sessionId, queryId)); 
        assertEquals(1, this.queryService.getQueries(userId, sessionId).size()); 
        
        
        // Test the release of a query.
        this.queryService.releaseQuery(userId, sessionId, queryId); 
        assertNull(this.queryService.getQuery(userId, sessionId, queryId)); 
        assertEquals(0, this.queryService.getQueries(userId, sessionId).size()); 
        
        
        // Close this session.
        this.sessionService.releaseSession(userId, sessionId); 
        
        finishTest();
    }
	
	public void testMoveDimensions() throws Exception 
	{
		String userId = "admin"; //$NON-NLS-1$
		String expectedMDX = "SELECT*new_line*{} ON COLUMNS,*new_line*CrossJoin({}, {}) ON ROWS*new_line*FROM [Quadrant Analysis]"; //$NON-NLS-1$
		expectedMDX = expectedMDX.replaceAll("\\*new\\_line\\*", System.getProperty( "line.separator" )); //$NON-NLS-1$ //$NON-NLS-2$
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		super.createConnection(this.sessionService, userId, sessionId); 
		
		// Select a cube
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_CUBE_NAME, "Quadrant Analysis"); //$NON-NLS-1$
		
		// Create a new query
		String queryId = this.queryService.createNewQuery(userId, sessionId);
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		
		// Move some dimensions around
		this.queryService.moveDimension(userId, sessionId, Axis.COLUMNS, "Department"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Department"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Positions"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, Axis.COLUMNS, "Region"); //$NON-NLS-1$
		
		// Verify the axis
		Query query = this.queryService.getQuery(userId, sessionId, queryId);
		assertNotNull(query);
		assertEquals(2, query.getAxes().get(Axis.ROWS).getDimensions().size());
		assertEquals(1, query.getAxes().get(Axis.COLUMNS).getDimensions().size());
		assertEquals(0, query.getAxes().get(Axis.FILTER).getDimensions().size());
		assertEquals("Region", query.getAxes().get(Axis.COLUMNS).getDimensions().get(0).getName()); //$NON-NLS-1$
		
		// Verify MDX
		Writer writer = new StringWriter();
		query.getSelect().unparse(new ParseTreeWriter(new PrintWriter(writer)));
		assertEquals(expectedMDX, writer.toString());
		
		// Launch query to make sure it's valid
		this.queryService.executeQuery2(userId, sessionId);
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); 
		
		finishTest();
	}
	
	
	public void testSelections() throws Exception
	{
		initTest();
		String userId = "admin"; //$NON-NLS-1$
		String expectedMDX = "SELECT*new_line*{[Department].[All Departments].[Finance].Siblings} ON COLUMNS,*new_line*CrossJoin({[Positions].[All Positions].[CTO]}, {{[Region].[All Regions].[Central], [Region].[All Regions].[Central].Children}}) ON ROWS*new_line*FROM [Quadrant Analysis]"; //$NON-NLS-1$
		expectedMDX = expectedMDX.replaceAll("\\*new\\_line\\*", System.getProperty( "line.separator" )); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		super.createConnection(this.sessionService, userId, sessionId); 
		
		// Select a cube
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_CUBE_NAME, "Quadrant Analysis"); //$NON-NLS-1$
		
		// Create a new query
		String queryId = this.queryService.createNewQuery(userId, sessionId);
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		
		// Place dimensions on axis
		this.queryService.moveDimension(userId, sessionId, Axis.COLUMNS, "Department"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Positions"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Region"); //$NON-NLS-1$
		
		// Select members 
		List<String> departmentSelections = new ArrayList<String>();
		departmentSelections.add("Finance"); //$NON-NLS-1$
		this.queryService.createSelection(userId, sessionId, "Department", departmentSelections, Selection.Operator.SIBLINGS); //$NON-NLS-1$
		List<String> positionSelections = new ArrayList<String>();
		positionSelections.add("CTO"); //$NON-NLS-1$
		this.queryService.createSelection(userId, sessionId, "Positions", positionSelections, Selection.Operator.MEMBER); //$NON-NLS-1$
		List<String> regionSelections = new ArrayList<String>();
		regionSelections.add("Central"); //$NON-NLS-1$
		this.queryService.createSelection(userId, sessionId, "Region", regionSelections, Selection.Operator.INCLUDE_CHILDREN); //$NON-NLS-1$
		
		// Verify MDX
		Query query = this.queryService.getQuery(userId, sessionId, queryId);
		Writer writer = new StringWriter();
		query.getSelect().unparse(new ParseTreeWriter(new PrintWriter(writer)));
		assertEquals(expectedMDX, writer.toString());
		
		// Launch query to make sure it's valid
		this.queryService.executeQuery2(userId, sessionId);
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); 
		
		finishTest();
	}
	
	
	
	
	
	private void initTest() {
		initTestContext();
		this.sessionService = (SessionService)applicationContext.getBean("sessionService"); //$NON-NLS-1$
        this.queryService = (QueryService)applicationContext.getBean("queryService"); //$NON-NLS-1$
        this.discoveryService = (DiscoveryService)applicationContext.getBean("discoveryService"); //$NON-NLS-1$
	}
	
	
	private void finishTest() {
		
	}
}
