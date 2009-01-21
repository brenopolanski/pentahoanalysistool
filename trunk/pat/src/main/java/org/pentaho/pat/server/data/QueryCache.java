package org.pentaho.pat.server.data;

import java.security.InvalidKeyException;

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
	 * @throws InvalidKeyException If no node corresponding to the 
	 * key were found.
	 */
	public SelectNode get(String queryId) throws InvalidKeyException;
	
	/**
	 * Removes a cached element.
	 * @param queryId The id of the element to remove from the cache.
	 */
	public void delete(String queryId);
}
