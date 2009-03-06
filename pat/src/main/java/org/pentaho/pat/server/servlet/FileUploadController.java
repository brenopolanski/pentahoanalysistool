package org.pentaho.pat.server.servlet;
import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.pentaho.pat.server.beans.FileUploadBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

public class FileUploadController extends AbstractCommandController implements
ResourceLoaderAware, InitializingBean {
  
	      

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
             if (this.resourceLoader == null)
                     throw new Exception("A resourceLoader is required.");
             if (this.basedir == null)
                     throw new Exception("A basedir is required.");
             try {
                     this.schemaDirectory = resourceLoader.getResource(basedir)
                                     .getFile();
             } catch (Exception e) {
                     throw new RuntimeException(e);
             }
     }


	        protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {  
	            
	        	try {
	        		
	        	FileUploadBean fileUploadBean = (FileUploadBean) command;  
	            MultipartFile files = fileUploadBean.getFile();
	            String tmpFileName = String.valueOf(UUID.randomUUID());
	            this.schemaDirectory.mkdir();
	            File mvt = new File(this.schemaDirectory,tmpFileName);
	            files.transferTo(mvt);
	            // System.out.println("Uploaded file:" + mvt.getAbsolutePath());
	            
                response.getWriter().print("#filename#" + mvt.toString() + "#/filename#");
                response.setStatus(HttpServletResponse.SC_OK);


	        	}
	        	catch (Exception e) {
	        	    log.error(e.getMessage());
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
	        	
                // return the result to the client
	        	
                response.getWriter().flush();

	           return null;  
	       }  
	     
	       // this method is overriding, and specify how spring convert multipart into a byte array that binds to our command class  
	       protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {  
	           binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());  
	           super.initBinder(request, binder);  
	       }  
}  