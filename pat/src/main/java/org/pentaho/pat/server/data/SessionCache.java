package org.pentaho.pat.server.data;


/**
 * Defines operations allowed on the preferences cache
 * @author Luc Boudreau
 */
public interface SessionCache {

	/**
	 * Stores a preference. If the key is already used,
	 * the new value will take it's place.
	 * @param key The id of the node to fetch.
	 * @param value The value to store.
	 * @return The unique id to fetch it back later on.
	 */
	public void put(String key, String value);
	
	/**
	 * Fetches a user preference from the store.
	 * @param key The id of the node to fetch.
	 * @return The desired preference value. Null if nothing found.
	 */
	public String get(String key);
	
	/**
	 * Removes a stored element.
	 * @param key The id of the element to remove from the cache.
	 */
	public void delete(String key);
}
