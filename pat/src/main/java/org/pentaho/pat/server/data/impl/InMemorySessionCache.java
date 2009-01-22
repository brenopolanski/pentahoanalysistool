package org.pentaho.pat.server.data.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.pentaho.pat.server.data.SessionCache;

public class InMemorySessionCache implements SessionCache {

	private ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<String, String>();
	
	public void delete(String key) {
		this.cache.remove(key);
	}

	public String get(String key) {
		return this.cache.get(key);
	}

	public void put(String key, String value) {
		this.cache.put(key, value);
	}

}
