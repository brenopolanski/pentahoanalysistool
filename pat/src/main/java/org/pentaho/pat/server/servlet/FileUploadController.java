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
package org.pentaho.pat.server.servlet;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pentaho.pat.server.beans.FileUploadBean;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.util.Base64Coder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

public class FileUploadController extends AbstractCommandController implements ResourceLoaderAware, InitializingBean,
        ApplicationContextAware {

    private static final String DATA_START = "[SCHEMA_START]"; //$NON-NLS-1$

    private static final String DATA_END = "[/SCHEMA_END]"; //$NON-NLS-1$

    Logger log = Logger.getLogger(this.getClass());

    private String basedir = null;

    private File schemaDirectory = null;

    private ResourceLoader resourceLoader;

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void afterPropertiesSet() throws Exception {

        Assert.notNull(this.resourceLoader);
        Assert.notNull(this.basedir);

        try {
            this.schemaDirectory = resourceLoader.getResource(basedir).getFile();
            if (!this.schemaDirectory.isDirectory()) {
                if (!this.schemaDirectory.mkdir()) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            log.warn(Messages.getString("Servlet.FileUpload.CantCreateTempDirectory"));//$NON-NLS-1$
            try {
                File tempDirectory = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
                this.schemaDirectory = new File(tempDirectory, basedir);
                if (!this.schemaDirectory.isDirectory()) {
                    if (!this.schemaDirectory.mkdir()) {
                        throw new Exception();
                    }
                }
            } catch (Exception e2) {
                log.error(Messages.getString("Servlet.FileUpload.CantCreateTempDirectory2"), e); //$NON-NLS-1$
                throw new RuntimeException(Messages.getString("Servlet.FileUpload.CantCreateTempDirectory2")); //$NON-NLS-1$
            }
        }
    }

    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
        FileUploadBean fileUploadBean = (FileUploadBean) command;

        try {
            MultipartFile files = fileUploadBean.getFile();
            // String schemaData = new String(files.getBytes());

            // Send a confirmation message to the client
            response.setContentType("text/plain"); //$NON-NLS-1$
            response.getWriter().print(DATA_START + (new String(Base64Coder.encode(files.getBytes()))) + DATA_END);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        // return the result to the client
        response.getWriter().flush();

        return null;
    }

    // this method is overriding, and specify how spring convert multipart into
    // a byte array that binds to our command class
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        super.initBinder(request, binder);
    }
}