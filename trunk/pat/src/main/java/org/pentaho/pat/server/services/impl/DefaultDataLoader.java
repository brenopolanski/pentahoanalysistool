package org.pentaho.pat.server.services.impl;

import org.apache.log4j.Logger;
import org.pentaho.pat.server.data.GroupManager;
import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.ConnectionType;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.User;
import org.pentaho.pat.server.messages.Messages;
import org.springframework.beans.factory.InitializingBean;

public class DefaultDataLoader implements InitializingBean {

    private UserManager userManager = null;
    
    private GroupManager groupManager = null;

    private boolean loadDefaultData = false;

    private transient Logger log = Logger.getLogger(this.getClass());

    public void afterPropertiesSet() throws Exception {
        if (this.loadDefaultData) {
            assert userManager != null;
            assert groupManager != null;

            // Test if data is already present
            if (userManager.getDefaultUsers().size() > 0)
                return;

            log.info(Messages.getString("Services.DefaultDataLoader.Loading")); //$NON-NLS-1$

            // Create default user
            User admin = new User();
            admin.setUsername("admin"); //$NON-NLS-1$
            admin.setPassword("admin"); //$NON-NLS-1$
            admin.setEnabled(true);

            // Create default group
            Group administrators = new Group();
            administrators.setName("Administrators"); //$NON-NLS-1$
            Group users = new Group();
            users.setName("Users"); //$NON-NLS-1$

            // Create default connection
            SavedConnection sc = new SavedConnection();
            sc.setName("Local Pentaho XML/A"); //$NON-NLS-1$
            sc.setType(ConnectionType.XMLA);
            sc.setUrl("http://localhost:8080/pentaho/Xmla"); //$NON-NLS-1$
            sc.setUsername("joe"); //$NON-NLS-1$
            sc.setPassword("password"); //$NON-NLS-1$
            admin.getSavedConnections().add(sc);

            // Save
            groupManager.createDefaultGroup(administrators);
            groupManager.createDefaultGroup(users);
            userManager.createDefaultUser(admin);

            // Create default association
            administrators.getMembers().add(admin);
            users.getMembers().add(admin);
            admin.getGroups().add(administrators);
            admin.getGroups().add(users);

            groupManager.updateDefaultGroup(administrators);
            groupManager.updateDefaultGroup(users);

            log.info(Messages.getString("Services.DefaultDataLoader.Done")); //$NON-NLS-1$
        }
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setLoadDefaultData(boolean loadDefaultData) {
        this.loadDefaultData = loadDefaultData;
    }
    
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }
}
