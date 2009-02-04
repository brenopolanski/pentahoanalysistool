package org.pentaho.pat.server.data;

import org.pentaho.pat.server.data.pojo.User;
import org.springframework.security.annotation.Secured;

public interface UserManager {

	@Secured ({"ROLE_ADMIN"})
	public void createUser(String userId);
	
	@Secured ({"ROLE_USER"})
	public User getUser(String userId);
	
	@Secured ({"ROLE_ADMIN"})
	public void saveUser(User user);
	
	@Secured ({"ROLE_ADMIN"})
	public void deleteUser(String userId);
	
}
