package org.pentaho.pat.server.data.impl;

import java.util.List;

import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.ConnectionType;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.User;

public class UserManagerImplTest extends AbstractManagerTest {
	
    private UserManager userManager;

    public void testGetUsers() throws Exception 
    {
        initTest();

        List<User> users = userManager.getUsers();
        assertNotNull(users);
        
        finishTest();
    }

    public void testGetUser() throws Exception 
    {
        initTest();

        User user = userManager.getUser("admin");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals(1, user.getGroups().size());
        Group group = user.getGroups().iterator().next();
        assertEquals("Administrators", group.getName());
        User user2 = group.getMembers().iterator().next();
        assertEquals("admin", user2.getUsername());
        
        assertEquals(1, user.getSavedConnections().size());
        SavedConnection conn = user.getSavedConnections().iterator().next();
        assertEquals("administrator_connection", conn.getName());
        assertEquals("username", conn.getUsername());
        assertEquals("url", conn.getUrl());
        assertEquals("password", conn.getPassword());
        assertEquals("driver_name", conn.getDriverClassName());
        assertEquals(ConnectionType.Mondrian, conn.getType());

        finishTest();
    }
    
    public void testDeleteUser() 
    {
        String[][] expectedUsers = new String[][] {};
        String[][] expectedMemberships = new String[][] {};
        String[][] expectedGroups = new String[][] {
                {"Administrators"}
        };
        String[][] expectedConnections = new String[][] {};
        String[][] expectedConnectionsAssociations = new String[][] {};
        
        initTest();
        
        String[][] currentGroups = runOnDatasource("select * from groups");
        assertTwoDimensionArrayEquals(expectedGroups, currentGroups);
        
        User user = userManager.getUser("admin");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals(1, user.getGroups().size());
        Group group = user.getGroups().iterator().next();
        assertEquals("Administrators", group.getName());
        User user2 = group.getMembers().iterator().next();
        assertEquals("admin", user2.getUsername());
        
        this.userManager.deleteUser("admin");
        
        user = userManager.getUser("admin");
        assertNull(user);
        
        String[][] currentUsers = runOnDatasource("select * from users");
        assertTwoDimensionArrayEquals(expectedUsers, currentUsers);
        
        String[][] currentMemberships = runOnDatasource("select * from groups_users");
        assertTwoDimensionArrayEquals(expectedMemberships, currentMemberships);
        
        currentGroups = runOnDatasource("select * from groups");
        assertTwoDimensionArrayEquals(expectedGroups, currentGroups);
        
        String[][] currentConnections = runOnDatasource("select * from connections");
        assertTwoDimensionArrayEquals(expectedConnections, currentConnections);
        
        String[][] currentConnectionsAssociations = runOnDatasource("select * from users_connections");
        assertTwoDimensionArrayEquals(expectedConnectionsAssociations, currentConnectionsAssociations);
        
        finishTest();
    }
    
    public void testCreateUser() throws Exception 
    {
        String[][] expectedUsers = new String[][] {
                {"admin"},
                {"new_user"}
        };
        initTest();

        User user = new User();
        user.setUsername("new_user");
        
        this.userManager.saveUser(user);
        
        user = userManager.getUser("new_user");
        assertNotNull(user);
        assertEquals("new_user", user.getUsername());
        assertEquals(user.getGroups().size(), 0);
        
        String[][] currentUsers = runOnDatasource("select * from users");
        assertTwoDimensionArrayEquals(expectedUsers, currentUsers);
        
        finishTest();
    }
    
    public void testCreateSavedConnection() throws Exception 
    {
        String[][] expectedConnections = new String[][] {
                {"administrator_connection", "driver_name", "password", null, "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"},
                {"my_connection", "driver_name", "password", null, "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"}
        };
        String[][] expectedMemberships = new String[][] {
                {"admin","administrator_connection"},
                {"new_user","my_connection"}
        };
        
        initTest();

        User user = new User();
        user.setUsername("new_user");
        
        SavedConnection conn = new SavedConnection();
        conn.setType(ConnectionType.Mondrian);
        conn.setDriverClassName("driver_name");
        conn.setUrl("url");
        conn.setUsername("username");
        conn.setPassword("password");
        conn.setName("my_connection");
        
        user.getSavedConnections().add(conn);
        
        this.userManager.saveUser(user);
        
        user = userManager.getUser("new_user");
        assertNotNull(user);
        assertEquals("new_user", user.getUsername());
        assertEquals(user.getGroups().size(), 0);
        
        assertEquals(1, user.getSavedConnections().size());
        conn = user.getSavedConnections().iterator().next();
        assertEquals("my_connection", conn.getName());
        assertEquals("username", conn.getUsername());
        assertEquals("url", conn.getUrl());
        assertEquals("password", conn.getPassword());
        assertEquals("driver_name", conn.getDriverClassName());
        assertEquals(ConnectionType.Mondrian, conn.getType());
        
        String[][] currentConnections = runOnDatasource("select * from connections");
        assertTwoDimensionArrayEquals(expectedConnections, currentConnections);
        
        String[][] currentMemberships = runOnDatasource("select * from users_connections");
        assertTwoDimensionArrayEquals(expectedMemberships, currentMemberships);
        
        finishTest();
    }
	
	private void initTest() {
	    super.init();
		this.userManager = (UserManager)applicationContext.getBean("userManager");
	}
	
	
	private void finishTest() {
	    // reset the database state.
	    initDatabase();
	}
}
