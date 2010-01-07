package org.pentaho.pat;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

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

                applicationContext.setClassLoader(pluginClassloader);
                applicationContext.setConfigLocation(appContextUrl);
                applicationContext.refresh();

                String hibernateConfigurationFile =  PentahoSystem.getSystemSetting( "hibernate/hibernate-settings.xml","settings/config-file", null);
                String configPath = PentahoSystem.getApplicationContext().getSolutionPath(hibernateConfigurationFile);
                Configuration ac = new Configuration();
                ac.configure(new File(configPath));

                applicationContext.setAllowBeanDefinitionOverriding(true);
                BasicDataSource a = (BasicDataSource)applicationContext.getBean("dataSource");
                a.setDriverClassName(ac.getProperties().getProperty("connection.driver_class"));
                a.setUrl(ac.getProperties().getProperty("connection.url"));
                a.setUsername(ac.getProperties().getProperty("connection.username"));
                a.setPassword(ac.getProperties().getProperty("connection.password"));


                URL hibernateUrl = pluginClassloader.getResource("pat-hibernate.cfg.xml");
                AnnotationConfiguration patConfig = new AnnotationConfiguration();
                patConfig.configure(hibernateUrl);
                //                patConfig.setProperties(ac.getProperties());


                SessionFactory sf = (SessionFactory)applicationContext.getBean("sessionFactory");
                //                sf = patConfig.buildSessionFactory();

                LocalSessionFactoryBean sb = (LocalSessionFactoryBean) applicationContext.getBean("&sessionFactory");
                //                sb.setDataSource(a);
                sb.setBeanClassLoader(pluginClassloader);
                Properties aproperties = new Properties();
                aproperties = patConfig.getProperties();
                aproperties.remove("connection.url");
                aproperties.put("connection.url", ac.getProperty("connection.url"));

                sb.setHibernateProperties(aproperties);
                sb.afterPropertiesSet();

                sf = (SessionFactory) sb.getObject();


                ((SessionServlet)targetSessionBean).setStandalone(true);
                SessionServlet.setApplicationContext(applicationContext);
                ((SessionServlet)targetSessionBean).init();

                ((QueryServlet)targetQueryBean).setStandalone(true);
                QueryServlet.setApplicationContext(applicationContext);
                ((QueryServlet)targetQueryBean).init();


                ((DiscoveryServlet)targetDiscoveryBean).setStandalone(true);
                DiscoveryServlet.setApplicationContext(applicationContext);
                ((DiscoveryServlet)targetDiscoveryBean).init();

            }
            else
            {
                System.out.println("[ERROR] Application Context could not be found");
            }
        } catch (ServiceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }







        // TODO Auto-generated method stub

    }

    public void unLoaded() throws PluginLifecycleException {
        // TODO Auto-generated method stub

    }

}
