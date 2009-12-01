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

    private static final Logger LOG = Logger.getLogger(DefaultDataLoader.class);

    public void afterPropertiesSet() throws Exception {
        if (this.loadDefaultData) {
            assert userManager != null;
            assert groupManager != null;
            // Test if data is already present
            if (userManager.getDefaultUsers().size() > 0) {
                return;
            }
            LOG.info(Messages.getString("Services.DefaultDataLoader.Loading")); //$NON-NLS-1$

            // Create default user
            final User admin = new User();
            admin.setUsername("admin"); //$NON-NLS-1$
            admin.setPassword("admin"); //$NON-NLS-1$
            admin.setEnabled(true);

            // Create default group
            final Group administrators = new Group();
            administrators.setName("Administrators"); //$NON-NLS-1$
            final Group users = new Group();
            users.setName("Users"); //$NON-NLS-1$

            // Create default connection
            final SavedConnection sc = new SavedConnection();
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

            LOG.info(Messages.getString("Services.DefaultDataLoader.Done")); //$NON-NLS-1$
        }
    }

    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }

    public void setLoadDefaultData(final boolean loadDefaultData) {
        this.loadDefaultData = loadDefaultData;
    }

    public void setGroupManager(final GroupManager groupManager) {
        this.groupManager = groupManager;
    }
}
