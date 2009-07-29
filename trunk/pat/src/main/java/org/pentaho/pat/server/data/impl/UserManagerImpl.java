package org.pentaho.pat.server.data.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.SavedConnection;
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

	@SuppressWarnings("unchecked")
    public User getUser(final String userId)
	{
	    List<User> users = getHibernateTemplate().find("from User where username = ?", userId); //$NON-NLS-1$
	    return users.isEmpty()?null:users.get(0);
	}
	public void createUser(User user) {
		getHibernateTemplate().save(user);
	}
    public void updateUser(User user) {
        getHibernateTemplate().update(user);
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

}
