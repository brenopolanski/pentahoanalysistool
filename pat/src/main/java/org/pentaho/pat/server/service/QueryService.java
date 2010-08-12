package org.pentaho.pat.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;
import javax.annotation.Resource;

@Path("/query") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service
public class QueryService
{

    
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
 
 
 @GET
 @Path("newQuery")
 public String newQuery(@Context HttpServletRequest request,@QueryParam("sessionId") String sessionId, @QueryParam("connectionId") String connectionId,@QueryParam("cubeName") String cubeName) throws ServletException{
     try {
    	 qs.init();
         return qs.createNewQuery(sessionId, connectionId, cubeName);
     } catch (RpcException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     }
     return null;
 }
 
 //@GET
 //@Path("loadQuery")
// public boolean loadQuery(@Context HttpServletRequest request,@QueryParam("queryID") String queryID);
 
 //@GET
 //@Path("saveQuery")
 //public boolean saveQuery(@Context HttpServletRequest request,@QueryParam("queryID") String queryID);
 
 //@GET
 //@Path("executeQuery")
// public void executeQuery(@Context HttpServletRequest request,@QueryParam("queryID") String queryID);
 
 //@GET
 //@Path("getDimensions")
// public String[] getDimensions(@Context HttpServletRequest request,@QueryParam("queryID") String queryID);
}