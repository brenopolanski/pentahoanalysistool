package org.pentaho.pat;

import java.net.URL;

import javax.servlet.ServletException;

import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.api.engine.IServiceManager;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import org.pentaho.platform.api.engine.ServiceException;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PatLifeCycleListener implements IPluginLifecycleListener {

    
    
    public void init() throws PluginLifecycleException {
        
        
    }

    public void loaded() throws PluginLifecycleException {

        IServiceManager serviceManager = PentahoSystem.get(IServiceManager.class, PentahoSessionHolder.getSession());
        try {
            Object targetSessionBean = serviceManager.getServiceBean("gwt","session.rpc");
            Object targetQueryBean = serviceManager.getServiceBean("gwt","query.rpc");
            Object targetDiscoveryBean = serviceManager.getServiceBean("gwt","discovery.rpc");
            IPluginManager pluginManager = PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession());
            ClassLoader pluginClassloader = pluginManager.getClassLoader("Pentaho Analysis Tool Plugin");
            if (pluginClassloader == null)
                throw new ServiceException("plugin classloader can't be loaded");
            
            URL contextUrl = pluginClassloader.getResource("pat-applicationContext.xml");
            
            if ( contextUrl!= null ) {
                String appContextUrl = contextUrl.toString();
                ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { appContextUrl }, false);
                
//                applicationContext.setClassLoader(pluginClassloader);
//                applicationContext.setConfigLocation(appContextUrl);
//                applicationContext.refresh();
                
            
            try {
                
                ((SessionServlet)targetSessionBean).setStandalone(true);
                SessionServlet.setApplicationContext(applicationContext);
                ((SessionServlet)targetSessionBean).init();
                
                ((QueryServlet)targetQueryBean).setStandalone(true);
                ((QueryServlet)targetQueryBean).init();
                QueryServlet.setApplicationContext(applicationContext);
                
                ((DiscoveryServlet)targetDiscoveryBean).setStandalone(true);
                ((DiscoveryServlet)targetDiscoveryBean).init();
                DiscoveryServlet.setApplicationContext(applicationContext);
                
            } catch (ServletException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
            else
            {
                System.out.println("[ERROR] Application Context could not be found");
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
