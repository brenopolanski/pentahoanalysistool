/**
 * 
 */
package org.pentaho.pat.server.data.impl;

import java.security.InvalidKeyException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.olap4j.mdx.SelectNode;
import org.pentaho.pat.server.data.QueryCache;

/**
 * Simple query cache store that stores everything in memory.
 * @author Luc Boudreau
 */
class InMemoryQueryCache implements QueryCache {

	private ConcurrentHashMap<String, SelectNode> cache = new ConcurrentHashMap<String, SelectNode>();

	public void delete(String guid) {
		this.cache.remove(guid);
	}

	public SelectNode get(String guid) throws InvalidKeyException {
		if (this.cache.containsKey(guid)) {
			return this.cache.get(guid);
		}
		throw new InvalidKeyException(
			"The supplied query GUID cannot be found in the cache.");
	}

	public String put(SelectNode node) {
		String ref = String.valueOf(UUID.randomUUID());
		this.cache.put(ref, node);
		return ref;
	}
}
