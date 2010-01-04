package org.pentaho.pat.server.servlet;

import java.util.ArrayList;

import org.junit.Assert;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.messages.Messages;


/**
 * Basic regression test suite for the GWT RPC layer.
 * It basically makes sure that the calls are doing what they should
 * and performs some very basic validations.
 * @author Luc Boudreau
 */
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
    
    /**
     * Test session creation and closing.
     * @throws Exception
     */
    public void testSessions() throws Exception
    {
        initDatabase();
        
        // test a simple session creation/destruction
        String sessionId = this.sessionServlet.createSession();
        assertNotNull(sessionId);
        this.sessionServlet.closeSession(sessionId);
        
        // test sending an invalid session id. by convention, it should fail
        try {
            this.sessionServlet.closeSession("fake-value"); //$NON-NLS-1$
            fail();
        } catch(RpcException e) {
        	assertTrue(true);
        }
    }
    
    /**
     * Tests saved connections.
     * @throws Exception
     */
    public void testConnections() throws Exception
    {
        final String newConnectionName = "new_connection_name"; //$NON-NLS-1$
        final String newConnectionId = "2222-2222-2222-2222"; //$NON-NLS-1$
        final String[][] expectedConnectionsArray = new String[][] {
                {"1111-1111-1111-1111","org.hsqldb.jdbcDriver","administrator_connection","","<?xml version=\"1.0\"?><Schema name=\"SampleData\"><!-- Shared dimensions -->  <Dimension name=\"Region\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Regions\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Region\" column=\"REGION\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Dimension name=\"Department\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Departments\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Department\" column=\"DEPARTMENT\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Dimension name=\"Positions\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Positions\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Positions\" column=\"POSITIONTITLE\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Cube name=\"Quadrant Analysis\">    <Table name=\"QUADRANT_ACTUALS\"/>    <DimensionUsage name=\"Region\" source=\"Region\"/>    <DimensionUsage name=\"Department\" source=\"Department\" />    <DimensionUsage name=\"Positions\" source=\"Positions\" />    <Measure name=\"Actual\" column=\"ACTUAL\" aggregator=\"sum\" formatString=\"#,###.00\"/>    <Measure name=\"Budget\" column=\"BUDGET\" aggregator=\"sum\" formatString=\"#,###.00\"/>    <Measure name=\"Variance\" column=\"VARIANCE\" aggregator=\"sum\" formatString=\"#,###.00\"/><!--    <CalculatedMember name=\"Variance Percent\" dimension=\"Measures\" formula=\"([Measures].[Variance]/[Measures].[Budget])*100\" /> -->  </Cube></Schema>", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e","jdbc:hsqldb:mem:pat_mondrian;default_schema=true;type=cached;write_delay=false","sa"}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                {"2222-2222-2222-2222","org.hsqldb.jdbcDriver","new_connection_name","","<?xml version=\"1.0\"?><Schema name=\"SampleData\"><!-- Shared dimensions -->  <Dimension name=\"Region\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Regions\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Region\" column=\"REGION\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Dimension name=\"Department\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Departments\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Department\" column=\"DEPARTMENT\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Dimension name=\"Positions\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Positions\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Positions\" column=\"POSITIONTITLE\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Cube name=\"Quadrant Analysis\">    <Table name=\"QUADRANT_ACTUALS\"/>    <DimensionUsage name=\"Region\" source=\"Region\"/>    <DimensionUsage name=\"Department\" source=\"Department\" />    <DimensionUsage name=\"Positions\" source=\"Positions\" />    <Measure name=\"Actual\" column=\"ACTUAL\" aggregator=\"sum\" formatString=\"#,###.00\"/>    <Measure name=\"Budget\" column=\"BUDGET\" aggregator=\"sum\" formatString=\"#,###.00\"/>    <Measure name=\"Variance\" column=\"VARIANCE\" aggregator=\"sum\" formatString=\"#,###.00\"/><!--    <CalculatedMember name=\"Variance Percent\" dimension=\"Measures\" formula=\"([Measures].[Variance]/[Measures].[Budget])*100\" /> -->  </Cube></Schema>", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e","jdbc:hsqldb:mem:pat_mondrian;default_schema=true;type=cached;write_delay=false","sa"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        };
        final String[][] expectedMembershipsArray = new String[][] {
                {"admin","1111-1111-1111-1111"}, //$NON-NLS-1$ //$NON-NLS-2$
                {"admin","2222-2222-2222-2222"} //$NON-NLS-1$ //$NON-NLS-2$
        };
        final String[][] expectedConnectionsArray2 = new String[][] {
                {"1111-1111-1111-1111","org.hsqldb.jdbcDriver","administrator_connection","","<?xml version=\"1.0\"?><Schema name=\"SampleData\"><!-- Shared dimensions -->  <Dimension name=\"Region\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Regions\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Region\" column=\"REGION\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Dimension name=\"Department\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Departments\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Department\" column=\"DEPARTMENT\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Dimension name=\"Positions\">    <Hierarchy hasAll=\"true\" allMemberName=\"All Positions\">      <Table name=\"QUADRANT_ACTUALS\"/>      <Level name=\"Positions\" column=\"POSITIONTITLE\" uniqueMembers=\"true\"/>    </Hierarchy>  </Dimension>  <Cube name=\"Quadrant Analysis\">    <Table name=\"QUADRANT_ACTUALS\"/>    <DimensionUsage name=\"Region\" source=\"Region\"/>    <DimensionUsage name=\"Department\" source=\"Department\" />    <DimensionUsage name=\"Positions\" source=\"Positions\" />    <Measure name=\"Actual\" column=\"ACTUAL\" aggregator=\"sum\" formatString=\"#,###.00\"/>    <Measure name=\"Budget\" column=\"BUDGET\" aggregator=\"sum\" formatString=\"#,###.00\"/>    <Measure name=\"Variance\" column=\"VARIANCE\" aggregator=\"sum\" formatString=\"#,###.00\"/><!--    <CalculatedMember name=\"Variance Percent\" dimension=\"Measures\" formula=\"([Measures].[Variance]/[Measures].[Budget])*100\" /> -->  </Cube></Schema>", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e","jdbc:hsqldb:mem:pat_mondrian;default_schema=true;type=cached;write_delay=false","sa"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        };
        final String[][] expectedMembershipsArray2 = new String[][] {
                {"admin","1111-1111-1111-1111"} //$NON-NLS-1$ //$NON-NLS-2$
        };
        
        initDatabase();
        
        // Get a list of saved connections
        String sessionId = this.sessionServlet.createSession();
        CubeConnection[] connections = this.sessionServlet.getConnections(sessionId);
        assertNotNull(connections);
        assertEquals(1, connections.length);
        CubeConnection connection = connections[0];
        
        // Try to activate one.
        this.sessionServlet.connect(sessionId, connection.getId());
        assertNotNull(this.discoveryServlet.getCubes(sessionId,connection.getId()));
        this.sessionServlet.disconnect(sessionId,connection.getId());
        
        // Create a new one that copies the previous
        connection.setName(newConnectionName);
        connection.setId(newConnectionId);
        this.sessionServlet.saveConnection(sessionId, connection);
        
        // Check if it saved correctly
        CubeConnection newConnection = this.sessionServlet.getConnection(sessionId, newConnectionId);
        assertEquals(newConnectionId, newConnection.getId());
        
        String[][] currentConnectionsArray = runOnDatasource("select id, driverClassName, name, password, schemadata, type, url, username from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnectionsArray, currentConnectionsArray);
        
        String[][] currentMembershipsArray = runOnDatasource("select user_id, connection_id from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMembershipsArray, currentMembershipsArray);
        
        // Test it
        this.sessionServlet.connect(sessionId, newConnection.getId());
        assertNotNull(this.discoveryServlet.getCubes(sessionId,newConnection.getId()));
        this.sessionServlet.disconnect(sessionId,newConnection.getId());
        
        // Delete a saved connection
        this.sessionServlet.deleteConnection(sessionId, newConnection.getId());
        assertNull(this.sessionServlet.getConnection(sessionId, newConnection.getId()));
        
        currentConnectionsArray = runOnDatasource("select id, driverClassName, name, password, schemadata, type, url, username from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnectionsArray2, currentConnectionsArray);
        
        currentMembershipsArray = runOnDatasource("select user_id, connection_id from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMembershipsArray2, currentMembershipsArray);
        
        this.sessionServlet.closeSession(sessionId);
    }

    /**
     * Tests the discovery of cubes and the the 
     * selection of a cube to work with.
     * @throws Exception
     */
    public void testCubesDiscovery() throws Exception
    {
        final String[] expectedCubeNames = new String[] {
                "Quadrant Analysis" //$NON-NLS-1$
        };
        initDatabase();
        
        // Create a connection
        String sessionId = this.sessionServlet.createSession();
        CubeConnection[] connections = this.sessionServlet.getConnections(sessionId);
        assertNotNull(connections);
        assertEquals(1, connections.length);
        CubeConnection connection = connections[0];
        this.sessionServlet.connect(sessionId, connection.getId());
        
        // Test the cubes list
        CubeItem[] cubes = this.discoveryServlet.getCubes(sessionId,connection.getId());
        String[] actualCubeNames = new String[cubes.length];
        for(int i=0; i<cubes.length; i++)
        	actualCubeNames[i]=cubes[i].getName();
        Assert.assertArrayEquals(expectedCubeNames, actualCubeNames);
        
        this.sessionServlet.closeSession(sessionId);
    }
}
