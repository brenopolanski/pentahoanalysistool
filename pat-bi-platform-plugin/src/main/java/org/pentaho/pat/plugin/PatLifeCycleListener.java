package org.pentaho.pat.plugin;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.jfree.util.Log;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.PlatformServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.api.engine.IServiceManager;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import org.pentaho.platform.api.engine.ServiceException;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianCatalog;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianCatalogHelper;
import org.pentaho.platform.plugin.services.pluginmgr.PluginClassLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class PatLifeCycleListener implements IPluginLifecycleListener {

    public void init() throws PluginLifecycleException {

    }

    public void loaded() throws PluginLifecycleException {

        IServiceManager serviceManager = (IServiceManager) PentahoSystem.get(IServiceManager.class, PentahoSessionHolder.getSession());
        try {
            Object targetSessionBean = serviceManager.getServiceBean("gwt","session.rpc");
            Object targetQueryBean = serviceManager.getServiceBean("gwt","query.rpc");
            Object targetDiscoveryBean = serviceManager.getServiceBean("gwt","discovery.rpc");
            Object targetPlatformBean = serviceManager.getServiceBean("gwt","platform.rpc");

            final IPluginManager pluginManager = (IPluginManager) PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession());
            final PluginClassLoader pluginClassloader = (PluginClassLoader)pluginManager.getClassLoader("Pentaho Analysis Tool Plugin");


            // TODO improve error logging
            if (pluginClassloader == null)
                throw new ServiceException("PAT-Plugin classloader can't be loaded");

            URL contextUrl = pluginClassloader.getResource("pat-applicationContext.xml");
            
            
            if ( contextUrl!= null ) {
                String appContextUrl = contextUrl.toString();
                final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { appContextUrl }, false);
                applicationContext.setClassLoader(pluginClassloader);
                applicationContext.setConfigLocation(appContextUrl);

                applicationContext.setAllowBeanDefinitionOverriding(true);

                String hibernateConfigurationFile =  PentahoSystem.getSystemSetting( "hibernate/hibernate-settings.xml","settings/config-file", null);
                String pentahoHibConfigPath = PentahoSystem.getApplicationContext().getSolutionPath(hibernateConfigurationFile);
                final Configuration pentahoHibConfig = new Configuration();
                pentahoHibConfig.configure(new File(pentahoHibConfigPath));

                URL patHibConfigUrl = pluginClassloader.getResource("pat-hibernate.cfg.xml");    
                if(patHibConfigUrl == null)
                    throw new ServiceException("Can't find PAT Hibernate Config");
                else
                    System.out.println("#### HIBERNATE CONFIG:" + patHibConfigUrl.toString());
                final AnnotationConfiguration patHibConfig = new AnnotationConfiguration();
                patHibConfig.configure(patHibConfigUrl);

                applicationContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {

                    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {

                        BasicDataSource dsBean = (BasicDataSource)factory.getBean("dataSource");
                        dsBean.setDriverClassName(pentahoHibConfig.getProperty("connection.driver_class"));
                        dsBean.setUrl(pentahoHibConfig.getProperty("connection.url"));
                        dsBean.setUsername(pentahoHibConfig.getProperty("connection.username"));
                        dsBean.setPassword(pentahoHibConfig.getProperty("connection.password"));

                        SessionFactory sfBean = (SessionFactory)factory.getBean("sessionFactory");
                        LocalSessionFactoryBean lsfBean = (LocalSessionFactoryBean) factory.getBean("&sessionFactory");

                        lsfBean.setBeanClassLoader(pluginClassloader);

                        patHibConfig.getProperties().setProperty("dialect", pentahoHibConfig.getProperty("dialect"));
                        patHibConfig.getProperties().setProperty("connection.url", pentahoHibConfig.getProperty("connection.url"));
                        patHibConfig.getProperties().setProperty("connection.username", pentahoHibConfig.getProperty("connection.username"));
                        patHibConfig.getProperties().setProperty("connection.password", pentahoHibConfig.getProperty("connection.password"));
                        lsfBean.setConfigLocation(null);
                        lsfBean.setHibernateProperties(patHibConfig.getProperties());


                        lsfBean.setDataSource(dsBean);
                        sfBean = (SessionFactory) lsfBean.getObject();
                    }

                });
                applicationContext.refresh();
                Thread.currentThread().setContextClassLoader(pluginClassloader);

                ((SessionServlet)targetSessionBean).setStandalone(true);
                SessionServlet.setApplicationContext(applicationContext);
                ((SessionServlet)targetSessionBean).init();

                ((QueryServlet)targetQueryBean).setStandalone(true);
                QueryServlet.setApplicationContext(applicationContext);
                ((QueryServlet)targetQueryBean).init();

                ((DiscoveryServlet)targetDiscoveryBean).setStandalone(true);
                DiscoveryServlet.setApplicationContext(applicationContext);
                ((DiscoveryServlet)targetDiscoveryBean).init();

                ((PlatformServlet)targetPlatformBean).setStandalone(true);
                PlatformServlet.setApplicationContext(applicationContext);
                ((PlatformServlet)targetPlatformBean).init();

                List<MondrianCatalog> catalogs = MondrianCatalogHelper.getInstance().listCatalogs(PentahoSessionHolder.getSession(), true);
                String pentahoXmlaUrl = "";
                if ( catalogs.size() > 0 ) {
                    // DEBUG MSG
                    System.out.println("PENTAHO XMLA URL: " + catalogs.get(0).getDataSource().getUrl());
                    pentahoXmlaUrl = catalogs.get(0).getDataSource().getUrl();
                    CubeConnection cc = new CubeConnection();
                    cc.setId("automatic-pentaho-connection-1234");
                    cc.setName("Automatic Pentaho XMLA");
                    cc.setUrl(pentahoXmlaUrl);
                    cc.setConnectionType(CubeConnection.ConnectionType.XMLA);
                    cc.setConnectOnStartup(true);

                    if (((SessionServlet)targetSessionBean).getConnection("1234", cc.getId()) == null) {
                        String defaultConId = ((SessionServlet)targetSessionBean).saveConnection("1234", cc);
                        System.out.println("##### CONNECTION SAVED");
                    }
                    else
                        System.out.println("#### automatic connection already saved");


                }


            }
            else
            {
                // TODO improve error logging
                System.out.println("[ERROR] PAT Application Context could not be found");
            }

            // TODO improve error logging
        } catch (ServiceException e1) { 
            e1.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unLoaded() throws PluginLifecycleException {
        // TODO implement plugin unload only when needed
    }

}
