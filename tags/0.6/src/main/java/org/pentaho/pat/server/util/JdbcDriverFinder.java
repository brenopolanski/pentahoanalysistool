/*
 * Copyright (C) 2009 Luc Boudreau
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
package org.pentaho.pat.server.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.pentaho.pat.server.messages.Messages;
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

    private final List<File> jdbcDriverDirectory = new ArrayList<File>();

    private ResourceLoader resourceLoader = null;

    private boolean preLoad = true;

    public void setPreLoad(final boolean preLoad) {
        this.preLoad = preLoad;
    }

    protected static final Logger LOG = Logger.getLogger(JdbcDriverFinder.class);

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jdbcDriverPath);
        Assert.notNull(resourceLoader);

        for (String currentPath : this.jdbcDriverPath) {
            final Resource res = resourceLoader.getResource(currentPath);

            if (res == null || !res.exists()) {
                continue;
            }

            final File currentFile = res.getFile();

            if (!currentFile.isDirectory() || !currentFile.canRead()) {
                continue;
            }

            this.jdbcDriverDirectory.add(currentFile);

            if (preLoad) {
                registerDrivers();
            }
        }
        if (this.jdbcDriverDirectory.size() == 0) {
            LOG.warn(Messages.getString("Util.JdbcDriverFinder.NoDriversInPath")); //$NON-NLS-1$
        }
    }

    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void registerDrivers() {

        final long timeMil = System.currentTimeMillis();

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

    private void scan(final long timeMil) {

        final FilenameFilter libs = new FilenameFilter() {

            public boolean accept(final File dir, final String pathname) {
                return pathname.endsWith("jar") || pathname.endsWith("zip"); //$NON-NLS-1$//$NON-NLS-2$
            }

        };

        final ResolverUtil<Driver> resolver = new ResolverUtil<Driver>();

        for (File directory : this.jdbcDriverDirectory) {
            for (File lib : directory.listFiles(libs)) {
                try {
                    resolver.loadImplementationsInJar("", lib.toURI().toURL(), //$NON-NLS-1$
                            new ResolverUtil.IsA(Driver.class));
                } catch (MalformedURLException e) {
                    LOG.trace(e);
                    continue;
                }
            }
        }
        List<String> drivers = new ArrayList<String>();
        for (Class<? extends Driver> cd : resolver.getClasses()) {
            drivers.add(cd.getName());
        }
        this.lastCall = timeMil;
        this.drivers = drivers;
    }

    public void setJdbcDriverPath(final List<String> jdbcDriverPathList) {
        this.jdbcDriverPath = jdbcDriverPathList;
    }

    public void setCacheTimeout(final Long timeoutDelay) {
        this.interval = timeoutDelay;
    }
}