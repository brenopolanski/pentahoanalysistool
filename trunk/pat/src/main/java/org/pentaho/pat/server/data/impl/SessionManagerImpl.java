package org.pentaho.pat.server.data.impl;

import org.pentaho.pat.server.data.SessionCache;
import org.pentaho.pat.server.data.SessionManager;

//TODO document me
public class SessionManagerImpl implements SessionManager {

	private SessionCache sessionCache;

	public void setSessionCache(SessionCache cache) {
		this.sessionCache = cache;
	}
	
	public void delete(String userId, String key) {
		this.sessionCache.delete(userId+key);
	}

	public String get(String userId, String key) {
		return this.sessionCache.get(userId+key);
	}

	public void put(String userId, String key, String value) {
		this.sessionCache.put(userId+key, value);
	}

}
