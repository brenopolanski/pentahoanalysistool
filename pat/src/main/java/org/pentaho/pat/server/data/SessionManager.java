package org.pentaho.pat.server.data;

/**
 * 
 * Stores session variables.
 * 
 * @author Luc Boudreau
 */
// TODO document me
public interface SessionManager {
	
	public void put(String userId, String key, String value);
	
	public String get(String userId, String key);
	
	public void delete(String userId, String key);
}