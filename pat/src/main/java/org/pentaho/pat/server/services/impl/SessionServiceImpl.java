package org.pentaho.pat.server.services.impl;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapWrapper;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.Session;
import org.pentaho.pat.server.data.pojo.User;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.DiscoveryService;
import org.pentaho.pat.server.services.SessionService;
import org.springframework.util.Assert;

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
		Assert.notNull(this.discoveryService);
	}
	
	
	
	public String createNewSession(String userId) 
	{
	    this.validateUser(userId);
		
		String generatedId = String.valueOf(UUID.randomUUID());
		
		if (!sessions.containsKey(userId))
			sessions.put(userId, new ConcurrentHashMap<String, Session>());
		
		sessions.get(userId).put(generatedId, new Session(generatedId));
		
		return generatedId;
	}

	
	
	
	public void releaseSession(String userId, String sessionId) 
	{
	    this.validateSession(userId, sessionId);
		this.releaseConnection(userId, sessionId);
		sessions.get(userId).get(sessionId).destroy();
		sessions.get(userId).remove(sessionId);
		
		if (sessions.get(userId).size()==0)
			sessions.remove(userId);
	}
	
	
	public Session getSession(String userId, String sessionId) 
	{
	    this.validateSession(userId, sessionId);
	    return sessions.get(userId).get(sessionId);
	}
	
	
	
	public void deleteUserSessionVariable(String userId, String sessionId, 
		String key) 
	{
	    this.validateSession(userId, sessionId);
		sessions.get(userId).get(sessionId)
			.getVariables().remove(key);
	}

	

	public void saveUserSessionVariable(String userId, String sessionId, 
		String key, Object value) 
	{
	    this.validateSession(userId, sessionId);
		sessions.get(userId).get(sessionId)
			.getVariables().put(key, value);
	}

	
	
	
	public Object getUserSessionVariable(String userId, String sessionId,
			String key) 
	{
	    this.validateSession(userId, sessionId);
		return sessions.get(userId).get(sessionId)
			.getVariables().get(key);
	}


	
	
	
	
	
	
	
	
	
	

	public void createConnection(String userId, String sessionId,
			String driverName, String connectStr, String username,
			String password) throws OlapException 
	{
	    this.validateSession(userId, sessionId);
		
		OlapConnection connection;

		try {
			Class.forName(driverName);
			
			if (username==null&&password==null)
			    connection = (OlapConnection) DriverManager.getConnection(connectStr);
			else
			    connection = (OlapConnection) DriverManager
					.getConnection(connectStr,username,password);
			
			OlapWrapper wrapper = connection;
			
			OlapConnection olapConnection = wrapper
					.unwrap(OlapConnection.class);

			if (olapConnection != null) {
				
				sessions.get(userId).get(sessionId)
					.setConnection(connection);
				
				// Obtaining a connection object doesn't mean that the
				// credentials are ok or whatever. We'll test it.
				this.discoveryService.getCubes(userId, sessionId);
				
			} else {
				throw new OlapException(
					Messages.getString("Services.Session.NullConnection")); //$NON-NLS-1$
			}

		} catch (ClassNotFoundException e) {
		    log.error(e);
			throw new OlapException(e.getMessage(), e);
		} catch (SQLException e) {
		    log.error(e);
			throw new OlapException(e.getMessage(), e);
		} catch (RuntimeException e) {
		    // The XMLA driver wraps some exceptions in Runtime stuff.
		    // That's on the FIX ME list but not fixed yet... c(T-T)b
		    if (e.getCause() instanceof OlapException)
		        throw (OlapException)e.getCause();
		    else
		        throw e;
		}
	}
	
	
	
	public OlapConnection getConnection(String userId, String sessionId) 
	{
	    this.validateSession(userId, sessionId);
		return sessions.get(userId).get(sessionId).getConnection();
	}
	

	public void releaseConnection(String userId, String sessionId) 
	{
	    this.validateSession(userId, sessionId);
		try {
			OlapConnection conn = sessions.get(userId).get(sessionId).getConnection();
			if (conn!=null)
				conn.close();
		} catch (SQLException e) {
			log.warn(Messages.getString("Services.Session.ConnectionCloseException"), e); //$NON-NLS-1$
		}
		sessions.get(userId).get(sessionId).setConnection(null);
	}

	public SavedConnection getSavedConnection(String userId,
	        String connectionName) 
	{
	    this.validateUser(userId);
	    return this.userManager.getSavedConnection(userId, connectionName);
	}
	
	public void saveConnection(String userId, SavedConnection connection) 
	{
	    this.validateUser(userId);
        User user = this.userManager.getUser(userId);
        user.getSavedConnections().add(connection);
        this.userManager.updateUser(user);
    }
	
	public List<SavedConnection> getSavedConnections(String userId) 
	{
	    this.validateUser(userId);
	    List<SavedConnection> connections = new ArrayList<SavedConnection>();
	    User user = this.userManager.getUser(userId);
	    connections.addAll(user.getSavedConnections());
	    return connections;
	}
	
	public void deleteSavedConnection(String userId, String connectionName) 
	{
	    this.validateUser(userId);
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



    public void validateSession(String userId, String sessionId)
            throws SecurityException {
        this.validateUser(userId);
        if (!sessions.containsKey(userId)
            || !sessions.get(userId).containsKey(sessionId))
            throw new SecurityException(
                Messages.getString("Services.InvalidSessionOrUserId")); //$NON-NLS-1$
    }



    public void validateUser(String userId) throws SecurityException 
    {
        // TODO continue from here.
//        User user = this.userManager.getUser(userId);
//        if (user==null)
//            throw new SecurityException(
//                Messages.getString("Services.InvalidSessionOrUserId")); //$NON-NLS-1$
    }
}
