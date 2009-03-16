package org.pentaho.pat.server.services.impl;

import java.sql.DriverManager;
import java.util.List;

import org.junit.Assert;
import org.olap4j.Axis;
import org.olap4j.metadata.Cube;
import org.pentaho.pat.Constants;
import org.pentaho.pat.client.util.StringTree;
import org.pentaho.pat.test.TestContext;

public class DiscoveryServiceImplTest extends TestContext {

	private SessionServiceImpl sessionService;
	private DiscoveryServiceStub discoveryService;
	
	
	public void testGetCubes() throws Exception 
	{
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		super.createConnection(this.sessionService, "user", sessionId);
		
		// Test returned cubes.
		List<String> cubes = this.discoveryService.getCubes("user", sessionId);
		assertNotNull(cubes);
		assertEquals(1, cubes.size());
		assertEquals("Quadrant Analysis", cubes.get(0));
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	public void testGetCube() throws Exception 
	{
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		super.createConnection(this.sessionService, "user", sessionId);
		
		// Test returned cubes.
		Cube cube = this.discoveryService.getCube("user", sessionId, "Quadrant Analysis");
		assertNotNull(cube);
		assertEquals("Quadrant Analysis", cube.getName());
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}

	
	
	
	public void testGetDimensions() throws Exception 
	{
		String[] refDims = new String[] {
				"Measures",
				"Region",
				"Department",
				"Positions"
		};
		
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		super.createConnection(this.sessionService, "user", sessionId);
		
		// Create and select a cube
		Cube cube = this.discoveryService.getCube("user", sessionId, "Quadrant Analysis");
		assertNotNull(cube);
		assertEquals("Quadrant Analysis", cube.getName());
		this.sessionService.saveUserSessionVariable("user", sessionId, Constants.CURRENT_CUBE_NAME, cube.getName());
		
		// Create and select a query
		String queryId = this.sessionService.createNewQuery("user", sessionId);
		this.sessionService.saveUserSessionVariable("user", sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		
		List<String> dims = this.discoveryService.getDimensions("user", sessionId, null);
		assertNotNull(dims);
		assertEquals(4, dims.size());
		String[] currentList = new String[4];
		dims.toArray(currentList);
		//assert(refDims, currentList);
		Assert.assertArrayEquals(refDims, currentList);
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	
	public void testGetMembers() throws Exception 
	{
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		super.createConnection(this.sessionService, "user", sessionId);
		
		// Create and select a cube
		Cube cube = this.discoveryService.getCube("user", sessionId, "Quadrant Analysis");
		this.sessionService.saveUserSessionVariable("user", sessionId, Constants.CURRENT_CUBE_NAME, cube.getName());
		
		// Create and select a query
		String queryId = this.sessionService.createNewQuery("user", sessionId);
		this.sessionService.saveUserSessionVariable("user", sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		
		StringTree members = this.discoveryService.getMembers("user", sessionId, "Region");
		assertNotNull(members);
		assertEquals("Region", members.getValue());
		assertEquals(1, members.getChildren().size());
		assertEquals(4, members.getChildren().get(0).getChildren().size());
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	public void testGetDrivers() throws Exception
	{
		initTest();
		
		this.discoveryService.getDrivers();
		
		finishTest();
	}
	
	
	
	
	private void initTest() {
		initTestContext();
		this.sessionService = new SessionServiceImpl();
		this.discoveryService = new DiscoveryServiceStub();
		this.sessionService.setDiscoveryService(this.discoveryService);
		this.discoveryService.setSessionService(this.sessionService);
	}
	
	
	private void finishTest() {
		this.sessionService.setDiscoveryService(null);
		this.discoveryService = null;
		this.sessionService = null;
	}
	
	
	private static class DiscoveryServiceStub extends DiscoveryServiceImpl {
		
	}
	
}
