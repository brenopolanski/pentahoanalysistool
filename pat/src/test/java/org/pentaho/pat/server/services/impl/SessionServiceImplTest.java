package org.pentaho.pat.server.services.impl;

import java.util.List;

import org.olap4j.OlapException;
import org.pentaho.pat.Constants;
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
		initTest();
		
		// Create a session.
		String sessionId = this.sessionService.createNewSession("user");
		
		// Check if it has been saved.
		assertNotNull(this.sessionService.getSession("user",sessionId));
		
		// Check if the created session object is valid.
		assertTrue(this.sessionService.getSession("user",sessionId).getQueries()!=null);
		assertTrue(this.sessionService.getSession("user",sessionId).getVariables()!=null);
		
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
		assertNotNull(this.sessionService.getSession("user",sessionId));
		
		// Release the session.
		this.sessionService.releaseSession("user", sessionId);
		
		// Check if it was released along with the user space.
		assertNull(this.sessionService.getSession("user",sessionId));
		
		// Create two sessions.
		String sessionId1 = this.sessionService.createNewSession("user");
		String sessionId2 = this.sessionService.createNewSession("user");
		
		// Check if they are saved properly
		assertNotNull(this.sessionService.getSession("user",sessionId1));
		assertNotNull(this.sessionService.getSession("user",sessionId2));
		
		// Release one of the sessions
		this.sessionService.releaseSession("user", sessionId1);
		
		// Assert only the session space was released and the other session is still there.
		assertNull(this.sessionService.getSession("user",sessionId1));
        assertNotNull(this.sessionService.getSession("user",sessionId2));
		
		// release the last session
		this.sessionService.releaseSession("user", sessionId2);
		
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
		
		// Tests the destruction of the connection
		this.sessionService.releaseConnection("user", sessionId);
		assertNull(this.sessionService.getConnection("user", sessionId));
		
		// Close this session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	
	
	
	
	
	public void testCreateQuery() throws Exception 
	{
		
		initTest();
		
		// Create a single session.
		String sessionId = this.sessionService.createNewSession("user");
		
		// Create a connection.
		createConnection(this.sessionService, "user", sessionId);
		
		// Creating a query without selecting a cube should fail
		try {
			this.sessionService.createNewQuery("user", sessionId);
			fail();
		} catch (OlapException e) {
			// ignore
		}
		
		// Create a query object.
		String cubeName = this.discoveryService.getCubes("user", sessionId).get(0);
		assertNotNull(cubeName);
		this.sessionService.saveUserSessionVariable("user", sessionId, Constants.CURRENT_CUBE_NAME, cubeName);
		String queryId = this.sessionService.createNewQuery("user", sessionId);
		assertNotNull(queryId);
		this.sessionService.saveUserSessionVariable("user", sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		assertNotNull(this.sessionService.getQuery("user", sessionId, queryId));
		assertEquals(1, this.sessionService.getQueries("user", sessionId).size());
		
		
		// Test the release of a query.
		this.sessionService.releaseQuery("user", sessionId, queryId);
		assertNull(this.sessionService.getQuery("user", sessionId, queryId));
		assertEquals(0, this.sessionService.getQueries("user", sessionId).size());
		
		
		// Close this session.
		this.sessionService.releaseSession("user", sessionId);
		
		finishTest();
	}
	
	
	
	
	public void testSavedConnections() throws Exception
	{
	    String[][] expectedConnectionsArray = new String[][] {
                {"administrator_connection", "driver_name", "password", null, "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"},
                {"my_connection", "driver_name", "password", null, "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"}
        };
        String[][] expectedMembershipsArray = new String[][] {
                {"admin","administrator_connection"},
                {"admin","my_connection"}
        };
        
	    String userId = "admin";
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
        conn.setDriverClassName("driver_name");
        conn.setUrl("url");
        conn.setUsername("username");
        conn.setPassword("password");
        conn.setName("my_connection");
        this.sessionService.saveConnection(userId, conn);
        
        String[][] currentConnectionsArray = runOnDatasource("select * from connections");
        assertTwoDimensionArrayEquals(expectedConnectionsArray, currentConnectionsArray);
        
        String[][] currentMembershipsArray = runOnDatasource("select * from users_connections");
        assertTwoDimensionArrayEquals(expectedMembershipsArray, currentMembershipsArray);
        
        // Verify if it's there.
        currentConnections = this.sessionService.getSavedConnections(userId);
        assertNotNull(currentConnections);
        assertEquals(connSize+1, currentConnections.size());
        
        // Validate it returns the right one.
        SavedConnection savedConn = this.sessionService.getSavedConnection(userId, "my_connection");
        assertEquals(conn.getDriverClassName(), savedConn.getDriverClassName());
        assertEquals(conn.getName(), savedConn.getName());
        assertEquals(conn.getPassword(), savedConn.getPassword());
        assertEquals(conn.getSchema(), savedConn.getSchema());
        assertEquals(conn.getUrl(), savedConn.getUrl());
        assertEquals(conn.getUsername(), savedConn.getUsername());
        assertEquals(conn.getType(), savedConn.getType());
        
        // Delete and test
        this.sessionService.deleteSavedConnection(userId, savedConn.getName());
        assertNull(this.sessionService.getSavedConnection(userId, "my_connection"));
        
        // Close this session.
        this.sessionService.releaseSession("user", sessionId);
        
	    finishTest();
	}
	
	
	
	
	private void initTest() {
		initTestContext();
		this.sessionService = (SessionService)applicationContext.getBean("sessionService");
        this.discoveryService = (DiscoveryService)applicationContext.getBean("discoveryService");
	}
	
	
	private void finishTest() {
	}
}
