package org.pentaho.pat.server.servlet;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.adapters.PrincipalSpringSecurityUserToken;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoader;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class AbstractServlet extends RemoteServiceServlet
{
	private static final long serialVersionUID = 1L;
	
	private static boolean initDone;
	private static boolean standaloneMode;
	private static Authentication standaloneAuth;
	protected static ApplicationContext applicationContext;
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	private final String[] contextFiles = {
			"./src/main/webapp/WEB-INF/pat-applicationContext.xml",
			"./src/main/webapp/WEB-INF/pat-securityContext.xml"
		};
	
	static 
	{
		initDone=false;
		standaloneMode=false;
	}
	
	@Override
	public void init() throws ServletException 
	{
		super.init();
		
		if (!initDone)
		{
	        applicationContext = ContextLoader.getCurrentWebApplicationContext();
	        
	        if (applicationContext==null)
	        {
	            log.info("Initializing PAT in standalone mode.");
	            
	        	// This happens if we launch PAT without a web context, like in the 
	        	// GWT shell for example. We'll initialize the context manually.
	        	applicationContext = new FileSystemXmlApplicationContext(contextFiles);
	        	
	        	standaloneMode=true;
	        	
	        	GrantedAuthority userAuths[] = {
	    				new GrantedAuthorityImpl("ROLE_USER"),
	    				new GrantedAuthorityImpl("ROLE_ADMIN")
	    			};
	    		
	    		standaloneAuth = new PrincipalSpringSecurityUserToken(
	    			"","admin","admin",userAuths,null);
	        }
	        
	        initDone=true;
		}
		
	}
	
	/**
	 * Helper method to gain access to the current user's security object.
	 * @return The current user name.
	 */
	protected String getCurrentUserId() 
	{
		if (standaloneMode)
			SecurityContextHolder.getContext().setAuthentication(standaloneAuth);
		
		return SecurityContextHolder.getContext().getAuthentication().getName();
		
	}

    protected void doUnexpectedFailure(Throwable e) {
        super.doUnexpectedFailure(new RpcException(e.getMessage(),e));
    }
}
