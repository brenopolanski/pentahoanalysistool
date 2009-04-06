package org.pentaho.pat.server.services.impl;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapWrapper;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;
import org.olap4j.query.Query;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.Session;
import org.pentaho.pat.server.data.pojo.User;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;

/**
 * Simple service implementation as a Spring bean.
 * @author Luc Boudreau
 */
public class SessionServiceImpl extends AbstractService
	implements SessionService
{

	Logger log = Logger.getLogger(this.getClass());
	



	
	private Map<String,Map<String, Session>> sessions = new ConcurrentHashMap<String, Map<String, Session>>();;
	
	
	
	
	
	private DiscoveryService discoveryService = null;
	
	

	public void afterPropertiesSet() throws Exception {
		if (this.discoveryService == null)
			throw new Exception("discoveryService est requis.");
	}
	
	
	
	public String createNewSession(String userId) {
		
		String generatedId = String.valueOf(UUID.randomUUID());
		
		if (!sessions.containsKey(userId))
			sessions.put(userId, new ConcurrentHashMap<String, Session>());
		
		sessions.get(userId).put(generatedId, new Session(generatedId));
		
		return generatedId;
	}

	
	
	
	public void releaseSession(String userId, String sessionId) {
		if (sessions.containsKey(userId) &&
			sessions.get(userId).containsKey(sessionId))
		{
			this.releaseConnection(userId, sessionId);
			sessions.get(userId).get(sessionId).destroy();
			sessions.get(userId).remove(sessionId);
			
			if (sessions.get(userId).size()==0)
				sessions.remove(userId);
		}
	}
	
	
	public Session getSession(String userId, String sessionId) {
	    if (sessions.containsKey(userId) &&
	            sessions.get(userId).containsKey(sessionId))
	    {
	        return sessions.get(userId).get(sessionId);
	    }
	    return null;
	}
	
	
	
	public void deleteUserSessionVariable(String userId, String sessionId, 
		String key) 
	{
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			sessions.get(userId).get(sessionId)
				.getVariables().remove(key);
		}
	}

	

	public void saveUserSessionVariable(String userId, String sessionId, 
		String key, Object value) 
	{
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			sessions.get(userId).get(sessionId)
				.getVariables().put(key, value);
		}
	}

	
	
	
	public Object getUserSessionVariable(String userId, String sessionId,
			String key) {
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			return sessions.get(userId).get(sessionId)
				.getVariables().get(key);
		}
		return null;
	}


	
	
	
	
	
	
	
	
	
	

	public void createConnection(String userId, String sessionId,
			String driverName, String connectStr, String username,
			String password) throws OlapException 
	{
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
		
			OlapConnection connection;
	
			try {
				Class.forName(driverName);
				connection = (OlapConnection) DriverManager
						.getConnection(connectStr,username,password);
				
				OlapWrapper wrapper = connection;
				
				OlapConnection olapConnection = wrapper
						.unwrap(OlapConnection.class);
	
				if (olapConnection != null) {
					
					sessions.get(userId).get(sessionId)
						.setConnection(connection);
					
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
	}
	
	
	
	public OlapConnection getConnection(String userId, String sessionId) {
		if (sessions.containsKey(userId) &&
			sessions.get(userId).containsKey(sessionId))
		{
			return sessions.get(userId).get(sessionId).getConnection();
		}
		return null;
	}
	

	public void releaseConnection(String userId, String sessionId) 
	{
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			try {
				OlapConnection conn = sessions.get(userId).get(sessionId).getConnection();
				if (conn!=null)
					conn.close();
			} catch (SQLException e) {
				log.warn("Exception encountered while closing a connection.", e);
			}
			sessions.get(userId).get(sessionId).setConnection(null);
		}
	}


	public String createNewQuery(String userId, String sessionId)
            throws OlapException {
        if (sessions.containsKey(userId)
                && sessions.get(userId).containsKey(sessionId)) {
            // We need to verify if the user has selected a cube.
            String cubeName = (String) sessions.get(userId).get(sessionId)
                    .getVariables().get(Constants.CURRENT_CUBE_NAME);

            if (cubeName == null)
                throw new OlapException(
                        "You asked to create a query but there was no cube previously selected.");

            Cube cube = this.getCube4Guid(userId, sessionId, cubeName);
            String generatedId = String.valueOf(UUID.randomUUID());
            Query newQuery;
            try {
                newQuery = new Query(generatedId, cube);
            } catch (SQLException e) {
                throw new OlapException(
                        "Exception encountered while creating a new Query object.",
                        e);
            }

            sessions.get(userId).get(sessionId).getQueries().put(generatedId,
                    newQuery);

            return generatedId;
        } else
            throw new RuntimeException("Invalid user/session ids provided.");
    }

	
	private Cube getCube4Guid(String userId, String sessionId, String cubeName) throws OlapException 
	{
		OlapConnection connection = this.getConnection(userId, sessionId);
		NamedList<Cube> cubes = connection.getSchema().getCubes();
		Cube cube = null;
		Iterator<Cube> iter = cubes.iterator();
		while (iter.hasNext() && cube == null) {
			Cube testCube = iter.next();
			if (cubeName.equals(testCube.getName())) {
				cube = testCube;
			}
		}
		if (cube != null) {
			return cube;
		}
		
		throw new OlapException("Programatic error. Invalid cube name.");
	}
	
	
	public Query getQuery(String userId, String sessionId, String queryId) 
	{
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			return sessions.get(userId).get(sessionId).getQueries().get(queryId);
		}
		else
			throw new RuntimeException("Invalid user/session ids provided.");
	}
	

	public List<String> getQueries(String userId, String sessionId) {
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			List<String> names = new ArrayList<String>();
			Set<Entry<String, Query>> entries = 
				sessions.get(userId).get(sessionId).getQueries().entrySet();
			for(Entry<String,Query> entry : entries)
			{
				names.add(entry.getKey());
			}
			return names;
		}
		else
			throw new RuntimeException("Invalid user/session ids provided.");
	}

	
	public void releaseQuery(String userId, String sessionId, String queryId) {
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			sessions.get(userId).get(sessionId).getQueries().remove(queryId);
		}
		else
			throw new RuntimeException("Invalid user/session ids provided.");
	}


	public SavedConnection getSavedConnection(String userId,
	        String connectionName) {
	    return this.userManager.getSavedConnection(userId, connectionName);
	}
	
	public void saveConnection(String userId, SavedConnection connection) {
        User user = this.userManager.getUser(userId);
        user.getSavedConnections().add(connection);
        this.userManager.updateUser(user);
    }
	
	public List<SavedConnection> getSavedConnections(String userId) {
	    List<SavedConnection> connections = new ArrayList<SavedConnection>();
	    User user = this.userManager.getUser(userId);
	    connections.addAll(user.getSavedConnections());
	    return connections;
	}
	
	public void deleteSavedConnection(String userId, String connectionName) {
	    User user = this.userManager.getUser(userId);
	    SavedConnection conn = getSavedConnection(userId, connectionName);
	    user.getSavedConnections().remove(conn);
	    this.userManager.updateUser(user);
	}


	
	public void setDiscoveryService(DiscoveryService discoveryService) {
		this.discoveryService = discoveryService;
	}


	/**
	 * Accessor. For testing purposes.
	 * @return
	 */
	protected Map<String,Map<String,Session>> getSessions() {
		return sessions;
	}
}
