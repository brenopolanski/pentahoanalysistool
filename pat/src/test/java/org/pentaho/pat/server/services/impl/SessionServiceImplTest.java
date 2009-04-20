package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.olap4j.OlapException;
import org.pentaho.pat.client.i18n.PatConstants;
import org.pentaho.pat.server.data.pojo.ConnectionType;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;

public class SessionServiceImplTest extends AbstractServiceTest {

	private SessionService sessionService;
	private DiscoveryService discoveryService;
	
	
	/**
	 * Tests the creation of a session.
	 * @throws Exception
	 */
	public void testCreateSession() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		
		// Check if it has been saved.
		assertNotNull(this.sessionService.getSession(userId,sessionId)); //$NON-NLS-1$
		
		// Check if the created session object is valid.
		assertTrue(this.sessionService.getSession(userId,sessionId).getQueries()!=null); //$NON-NLS-1$
		assertTrue(this.sessionService.getSession(userId,sessionId).getVariables()!=null); //$NON-NLS-1$
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
	}
	
	
	
	
	
	
	
	
	/**
	 * Tests the creation of a session.
	 * @throws Exception
	 */
	public void testDestroySession() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		
		// Check if it has been saved.
		assertNotNull(this.sessionService.getSession(userId,sessionId)); //$NON-NLS-1$
		
		// Release the session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		// Check if it was released along with the user space.
		try {
		    this.sessionService.getSession(userId,sessionId); //$NON-NLS-1$
		} catch (SecurityException e) {}
		
		// Create two sessions.
		String sessionId1 = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		String sessionId2 = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		
		// Check if they are saved properly
		assertNotNull(this.sessionService.getSession(userId,sessionId1)); //$NON-NLS-1$
		assertNotNull(this.sessionService.getSession(userId,sessionId2)); //$NON-NLS-1$
		
		// Release one of the sessions
		this.sessionService.releaseSession(userId, sessionId1); //$NON-NLS-1$
		
		// Assert only the session space was released and the other session is still there.
		try {
		    this.sessionService.getSession(userId,sessionId1); //$NON-NLS-1$
		} catch (SecurityException e) {}
        assertNotNull(this.sessionService.getSession(userId,sessionId2)); //$NON-NLS-1$
		
		// release the last session
		this.sessionService.releaseSession(userId, sessionId2); //$NON-NLS-1$
		
		finishTest();
	}
	
	
	
	
	
	
	
	public void testSessionVariables() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		
		// Save a variable to the session
		this.sessionService.saveUserSessionVariable(userId, sessionId, "key", "value"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		// Check if it's there through a call to the object.
		assertEquals("value", this.sessionService.getUserSessionVariable(userId, sessionId, "key")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		// Release it
		this.sessionService.deleteUserSessionVariable(userId, sessionId, "key"); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Make sure it's gone
		assertNull(this.sessionService.getUserSessionVariable(userId, sessionId, "key")); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Close this session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
		
	}
	
	
	
	
	
	
	
	public void testConnection() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		
		// Create a connection.
		createConnection(this.sessionService, userId, sessionId); //$NON-NLS-1$
		
		// Check some basic stuff to test the connection.
		assertEquals("LOCALDB", this.sessionService.getConnection(userId, sessionId).getCatalogs().get(0).getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull(this.sessionService.getConnection(userId, sessionId).createStatement().executeOlapQuery("SELECT {[Measures].Children} on COLUMNS from [Quadrant Analysis]")); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Tests the destruction of the connection
		this.sessionService.releaseConnection(userId, sessionId); //$NON-NLS-1$
		assertNull(this.sessionService.getConnection(userId, sessionId)); //$NON-NLS-1$
		
		// Close this session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
	}
	
	
	
	
	
	
	
	
	public void testCreateQuery() throws Exception 
	{
	    final String userId = "admin";
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession(userId); //$NON-NLS-1$
		
		// Create a connection.
		createConnection(this.sessionService, userId, sessionId); //$NON-NLS-1$
		
		// Creating a query without selecting a cube should fail
		try {
			this.sessionService.createNewQuery(userId, sessionId); //$NON-NLS-1$
			fail();
		} catch (OlapException e) {
			// ignore
		}
		
		// Create a query object.
		String cubeName = this.discoveryService.getCubes(userId, sessionId).get(0); //$NON-NLS-1$
		assertNotNull(cubeName);
		this.sessionService.saveUserSessionVariable(userId, sessionId, PatConstants.CURRENT_CUBE_NAME, cubeName); //$NON-NLS-1$
		String queryId = this.sessionService.createNewQuery(userId, sessionId); //$NON-NLS-1$
		assertNotNull(queryId);
		this.sessionService.saveUserSessionVariable(userId, sessionId, PatConstants.CURRENT_QUERY_NAME, queryId); //$NON-NLS-1$
		assertNotNull(this.sessionService.getQuery(userId, sessionId, queryId)); //$NON-NLS-1$
		assertEquals(1, this.sessionService.getQueries(userId, sessionId).size()); //$NON-NLS-1$
		
		
		// Test the release of a query.
		this.sessionService.releaseQuery(userId, sessionId, queryId); //$NON-NLS-1$
		assertNull(this.sessionService.getQuery(userId, sessionId, queryId)); //$NON-NLS-1$
		assertEquals(0, this.sessionService.getQueries(userId, sessionId).size()); //$NON-NLS-1$
		
		
		// Close this session.
		this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
		
		finishTest();
	}
	
	
	
	
	public void testSavedConnections() throws Exception
	{
	    String[][] expectedConnectionsArray = new String[][] {
                {"administrator_connection", "driver_name", "password", "", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                {"my_connection", "driver_name", "password", "", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        };
        String[][] expectedMembershipsArray = new String[][] {
                {"admin","administrator_connection"}, //$NON-NLS-1$ //$NON-NLS-2$
                {"admin","my_connection"} //$NON-NLS-1$ //$NON-NLS-2$
        };
        
	    String userId = "admin"; //$NON-NLS-1$
	    initTest();
	    
	    // Create a single session.
        String sessionId = this.sessionService.createNewSession(userId);
	    
        // Get a list of current connections.
        List<SavedConnection> currentConnections = this.sessionService.getSavedConnections(userId);
        assertNotNull(currentConnections);
        int connSize = currentConnections.size();
        
        // Test the saving of a connection.
        SavedConnection conn = new SavedConnection();
        conn.setType(ConnectionType.Mondrian);
        conn.setDriverClassName("driver_name"); //$NON-NLS-1$
        conn.setUrl("url"); //$NON-NLS-1$
        conn.setUsername("username"); //$NON-NLS-1$
        conn.setPassword("password"); //$NON-NLS-1$
        conn.setName("my_connection"); //$NON-NLS-1$
        this.sessionService.saveConnection(userId, conn);
        
        String[][] currentConnectionsArray = runOnDatasource("select * from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnectionsArray, currentConnectionsArray);
        
        String[][] currentMembershipsArray = runOnDatasource("select * from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMembershipsArray, currentMembershipsArray);
        
        // Verify if it's there.
        currentConnections = this.sessionService.getSavedConnections(userId);
        assertNotNull(currentConnections);
        assertEquals(connSize+1, currentConnections.size());
        
        // Validate it returns the right one.
        SavedConnection savedConn = this.sessionService.getSavedConnection(userId, "my_connection"); //$NON-NLS-1$
        assertEquals(conn.getDriverClassName(), savedConn.getDriverClassName());
        assertEquals(conn.getName(), savedConn.getName());
        assertEquals(conn.getPassword(), savedConn.getPassword());
        assertEquals(conn.getSchema(), savedConn.getSchema());
        assertEquals(conn.getUrl(), savedConn.getUrl());
        assertEquals(conn.getUsername(), savedConn.getUsername());
        assertEquals(conn.getType(), savedConn.getType());
        
        // Delete and test
        this.sessionService.deleteSavedConnection(userId, savedConn.getName());
        assertNull(this.sessionService.getSavedConnection(userId, "my_connection")); //$NON-NLS-1$
        
        // Close this session.
        this.sessionService.releaseSession(userId, sessionId); //$NON-NLS-1$
        
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
