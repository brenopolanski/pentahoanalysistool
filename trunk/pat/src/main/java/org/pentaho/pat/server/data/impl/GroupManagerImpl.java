package org.pentaho.pat.server.data.impl;

import org.pentaho.pat.server.data.GroupManager;
import org.pentaho.pat.server.data.pojo.Group;

public class GroupManagerImpl extends AbstractManager implements GroupManager {

    public void createGroup(Group group) {
        getHibernateTemplate().save(group);
    }
    
    public void createDefaultGroup(Group group) {
        this.createGroup(group);
    }

    public void updateGroup(Group group) {
        getHibernateTemplate().update(group);
    }
    
    public void updateDefaultGroup(Group group) {
        this.updateGroup(group);
    }
}
