package org.pentaho.pat;

import javax.servlet.ServletException;

import org.pentaho.pat.server.servlet.SessionServlet;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.IServiceManager;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import org.pentaho.platform.api.engine.ServiceException;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;

public class PatLifeCycleListener implements IPluginLifecycleListener {

    
    
    public void init() throws PluginLifecycleException {
        
        
    }

    public void loaded() throws PluginLifecycleException {

        IServiceManager serviceManager = PentahoSystem.get(IServiceManager.class, PentahoSessionHolder.getSession());
        try {
            Object targetSessionBean = serviceManager.getServiceBean("gwt","session.rpc");
            Object targetQueryBean = serviceManager.getServiceBean("gwt","query.rpc");
            Object targetDiscoveryBean = serviceManager.getServiceBean("gwt","discovery.rpc");
            
            try {
                ((SessionServlet)targetSessionBean).setStandalone(true);
                ((SessionServlet)targetSessionBean).init();
                ((SessionServlet)targetQueryBean).setStandalone(true);
                ((SessionServlet)targetQueryBean).init();
                ((SessionServlet)targetDiscoveryBean).setStandalone(true);
                ((SessionServlet)targetDiscoveryBean).init();
            } catch (ServletException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        } catch (ServiceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        
        
        
        
        

        // TODO Auto-generated method stub

    }

    public void unLoaded() throws PluginLifecycleException {
        // TODO Auto-generated method stub

    }

}
