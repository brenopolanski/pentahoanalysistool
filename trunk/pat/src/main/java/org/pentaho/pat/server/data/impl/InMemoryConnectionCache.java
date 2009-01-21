/**
 * 
 */
package org.pentaho.pat.server.data.impl;

import java.security.InvalidKeyException;
import java.util.concurrent.ConcurrentHashMap;

import org.olap4j.OlapConnection;
import org.pentaho.pat.server.data.ConnectionCache;

/**
 * Simple connection cache store that stores everything in memory.
 * @author Luc Boudreau
 */
public class InMemoryConnectionCache implements ConnectionCache {

	private ConcurrentHashMap<String, OlapConnection> cache = new ConcurrentHashMap<String, OlapConnection>();

	public void delete(String guid) {
		this.cache.remove(guid);
	}

	public OlapConnection get(String guid) throws InvalidKeyException {
		if (this.cache.containsKey(guid)) {
			return this.cache.get(guid);
		}
		throw new InvalidKeyException(
			"The supplied query GUID cannot be found in the cache.");
	}

	public void put(String ref, OlapConnection conn) {
		this.cache.put(ref, conn);
	}
}
