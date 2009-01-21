package org.pentaho.pat.server.data.impl;

import java.security.InvalidKeyException;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.pentaho.pat.server.data.ConnectionCache;
import org.pentaho.pat.server.data.ConnectionManager;

public class ConnectionManagerImpl implements ConnectionManager {

	private ConnectionCache connectionCache;

	public OlapConnection connect(String ownerId, String driverName, String connectStr,
			String username, String password) throws OlapException {
		// TODO Auto-generated method stub
		return null;
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
