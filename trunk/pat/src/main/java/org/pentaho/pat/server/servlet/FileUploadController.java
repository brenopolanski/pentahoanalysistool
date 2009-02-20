package org.pentaho.pat.server.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.annotation.Secured;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class FileUploadController extends AbstractController implements
		ResourceLoaderAware, InitializingBean {

	Logger log = Logger.getLogger(this.getClass());
	
	private String basedir = null;
	private File schemaDirectory = null;
	private ResourceLoader resourceLoader;

	public void setBasedir(String basedir) {
		this.basedir = basedir;
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

	@SuppressWarnings("unchecked")
	@Override
	@Secured( {"ROLE_USER"} )
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		try 
		{
			String tmpFileName = String.valueOf(UUID.randomUUID());
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = upload.parseRequest(request);
			Iterator it = items.iterator();

			while (it.hasNext()) {

				FileItem item = (FileItem) it.next();
				
				if (!item.isFormField()) 
				{
					byte[] data = item.get();
					
					// TODO make schema validation work.
//					// validate the file contents
//					String validationResult = SchemaValidator.validateAgainstXsd(new String(data));
//					if (validationResult==null)
//					{
						// Create the directory if needed
						this.schemaDirectory.mkdir();
						
						// create an accessor for the temp file.
						File tmpFile = new File(this.schemaDirectory, tmpFileName);
						
						// write the file
						FileWriter fw = new FileWriter(tmpFile);
						BufferedWriter out = new BufferedWriter(fw);
						out.write(new String(data));
						out.close();
						
						// append it to the response
						response.getWriter().print(tmpFile.toString());
//					}
//					else
//					{
//						log.error(validationResult);
//						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//					}
				}
			}
			// return a success code
			response.setStatus(HttpServletResponse.SC_OK);
		} 
		catch (Exception e) 
		{
			log.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		// return the result to the client
		response.getWriter().flush();

		return null;
	}

	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
