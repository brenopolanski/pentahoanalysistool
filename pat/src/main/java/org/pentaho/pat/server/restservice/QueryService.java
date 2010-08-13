package org.pentaho.pat.server.restservice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.olap4j.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.ConnectionObject;
import org.pentaho.pat.server.restservice.restobjects.CubeObject;
import org.pentaho.pat.server.restservice.restobjects.DimensionObject;
import org.pentaho.pat.server.restservice.restobjects.ResultSetObject;
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
 @Path("newQuery")
 public String newQuery(@Context HttpServletRequest request,@QueryParam("sessionId") String sessionId, @QueryParam("connectionId") String connectionId,@QueryParam("cubeName") String cubeName) throws ServletException{
	 qs.init();
	 try {
    	 
         return qs.createNewQuery(sessionId, connectionId, cubeName);
     } catch (RpcException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return null;
     }
     
 }
 
 //@GET
 //@Path("loadQuery")
// public boolean loadQuery(@Context HttpServletRequest request,@QueryParam("queryID") String queryID);
 
 //@GET
 //@Path("saveQuery")
 //public boolean saveQuery(@Context HttpServletRequest request,@QueryParam("queryID") String queryID);
 
 @GET
 @Path("executeQuery")
 public ResultSetObject executeQuery(@Context HttpServletRequest request,@QueryParam("sessionId") String sessionId, @QueryParam("queryID") String queryID){
	 try {
		qs.init();
	} catch (ServletException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 
	 CellDataSet result = null;
	try {
		result = qs.executeQuery(sessionId, queryID);
	} catch (RpcException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 return new ResultSetObject(result);
	 
 }
 
 /*@GET
 @Path("getDimensions")
 @Produces({"application/xml","application/json"})
 public DimensionObject getDimensions(@Context HttpServletRequest request,@QueryParam("queryID") String queryID){
	 return null;
 }*/
 
 @GET
 @Path("getDimensions")
 @Produces({"application/xml","application/json"})
 public DimensionObject getDimensions(@Context HttpServletRequest request,@QueryParam("sessionId") String sessionId, @QueryParam("queryID") String queryID, @QueryParam("axis") String axis){
	 try {
		ds.init();
	} catch (ServletException e1) {
		e1.printStackTrace();
	}
	 
	 DimensionObject dob = new DimensionObject();
	 
	 try {
		String[] dims = ds.getDimensions(sessionId, queryID, IAxis.Standard.valueOf(axis));
		for (int i = 0; i<dims.length; i++){
			dob.newDimension(dims[i], axis);
		}
	} catch (RpcException e) {
		e.printStackTrace();
	}
	
	return dob;
 }
}