/*
 * Copyright (C) 2009 Luc Boudreau
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.server.data;

import java.util.List;

import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.SavedQuery;
import org.pentaho.pat.server.data.pojo.User;
import org.springframework.security.annotation.Secured;

public interface UserManager {

    /**
     * Obtain a PAT user object from the database.
     * 
     * @param userId
     *            The id of the user we want.
     * @return The corresponding user object, null if none found by that name.
     */
    @Secured( {"Users"})
    User getUser(String userId);

    /**
     * Loads a list of all users from the database.
     * 
     * @return A List object of all users. Could be an empty list.
     */
    @Secured( {"Administrators"})
    List<User> getUsers();

    /**
     * Internal unsecured hook for default data loading.
     * 
     * @return The list of current users.
     */
    List<User> getDefaultUsers();

    /**
     * Adds a user object to the database.
     * 
     * @param user
     *            The user to create.
     */
    @Secured( {"Administrators"})
    void createUser(User user);

    /**
     * Internal unsecured call to create default users in the system.
     * 
     * @param user
     *            The user to save.
     */
    void createDefaultUser(User user);

    /**
     * Updates a user object in the database.
     * 
     * @param user
     *            The user that contains the modifications to save.
     */
    @Secured( {"Users"})
    void updateUser(User user);

    void updateDefaultUser(User user);

    /**
     * Deletes a user from the database.
     * 
     * @param userId
     *            The user id to delete.
     */
    @Secured( {"Administrators"})
    void deleteUser(String userId);

    /**
     * Retreives a saved connection object from the database.
     * 
     * @param userId
     *            The owner id of the connection.
     * @param connectionName
     *            The connection unique name.
     * @return The connection object, null if none found for those criteria.
     */
    @Secured( {"Users"})
    SavedConnection getSavedConnection(String userId, String connectionName);

    @Secured( {"Users"})
    SavedQuery getSavedQuery(String userId, String savedQueryName);
}
