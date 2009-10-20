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
package org.pentaho.pat.server.data.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.SavedQuery;
import org.pentaho.pat.server.data.pojo.User;
import org.springframework.beans.factory.InitializingBean;

/**
 * This is a UserManager implementation that supports Hibernate as it's backend
 * data provider.
 * @author Luc Boudreau
 */
public class UserManagerImpl extends AbstractManager implements UserManager, InitializingBean {

    public void deleteUser(String userId) 
    {
        // Since group is the owner of the relation, we delete the user
        // from the groups first.
        User user = getUser(userId);
        Collection<Group> groups = user.getGroups();
        
        for (Group group : groups)
        {
            group.getMembers().remove(user);
            getHibernateTemplate().update(group);
        }
        
        getHibernateTemplate().delete(user);
	}
    
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return getHibernateTemplate().find("from User"); //$NON-NLS-1$
    }

    public List<User> getDefaultUsers() {
        return this.getUsers();
    }
    
	@SuppressWarnings("unchecked")
    public User getUser(final String userId)
	{
	    List<User> users = getHibernateTemplate().find("from User where username = ?", userId); //$NON-NLS-1$
	    return users.isEmpty()?null:users.get(0);
	}
	public void createUser(User user) {
		getHibernateTemplate().save(user);
	}
	
	public void createDefaultUser(User user) {
        this.createUser(user);
    }
	
    public void updateUser(User user) {
        getHibernateTemplate().update(user);
    }
    
    public void updateDefaultUser(User user) {
        this.updateUser(user);
    }

    @SuppressWarnings("unchecked")
    public SavedConnection getSavedConnection(String userId, String connectionId) {
        List<User> users = getHibernateTemplate().find("from User where username = ?", userId); //$NON-NLS-1$
        if (users.size()==1)
        {
            Set<SavedConnection> sc = users.get(0).getSavedConnections();
            for (SavedConnection conn : sc)
                if (conn.getId().equals(connectionId))
                    return conn;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.server.data.UserManager#getSavedQuery(java.lang.String, java.lang.String)
     */
    public SavedQuery getSavedQuery(String userId, String queryName) {
        List<User> users = getHibernateTemplate().find("from User where username = ?", userId); //$NON-NLS-1$
        if (users.size()==1)
        {
            Set<SavedQuery> sc = users.get(0).getSavedQueries();
            for (SavedQuery conn : sc)
                if (conn.getId().equals(queryName))
                    return conn;
        }
        return null;

    }

}
