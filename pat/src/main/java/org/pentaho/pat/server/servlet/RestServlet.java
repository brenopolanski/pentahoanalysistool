package org.pentaho.pat.server.servlet;
        
import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.RestService;
import org.pentaho.pat.server.services.SessionService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
@Produces( { "application/xml" }) // it is to set the response type
@Resource // to make it spring set the response type
@Path("/foo") /* to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo */
@Scope("request") // to set the scope of service

public class RestServlet extends AbstractServlet {
  
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = Logger.getLogger(RestServlet.class);

    private RestService restService;
    
    private SessionService sessionService;
    
    public void init() throws ServletException {
        super.init();
        restService = (RestService) applicationContext.getBean("restService"); //$NON-NLS-1$
        sessionService = (SessionService) applicationContext.getBean("sessionService"); //$NON-NLS-1$
        if (restService == null) {
            throw new ServletException(Messages.getString("Servlet.QueryServiceNotFound")); //$NON-NLS-1$
        }
        if (sessionService == null) {
            throw new ServletException(Messages.getString("Servlet.SessionServiceNotFound")); //$NON-NLS-1$
        }
    }
    
    @GET // to be accessed using http get method
    @Path("hello")
    public String getIt() {
        return "Hi there!";
    }
}
