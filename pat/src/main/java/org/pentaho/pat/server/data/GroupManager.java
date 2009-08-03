package org.pentaho.pat.server.data;

import org.pentaho.pat.server.data.pojo.Group;
import org.springframework.security.annotation.Secured;

public interface GroupManager {

    @Secured ({"Administrators"})
    public void createGroup(Group group);
    
    public void createDefaultGroup(Group group);

    @Secured ({"Administrators"})
    public void updateGroup(Group group);
    
    public void updateDefaultGroup(Group group);
}
