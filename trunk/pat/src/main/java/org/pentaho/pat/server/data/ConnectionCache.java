package org.pentaho.pat.server.data;

import java.security.InvalidKeyException;

import org.olap4j.OlapConnection;

/**
 * Defines all operations allowed on a connection cache.
 * @author Luc Boudreau
 *
 */
public interface ConnectionCache {

	/**
	 * Stores a connection object in cache.
	 * @param connectionId The desired connection object unique id.
	 * @param conn The connection to store.
	 */
	public void put(String connectionId, OlapConnection conn);
	
	/**
	 * Fetches a connection object from the cache.
	 * @param connectionId The desired connection object unique id.
	 * @return The desired connection object.
	 * @throws InvalidKeyException When no connection of that name were found.
	 */
	public OlapConnection get(String connectionId) throws InvalidKeyException;
	
	/**
	 * Removes a connection object from the cache.
	 * @param guid he connection id to remove from cache.
	 */
	public void delete(String guid);
}
