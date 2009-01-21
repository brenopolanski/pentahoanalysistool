package org.pentaho.pat.server.data;

import java.security.InvalidKeyException;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;

/**
 * Defines operations permitted on the connections pool.
 * 
 * @author Luc Boudreau
 */
public interface ConnectionManager {

	/**
	 * Creates an OLAP connection through regular JDBC conventions.
	 * Once the connection is in it's pool, the connection object is
	 * sent back for handling. No need to keep a hold of it, you can simply
	 * supply back the ownerId you sent upon creation to fetch it again.
	 * @param driverName The JDBC driver fully qualified class name
	 * to use.
	 * @param connectStr The JDBC connection string to use.
	 * @param username The JDBC user name to send. Giving a value other
	 * than null to this parameter overrides values included in the
	 * connection URL.
	 * @param password The JDBC password to send. Giving a value other
	 * than null to this parameter overrides values included in the
	 * connection URL.
	 * @return A unique identification number for a connection to use.
	 * @throws OlapException If anything turns sour, an OlapException
	 * will be sent along with details.
	 */
	public OlapConnection connect(String ownerId, String driverName, String connectStr, 
			String username, String password) throws OlapException;
	
	/**
	 * Closes a connection. Does not throw any exceptions and silently
	 * fails if the something happens.
	 */
	public void disconnect(String ownerId);
	
	/**
	 * Returns a valid connection from the connection cache. 
	 * 
	 * <p>If the GUID provided is a valid one, it is the 
	 * responsibility of implementation to ensure that 
	 * the returned connection is neither stale or invalid.
	 * Therefore, if a valid GUID was provided but the connection
	 * is not in a valid state anymore, implementations must
	 * establish a new one and associate with the provided GUID.
	 * 
	 * @param ownerId The GUID of the connection we wish to fetch.
	 * @return A valid OLAP connection.
	 * @throws InvalidKeyException If the supplied GUID is not valid.
	 * @throws OlapException If an exception was encountered while trying
	 * to re-establish a lost connection, an OlapException will be thrown.
	 */
	public OlapConnection getConnection(String ownerId) 
		throws InvalidKeyException, OlapException;
	
	/**
	 * Injects the connection cache tu use.
	 * @param cache The connection cache object.
	 */
	public void setConnectionCache(ConnectionCache cache);
}
