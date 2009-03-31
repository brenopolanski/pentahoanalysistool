package org.pentaho.pat.server.data.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is a UserManager implementation that supports Hibernate as it's backend
 * data provider.
 * @author Luc Boudreau
 */
public class UserManagerImpl extends AbstractManager implements UserManager, InitializingBean {

    @Transactional(readOnly=false)
	public void deleteUser(String userId) 
    {
        // Since group is the owner of the relation, we delete the user
        // from the groups first.
        User user = getUser(userId);
        Collection<Group> groups = user.getGroups();
        
        for (Group group : groups)
            group.getMembers().remove(user);
        
        getHibernateTemplate().delete(user);
	}
    
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return getHibernateTemplate().find(
            "from User");
    }

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public User getUser(final String userId)
	{
	    ArrayList<User> results = (ArrayList<User>)this.getHibernateTemplate().execute(
	        new HibernateCallback() {
                public Object doInHibernate(Session session) {
                    Criteria criteria = session.createCriteria(User.class);
                    criteria.add(Restrictions.idEq(userId));
                    criteria.setMaxResults(1);
                    return criteria.list();
            }
        });
	    return results.size()==1?results.get(0):null;
	}

	@Transactional(readOnly=false)
	public void saveUser(User user) {
		getHibernateTemplate().save(user);
	}

}
