package org.pentaho.pat.server.services.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.olap4j.Axis;
import org.olap4j.mdx.ParseTreeWriter;
import org.olap4j.query.Query;
import org.olap4j.query.Selection;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;

public class QueryServiceImplTest extends AbstractServiceTest {

	private SessionService sessionService;
	private QueryService queryService;
	
	
	public void testMoveDimensions() throws Exception 
	{
		String userId = "user";
		String expectedMDX = "SELECT\r\n{} ON COLUMNS,\r\nCrossJoin({}, {}) ON ROWS\r\nFROM [Quadrant Analysis]";
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		super.createConnection(this.sessionService, "user", sessionId);
		
		// Select a cube
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_CUBE_NAME, "Quadrant Analysis");
		
		// Create a new query
		String queryId = this.sessionService.createNewQuery(userId, sessionId);
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		
		// Move some dimensions around
		this.queryService.moveDimension(userId, sessionId, Axis.COLUMNS, "Department");
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Department");
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Positions");
		this.queryService.moveDimension(userId, sessionId, Axis.COLUMNS, "Region");
		
		// Verify the axis
		Query query = this.sessionService.getQuery(userId, sessionId, queryId);
		assertNotNull(query);
		assertEquals(2, query.getAxes().get(Axis.ROWS).getDimensions().size());
		assertEquals(1, query.getAxes().get(Axis.COLUMNS).getDimensions().size());
		assertEquals(0, query.getAxes().get(Axis.FILTER).getDimensions().size());
		assertEquals("Region", query.getAxes().get(Axis.COLUMNS).getDimensions().get(0).getName());
		
		// Verify MDX
		Writer writer = new StringWriter();
		query.getSelect().unparse(new ParseTreeWriter(new PrintWriter(writer)));
		assertEquals(expectedMDX, writer.toString());
		
		// Launch query to make sure it's valid
		this.queryService.executeQuery(userId, sessionId);
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	public void testSelections() throws Exception
	{
		initTest();
		String userId = "user";
		String expectedMDX = "SELECT\r\n{[Department].[All Departments].[Finance].Siblings} ON COLUMNS,\r\nCrossJoin({[Positions].[All Positions].[CTO]}, {{[Region].[All Regions].[Central], [Region].[All Regions].[Central].Children}}) ON ROWS\r\nFROM [Quadrant Analysis]";
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		super.createConnection(this.sessionService, "user", sessionId);
		
		// Select a cube
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_CUBE_NAME, "Quadrant Analysis");
		
		// Create a new query
		String queryId = this.sessionService.createNewQuery(userId, sessionId);
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		
		// Place dimensions on axis
		this.queryService.moveDimension(userId, sessionId, Axis.COLUMNS, "Department");
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Positions");
		this.queryService.moveDimension(userId, sessionId, Axis.ROWS, "Region");
		
		// Select members 
		List<String> departmentSelections = new ArrayList<String>();
		departmentSelections.add("Finance");
		this.queryService.createSelection(userId, sessionId, "Department", departmentSelections, Selection.Operator.SIBLINGS);
		List<String> positionSelections = new ArrayList<String>();
		positionSelections.add("CTO");
		this.queryService.createSelection(userId, sessionId, "Positions", positionSelections, Selection.Operator.MEMBER);
		List<String> regionSelections = new ArrayList<String>();
		regionSelections.add("Central");
		this.queryService.createSelection(userId, sessionId, "Region", regionSelections, Selection.Operator.INCLUDE_CHILDREN);
		
		// Verify MDX
		Query query = this.sessionService.getQuery(userId, sessionId, queryId);
		Writer writer = new StringWriter();
		query.getSelect().unparse(new ParseTreeWriter(new PrintWriter(writer)));
		assertEquals(expectedMDX, writer.toString());
		
		// Launch query to make sure it's valid
		this.queryService.executeQuery(userId, sessionId);
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	
	
	private void initTest() {
		initTestContext();
		this.sessionService = (SessionService)applicationContext.getBean("sessionService");
        this.queryService = (QueryService)applicationContext.getBean("queryService");
        
	}
	
	
	private void finishTest() {
		
	}
}
