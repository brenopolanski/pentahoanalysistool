package org.pentaho.pat.rpc;

import java.util.List;

import org.pentaho.pat.rpc.beans.CubeConnection;
import org.springframework.security.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Manages the operations on the GUI and persists it's current
 * selections. 
 * 
 * @author Luc Boudreau
 */
public interface Session extends RemoteService {

	
	@Secured ({"ROLE_USER"})
	public String createSession();
	
	@Secured ({"ROLE_USER"})
	public Boolean closeSession(String sessionId);
	

	
	
	
	
//	@Secured ({"ROLE_USER"})
//	public Boolean connect(String sessionId, String driverClassName, String url, 
//			String username, String password);
	@Secured ({"ROLE_USER"})
    public Boolean connect(String sessionId, CubeConnection connection);
	
	@Secured ({"ROLE_USER"})
	public Boolean disconnect(String sessionId);	
	
	
	
	
	
	
	
	
	
	
	
	
	@Secured ({"ROLE_USER"})
	public Boolean setCurrentQuery(String sessionId, String queryId);
	
	@Secured ({"ROLE_USER"})
	public String getCurrentQuery(String sessionId);
	
	@Secured ({"ROLE_USER"})
	public String createNewQuery(String sessionId);
	
	@Secured ({"ROLE_USER"})
	public List<String> getQueries(String sessionId);

	@Secured ({"ROLE_USER"})
	public Boolean deleteQuery(String sessionId, String queryId);
	
	
	
	
	
	
	
	@Secured ({"ROLE_USER"})
	public Boolean setCurrentCube(String sessionId, String cubeId);
	
	
	@Secured ({"ROLE_USER"})
	public String getCurrentCube(String sessionId);

	
}
