package org.pentaho.pat.server.restservice;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.ConnectionObject;
import org.pentaho.pat.server.restservice.restobjects.CubeObject;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;

@Path("/service") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service
public class SessionService {

    SessionServlet ss = new SessionServlet();
    QueryServlet qs = new QueryServlet();
    DiscoveryServlet ds = new DiscoveryServlet();

    
    
    
 @GET
 @Path("createSession")
 @Produces( { "text/plain" }) // it is to set the response type
@Resource // to make it spring set the response type
 public String createSession(@Context HttpServletRequest request) throws RpcException, ServletException{
    ss.init();
    //sessionId = ss.createSession();
    return ss.createSession();
 }
 
 @GET
 @Path("getActiveConnections")
 @Produces({"application/xml","application/json"})
 @Resource // to make it spring set the response type
 public ConnectionObject getActiveConnections(@Context HttpServletRequest request, @QueryParam("sessionId") String sessionId) throws RpcException, ServletException{
    ss.init();
    
    
    CubeConnection[] ret = ss.getActiveConnections(sessionId);
    //sessionId = ss.createSession();
    ConnectionObject cob = new ConnectionObject();
    for(int i=0; i<ret.length; i++){
    	cob.addConnection(ret[i].getId(), ret[i].getName());
    	
    }
    return cob;
 }
 
 @GET
 @Path("getAvailableCubes")
 @Produces({"application/xml","application/json"})
 @Resource // to make it spring set the response type
 public CubeObject getAvailableCubes(@Context HttpServletRequest request, @QueryParam("sessionId") String sessionId) throws RpcException, ServletException{
    ss.init();
    ds.init();
    
    CubeConnection[] ret = ss.getActiveConnections(sessionId);
    //sessionId = ss.createSession();
    CubeObject cob = new CubeObject();
    for(int i=0; i<ret.length; i++){
    	CubeItem[] out = ds.getCubes(sessionId, ret[i].getId());
    	for(int j=0; j<out.length; j++){
    	cob.addCube(ret[i].getId(), out[i].getCatalog(), out[i].getName(), out[i].getSchema());
    	}
    }
    return cob;
 }
 
}
