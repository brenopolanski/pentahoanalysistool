package org.pentaho.pat.server.data.impl;

import org.junit.Assert;
import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.User;

public class UserManagerImplTest extends AbstractManagerTest {
	
    private UserManager userManager;


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
    
    public void testDeleteUser() {
        initTest();
        
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
