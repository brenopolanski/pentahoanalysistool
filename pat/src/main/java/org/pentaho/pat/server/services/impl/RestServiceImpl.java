package org.pentaho.pat.server.services.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.pentaho.pat.server.services.RestService;
import org.pentaho.pat.server.services.SessionService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Path("/myresource2")
@Component
@Scope("request")

public class RestServiceImpl extends AbstractService implements RestService {

    private SessionService sessionService = null;
    
    private final static Logger LOG = Logger.getLogger(RestServiceImpl.class);
    
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.sessionService);
    }

    /**
     * 
     * TODO JAVADOC
     * 
     * @param service
     */
    public void setSessionService(final SessionService service) {
        this.sessionService = service;
    }
    
 // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String getIt() {
        return "Hi there!";
    }
    
}
