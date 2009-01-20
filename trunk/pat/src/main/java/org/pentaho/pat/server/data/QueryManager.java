package org.pentaho.pat.server.data;

import java.security.InvalidKeyException;

import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;

/**
 * Handles queries structures stored on the server. Implementing
 * classes must support the injection of dependency pattern.
 * 
 * @author Luc Boudreau
 */
public interface QueryManager {
	
	/**
	 * Creates a new empty select node object. In order to create a new query,
	 * one must supply both a connection GUID to use and a cube
	 * name on which the query will be built. 
	 * @return A unique identity name for the created SelectNode object.
	 * @throws OlapException
	 */
	public String create(String ownerId, String connectionId, String cubeName) throws OlapException;
	
	/**
	 * Fetches a previously saved query object from this manager.
	 * @param queryGuid The GUD of the query to fetch.
	 * @return The SelectNode object you asked for.
	 * @throws InvalidKeyException If no SelectNode with the given
	 * GUID can be found.
	 * @throws OlapException If something fishy happens.
	 */
	public SelectNode get(String ownerId, String queryGuid) throws InvalidKeyException,
		OlapException;
	
	/**
	 * Executes a stored query with the selected name.
	 * @param queryGuid
	 * @return
	 * @throws OlapException
	 */
	public CellSet execute(String ownerId, String queryGuid) throws OlapException;
	
	/**
	 * Releases a query object from this manager.
	 * @param queryGuid The GUID of the query to release.
	 */
	public void release(String ownerId, String queryGuid);
}