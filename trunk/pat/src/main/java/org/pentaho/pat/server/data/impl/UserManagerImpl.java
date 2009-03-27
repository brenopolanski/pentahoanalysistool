package org.pentaho.pat.server.data.impl;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This is a UserManager implementation that supports Hibernate as it's backend
 * data provider.
 * @author Luc Boudreau
 */
public class UserManagerImpl extends HibernateDaoSupport implements UserManager {
	
	public void createUser(String userId) {
		// TODO Auto-generated method stub
	}

	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
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

	public void saveUser(User user) {
		// TODO Auto-generated method stub
	}

}
