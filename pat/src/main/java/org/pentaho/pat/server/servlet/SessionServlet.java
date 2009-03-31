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
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends AbstractServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;
	
	public void init() throws ServletException {
		super.init();
		sessionService = (SessionService)applicationContext.getBean("sessionService");
		if (sessionService==null)
		    throw new ServletException("No SessionService was found in the application context.");
	}

	public Boolean connect(String sessionId, CubeConnection connection) {
		try 
		{
		    String olap4jUrl=null;
		    String olap4jDriver=null;
		    
		    switch (connection.getConnectionType())
		    {
		    case XMLA:
		        olap4jUrl = "jdbc:xmla:Server=".concat(connection.getUrl());
		        olap4jDriver = "org.olap4j.driver.xmla.Olap4jXmlaDriver";
		        break;
		    case Mondrian:
		        olap4jUrl = "jdbc:mondrian:"
		            .concat("Jdbc=").concat(connection.getUrl()).concat(";")
		            .concat("JdbcDrivers=").concat(connection.getDriverClassName()).concat(";")
		            .concat("JdbcUser=").concat(connection.getUsername()).concat(";")
		            .concat("JdbcPassword=").concat(connection.getPassword()).concat(";")
		            .concat("Catalog=").concat(connection.getSchemaPath());
		        olap4jDriver="mondrian.olap4j.MondrianOlap4jDriver";
		    }
		    
			sessionService.createConnection(getCurrentUserId(),sessionId, 
				olap4jDriver, olap4jUrl, 
				connection.getUsername(), connection.getPassword());
			
			return true;
		} 
		catch (OlapException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public CubeConnection getConnection(String sessionId, String connectionName) {
	    SavedConnection savedConn = this.sessionService.getSavedConnection(getCurrentUserId(), connectionName);
	    try {
            return this.convert(savedConn);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}
	
	public Boolean saveConnection(String sessionId, CubeConnection connection) {
	    try {
            this.sessionService.saveConnection(getCurrentUserId(),
                    this.convert(connection));
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
	
	public String[] getConnections(String sessionId) {
	    List<String> connections = this.sessionService.getConnections(getCurrentUserId());
	    return connections.toArray(new String[connections.size()]);
	}

	public String createNewQuery(String sessionId) {
		try {
			return sessionService.createNewQuery(getCurrentUserId(), sessionId);
		} catch (OlapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Boolean deleteQuery(String sessionId, String queryId) {
		sessionService.releaseQuery(getCurrentUserId(), sessionId, queryId);
		return true;
	}

	public Boolean disconnect(String sessionId) {
		sessionService.releaseConnection(getCurrentUserId(),sessionId);
		return true;
	}

	public String getCurrentCube(String sessionId) {
		return (String)sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_CUBE_NAME);
	}

	public String getCurrentQuery(String sessionId) {
		return (String)sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_QUERY_NAME);
	}

	public List<String> getQueries(String sessionId) {
		return sessionService.getQueries(getCurrentUserId(), sessionId);
	}

	public Boolean setCurrentCube(String sessionId, String cubeId) {
		sessionService.saveUserSessionVariable(getCurrentUserId(), 
			sessionId, Constants.CURRENT_CUBE_NAME, cubeId);
		return true;
	}

	public Boolean setCurrentQuery(String sessionId, String queryId) {
		sessionService.saveUserSessionVariable(getCurrentUserId(), 
				sessionId, Constants.CURRENT_QUERY_NAME, queryId);
		return true;
	}
	
	

	public Boolean closeSession(String sessionId) {
		sessionService.releaseSession(getCurrentUserId(), sessionId);
		return true;
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
        StringBuilder schema = new StringBuilder("");
        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
          // Print the content on the console
          schema.append(strLine);
        }
        
        sc.setSchema(schema.toString());
	    
	    return sc;
	}

}
