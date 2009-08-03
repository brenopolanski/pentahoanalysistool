package org.pentaho.pat.server.data;

import java.util.List;

import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.User;
import org.springframework.security.annotation.Secured;

public interface UserManager {
	
    /**
     * Obtain a PAT user object from the database.
     * @param userId The id of the user we want.
     * @return The corresponding user object, null if none found by that name.
     */
	@Secured ({"Users"})
	public User getUser(String userId);
	
	/**
	 * Loads a list of all users from the database.
	 * @return A List object of all users. Could be an empty list.
	 */
	@Secured ({"Administrators"})
	public List<User> getUsers();
	
	/**
	 * Internal unsecured hook for default data loading.
	 * @return The list of current users.
	 */
	public List<User> getDefaultUsers();
	
	/**
	 * Adds a user object to the database.
	 * @param user The user to create.
	 */
	@Secured ({"Administrators"})
	public void createUser(User user);
	
	/**
	 * Internal unsecured call to create default users in the system.
	 * @param user The user to save.
	 */
	public void createDefaultUser(User user);
	
	/**
	 * Updates a user object in the database.
	 * @param user The user that contains the modifications to save.
	 */
	@Secured ({"Users"})
    public void updateUser(User user);

    public void updateDefaultUser(User user);
	
	/**
	 * Deletes a user from the database.
	 * @param userId The user id to delete.
	 */
	@Secured ({"Administrators"})
	public void deleteUser(String userId);
	
	/**
	 * Retreives a saved connection object from the database.
	 * @param userId The owner id of the connection. 
	 * @param connectionName The connection unique name.
	 * @return The connection object, null if none found for those criteria.
	 */
	@Secured ({"Users"})
    public SavedConnection getSavedConnection(String userId, String connectionName);
}
