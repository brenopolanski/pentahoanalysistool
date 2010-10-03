package org.pentaho.pat.server.restservice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.ConnectionObject;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;

import com.sun.jersey.api.json.JSONWithPadding;

import javax.annotation.Resource;

@Path("/dimension") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service
public class DimensionService
{
	SessionServlet ss = new SessionServlet();
    QueryServlet qs = new QueryServlet();
    DiscoveryServlet ds = new DiscoveryServlet();
	

    @GET
    @Path("moveDimension")
    @Produces({"application/x-javascript", "application/xml","application/json"})
    public JSONWithPadding moveDimension(@QueryParam("sessionId") String sessionId, 
    		@QueryParam("queryID") String queryID, @QueryParam("axis") String axis, @QueryParam("dimensionName") String dimensionName, 
    		@QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback){
    	try {
			qs.init();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			qs.moveDimension(sessionId, queryID, IAxis.Standard.valueOf(axis), dimensionName);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String bool = "true";
		return new JSONWithPadding(new GenericEntity<String>(bool) {}, jsoncallback);
    }


}