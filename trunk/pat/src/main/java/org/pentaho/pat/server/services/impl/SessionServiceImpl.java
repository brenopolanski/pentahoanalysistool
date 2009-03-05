package org.pentaho.pat.server.services.impl;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.olap4j.mdx.AxisNode;
import org.olap4j.mdx.IdentifierNode;
import org.olap4j.mdx.ParseTreeNode;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.impl.DefaultMdxParserImpl;
import org.olap4j.query.Query;
import org.pentaho.pat.Constants;
import org.pentaho.pat.server.data.pojo.Session;
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
	



	
	private static Map<String,Map<String, Session>> sessions = null;
	
	
	
	
	
	static {
		sessions = new ConcurrentHashMap<String, Map<String, Session>>();
	}
	
	
	
	
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
	
	
	
	
	
	
	public void deleteUserSessionVariable(String userId, String sessionId, 
		String key) 
	{
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			sessions.get(userId).get(sessionId)
				.getVariables().remove(key);
		}
		else
			throw new RuntimeException("Invalid user/session ids provided.");
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
		else
			throw new RuntimeException("Invalid user/session ids provided.");
	}

	
	
	
	public Object getUserSessionVariable(String userId, String sessionId,
			String key) {
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			return sessions.get(userId).get(sessionId)
				.getVariables().get(key);
		}
		throw new RuntimeException("Invalid user/session ids provided.");
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
		else
			throw new RuntimeException("Invalid user/session ids provided.");
	}
	
	
	
	public OlapConnection getConnection(String userId, String sessionId) {
		if (sessions.containsKey(userId) &&
			sessions.get(userId).containsKey(sessionId))
		{
			return sessions.get(userId).get(sessionId).getConnection();
		}
		throw new RuntimeException("Invalid user/session ids provided.");
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
				e.printStackTrace();
			}
			sessions.get(userId).get(sessionId).setConnection(null);
		}
		else
			throw new RuntimeException("Invalid user/session ids provided.");
	}
	
	
	
	
	
	
	
	



	public String createNewQuery(String userId, String sessionId) 
		throws OlapException 
	{
		if (sessions.containsKey(userId) &&
				sessions.get(userId).containsKey(sessionId))
		{
			// We need to verify if the user has selected a cube.
			String cubeName = (String)sessions.get(userId).get(sessionId).getVariables()
				.get(Constants.CURRENT_CUBE_NAME); 
			
			if (cubeName==null)
				throw new OlapException("You asked to create a query but there was no cube previously selected.");
			
			String generatedId = String.valueOf(UUID.randomUUID());
			String base_mdx = "SELECT {} ON ROWS, {} ON COLUMNS FROM ["+cubeName+"]";
				
			DefaultMdxParserImpl parser = new DefaultMdxParserImpl(getConnection(userId, sessionId));
			SelectNode query = parser.parseSelect(base_mdx);
			
			sessions.get(userId).get(sessionId).getQueries()
				.put(generatedId, query);
			
			return generatedId;
		}
		else
			throw new RuntimeException("Invalid user/session ids provided.");
	}

	
	
	public SelectNode getQuery(String userId, String sessionId, String queryId) 
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
			Set<Entry<String, SelectNode>> entries = 
				sessions.get(userId).get(sessionId).getQueries().entrySet();
			for(Entry<String,SelectNode> entry : entries)
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
