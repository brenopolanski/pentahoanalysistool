/*
 * Copyright (C) 2009 Luc Boudreau
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.server.services.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.pentaho.pat.server.data.pojo.ConnectionType;
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
		this.disconnect(userId, sessionId);
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


	public void connect(String userId, String sessionId,
	        String connectionId) throws OlapException
	{
	    this.validateSession(userId, sessionId);
        SavedConnection sc = getConnection(userId, connectionId);
        if (sc == null) {
            throw new OlapException(Messages.getString("Services.Session.NoSuchConnectionId")); //$NON-NLS-1$
        }
        this.connect(userId, sessionId, sc);
	}
	
	
	
	public void connect(String userId, String sessionId,
            SavedConnection sc) throws OlapException 
    {
	    this.validateSession(userId, sessionId);
	    
	    OlapConnection connection;
        String olap4jUrl=null;
        String olap4jDriver=null;
        
        if (sc.getType() == ConnectionType.Mondrian) 
        {
            File schema;
            try {
                // First, we need to create a temporary file for Mondrian
                schema = File.createTempFile(String.valueOf(UUID.randomUUID()),""); //$NON-NLS-1$
                schema.deleteOnExit();
                FileWriter fw = new FileWriter(schema);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(sc.getSchemaData());
                bw.close();
                fw.close();
            } catch (IOException ioe) {
                throw new RuntimeException("Cannot create temporary schema file on server.",ioe); //$NON-NLS-1$
            }

            olap4jDriver="mondrian.olap4j.MondrianOlap4jDriver"; //$NON-NLS-1$
            olap4jUrl = "jdbc:mondrian:" //$NON-NLS-1$
                .concat("Jdbc=").concat(sc.getUrl()).concat(";") //$NON-NLS-1$ //$NON-NLS-2$
                .concat("JdbcDrivers=").concat(sc.getDriverClassName()).concat(";") //$NON-NLS-1$ //$NON-NLS-2$
                .concat("Catalog=").concat(schema.getAbsolutePath()); //$NON-NLS-1$
            
            if (sc.getUsername()!=null)
            {
                olap4jUrl = olap4jUrl
                    .concat(";").concat("JdbcUser=").concat(sc.getUsername()).concat(";"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            if (sc.getPassword()!= null)
            {
                olap4jUrl = olap4jUrl
                    .concat("JdbcPassword=").concat(sc.getPassword()); //$NON-NLS-1$
            }            
        } else if (sc.getType() == ConnectionType.XMLA) {
            olap4jUrl = "jdbc:xmla:Server=".concat(sc.getUrl());
            if (sc.getCatalog() != null && sc.getCatalog().length() > 0) {
                olap4jUrl = olap4jUrl.concat(";Catalog=").concat(sc.getCatalog()); //$NON-NLS-1$
            }
            olap4jDriver = "org.olap4j.driver.xmla.XmlaOlap4jDriver"; //$NON-NLS-1$
            // CONNECT
        } else {
            throw new RuntimeException("Programming error. No connection type implemented."); //$NON-NLS-1$
        }

        try {
        
            // Init the olap4j driver, in case JVM < 6
            Class.forName(olap4jDriver);

            
            if (sc.getType().equals(ConnectionType.Mondrian) ||
                (sc.getUsername() == null && sc.getPassword() == null))
            {
                connection = (OlapConnection) DriverManager.getConnection(olap4jUrl);
            } else {
                connection = (OlapConnection) DriverManager
                    .getConnection(olap4jUrl,sc.getUsername(),sc.getPassword());
            }
            OlapWrapper wrapper = connection;
            
            OlapConnection olapConnection = wrapper
                    .unwrap(OlapConnection.class);

            if (olapConnection != null) {
                
                sessions.get(userId).get(sessionId).putConnection(sc.getId(), olapConnection);
                
                // Obtaining a connection object doesn't mean that the
                // credentials are ok or whatever. We'll test it.
                this.discoveryService.getCubes(userId, sessionId, sc.getId());
                
            } else {
                sessions.get(userId).get(sessionId).closeConnection(sc.getId());
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
	
	
	
	public String createConnection(String userId, String sessionId,
            String driverName, String connectStr, String username,
            String password) throws OlapException 
    {
        this.validateSession(userId, sessionId);
        
        OlapConnection connection;
        String connectionId = UUID.randomUUID().toString();

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
                
                sessions.get(userId).get(sessionId).putConnection(connectionId, olapConnection);
                
                // Obtaining a connection object doesn't mean that the
                // credentials are ok or whatever. We'll test it.
                this.discoveryService.getCubes(userId, sessionId, connectionId);
                
                return connectionId;
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
	
	
	public OlapConnection getNativeConnection(String userId, String sessionId,
	        String connectionId) 
	{
	    this.validateSession(userId, sessionId);
		return sessions.get(userId).get(sessionId).getConnection(connectionId);
	}
	

	/*
	 * Closes all active connections.
	 */
	private void disconnect(String userId, String sessionId)
	{
	    for (String connId : this.getSession(userId, sessionId).getActiveConnectionsId()) {
	        this.disconnect(userId, sessionId, connId);
	    }
	}
	
	public void disconnect(String userId, String sessionId, String connectionId) 
	{
	    this.validateSession(userId, sessionId);
		sessions.get(userId).get(sessionId).closeConnection(connectionId);
	}

	public SavedConnection getConnection(String userId,
	        String connectionId) 
	{
	    this.validateUser(userId);
	    return this.userManager.getSavedConnection(userId, connectionId);
	}
	
	public void saveConnection(String userId, SavedConnection connection) 
	{
	    this.validateUser(userId);
        User user = this.userManager.getUser(userId);
        user.getSavedConnections().add(connection);
        this.userManager.updateUser(user);
    }
	
	public List<SavedConnection> getConnections(String userId) 
	{
	    this.validateUser(userId);
	    List<SavedConnection> connections = new ArrayList<SavedConnection>();
	    User user = this.userManager.getUser(userId);
	    connections.addAll(user.getSavedConnections());
	    return connections;
	}
	
	public List<SavedConnection> getActiveConnections(String userId, String sessionId) {
	    this.validateSession(userId, sessionId);
        List<SavedConnection> conns = new ArrayList<SavedConnection>();
        for (String connectionId : this.getSession(userId, sessionId).getActiveConnectionsId()) {
            conns.add(this.getConnection(userId, connectionId));
        }
        return conns;
    }
	
	public void deleteConnection(String userId, String connectionName) 
	{
	    this.validateUser(userId);
	    User user = this.userManager.getUser(userId);
	    SavedConnection conn = getConnection(userId, connectionName);
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
        User user = this.userManager.getUser(userId);
        if (user==null)
            throw new SecurityException(
                Messages.getString("Services.InvalidSessionOrUserId")); //$NON-NLS-1$
    }

}
