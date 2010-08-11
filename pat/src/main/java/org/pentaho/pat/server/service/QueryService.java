package org.pentaho.pat.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;
import javax.annotation.Resource;
@Produces( { "application/xml" }) // it is to set the response type
@Resource // to make it spring set the response type
@Path("/queryrest") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service
public class QueryService
{

    
    SessionServlet ss = new SessionServlet();
    QueryServlet qs = new QueryServlet();
    String sessionId;
    String connectionId;
    String cubeName;
    
    
    
 @GET
 @Path("createSession")
 public String createSession(@Context HttpServletRequest request) throws RpcException{
    
        return ss.createSession();
    
 }
 
 //@GET
 //@Path("newQuery")
 //public String newQuery(@Context HttpServletRequest request);
 
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