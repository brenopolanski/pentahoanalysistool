package org.pentaho.pat.server.data;

import java.security.InvalidKeyException;

import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.query.Query;

/**
 * Handles queries structures stored on the server. Implementing
 * classes must support the injection of dependency pattern.
 * 
 * @author Luc Boudreau
 */
public interface QueryManager {
	
	/**
	 * Creates a new empty query object. In order to create a new query,
	 * one must supply both a connection GUID to use and a cube
	 * name on which the query will be built. 
	 * @return A unique identity name for the created Query object.
	 * @throws OlapException If something went wrong while creating 
	 * the query.
	 */
	public String create(String cubeName) throws OlapException;
	
	/**
	 * Fetches a previously saved query object from this manager.
	 * @param queryGuid The GUD of the query to fetch.
	 * @return The Query object you asked for.
	 * @throws OlapException If something fishy happens.
	 */
	public Query get(String queryGuid) throws OlapException;
	
	/**
	 * Executes a stored query with the selected name.
	 * @param queryGuid
	 * @return
	 * @throws OlapException
	 */
	public CellSet execute(String queryGuid) throws OlapException;
	
	/**
	 * Releases a query object from this manager.
	 * @param queryGuid The GUID of the query to release.
	 */
	public void release(String queryGuid);
	
	/**
	 * Injects the query cache to use.
	 * @param cache The cache object to use.
	 */
	public void setQueryCache(QueryCache cache);
}