package org.pentaho.pat.server.servlet;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.User;
import org.pentaho.pat.server.messages.Messages;
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

    private Logger log = Logger.getLogger(AbstractServlet.class);
	
	private final String[] contextFiles = {
			"./src/main/webapp/WEB-INF/pat-applicationContext.xml", //$NON-NLS-1$
			"./src/main/webapp/WEB-INF/pat-securityContext.xml" //$NON-NLS-1$
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
	            log.info(Messages.getString("Servlet.AbstractServlet.StandAlone")); //$NON-NLS-1$
	            
	        	// This happens if we launch PAT without a web context, like in the 
	        	// GWT shell for example. We'll initialize the context manually.
	            applicationContext = new FileSystemXmlApplicationContext(contextFiles);
	        	
	        	standaloneMode=true;
	        	
	        	initSecurityTokens();
	        }
	        
	        initDone=true;
		}
	}

	private static void initSecurityTokens() {
	    if (standaloneAuth==null&&standaloneMode)
	    {
	        UserManager userManager = (UserManager)applicationContext.getBean("userManager"); //$NON-NLS-1$
	        User user = userManager.getUser("admin"); //$NON-NLS-1$
	        
	        GrantedAuthority userAuths[] = {
                    new GrantedAuthorityImpl("Users"), //$NON-NLS-1$
                    new GrantedAuthorityImpl("Administrators") //$NON-NLS-1$
                };
            
            standaloneAuth = new PrincipalSpringSecurityUserToken(
                "", //$NON-NLS-1$
                "admin", //$NON-NLS-1$
                "admin", //$NON-NLS-1$
                userAuths,
                user);
	    }
	}
	
    /**
	 * Helper method to gain access to the current user's security object.
	 * @return The current user name.
	 */
	public static String getCurrentUserId() 
	{
		if (standaloneMode)
			SecurityContextHolder.getContext().setAuthentication(standaloneAuth);
		
		return SecurityContextHolder.getContext().getAuthentication().getName();
		
	}
	
	/**
	 * Setter for testing purposes. Injects the application context to use.
	 * Calling this method also configures the servlets in "stand alone" mode. No security
	 * config will be used.
	 * @param applicationContext The application context to use.
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) {
        AbstractServlet.applicationContext = applicationContext;
        standaloneMode=true;
        initSecurityTokens();
        initDone=true;
    }

    protected void doUnexpectedFailure(Throwable e) {
        log.error(Messages.getString("Servlet.AbstractServlet.RpcCallException"),e); //$NON-NLS-1$
        super.doUnexpectedFailure((e instanceof RpcException)?e:new RpcException(e.getMessage(),e));
    }

    public boolean isStandalone() {
        return standaloneMode;
    }
}
