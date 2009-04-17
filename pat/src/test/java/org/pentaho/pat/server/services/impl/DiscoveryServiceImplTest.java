package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.junit.Assert;
import org.olap4j.Axis;
import org.olap4j.metadata.Cube;
import org.pentaho.pat.Constants;
import org.pentaho.pat.rpc.beans.StringTree;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;

public class DiscoveryServiceImplTest extends AbstractServiceTest {

	private SessionService sessionService;
	private DiscoveryService discoveryService;
	
	
	public void testGetCubes() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		super.createConnection(this.sessionService, userId, sessionId); //$NON-NLS-1$
		
		// Test returned cubes.
		List<String> cubes = this.discoveryService.getCubes(userId, sessionId); //$NON-NLS-1$
		assertNotNull(cubes);
		assertEquals(1, cubes.size());
		assertEquals("Quadrant Analysis", cubes.get(0)); //$NON-NLS-1$
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
	}
	
	
	
	public void testGetCube() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		super.createConnection(this.sessionService, userId, sessionId); //$NON-NLS-1$
		
		// Test returned cubes.
		Cube cube = this.discoveryService.getCube(userId, sessionId, "Quadrant Analysis"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(cube);
		assertEquals("Quadrant Analysis", cube.getName()); //$NON-NLS-1$
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
	}

	
	
	
	public void testGetDimensions() throws Exception 
	{
	    final String userId = "admin";
		String[] refDims = new String[] {
				"Measures", //$NON-NLS-1$
				"Region", //$NON-NLS-1$
				"Department", //$NON-NLS-1$
				"Positions" //$NON-NLS-1$
		};
		
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		super.createConnection(this.sessionService, userId, sessionId); //$NON-NLS-1$
		
		// Create and select a cube
		Cube cube = this.discoveryService.getCube(userId, sessionId, "Quadrant Analysis"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(cube);
		assertEquals("Quadrant Analysis", cube.getName()); //$NON-NLS-1$
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_CUBE_NAME, cube.getName()); //$NON-NLS-1$
		
		// Create and select a query
		String queryId = this.sessionService.createNewQuery(userId, sessionId); //$NON-NLS-1$
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_QUERY_NAME, queryId); //$NON-NLS-1$
		
		List<String> dims = this.discoveryService.getDimensions(userId, sessionId, Axis.UNUSED); //$NON-NLS-1$
		assertNotNull(dims);
		assertEquals(4, dims.size());
		String[] currentList = new String[4];
		dims.toArray(currentList);
		//assert(refDims, currentList);
		Assert.assertArrayEquals(refDims, currentList);
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
	}
	
	
	
	
	public void testGetMembers() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		super.createConnection(this.sessionService, userId, sessionId); //$NON-NLS-1$
		
		// Create and select a cube
		Cube cube = this.discoveryService.getCube(userId, sessionId, "Quadrant Analysis"); //$NON-NLS-1$ //$NON-NLS-2$
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_CUBE_NAME, cube.getName()); //$NON-NLS-1$
		
		// Create and select a query
		String queryId = this.sessionService.createNewQuery(userId, sessionId); //$NON-NLS-1$
		this.sessionService.saveUserSessionVariable(userId, sessionId, Constants.CURRENT_QUERY_NAME, queryId); //$NON-NLS-1$
		
		StringTree members = this.discoveryService.getMembers(userId, sessionId, "Region"); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(members);
		assertEquals("Region", members.getValue()); //$NON-NLS-1$
		assertEquals(1, members.getChildren().size());
		assertEquals(4, members.getChildren().get(0).getChildren().size());
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
	}
	
	
	
	public void testGetDrivers() throws Exception
	{
		initTest();
		
		List<String> drivers = this.discoveryService.getDrivers();
		assertNotNull(drivers);
		assertTrue(drivers.size()>0);
		
		for (String value : drivers)
		    assertNotNull(value);
		
		finishTest();
	}
	
	
	
	
	private void initTest() {
		initTestContext();
		this.sessionService = (SessionService)applicationContext.getBean("sessionService"); //$NON-NLS-1$
		this.discoveryService = (DiscoveryService)applicationContext.getBean("discoveryService"); //$NON-NLS-1$
	}
	
	
	private void finishTest() {
	}
}
