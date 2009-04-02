package org.pentaho.pat.server.data;

import java.util.List;

import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.User;
import org.springframework.security.annotation.Secured;

public interface UserManager {
	
	@Secured ({"ROLE_USER"})
	public User getUser(String userId);
	
	@Secured ({"ROLE_ADMIN"})
	public List<User> getUsers();
	
	@Secured ({"ROLE_ADMIN"})
	public void createUser(User user);
	
	@Secured ({"ROLE_USER"})
    public void updateUser(User user);
	
	@Secured ({"ROLE_ADMIN"})
	public void deleteUser(String userId);
	
	@Secured ({"ROLE_USER"})
    public SavedConnection getSavedConnection(String userId, String connectionName);
	
}
