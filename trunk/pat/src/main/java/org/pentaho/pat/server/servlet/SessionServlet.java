package org.pentaho.pat.server.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;

import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.rpc.beans.CubeConnection;
import org.pentaho.pat.rpc.beans.CubeConnection.ConnectionType;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.services.SessionService;

import com.mysql.jdbc.Messages;

public class SessionServlet extends AbstractServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;
	
	public void init() throws ServletException {
		super.init();
		sessionService = (SessionService)applicationContext.getBean("sessionService"); //$NON-NLS-1$
		if (sessionService==null)
		    throw new ServletException(Messages.getString("Servlet.SessionServiceNotFound")); //$NON-NLS-1$
	}

	public void connect(String sessionId, CubeConnection connection) throws RpcException 
	{
		try 
		{
		    String olap4jUrl=null;
		    String olap4jDriver=null;
		    
		    switch (connection.getConnectionType())
		    {
		    case XMLA:
		        olap4jUrl = "jdbc:xmla:Server=".concat(connection.getUrl()); //$NON-NLS-1$
		        olap4jDriver = "org.olap4j.driver.xmla.Olap4jXmlaDriver"; //$NON-NLS-1$
		        sessionService.createConnection(getCurrentUserId(),sessionId, 
		                olap4jDriver, 
		                olap4jUrl, 
		                connection.getUsername(), 
		                connection.getPassword());
		        break;
		    case Mondrian:
		        olap4jUrl = "jdbc:mondrian:" //$NON-NLS-1$
		            .concat("Jdbc=").concat(connection.getUrl()).concat(";") //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
		            .concat("JdbcDrivers=").concat(connection.getDriverClassName()).concat(";") //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
		            .concat("Catalog=").concat(connection.getSchemaPath()); //$NON-NLS-1$
		        if (connection.getUsername()!=null)
		        {
		            olap4jUrl = olap4jUrl
		                .concat(";").concat("JdbcUser=").concat(connection.getUsername()).concat(";"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ $NON-NLS-2$
		        }
		        if (connection.getPassword()!= null)
		        {
		        	olap4jUrl = olap4jUrl
		        		.concat("JdbcPassword=").concat(connection.getPassword()); //$NON-NLS-1$ $NON-NLS-2$
		        }
		        olap4jDriver="mondrian.olap4j.MondrianOlap4jDriver"; //$NON-NLS-1$
		        
		        sessionService.createConnection(getCurrentUserId(),sessionId, 
		                olap4jDriver, olap4jUrl,null,null);
		    }
		} 
		catch (OlapException e) 
		{
			throw new RpcException(Messages.getString("Servlet.Session.ConnectionFailed")); //$NON-NLS-1$
		}
	}
	
	public CubeConnection getConnection(String sessionId, String connectionName) throws RpcException
	{
	    SavedConnection savedConn = this.sessionService.getSavedConnection(getCurrentUserId(), connectionName);
	    try {
            return this.convert(savedConn);
        } catch (IOException e) {
            throw new RpcException(Messages.getString("Servlet.Session.SchemaFileSystemAccessError")); //$NON-NLS-1$
        }
	}
	
	public void saveConnection(String sessionId, CubeConnection connection) throws RpcException
	{
        try {
            this.sessionService.saveConnection(getCurrentUserId(),
                    this.convert(connection));
        } catch (Exception e) {
            throw new RpcException(Messages.getString("Servlet.Session.SchemaFileSystemAccessError")); //$NON-NLS-1$
        }
	}
	
	public CubeConnection[] getSavedConnections(String sessionId) throws RpcException
	{
	    try {
	        List<SavedConnection> savedConnections = this.sessionService.getSavedConnections(getCurrentUserId());
	        CubeConnection[] cubeConnections = new CubeConnection[savedConnections.size()];
	        for (int cpt=0;cpt<savedConnections.size();cpt++)
	            cubeConnections[cpt]=convert(savedConnections.get(cpt));
	        return cubeConnections;
        } catch (IOException e) {
            throw new RpcException(Messages.getString("Servlet.Session.SchemaFileSystemAccessError")); //$NON-NLS-1$
        }
	}
	
	public void deleteSavedConnection(String sessionId, String connectionName) {
	    this.sessionService.deleteSavedConnection(getCurrentUserId(),connectionName);
	}

	public String createNewQuery(String sessionId) {
		try {
			return sessionService.createNewQuery(getCurrentUserId(), sessionId);
		} catch (OlapException e) {
			log.error(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
			return null;
		}
	}

	public void deleteQuery(String sessionId, String queryId) {
		sessionService.releaseQuery(getCurrentUserId(), sessionId, queryId);
	}

	public void disconnect(String sessionId) {
		sessionService.releaseConnection(getCurrentUserId(),sessionId);
	}

	public String getCurrentCube(String sessionId) {
		return (String)sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_CUBE_NAME);
	}

	public String getCurrentQuery(String sessionId) {
		return (String)sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_QUERY_NAME);
	}

	public String[] getQueries(String sessionId) {
	    List<String> list = sessionService.getQueries(getCurrentUserId(), sessionId);
		return list.toArray(new String[list.size()]);
	}

	public void setCurrentCube(String sessionId, String cubeId) {
		sessionService.saveUserSessionVariable(getCurrentUserId(), 
			sessionId, Constants.CURRENT_CUBE_NAME, cubeId);
	}

	public void setCurrentQuery(String sessionId, String queryId) {
		sessionService.saveUserSessionVariable(getCurrentUserId(), 
				sessionId, Constants.CURRENT_QUERY_NAME, queryId);
	}
	
	

	public void closeSession(String sessionId) {
		sessionService.releaseSession(getCurrentUserId(), sessionId);
	}

	public String createSession() {
		return sessionService.createNewSession(getCurrentUserId());
	}
	
	private CubeConnection convert(SavedConnection sc) throws IOException {
	    
	    CubeConnection cc = new CubeConnection();
	    cc.setName(sc.getName());
	    cc.setDriverClassName(sc.getDriverClassName());
	    cc.setConnectionType(ConnectionType.valueOf(sc.getType().toString()));
	    cc.setUrl(sc.getUrl());
	    cc.setUsername(sc.getUsername());
	    cc.setPassword(sc.getPassword());
	    
	    String tmpFileName = String.valueOf(UUID.randomUUID());
	    File schema = File.createTempFile(tmpFileName,null);
	    FileWriter fw = new FileWriter(schema);
	    BufferedWriter bw = new BufferedWriter(fw);
	    bw.write(sc.getSchema());
	    bw.close();
	    
	    cc.setCatalog(tmpFileName);
	    
	    return cc;
	}
	
	private SavedConnection convert(CubeConnection cc) throws Exception {
	    
	    SavedConnection sc = new SavedConnection();
	    
	    sc.setName(cc.getName());
	    sc.setDriverClassName(cc.getDriverClassName());
	    sc.setUrl(cc.getUrl());
	    sc.setUsername(cc.getUsername());
	    sc.setPassword(cc.getPassword());
	    sc.setType(org.pentaho.pat.server.data.pojo.ConnectionType.getInstance(cc.getConnectionType().name()));
	    
	    FileInputStream fis = new FileInputStream(cc.getCatalog());
	    DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        StringBuilder schema = new StringBuilder("");//$NON-NLS-1$
        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
          // Print the content on the console
          schema.append(strLine);
        }
        
        sc.setSchema(schema.toString());
	    
	    return sc;
	}

}
