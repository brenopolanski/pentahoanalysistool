package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.junit.Assert;
import org.olap4j.Axis;
import org.olap4j.metadata.Cube;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;

public class DiscoveryServiceImplTest extends AbstractServiceTest {

	private SessionService sessionService;
	private DiscoveryService discoveryService;
	private QueryService queryService;
	
	
	public void testGetCubes() throws Exception 
	{
	    final String userId = "admin"; //$NON-NLS-1$
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		String connectionId = createConnection(this.sessionService, userId, sessionId); 
		
		// Test returned cubes.
		List<CubeItem> cubes = this.discoveryService.getCubes(userId, sessionId, connectionId); 
		assertNotNull(cubes);
		assertEquals(1, cubes.size());
		assertEquals("Quadrant Analysis", cubes.get(0).getName()); //$NON-NLS-1$
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); 
		
		finishTest();
	}
	
	
	
	public void testGetCube() throws Exception 
	{
	    final String userId = "admin"; //$NON-NLS-1$
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		String connectionId = createConnection(this.sessionService, userId, sessionId); 
		
		// Test returned cubes.
		Cube cube = this.discoveryService.getCube(userId, sessionId, connectionId, "Quadrant Analysis"); //$NON-NLS-1$ 
		assertNotNull(cube);
		assertEquals("Quadrant Analysis", cube.getName()); //$NON-NLS-1$
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); 
		
		finishTest();
	}

	
	
	
	public void testGetDimensions() throws Exception 
	{
	    final String userId = "admin"; //$NON-NLS-1$
		String[] refDims = new String[] {
				"Measures", //$NON-NLS-1$
				"Region", //$NON-NLS-1$
				"Department", //$NON-NLS-1$
				"Positions" //$NON-NLS-1$
		};
		
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		String connectionId = createConnection(this.sessionService, userId, sessionId); 
		
		// Create and select a cube
		Cube cube = this.discoveryService.getCube(userId, sessionId, connectionId, "Quadrant Analysis"); //$NON-NLS-1$ 
		assertNotNull(cube);
		assertEquals("Quadrant Analysis", cube.getName()); //$NON-NLS-1$ 
		
		// Create and select a query
		String queryId = this.queryService.createNewQuery(userId, sessionId, connectionId, cube.getName());
		
		List<String> dims = this.discoveryService.getDimensions(userId, sessionId, queryId, Axis.UNUSED);
		assertNotNull(dims);
		assertEquals(4, dims.size());
		String[] currentList = new String[4];
		dims.toArray(currentList);
		//assert(refDims, currentList);
		Assert.assertArrayEquals(refDims, currentList);
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); 
		
		finishTest();
	}
	
	
	
	
	public void testGetMembers() throws Exception 
	{
	    final String userId = "admin"; //$NON-NLS-1$
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); 
		String connectionId = createConnection(this.sessionService, userId, sessionId); 
		
		// Create and select a cube
		Cube cube = this.discoveryService.getCube(userId, sessionId, connectionId, "Quadrant Analysis"); //$NON-NLS-1$  
		
		// Create and select a query
		String queryId = this.queryService.createNewQuery(userId, sessionId, connectionId, cube.getName());  
		
		StringTree members = this.discoveryService.getMembers(userId, sessionId, queryId, "Region"); //$NON-NLS-1$ 
		assertNotNull(members);
		assertEquals("Region", members.getValue()); //$NON-NLS-1$
		assertEquals(5, members.getChildren().size());
		assertEquals(0, members.getChildren().get(0).getChildren().size());
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); 
		
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
		this.queryService = (QueryService)applicationContext.getBean("queryService"); //$NON-NLS-1$
	}
	
	
	private void finishTest() {
	}
}
