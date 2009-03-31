package org.pentaho.pat.server.data.impl;

import java.util.List;

import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.Group;
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

        finishTest();
    }
    
    public void testDeleteUser() 
    {
        String[][] expectedUsers = new String[][] {};
        String[][] expectedMemberships = new String[][] {};
        String[][] expectedGroups = new String[][] {
                {"Administrators"}
        };
        
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
	
	private void initTest() {
	    super.init();
		this.userManager = (UserManager)applicationContext.getBean("userManager");
	}
	
	
	private void finishTest() {
	    // reset the database state.
	    initDatabase();
	}
}
