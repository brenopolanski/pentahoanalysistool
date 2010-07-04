package org.pentaho.pat.server.services.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.olap4j.Axis;
import org.olap4j.mdx.ParseTreeWriter;
import org.olap4j.query.Query;
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.enums.SelectionType;
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
        String connectionId = createConnection(this.sessionService, userId, sessionId); 
        
        // Create a query object.
        String cubeName = this.discoveryService.getCubes(userId, sessionId, connectionId).get(0).getName();
        assertNotNull(cubeName); 
        String queryId = this.queryService.createNewQuery(userId, sessionId, connectionId, cubeName); 
        assertNotNull(queryId); 
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
		String expectedMDX = "SELECT*new_line*{[Department].[All Departments]} ON COLUMNS,*new_line*CrossJoin({[Positions].[All Positions]}, {[Region].[All Regions]}) ON ROWS*new_line*FROM [Quadrant Analysis]"; //$NON-NLS-1$
		expectedMDX = expectedMDX.replaceAll("\\*new\\_line\\*", System.getProperty( "line.separator" )); //$NON-NLS-1$ //$NON-NLS-2$
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		String connectionId = createConnection(this.sessionService, userId, sessionId); 
		
		// Create a new query
		String queryId = this.queryService.createNewQuery(userId, sessionId, connectionId, "Quadrant Analysis"); //$NON-NLS-1$
		
		// Move some dimensions around
		this.queryService.moveDimension(userId, sessionId, queryId, Axis.COLUMNS, "Department"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, queryId, Axis.ROWS, "Positions"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, queryId, Axis.ROWS, "Region"); //$NON-NLS-1$
		

		// Verify the axis
		Query query = this.queryService.getQuery(userId, sessionId, queryId);
		assertNotNull(query);
		assertEquals(2, query.getAxes().get(Axis.ROWS).getDimensions().size());
		assertEquals(1, query.getAxes().get(Axis.COLUMNS).getDimensions().size());
		assertTrue(query.getAxes().containsKey(Axis.FILTER));
		assertEquals("Department", query.getAxes().get(Axis.COLUMNS).getDimensions().get(0).getName()); //$NON-NLS-1$
		assertEquals("Positions", query.getAxes().get(Axis.ROWS).getDimensions().get(0).getName()); //$NON-NLS-1$
		assertEquals("Region", query.getAxes().get(Axis.ROWS).getDimensions().get(1).getName()); //$NON-NLS-1$
		// Verify MDX
		query.validate();
		Writer writer = new StringWriter();
		//query.getSelect().unparse(new ParseTreeWriter(new PrintWriter(writer)));
		
		//assertEquals(expectedMDX, writer.toString());
		
		// Launch query to make sure it's valid
		this.queryService.executeQuery(userId, sessionId, queryId);
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); 
		
		finishTest();
	}
	
	
	public void testSelections() throws Exception
	{
		initTest();
		String userId = "admin"; //$NON-NLS-1$
		String expectedMDX = "SELECT*new_line*NON EMPTY {[Department].[Finance].Siblings} ON COLUMNS,*new_line*NON EMPTY CrossJoin({[Positions].[CTO]}, {[Region].[Central], [Region].[Central].Children}) ON ROWS*new_line*FROM [Quadrant Analysis]"; //$NON-NLS-1$
		expectedMDX = expectedMDX.replaceAll("\\*new\\_line\\*", System.getProperty( "line.separator" )); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		String connectionId = createConnection(this.sessionService, userId, sessionId);
		
		// Create a new query
		String queryId = this.queryService.createNewQuery(userId, sessionId, connectionId, "Quadrant Analysis"); //$NON-NLS-1$
		
		// Place dimensions on axis
		this.queryService.moveDimension(userId, sessionId, queryId, Axis.COLUMNS, "Department"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, queryId, Axis.ROWS, "Positions"); //$NON-NLS-1$
		this.queryService.moveDimension(userId, sessionId, queryId, Axis.ROWS, "Region"); //$NON-NLS-1$
		
		// Select members 
		List<String> departmentSelections = new ArrayList<String>();
		departmentSelections.add("Finance"); //$NON-NLS-1$
		//this.queryService.createSelection(userId, sessionId, queryId, "Department", departmentSelections, Selection.Operator.SIBLINGS); //$NON-NLS-1$
		this.queryService.createSelection(userId, sessionId, queryId, "[Department].[Finance]", ObjectType.MEMBER, SelectionType.SIBLINGS);
		List<String> positionSelections = new ArrayList<String>();
		positionSelections.add("CTO"); //$NON-NLS-1$
		this.queryService.createSelection(userId, sessionId, queryId, "[Positions].[CTO]", ObjectType.MEMBER, SelectionType.MEMBER); //$NON-NLS-1$
		List<String> regionSelections = new ArrayList<String>();
		regionSelections.add("Central"); //$NON-NLS-1$
		this.queryService.createSelection(userId, sessionId, queryId, "[Region].[Central]", ObjectType.MEMBER, SelectionType.INCLUDE_CHILDREN); //$NON-NLS-1$
		
		// Verify MDX
		Query query = this.queryService.getQuery(userId, sessionId, queryId);
		Writer writer = new StringWriter();
		query.getSelect().unparse(new ParseTreeWriter(new PrintWriter(writer)));
		assertEquals(expectedMDX, writer.toString());
		
		// Launch query to make sure it's valid
		this.queryService.executeQuery(userId, sessionId, queryId);
		
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
