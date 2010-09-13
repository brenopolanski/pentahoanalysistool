package org.pentaho.pat.server.restservice;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;

import org.olap4j.query.Query;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.DimensionObject;
import org.pentaho.pat.server.restservice.restobjects.QueryObject;
import org.pentaho.pat.server.restservice.restobjects.DimensionObject.Dimension;
import org.pentaho.pat.server.restservice.restobjects.ResultSetObject;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * The Query Service for the PAT Restful Interface, this is WIP DO NOT TAKE IT AS A FINSIHED API.
 * 
 * @author tom(at)wamonline.org.uk
 * @since 0.9.0
 */
@SuppressWarnings("restriction")
@Path("/{user}/{schema}/{cube}/{queryname}") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service
public class QueryService
{

    
    SessionServlet ss = new SessionServlet();
    QueryServlet qs = new QueryServlet();
    DiscoveryServlet ds = new DiscoveryServlet();

    
 
 

    /**
     *
     * This method allows you to create a query object in one request,
     * as per the idea of Tiemonster.<br>
     * To use this method you need to have a Session Object available.<br>
     * HTTP POST.<br>
     * <pre>curl -XPOST -d "connectionId=792bcd2d-41c5-4af5-84b6-16fd65c4f7bb" -d "cubeName=SteelWheelsSales"  --basic -u "admin:admin" "http://localhost:8080/rest/53c72f85-7ff2-4a63-b4d2-dbd55a56255f/query"</pre>
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException
     * @throws ServletException
     */
    @POST
    @Produces({"application/xml",
            "application/json" })
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized QueryObject createNewQuery(
            @FormParam("sessionId") String sessionId,
            @FormParam("connectionId") String connectionId,
            @PathParam("cube") String cube,
            @PathParam("user") String user,
            @PathParam("schema") String schema,
            @PathParam("queryname") String queryname,
            @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
            throws RpcException, ServletException {
        //ss.init();
        qs.init();
        ds.init();

        QueryObject qob = new QueryObject();
        qob.setQueryId(qs.createNewQuery(sessionId, connectionId, cube));

        DimensionObject dob = new DimensionObject();

        String[] dims = ds.getDimensions(sessionId, qob.getQueryId(),
                IAxis.Standard.valueOf("UNUSED"));
        for (int i = 0; i < dims.length; i++) {
            dob.newDimension(dims[i], "UNUSED");
        }

        qob.setDimensions(dob);

        return qob;
    }

    /**
     *
     * This method allows you to delete a query,
     * as per the idea of Tiemonster.<br>
     * HTTP DELETE<br>
     * 
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException
     * @throws ServletException
     */
    @DELETE
    @Path("{queryId}")
    @Consumes({"application/xml",
            "application/json" })
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized void deleteQuery(@PathParam("queryId") String queryId, 
    		@FormParam("sessionId") String sessionId,
            @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
            throws RpcException, ServletException {
        ss.init();
        qs.init();
        ds.init();

        this.qs.deleteQuery(sessionId, queryId);
    }
    
    /**
     *
     * This method allows you to overwrite a query object in one request,
     * as per the idea of Tiemonster.<br>
     * HTTP PUT.<br>
     * 
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException
     * @throws ServletException
     */
    @PUT
    @Path("{queryId}")
    @Consumes({"application/xml",
            "application/json" })
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized void saveQuery(@PathParam("queryId") String queryId, 
    		@FormParam("sessionId") String sessionId,
            @FormParam("queryName") String queryName, @FormParam("connectionId") String connectionId,
            @FormParam("cubeCatalog") String catalog, @FormParam("cubeName") String cubeName, @FormParam("schema") String schema,
            @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
            throws RpcException, ServletException {
        ss.init();
        qs.init();
        ds.init();
        CubeItem ci = new CubeItem(cubeName, catalog, schema);
        
        this.qs.saveQuery(sessionId, queryId, queryName, connectionId, ci, cubeName);
    }
    
    /**
     *
     * This method allows you to get a query object in one request,
     * as per the idea of Tiemonster.<br>
     * HTTP GET.<br>
     * curl --basic -u "admin:admin" "http://localhost:8080/rest/service/query?sessionId=idstring&connectionId=connectionstring&cubeName=cubestring"
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException 
     * @throws RpcException
     * @throws ServletException 
     * @throws ServletException
     */
  /*  @GET
    @Path("{queryId}")
    @Produces({ "application/x-javascript", "application/xml",
            "application/json" })
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized void loadQuery(@PathParam("queryId") String queryId,
            @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
            throws RpcException, ServletException {
        ss.init();
        qs.init();
        ds.init();


    }*/
    
  
    /**
     * curl --basic -u "admin:admin" -XPUT -H 'Content-Type: application/json' -d '' http://localhost:8080/rest/admin/SteelWheels/SteelWheelsSales/tomsquery/run?sessionId=10 -v
     */
    @PUT // to be accessed using http get method
    @Path("run")
    @Consumes("application/json")
    @Produces("application/json")
    public ResultSetObject run(QueryObject qob, @QueryParam("sessionId") String sessionId,
            @PathParam("cube") String cube,
            @PathParam("user") String user,
            @PathParam("schema") String schema,
            @PathParam("queryname") String queryname,
            @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback) throws RpcException, ServletException
    {
    
        qs.init();
        
        for (int i=0; i<qob.getDimensions().getDimensionList().size(); i++){
            Dimension dimObj = qob.getDimensions().getDimensionList().get(i);
            qs.moveDimension(sessionId, qob.getQueryId(), IAxis.Standard.valueOf(dimObj.getAxis()), dimObj.getName());
        }
        
        
        
        ResultSetObject rso = new ResultSetObject(qs.executeQuery(sessionId, qob.getQueryId()));
        return rso;
        
    }
    
    
}