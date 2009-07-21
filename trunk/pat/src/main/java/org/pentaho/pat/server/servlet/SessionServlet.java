package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Session;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.SessionService;

public class SessionServlet extends AbstractServlet implements Session {

	private static final long serialVersionUID = 1L;
	
	private transient Logger log = Logger.getLogger(SessionServlet.class);
	
	private transient SessionService sessionService;
	
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
		    this.sessionService.createConnection(
		        getCurrentUserId(),
		        sessionId,
		        this.convert(connection));
		} 
		catch (OlapException e) 
		{
		    log.error(Messages.getString("Servlet.Session.ConnectionFailed"),e); //$NON-NLS-1$
			throw new RpcException(Messages.getString("Servlet.Session.ConnectionFailed"),e); //$NON-NLS-1$
		}
	}
	
	public CubeConnection getConnection(String sessionId, String connectionName) throws RpcException
	{
	    SavedConnection savedConn = this.sessionService.getSavedConnection(getCurrentUserId(), connectionName);
	    return savedConn==null?null:this.convert(savedConn);
	}
	
	public void saveConnection(String sessionId, CubeConnection connection) throws RpcException
	{
        try {
            this.sessionService.saveConnection(getCurrentUserId(),
                    this.convert(connection));
        } catch (Exception e) {
            log.error(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Session.SchemaFileSystemAccessError"),e); //$NON-NLS-1$
        }
	}
	
	public CubeConnection[] getSavedConnections(String sessionId) throws RpcException
	{
        List<SavedConnection> savedConnections = this.sessionService.getSavedConnections(getCurrentUserId());
        CubeConnection[] cubeConnections = new CubeConnection[savedConnections.size()];
        for (int cpt=0;cpt<savedConnections.size();cpt++)
            cubeConnections[cpt]=convert(savedConnections.get(cpt));
        return cubeConnections;
	}
	
	public void deleteSavedConnection(String sessionId, String connectionName) throws RpcException
	{
	    this.sessionService.deleteSavedConnection(getCurrentUserId(),connectionName);
	}

	public void disconnect(String sessionId) throws RpcException
	{
		sessionService.releaseConnection(getCurrentUserId(),sessionId);
	}

	public String getCurrentCube(String sessionId) throws RpcException
	{
		return (String)sessionService.getUserSessionVariable(
				getCurrentUserId(), sessionId, Constants.CURRENT_CUBE_NAME);
	}

	public void setCurrentCube(String sessionId, String cubeId) throws RpcException
	{
		sessionService.saveUserSessionVariable(getCurrentUserId(), 
			sessionId, Constants.CURRENT_CUBE_NAME, cubeId);
	}

	public void closeSession(String sessionId) throws RpcException
	{
		sessionService.releaseSession(getCurrentUserId(), sessionId);
	}

	public String createSession() throws RpcException
	{
		return sessionService.createNewSession(getCurrentUserId());
	}
	
	private CubeConnection convert(SavedConnection sc) {
	    
	    CubeConnection cc = new CubeConnection();
	    cc.setName(sc.getName());
	    cc.setDriverClassName(sc.getDriverClassName());
	    cc.setConnectionType(ConnectionType.valueOf(sc.getType().toString()));
	    cc.setUrl(sc.getUrl());
	    cc.setUsername(sc.getUsername());
	    cc.setPassword(sc.getPassword());
	    cc.setSchemaData(sc.getSchema());

	    return cc;
	}
	
	private SavedConnection convert(CubeConnection cc) {
	    
	    SavedConnection sc = new SavedConnection();
	    
	    sc.setName(cc.getName());
	    sc.setDriverClassName(cc.getDriverClassName());
	    sc.setUrl(cc.getUrl());
	    sc.setUsername(cc.getUsername());
	    sc.setPassword(cc.getPassword());
	    sc.setType(org.pentaho.pat.server.data.pojo.ConnectionType.getInstance(cc.getConnectionType().name()));
	    sc.setSchema(cc.getSchemaData());

	    return sc;
	}

}
