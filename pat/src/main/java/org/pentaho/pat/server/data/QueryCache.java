package org.pentaho.pat.server.data;

import org.olap4j.mdx.SelectNode;

/**
 * Defines operations allowed on a query cache.
 * @author Luc Boudreau
 */
public interface QueryCache {

	/**
	 * Stores a SelectNode in cache.
	 * @param node The node to store.
	 * @return The unique id to fetch it back later on.
	 */
	public String put(SelectNode node);
	
	/**
	 * Fetches a SelectNode from the cache.
	 * @param queryId The id of the node to fetch.
	 * @return The desired node.
	 */
	public SelectNode get(String queryId);
	
	/**
	 * Removes a cached element.
	 * @param queryId The id of the element to remove from the cache.
	 */
	public void delete(String queryId);
}
