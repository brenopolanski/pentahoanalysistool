package org.pentaho.pat.server.restservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Scope;
import javax.annotation.Resource;
@Produces( { "application/xml" }) // it is to set the response type
@Resource // to make it spring set the response type
@Path("/foo") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service
public class MemberService
{
@GET // to be accessed using http get method
@Path("hello")
public String sayHello(@Context HttpServletRequest request,@QueryParam("userName") String userName)
{
// here we are creating xml as a string
return " hi "+userName+"";
}
@POST // to be accessed using http post method
@Path("postService")
public Response sayHello(@Context HttpServletRequest request)
{
//String userName = request.getParamter("userName");
//here we are returning a java object but it will be converted to string
// will create it at the end see below for example
return null;// new Response("hi");
}
}