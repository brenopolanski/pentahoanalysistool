package org.pentaho.pat.server.data;

import java.security.InvalidKeyException;

import org.olap4j.OlapConnection;

/**
 * Defines all operations allowed on a connection cache.
 * @author Luc Boudreau
 *
 */
interface ConnectionCache {

	/**
	 * Stores a connection object in cache.
	 * @param conn The connection to store.
	 * @return A unique id to retrieve the object later on.
	 */
	public String put(OlapConnection conn);
	
	/**
	 * Fetches a connection object from the cache.
	 * @param guid The desired connection object unique id.
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
