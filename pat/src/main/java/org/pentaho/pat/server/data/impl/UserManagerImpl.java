package org.pentaho.pat.server.data.impl;

import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.Session;
import org.pentaho.pat.server.data.pojo.User;

public class UserManagerImpl implements UserManager {

	private static User defaultUser = null;
	
	public void createUser(String userId) {
		// TODO Auto-generated method stub
	}

	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
	}

	public User getUser(String userId) {
		// TODO Auto-generated method stub
		
		if (defaultUser == null)
		{
			User user  = new User();
			user.setUsername(userId);
			Group group = new Group();
			group.setName("PAT-Users");
			group.getMembers().add(user);
			user.getGroups().add(group);
			
			defaultUser = user;
		
			Session session = new Session();
		}
		return defaultUser;
	}

	public void saveUser(User user) {
		// TODO Auto-generated method stub
	}

}
