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
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResolverUtil<T> {

    private static final Log log = LogFactory.getLog("pat"); //$NON-NLS-1$

    public static interface Test {

        boolean matches(Class<?> type);
    }

    public static class IsA implements Test {

        private Class<?> parent;

        public IsA(Class<?> parentType) {
            super();
            this.parent = parentType;
        }

        public boolean matches(Class<?> type) {
            return type != null && parent.isAssignableFrom(type);
        }

        @Override()
        public String toString() {
            return "is assignable to " + parent.getSimpleName(); //$NON-NLS-1$
        }
    }

    public static class NameEndsWith implements Test {

        private String suffix;

        public NameEndsWith(String suffix) {
            super();
            this.suffix = suffix;
        }

        public boolean matches(Class<?> type) {
            return type != null && type.getName().endsWith(suffix);
        }

        @Override()
        public String toString() {
            return "ends with the suffix " + suffix; //$NON-NLS-1$
        }
    }

    public static class AnnotatedWith implements Test {

        private Class<? extends Annotation> annotation;

        public AnnotatedWith(Class<? extends Annotation> annotation) {
            super();
            this.annotation = annotation;
        }

        public boolean matches(Class<?> type) {
            return type != null && type.isAnnotationPresent(annotation);
        }

        @Override()
        public String toString() {
            return "annotated with @" + annotation.getSimpleName(); //$NON-NLS-1$
        }
    }

    private Set<Class<? extends T>> matches = new HashSet<Class<? extends T>>();

    private ClassLoader classloader;

    public int size() {
        return matches.size();
    }

    public Set<Class<? extends T>> getClasses() {
        return matches;
    }

    public ClassLoader getClassLoader() {
        return classloader == null ? Thread.currentThread().getContextClassLoader() : classloader;
    }

    public void setClassLoader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public void findImplementations(Class<?> parent, String... packageNames) {
        if (packageNames == null)
            return;
        Test test = new IsA(parent);
        for (String pkg : packageNames) {
            findInPackage(pkg, test);
        }
    }

    public void findSuffix(String suffix, String... packageNames) {
        if (packageNames == null)
            return;
        Test test = new NameEndsWith(suffix);
        for (String pkg : packageNames) {
            findInPackage(pkg, test);
        }
    }

    public void findAnnotated(Class<? extends Annotation> annotation, String... packageNames) {
        if (packageNames == null)
            return;
        Test test = new AnnotatedWith(annotation);
        for (String pkg : packageNames) {
            findInPackage(pkg, test);
        }
    }

    public void find(Test[] test, String... packageNames) {
        if (packageNames == null)
            return;
        for (String pkg : packageNames) {
            findInPackage(pkg, test);
        }
    }

    public void find(Test test, String... packageNames) {
        find(new Test[] {test}, packageNames);
    }

    public void findInPackage(String packageName, Test... tests) {
        packageName = packageName.replace('.', '/');
        ClassLoader loader = getClassLoader();
        Enumeration<URL> urls;
        try {
            urls = loader.getResources(packageName);
        } catch (IOException ioe) {
            log.trace("Could not read package: " + packageName, ioe); //$NON-NLS-1$
            return;
        }
        while (urls.hasMoreElements()) {
            try {
                URL eurl = urls.nextElement();
                String urlPath = eurl.toURI().toString();

                if (urlPath.indexOf('!') > 0) {
                    urlPath = urlPath.substring(0, urlPath.indexOf('!'));
                    if (urlPath.startsWith("jar:")) //$NON-NLS-1$
                        urlPath = urlPath.substring(4);
                    eurl = new URL(urlPath);
                }
                log.trace("Scanning for classes in [" + urlPath //$NON-NLS-1$
                        + "] matching criteria: " + tests); //$NON-NLS-1$

                // is it a file?
                File file = new File(URLDecoder.decode(eurl.getFile(), "UTF-8")); //$NON-NLS-1$
                // File file = new File(eurl.getFile());
                if (file.exists() && file.isDirectory()) {
                    loadImplementationsInDirectory(packageName, file, tests);
                } else {
                    loadImplementationsInJar(packageName, eurl, tests);
                }
            } catch (IOException e) {
                log.trace("could not read entries", e); //$NON-NLS-1$
            } catch (URISyntaxException se) {
                log.trace("could not read entries", se); //$NON-NLS-1$
            }
        }
    }

    public void loadImplementationsInDirectory(String parent, File location, Test... tests) {
        File[] files = location.listFiles();
        StringBuilder builder = null;
        for (File file : files) {
            builder = new StringBuilder(100);
            builder.append(parent).append("/").append(file.getName()); //$NON-NLS-1$
            String packageOrClass = (parent == null ? file.getName() : builder.toString());
            if (file.isDirectory()) {
                loadImplementationsInDirectory(packageOrClass, file, tests);
            } else if (file.getName().endsWith(".class")) { //$NON-NLS-1$
                addIfMatching(packageOrClass, tests);
            }
        }
    }

    public void loadImplementationsInJar(String parent, URL jarfile, Test... tests) {
        JarInputStream jarStream = null;
        try {
            JarEntry entry;
            jarStream = new JarInputStream(jarfile.openStream());
            while ((entry = jarStream.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (!entry.isDirectory() && name.startsWith(parent) && name.endsWith(".class")) { //$NON-NLS-1$
                    addIfMatching(name, tests);
                }
            }
        } catch (IOException ioe) {
            log.trace("Could not search jar file \\\'" + jarfile //$NON-NLS-1$
                    + "\\\' for classes matching criteria: " + tests //$NON-NLS-1$
                    + " due to an IOException", ioe); //$NON-NLS-1$
        } finally {
            if (jarStream != null)
                try {
                    jarStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @SuppressWarnings("unchecked")
    protected void addIfMatching(String fqn, Test... tests) {
        try {
            String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
            ClassLoader loader = getClassLoader();

            Class<?> type = loader.loadClass(externalName);

            for (Test test : tests) {
                if (test.matches(type)) {
                    matches.add((Class<T>) type);
                    if (log.isDebugEnabled()) {
                        log.info("Found JDBC driver :" + type.getName()); //$NON-NLS-1$
                    }
                }
            }
        } catch (Throwable t) {
            //      log.trace("Could not examine class \\\'" + fqn + "\\\' due to a " //$NON-NLS-1$ //$NON-NLS-2$
            //          + t.getClass().getName() + " with message: " //$NON-NLS-1$
            // + t.getMessage());
        }
    }
}
