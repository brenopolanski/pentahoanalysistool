package org.pentaho.pat.server.servlet;

import org.pentaho.pat.rpc.beans.CubeConnection;



public class GwtServletTest extends AbstractServletTest {

    private QueryServlet queryServlet;
    private SessionServlet sessionServlet;
    private DiscoveryServlet discoveryServlet;

    public GwtServletTest() throws Exception {
        super();
        initTestContext();
        AbstractServlet.setApplicationContext(applicationContext);
        this.queryServlet = new QueryServlet();
        this.queryServlet.init();
        this.sessionServlet = new SessionServlet();
        this.sessionServlet.init();
        this.discoveryServlet = new DiscoveryServlet();
        this.discoveryServlet.init();
    }
    
    public void testSessions() throws Exception
    {
        initDatabase();
        
        // test a simple session creation/destruction
        String sessionId = this.sessionServlet.createSession();
        assertNotNull(sessionId);
        this.sessionServlet.closeSession(sessionId);
        
        // test sending an invalid session id. by convention, it should fail
        try {
            this.sessionServlet.closeSession("fake-value");
            fail();
        } catch(SecurityException e) {}
    }
    
    public void testConnections() throws Exception
    {
        final String newConnectionName = "new_connection_name";
        initDatabase();
        
        // Get a list of saved connections
        String sessionId = this.sessionServlet.createSession();
        CubeConnection[] connections = this.sessionServlet.getSavedConnections(sessionId);
        assertNotNull(connections);
        assertEquals(1, connections.length);
        CubeConnection connection = connections[0];
        
        // Try to activate one.
        this.sessionServlet.connect(sessionId, connection);
        assertNotNull(this.discoveryServlet.getCubes(sessionId));
        this.sessionServlet.disconnect(sessionId);
        
        // Create a new one that copies the previous
        connection.setName(newConnectionName);
        this.sessionServlet.saveConnection(sessionId, connection);
        
        // Check if it saved correctly
        CubeConnection newConnection = this.sessionServlet.getConnection(sessionId, newConnectionName);
        assertEquals(newConnectionName, newConnection.getName());
        
        // Test it
        this.sessionServlet.connect(sessionId, newConnection);
        assertNotNull(this.discoveryServlet.getCubes(sessionId));
        this.sessionServlet.disconnect(sessionId);
    }
}
