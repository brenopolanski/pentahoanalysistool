package org.pentaho.pat.server.data;

import org.pentaho.pat.server.data.pojo.User;

public interface UserManager {

	public void createUser(String userId);
	
	public User getUser(String userId);
	
	public void saveUser(User user);
	
	public void deleteUser(String userId);
	
}
