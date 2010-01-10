package org.pentaho.pat;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.server.data.pojo.ConnectionType;
import org.pentaho.pat.server.data.pojo.SavedConnection;
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
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianCatalog;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianCatalogHelper;
import org.pentaho.platform.plugin.action.mondrian.catalog.MondrianDataSource;
import org.pentaho.platform.web.servlet.PentahoXmlaServlet;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.parser.MondrianConnectionReadHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
            
            final IPluginManager pluginManager = PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession());
            final ClassLoader pluginClassloader = pluginManager.getClassLoader("Pentaho Analysis Tool Plugin");

            // TODO improve error logging
            if (pluginClassloader == null)
                throw new ServiceException("PAT-Plugin classloader can't be loaded");

            URL contextUrl = pluginClassloader.getResource("pat-applicationContext.xml");

            if ( contextUrl!= null ) {
                String appContextUrl = contextUrl.toString();
                ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { appContextUrl }, false);
                applicationContext.setClassLoader(pluginClassloader);
                applicationContext.setConfigLocation(appContextUrl);
                applicationContext.refresh();
                applicationContext.setAllowBeanDefinitionOverriding(true);

                String hibernateConfigurationFile =  PentahoSystem.getSystemSetting( "hibernate/hibernate-settings.xml","settings/config-file", null);
                String pentahoHibConfigPath = PentahoSystem.getApplicationContext().getSolutionPath(hibernateConfigurationFile);
                final Configuration pentahoHibConfig = new Configuration();
                pentahoHibConfig.configure(new File(pentahoHibConfigPath));

                URL patHibConfigUrl = pluginClassloader.getResource("pat-hibernate.cfg.xml");
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
                        lsfBean.setHibernateProperties(patHibConfig.getProperties());
                        
                        try {
                            lsfBean.afterPropertiesSet();
                        } catch (Exception e) {
                            // TODO improve logging
                            e.printStackTrace();
                        }
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
                
                
                List<MondrianCatalog> catalogs = MondrianCatalogHelper.getInstance().listCatalogs(PentahoSessionHolder.getSession(), true);
                String pentahoXmlaUrl = "";
                if ( catalogs.size() > 0 ) {
                    // DEBUG MSG
                    System.out.println("PENTAHO XMLA URL: " + catalogs.get(0).getDataSource().getUrl());
                    pentahoXmlaUrl = catalogs.get(0).getDataSource().getUrl();
                    CubeConnection cc = new CubeConnection();
                    cc.setName("Automatic Pentaho XMLA");
                    cc.setUrl(pentahoXmlaUrl);
                    cc.setConnectionType(CubeConnection.ConnectionType.XMLA);
                    cc.setConnectOnStartup(true);
                    
                    String defaultConId = ((SessionServlet)targetSessionBean).saveConnection("1234", cc);
                    System.out.println("##### CONNECTION SAVED");
                    
                    
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
