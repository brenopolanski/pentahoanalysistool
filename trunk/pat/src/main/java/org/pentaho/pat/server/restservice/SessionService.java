package org.pentaho.pat.server.restservice;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.ConnectionObject;
import org.pentaho.pat.server.restservice.restobjects.CubeObject;
import org.pentaho.pat.server.restservice.restobjects.SchemaObject;
import org.pentaho.pat.server.restservice.restobjects.SessionObject;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * The Session Service for the PAT Restful Interface, this is WIP DO NOT TAKE IT
 * AS A FINSIHED API.
 * 
 * @author tom(at)wamonline.org.uk
 * @since 0.9.0
 */
@SuppressWarnings("restriction")
@Path("{user}/session")
/*
 * to set the path on which the service will be accessed e.g.
 * http://{serverIp}/{contextPath}/foo
 */
@Scope("request")
// to set the scope of service
public class SessionService {

	SessionServlet ss = new SessionServlet();
	QueryServlet qs = new QueryServlet();
	DiscoveryServlet ds = new DiscoveryServlet();

	/**
	 * This method allows you to login and get a session object in one request,
	 * as per the idea of Tiemonster.<br>
	 * <pre>curl --basic -u "admin:admin" -XPOST http://localhost:8080/rest/service/session</pre><br>
	 * @param jsoncallback
	 * @return
	 * @throws RpcException
	 * @throws ServletException
	 */
	@POST
	@Produces({ "application/x-javascript", "application/xml",
			"application/json" })
	// it is to set the response type
	@Resource
	// to make it spring set the response type
	public JSONWithPadding createNewSession(
			@QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
			throws RpcException, ServletException {
		ss.init();
		qs.init();
		ds.init();
		
		SessionObject string = new SessionObject();
		string.setSessionId(ss.createSession());

		CubeConnection[] ret = ss.getActiveConnections(string.getSessionId());


		ConnectionObject cob = new ConnectionObject();

		for (int i = 0; i < ret.length; i++){
			CubeItem[] out = ds.getCubes(string.getSessionId(), ret[i].getId());
			SchemaObject sob = new SchemaObject();
			
			Set<String> schemas = new HashSet<String>();
			for (CubeItem item : out){
				schemas.add(item.getSchema());
			}
			
			Iterator<String> it = schemas.iterator(); 
			while (it.hasNext()) { 
				String name = it.next().toString();
				CubeObject cube = new CubeObject();
				for(int k =0; k<out.length; k++){
					if(out[k].getSchema().equals(name)){
						cube.addCube(out[k].getName());
					}
				}
			sob.addSchema(name, cube);
			}
			cob.addConnection(ret[i].getId(), ret[i].getName(), sob);
		}
		
	

		string.setConnectionObject(cob);

		return new JSONWithPadding(new GenericEntity<SessionObject>(string) {
		}, jsoncallback);
	}

}
