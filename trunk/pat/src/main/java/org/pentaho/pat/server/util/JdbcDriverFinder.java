package org.pentaho.pat.server.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

public class JdbcDriverFinder implements InitializingBean, ResourceLoaderAware {

    private long interval = 300000; // every five mins

    private long lastCall = 0;

    private List<String> drivers = new ArrayList<String>();

    private List<String> jdbcDriverPath = null;

    private File jdbcDriverDirectory = null;

    private ResourceLoader resourceLoader = null;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jdbcDriverPath);
        Assert.notNull(resourceLoader);

        for (String currentPath : this.jdbcDriverPath)
        {
            Resource res = resourceLoader.getResource(currentPath);

            if (res == null || !res.exists())
                continue;

            File currentFile = res.getFile();

            if (!currentFile.isDirectory()
                    || !currentFile.canRead())
                continue;
            
            this.jdbcDriverDirectory=currentFile;
            break;
        }
        if (this.jdbcDriverDirectory==null)
          throw new Exception("The specified JDBC directory path doesn't exist.");
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void registerDrivers() {

        long timeMil = System.currentTimeMillis();

        if ((timeMil - this.lastCall) > interval) {
            this.scan(timeMil);
        }

        for (String driverName : this.drivers) {
            try {
                Class.forName(driverName);
            } catch (Exception e) {
                // Nothing.
            }
        }
    }

    private void scan(long timeMil) {

        FilenameFilter libs = new FilenameFilter() {

            public boolean accept(File dir, String pathname) {
                return pathname.endsWith("jar") || pathname.endsWith("zip"); //$NON-NLS-1$//$NON-NLS-2$
            }

        };

        ResolverUtil<Driver> resolver = new ResolverUtil<Driver>();

        for (File lib : this.jdbcDriverDirectory.listFiles(libs)) {
            try {
                resolver.loadImplementationsInJar("", lib.toURI().toURL(), //$NON-NLS-1$
                        new ResolverUtil.IsA(Driver.class));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                continue;
            }
        }

        List<String> drivers = new ArrayList<String>();
        for (Class<? extends Driver> cd : resolver.getClasses())
            drivers.add(cd.getName());

        this.lastCall = timeMil;
        this.drivers = drivers;
    }

    public void setJdbcDriverPath(List<String> jdbcDriverPathList) {
        this.jdbcDriverPath = jdbcDriverPathList;
    }

    public void setCacheTimeout(Long timeoutDelay) {
        this.interval = timeoutDelay;
    }
}