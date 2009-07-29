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

        User user = userManager.getUser("admin"); //$NON-NLS-1$
        assertNotNull(user);
        assertEquals("admin", user.getUsername()); //$NON-NLS-1$
        assertEquals(1, user.getGroups().size());
        Group group = user.getGroups().iterator().next();
        assertEquals("Administrators", group.getName()); //$NON-NLS-1$
        User user2 = group.getMembers().iterator().next();
        assertEquals("admin", user2.getUsername()); //$NON-NLS-1$
        
        assertEquals(1, user.getSavedConnections().size());
        SavedConnection conn = user.getSavedConnections().iterator().next();
        assertEquals("1111-1111-1111-1111", conn.getId()); //$NON-NLS-1$
        assertEquals("administrator_connection", conn.getName()); //$NON-NLS-1$
        assertEquals("username", conn.getUsername()); //$NON-NLS-1$
        assertEquals("url", conn.getUrl()); //$NON-NLS-1$
        assertEquals("password", conn.getPassword()); //$NON-NLS-1$
        assertEquals("driver_name", conn.getDriverClassName()); //$NON-NLS-1$
        assertEquals(ConnectionType.Mondrian, conn.getType());

        finishTest();
    }
    
    public void testDeleteUser() 
    {
        String[][] expectedUsers = new String[][] {};
        String[][] expectedMemberships = new String[][] {};
        String[][] expectedGroups = new String[][] {
                {"Administrators"} //$NON-NLS-1$
        };
        String[][] expectedConnections = new String[][] {};
        String[][] expectedConnectionsAssociations = new String[][] {};
        
        initTest();
        
        String[][] currentGroups = runOnDatasource("select * from groups"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedGroups, currentGroups);
        
        User user = userManager.getUser("admin"); //$NON-NLS-1$
        assertNotNull(user);
        assertEquals("admin", user.getUsername()); //$NON-NLS-1$
        assertEquals(1, user.getGroups().size());
        Group group = user.getGroups().iterator().next();
        assertEquals("Administrators", group.getName()); //$NON-NLS-1$
        User user2 = group.getMembers().iterator().next();
        assertEquals("admin", user2.getUsername()); //$NON-NLS-1$
        
        this.userManager.deleteUser("admin"); //$NON-NLS-1$
        
        user = userManager.getUser("admin"); //$NON-NLS-1$
        assertNull(user);
        
        String[][] currentUsers = runOnDatasource("select * from users"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedUsers, currentUsers);
        
        String[][] currentMemberships = runOnDatasource("select * from groups_users"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMemberships, currentMemberships);
        
        currentGroups = runOnDatasource("select * from groups"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedGroups, currentGroups);
        
        String[][] currentConnections = runOnDatasource("select * from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnections, currentConnections);
        
        String[][] currentConnectionsAssociations = runOnDatasource("select * from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnectionsAssociations, currentConnectionsAssociations);
        
        finishTest();
    }
    
    public void testCreateUser() throws Exception 
    {
        String[][] expectedUsers = new String[][] {
                {"admin"}, //$NON-NLS-1$
                {"new_user"} //$NON-NLS-1$
        };
        initTest();

        User user = new User();
        user.setUsername("new_user"); //$NON-NLS-1$
        
        this.userManager.createUser(user);
        
        user = userManager.getUser("new_user"); //$NON-NLS-1$
        assertNotNull(user);
        assertEquals("new_user", user.getUsername()); //$NON-NLS-1$
        assertEquals(user.getGroups().size(), 0);
        
        String[][] currentUsers = runOnDatasource("select * from users"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedUsers, currentUsers);
        
        finishTest();
    }
    
    public void testCreateSavedConnection() throws Exception 
    {
        String[][] expectedConnections = new String[][] {
                {"1111-1111-1111-1111", "administrator_connection", "driver_name", "password", null, "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
                {"2222-2222-2222-2222", "my_connection", "driver_name", "password", "", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        };
        String[][] expectedMemberships = new String[][] {
                {"admin","1111-1111-1111-1111"}, //$NON-NLS-1$ //$NON-NLS-2$
                {"new_user","2222-2222-2222-2222"} //$NON-NLS-1$ //$NON-NLS-2$
        };
        String[][] expectedConnections2 = new String[][] {
                {"1111-1111-1111-1111", "administrator_connection", "driver_name", "password", null, "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        };
        String[][] expectedMemberships2 = new String[][] {
                {"admin","1111-1111-1111-1111"} //$NON-NLS-1$ //$NON-NLS-2$
        };
        
        initTest();

        User user = new User();
        user.setUsername("new_user"); //$NON-NLS-1$
        
        this.userManager.createUser(user);
        
        SavedConnection conn = new SavedConnection("2222-2222-2222-2222"); //$NON-NLS-1$
        conn.setType(ConnectionType.Mondrian);
        conn.setDriverClassName("driver_name"); //$NON-NLS-1$
        conn.setUrl("url"); //$NON-NLS-1$
        conn.setUsername("username"); //$NON-NLS-1$
        conn.setPassword("password"); //$NON-NLS-1$
        conn.setName("my_connection"); //$NON-NLS-1$
        
        user.getSavedConnections().add(conn);
        
        this.userManager.updateUser(user);
        
        user = userManager.getUser("new_user"); //$NON-NLS-1$
        assertNotNull(user);
        assertEquals("new_user", user.getUsername()); //$NON-NLS-1$
        assertEquals(user.getGroups().size(), 0);
        
        assertEquals(1, user.getSavedConnections().size());
        conn = user.getSavedConnections().iterator().next();
        assertEquals("2222-2222-2222-2222", conn.getId()); //$NON-NLS-1$
        assertEquals("my_connection", conn.getName()); //$NON-NLS-1$
        assertEquals("username", conn.getUsername()); //$NON-NLS-1$
        assertEquals("url", conn.getUrl()); //$NON-NLS-1$
        assertEquals("password", conn.getPassword()); //$NON-NLS-1$
        assertEquals("driver_name", conn.getDriverClassName()); //$NON-NLS-1$
        assertEquals(ConnectionType.Mondrian, conn.getType());
        
        String[][] currentConnections = runOnDatasource("select * from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnections, currentConnections);
        
        String[][] currentMemberships = runOnDatasource("select * from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMemberships, currentMemberships);
        
        // Test if we remove a saved connection
        user = this.userManager.getUser(user.getUsername());
        conn = user.getSavedConnections().iterator().next();
        String connId = conn.getId();
        user.getSavedConnections().remove(conn);
        for (SavedConnection connCheckup : user.getSavedConnections())
            if (connCheckup.getId().equals(connId))
                fail();
            
        this.userManager.updateUser(user);
        user = this.userManager.getUser(user.getUsername());
        
        for (SavedConnection connCheckup : user.getSavedConnections())
            if (connCheckup.getId().equals(connId))
                fail();
        
        currentConnections = runOnDatasource("select * from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnections2, currentConnections);
        
        currentMemberships = runOnDatasource("select * from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMemberships2, currentMemberships);
        
        finishTest();
    }
    
    public void testSavedConnectionUpdateBug() throws Exception
    {
        String[][] expectedConnections = new String[][] {
                {"1111-1111-1111-1111", "administrator_connection", "driver_name", "password", "", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"}, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                {"2222-2222-2222-2222", "my_connection", "driver_name", "password", "", "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        };
        String[][] expectedMemberships = new String[][] {
                {"admin","1111-1111-1111-1111"}, //$NON-NLS-1$ //$NON-NLS-2$
                {"admin","2222-2222-2222-2222"} //$NON-NLS-1$ //$NON-NLS-2$
        };
        String[][] expectedConnections2 = new String[][] {
                {"1111-1111-1111-1111", "administrator_connection", "driver_name", "password", null, "aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e", "url", "username"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        };
        String[][] expectedMemberships2 = new String[][] {
                {"admin","1111-1111-1111-1111"} //$NON-NLS-1$ //$NON-NLS-2$
        };
        String userId = "admin"; //$NON-NLS-1$
        initTest();
        
        String[][] currentConnections = runOnDatasource("select * from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnections2, currentConnections);
        
        String[][] currentMemberships = runOnDatasource("select * from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMemberships2, currentMemberships);
        
        User user = this.userManager.getUser(userId);
        assertEquals(1, user.getSavedConnections().size());
        SavedConnection conn = new SavedConnection("2222-2222-2222-2222"); //$NON-NLS-1$
        conn.setType(ConnectionType.Mondrian);
        conn.setDriverClassName("driver_name"); //$NON-NLS-1$
        conn.setUrl("url"); //$NON-NLS-1$
        conn.setUsername("username"); //$NON-NLS-1$
        conn.setPassword("password"); //$NON-NLS-1$
        conn.setName("my_connection"); //$NON-NLS-1$
        user.getSavedConnections().add(conn);
        this.userManager.updateUser(user);
        
        currentConnections = runOnDatasource("select * from connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedConnections, currentConnections);
        
        currentMemberships = runOnDatasource("select * from users_connections"); //$NON-NLS-1$
        assertTwoDimensionArrayEquals(expectedMemberships, currentMemberships);
        
        runOnDatasource("select top 50 this_.username as username1_2_, groups2_.user_id as user2_4_, group3_.name as group1_4_, group3_.name as name2_0_, savedconne4_.user_id as user1_5_, savedconne5_.name as connection2_5_, savedconne5_.name as name0_1_, savedconne5_.driverClassName as driverCl2_0_1_, savedconne5_.password as password0_1_, savedconne5_.schema as schema0_1_, savedconne5_.type as type0_1_, savedconne5_.url as url0_1_, savedconne5_.username as username0_1_ from users this_ left outer join groups_users groups2_ on this_.username=groups2_.user_id left outer join groups group3_ on groups2_.group_id=group3_.name left outer join users_connections savedconne4_ on this_.username=savedconne4_.user_id left outer join connections savedconne5_ on savedconne4_.connection_id=savedconne5_.name where this_.username = 'admin'"); //$NON-NLS-1$
        
        user = this.userManager.getUser(userId);
        assertEquals(2,user.getSavedConnections().size());
        
        finishTest();
    }
	
	private void initTest() {
	    super.init();
		this.userManager = (UserManager)applicationContext.getBean("userManager"); //$NON-NLS-1$
	}
	
	
	private void finishTest() {
	    // reset the database state.
	    initDatabase();
	}
}
