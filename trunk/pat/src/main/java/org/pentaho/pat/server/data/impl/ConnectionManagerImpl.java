package org.pentaho.pat.server.data.impl;

import java.security.InvalidKeyException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapWrapper;
import org.pentaho.pat.server.data.ConnectionCache;
import org.pentaho.pat.server.data.ConnectionManager;

public class ConnectionManagerImpl implements ConnectionManager {

	Logger log = Logger.getLogger(this.getClass());

	private ConnectionCache connectionCache;

	public OlapConnection connect(String ownerId, String driverName,
			String connectStr, String username, String password)
			throws OlapException {
		OlapConnection connection;

		try {
			Class.forName(driverName); //$NON-NLS-1$
			connection = (OlapConnection) DriverManager
					.getConnection(connectStr);
			OlapWrapper wrapper = connection;
			OlapConnection olapConnection = wrapper
					.unwrap(OlapConnection.class);

			if (olapConnection != null) {
				connectionCache.put(ownerId, olapConnection);
				return olapConnection;
			} else {
				throw new OlapException(
						"Creating a connection object returned null.");
			}

		} catch (ClassNotFoundException e) {
			throw new OlapException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new OlapException(e.getMessage(), e);
		}
	}

	public void disconnect(String ownerId, String connectionId) {
		// TODO Auto-generated method stub

	}

	public OlapConnection getConnection(String ownerId)
			throws InvalidKeyException, OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setConnectionCache(ConnectionCache cache) {
		this.connectionCache = cache;
	}

}
