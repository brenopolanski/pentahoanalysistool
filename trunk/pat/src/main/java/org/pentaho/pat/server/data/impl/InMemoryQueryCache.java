/**
 * 
 */
package org.pentaho.pat.server.data.impl;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.olap4j.mdx.SelectNode;
import org.pentaho.pat.server.data.QueryCache;

/**
 * Simple query cache store that stores everything in memory.
 * @author Luc Boudreau
 */
public class InMemoryQueryCache implements QueryCache {

	private ConcurrentHashMap<String, SelectNode> cache = new ConcurrentHashMap<String, SelectNode>();

	public void delete(String guid) {
		this.cache.remove(guid);
	}

	public SelectNode get(String guid) {
		return this.cache.get(guid);
	}

	public String put(SelectNode node) {
		String ref = String.valueOf(UUID.randomUUID());
		this.cache.put(ref, node);
		return ref;
	}
}
