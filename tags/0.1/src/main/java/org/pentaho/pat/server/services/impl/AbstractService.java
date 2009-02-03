package org.pentaho.pat.server.services.impl;

import org.pentaho.pat.server.data.UserManager;
import org.springframework.beans.factory.InitializingBean;


public abstract class AbstractService implements InitializingBean {
	
	protected UserManager userManager;

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}
