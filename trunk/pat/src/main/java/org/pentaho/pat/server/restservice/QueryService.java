package org.pentaho.pat.server.restservice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;

import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.DimensionObject;
import org.pentaho.pat.server.restservice.restobjects.QueryObject;
import org.pentaho.pat.server.restservice.restobjects.ResultSetObject;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * The Query Service for the PAT Restful Interface, this is WIP DO NOT TAKE IT AS A FINSIHED API.
 * 
 * @author tombarber
 * @since 0.9.0
 */
@Path("/query") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service
public class QueryService
{

    
    SessionServlet ss = new SessionServlet();
    QueryServlet qs = new QueryServlet();
    DiscoveryServlet ds = new DiscoveryServlet();

    
 
 
 @GET
 @Path("createQuery")
 @Produces({"application/x-javascript", "application/xml","application/json"})
 public JSONWithPadding createQuery(@Context HttpServletRequest request,@QueryParam("sessionId") String sessionId, @QueryParam("connectionId") String connectionId,@QueryParam("cubeName") String cubeName
		 , @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback) throws ServletException{
	 qs.init();
	 try {
    	 QueryObject qob = new QueryObject();
    	 qob.setQueryId(qs.createNewQuery(sessionId, connectionId, cubeName));
         return new JSONWithPadding(new GenericEntity<QueryObject>(qob) {}, jsoncallback);
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
 @Produces({"application/x-javascript", "application/xml","application/json"})
 public JSONWithPadding executeQuery(@Context HttpServletRequest request,@QueryParam("sessionId") String sessionId, @QueryParam("queryID") String queryID
		 , @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback){
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
	 ResultSetObject rso = new ResultSetObject(result);
	 return new JSONWithPadding(new GenericEntity<ResultSetObject>(rso) {}, jsoncallback);
	 
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