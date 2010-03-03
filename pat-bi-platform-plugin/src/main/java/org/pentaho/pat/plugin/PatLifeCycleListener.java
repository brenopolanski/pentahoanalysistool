/*
 * Copyright (C) 2010 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */

package org.pentaho.pat.plugin;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.pentaho.pat.plugin.messages.Messages;
import org.pentaho.pat.plugin.util.PluginConfig;
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
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class PatLifeCycleListener implements IPluginLifecycleListener {

    private static final String PAT_PLUGIN_NAME = PluginConfig.PAT_PLUGIN_NAME;
    private static final String PAT_APP_CONTEXT = "pat-applicationContext.xml";
    private static final String PAT_HIBERNATE_CONFIG = "pat-hibernate.cfg.xml";

    private final static Log LOG = LogFactory.getLog(PatLifeCycleListener.class);

    private SessionServlet sessionBean = null;
    private QueryServlet queryBean = null;
    private DiscoveryServlet discoveryBean = null;
    private PlatformServlet platformBean = null;


    public void init() throws PluginLifecycleException {

    }

    public void loaded() throws PluginLifecycleException {
        ClassLoader origContextClassloader = Thread.currentThread().getContextClassLoader();
        IServiceManager serviceManager = (IServiceManager) PentahoSystem.get(IServiceManager.class, PentahoSessionHolder.getSession());
        try {
            sessionBean = (SessionServlet) serviceManager.getServiceBean("gwt","session.rpc"); //$NON-NLS-1$
            queryBean = (QueryServlet) serviceManager.getServiceBean("gwt","query.rpc"); //$NON-NLS-1$
            discoveryBean = (DiscoveryServlet) serviceManager.getServiceBean("gwt","discovery.rpc"); //$NON-NLS-1$
            platformBean = (PlatformServlet) serviceManager.getServiceBean("gwt","platform.rpc"); //$NON-NLS-1$

            final IPluginManager pluginManager = (IPluginManager) PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession());
            final PluginClassLoader pluginClassloader = (PluginClassLoader)pluginManager.getClassLoader(PAT_PLUGIN_NAME);
            final String hibernateConfigurationFile =  PentahoSystem.getSystemSetting( "hibernate/hibernate-settings.xml","settings/config-file", null); //$NON-NLS-1$
            final String pentahoHibConfigPath = PentahoSystem.getApplicationContext().getSolutionPath(hibernateConfigurationFile);

            if (pluginClassloader == null)
                throw new ServiceException(Messages.getString("LifeCycleListener.NoPluginClassloader")); //$NON-NLS-1$

            Thread.currentThread().setContextClassLoader(pluginClassloader);
            final URL contextUrl = pluginClassloader.getResource(PAT_APP_CONTEXT); 
            final URL patHibConfigUrl = pluginClassloader.getResource(PAT_HIBERNATE_CONFIG);    
            if(patHibConfigUrl == null)
                throw new ServiceException("Can't find PAT Hibernate Config");
            else
                LOG.debug(PAT_PLUGIN_NAME + ": PAT Hibernate Config:" + patHibConfigUrl.toString());

            if ( contextUrl!= null ) {
                String appContextUrl = contextUrl.toString();
                final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { appContextUrl }, false);
                applicationContext.setClassLoader(pluginClassloader);
                applicationContext.setConfigLocation(appContextUrl);
                applicationContext.setAllowBeanDefinitionOverriding(true);

                final Configuration pentahoHibConfig = new Configuration();
                pentahoHibConfig.configure(new File(pentahoHibConfigPath));

                final AnnotationConfiguration patHibConfig = new AnnotationConfiguration();
                patHibConfig.configure(patHibConfigUrl);

                applicationContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {

                    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
                        factory.setBeanClassLoader(pluginClassloader);
                        factory.setTempClassLoader(pluginClassloader);

                        BasicDataSource dsBean = (BasicDataSource)factory.getBean("dataSource");                        
                        dsBean.setDriverClassName(pentahoHibConfig.getProperty("connection.driver_class"));
                        dsBean.setUrl(pentahoHibConfig.getProperty("connection.url"));
                        dsBean.setUsername(pentahoHibConfig.getProperty("connection.username"));
                        dsBean.setPassword(pentahoHibConfig.getProperty("connection.password"));

                        patHibConfig.getProperties().setProperty("dialect", pentahoHibConfig.getProperty("dialect"));
                        patHibConfig.getProperties().setProperty("connection.url", pentahoHibConfig.getProperty("connection.url"));
                        patHibConfig.getProperties().setProperty("connection.username", pentahoHibConfig.getProperty("connection.username"));
                        patHibConfig.getProperties().setProperty("connection.password", pentahoHibConfig.getProperty("connection.password"));

                        LocalSessionFactoryBean lsfBean = (LocalSessionFactoryBean) factory.getBean("&sessionFactory");
                        lsfBean.setBeanClassLoader(pluginClassloader);
                        lsfBean.setConfigLocation(null);
                        lsfBean.setHibernateProperties(patHibConfig.getProperties());
                        lsfBean.setDataSource(dsBean);

                        Object sfBean = factory.getBean("sessionFactory");
                        sfBean = lsfBean.getObject();
                    }

                });
                applicationContext.setClassLoader(pluginClassloader);
                applicationContext.refresh();


                sessionBean.setStandalone(true);
                SessionServlet.setApplicationContext(applicationContext);
                sessionBean.init();

                queryBean.setStandalone(true);
                QueryServlet.setApplicationContext(applicationContext);
                queryBean.init();

                discoveryBean.setStandalone(true);
                DiscoveryServlet.setApplicationContext(applicationContext);
                discoveryBean.init();

                platformBean.setStandalone(true);
                PlatformServlet.setApplicationContext(applicationContext);
                platformBean.init();

                injectPentahoXmlaUrl();
            }
            else
            {
                throw new Exception(Messages.getString("LifeCycleListener.AppContextNotFound"));
            }

        } catch (ServiceException e1) { 
            LOG.error(e1.getMessage(),e1);
        } catch (ServletException e) {
            LOG.error(e.getMessage(),e);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        } finally {
            // reset the classloader of the current thread
            if (origContextClassloader != null) {
                Thread.currentThread().setContextClassLoader(origContextClassloader);
            }
        }
    }

    public void unLoaded() throws PluginLifecycleException {
        // TODO implement plugin unload only when needed
    }

    private void injectPentahoXmlaUrl() throws Exception {
        List<MondrianCatalog> catalogs = MondrianCatalogHelper.getInstance().listCatalogs(PentahoSessionHolder.getSession(), true);
        String pentahoXmlaUrl = "";
        if ( catalogs.size() > 0 ) {
            LOG.debug(PAT_PLUGIN_NAME + ": PENTAHO XMLA URL: " + catalogs.get(0).getDataSource().getUrl());
            pentahoXmlaUrl = catalogs.get(0).getDataSource().getUrl();
            CubeConnection cc = new CubeConnection();
            cc.setId("automatic-pentaho-connection-1234");
            cc.setName("Automatic Pentaho XMLA");
            cc.setUrl(pentahoXmlaUrl);
            cc.setConnectionType(CubeConnection.ConnectionType.XMLA);
            cc.setConnectOnStartup(true);

            if (sessionBean.getConnection("1234", cc.getId()) == null) {
                sessionBean.saveConnection("1234", cc);
                LOG.debug(PAT_PLUGIN_NAME + ": Automatic Pentaho Xmla connection saved");
            }
            else
                LOG.debug(PAT_PLUGIN_NAME + ": Automatic Pentaho Xmla connection was already saved");


        }

    }

}
