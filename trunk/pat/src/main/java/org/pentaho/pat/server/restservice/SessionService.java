package org.pentaho.pat.server.restservice;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;

import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.ConnectionObject;
import org.pentaho.pat.server.restservice.restobjects.CubeObject;
import org.pentaho.pat.server.restservice.restobjects.DimensionObject;
import org.pentaho.pat.server.restservice.restobjects.QueryObject;
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
 * @author tombarber
 * @since 0.9.0
 */
@SuppressWarnings("restriction")
@Path("/service")
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
	 * as per the idea of Tiemonster.
	 * curl --basic -u "admin:admin" http://localhost:8080/rest/service/createSession
	 * @param jsoncallback
	 * @return
	 * @throws RpcException
	 * @throws ServletException
	 */
	@GET
	@Path("createSession")
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

		for (int i = 0; i < ret.length; i++) {
			CubeItem[] out = ds.getCubes(string.getSessionId(), ret[i].getId());
			CubeObject cubeobj = new CubeObject();
			for (CubeItem item : out) {

				cubeobj.addCube(ret[i].getId(), item.getCatalog(),
						item.getName(), item.getSchema());

			}
			cob.addConnection(ret[i].getId(), ret[i].getName(), cubeobj);

		}

		string.setConnectionObject(cob);

		return new JSONWithPadding(new GenericEntity<SessionObject>(string) {
		}, jsoncallback);
		// return new JSONWithPadding(string, jsoncallback);
	}

	/**
	 *
	 * This method allows you to get a query object in one request,
	 * as per the idea of Tiemonster.
	 * curl --basic -u "admin:admin" "http://localhost:8080/rest/service/createQuery?sessionId=idstring&connectionId=connectionstring&cubeName=cubestring"
	 * @param sessionId
	 * @param connectionId
	 * @param cubeName
	 * @param jsoncallback
	 * @return
	 * @throws RpcException
	 * @throws ServletException
	 */
	@GET
	@Path("createQuery")
	@Produces({ "application/x-javascript", "application/xml",
			"application/json" })
	// it is to set the response type
	@Resource
	// to make it spring set the response type
	public JSONWithPadding createNewQuery(
			@QueryParam("sessionId") String sessionId,
			@QueryParam("connectionId") String connectionId,
			@QueryParam("cubeName") String cubeName,
			@QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
			throws RpcException, ServletException {
		ss.init();
		qs.init();
		ds.init();

		QueryObject qob = new QueryObject();
		qob.setQueryId(qs.createNewQuery(sessionId, connectionId, cubeName));

		DimensionObject dob = new DimensionObject();

		String[] dims = ds.getDimensions(sessionId, qob.getQueryId(),
				IAxis.Standard.valueOf("UNUSED"));
		for (int i = 0; i < dims.length; i++) {
			dob.newDimension(dims[i], "UNUSED");
		}

		qob.setDimensions(dob);

		return new JSONWithPadding(new GenericEntity<QueryObject>(qob) {
		}, jsoncallback);
	}

	/**
	 * Create a session via the http api.
	 * 
	 * @param jsoncallback
	 * @return
	 * @throws RpcException
	 * @throws ServletException
	 */
	
	@GET
	@Path("createSession")
	@Produces({ "application/x-javascript", "application/xml",
			"application/json" })
	// it is to set the response type
	@Resource
	// to make it spring set the response type
	@Deprecated
	public JSONWithPadding createSession(
			@QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
			throws RpcException, ServletException {
		ss.init();
		// sessionId = ss.createSession();
		// return ss.createSession();

		SessionObject string = new SessionObject();
		string.setSessionId(ss.createSession());

		return new JSONWithPadding(new GenericEntity<SessionObject>(string) {
		}, jsoncallback);
		// return new JSONWithPadding(string, jsoncallback);
	}

	/**
	 * Return the active connections.
	 * 
	 * @param sessionId
	 * @param jsoncallback
	 * @return
	 * @throws RpcException
	 * @throws ServletException
	 */
	
	@GET
	@Path("getActiveConnections")
	@Produces({ "application/x-javascript", "application/xml",
			"application/json" })
	@Resource
	// to make it spring set the response type
	@Deprecated
	public JSONWithPadding getActiveConnections(
			@QueryParam("sessionId") String sessionId,
			@QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback)
			throws RpcException, ServletException {
		ss.init();

		CubeConnection[] ret = ss.getActiveConnections(sessionId);
		// sessionId = ss.createSession();
		ConnectionObject cob = new ConnectionObject();
		for (int i = 0; i < ret.length; i++) {
			cob.addConnection(ret[i].getId(), ret[i].getName());

		}
		return new JSONWithPadding(new GenericEntity<ConnectionObject>(cob) {
		}, jsoncallback);
	}

	/**
	 * Return the available cubes for a connection.x
	 * 
	 * @param request
	 * @param sessionId
	 * @param queryId
	 * @return
	 * @throws RpcException
	 * @throws ServletException
	 */
	
	@GET
	@Path("getAvailableCubes")
	@Produces({ "application/xml", "application/json" })
	@Resource
	// to make it spring set the response type
	@Deprecated
	public CubeObject getAvailableCubes(@Context HttpServletRequest request,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("connectionId") String connectionId)
			throws RpcException, ServletException {
		ss.init();
		ds.init();

		// sessionId = ss.createSession();
		CubeObject cob = new CubeObject();
		CubeItem[] out = ds.getCubes(sessionId, connectionId);

		for (int j = 0; j < out.length; j++) {
			cob.addCube(connectionId, out[j].getCatalog(), out[j].getName(),
					out[j].getSchema());
		}

		return cob;
	}

}
