package org.pentaho.pat.server.services.impl;

import org.pentaho.pat.test.TestContext;

public class SessionServiceImplTest extends TestContext {

	private SessionServiceImpl sessionService;
	private DiscoveryServiceStub discoveryService;
	
	
	/**
	 * Tests the creation of a session.
	 * @throws Exception
	 */
	public void testCreateSession() throws Exception 
	{
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		
		// Check if it has been saved.
		assertTrue(this.sessionService.getSessions().containsKey("user"));
		assertTrue(this.sessionService.getSessions().get("user").containsKey(sessionId));
		
		// Check if the created session object is valid.
		assertEquals(sessionId, this.sessionService.getSessions().get("user").get(sessionId).getId());
		assertTrue(this.sessionService.getSessions().get("user").get(sessionId).getQueries()!=null);
		assertTrue(this.sessionService.getSessions().get("user").get(sessionId).getVariables()!=null);
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	
	
	
	
	
	/**
	 * Tests the creation of a session.
	 * @throws Exception
	 */
	public void testDestroySession() throws Exception 
	{
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession("user");
		
		// Check if it has been saved.
		assertTrue(this.sessionService.getSessions().containsKey("user"));
		assertTrue(this.sessionService.getSessions().get("user").containsKey(sessionId));
		
		// Check if the created session object is valid.
		assertEquals(sessionId, this.sessionService.getSessions().get("user").get(sessionId).getId());
		assertTrue(this.sessionService.getSessions().get("user").get(sessionId).getQueries()!=null);
		assertTrue(this.sessionService.getSessions().get("user").get(sessionId).getVariables()!=null);
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		// Check if it was released along with the user space.
		assertTrue(!this.sessionService.getSessions().containsKey("user"));
		
		// Create two sessions.
		String sessionId1 = this.sessionService.createNewSession("user");
		String sessionId2 = this.sessionService.createNewSession("user");
		
		// Check if they are saved properly
		assertTrue(this.sessionService.getSessions().containsKey("user"));
		assertTrue(this.sessionService.getSessions().get("user").containsKey(sessionId1));
		assertTrue(this.sessionService.getSessions().get("user").containsKey(sessionId2));
		
		// Release one of the sessions
		this.sessionService.releaseSession("user", sessionId1);
		
		// Assert only the session space was released and the other session is still there.
		assertTrue(this.sessionService.getSessions().containsKey("user"));
		assertTrue(!this.sessionService.getSessions().get("user").containsKey(sessionId1));
		assertTrue(this.sessionService.getSessions().get("user").containsKey(sessionId2));
		
		// release the last session
		this.sessionService.releaseSession("user", sessionId2);
		
		// Assert the user space is not there anymore
		assertTrue(!this.sessionService.getSessions().containsKey("user"));
		
		finishTest();
	}
	
	
	
	
	
	
	
	public void testSessionVariables() throws Exception 
	{
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession("user");
		
		// Save a variable to the session
		this.sessionService.saveUserSessionVariable("user", sessionId, "key", "value");
		
		// Check if it's there through a call to the object.
		assertEquals("value", this.sessionService.getUserSessionVariable("user", sessionId, "key"));
		
		// Release it
		this.sessionService.deleteUserSessionVariable("user", sessionId, "key");
		
		// Make sure it's gone
		assertNull(this.sessionService.getUserSessionVariable("user", sessionId, "key"));
		
		// Close this session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
		
	}
	
	
	
	
	
	
	
	public void testConnection() throws Exception 
	{
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession("user");
		
		// Create a connection.
		createConnection(this.sessionService, "user", sessionId);
		
		// Check some basic stuff to test the connection.
		assertEquals("LOCALDB", this.sessionService.getConnection("user", sessionId).getCatalogs().get(0).getName());
		assertNotNull(this.sessionService.getConnection("user", sessionId).createStatement().executeOlapQuery("SELECT {[Measures].Children} on ROWS from [Quadrant Analysis]"));
		
		// Close this session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	
	
	
	
	
	private void initTest() {
		initTestContext();
		this.sessionService = new SessionServiceImpl();
		this.discoveryService = new DiscoveryServiceStub();
		this.sessionService.setDiscoveryService(this.discoveryService);
	}
	
	
	private void finishTest() {
		this.sessionService.setDiscoveryService(null);
		this.discoveryService = null;
		this.sessionService = null;
	}
	
	
	private static class DiscoveryServiceStub extends DiscoveryServiceImpl {
		
	}
	
}
