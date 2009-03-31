package org.pentaho.pat.server.servlet;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.pentaho.pat.server.beans.FileUploadBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

public class FileUploadController extends AbstractCommandController implements
		ResourceLoaderAware, InitializingBean {
	
//	private static final String FILENAME_TAG_START = "#filename#";
//	private static final String FILENAME_TAG_END = "#/filename#";
//	
	private static final String FILENAME_TAG_START = "pat_schema_filename_start";
	private static final String FILENAME_TAG_END = "pat_schema_filename_end";
	
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
		
		Assert.notNull(this.resourceLoader, "A resourceLoader is required.");
		Assert.notNull(this.basedir, "A basedir is required.");
		
		try {
			this.schemaDirectory = resourceLoader.getResource(basedir).getFile();
			this.schemaDirectory.mkdir();
		} catch (Exception e) {
			log
					.warn("Unable to create the schema directory within the application directory. The application might be contained in a non expanded war file. Will try to store schemas in the java.io.tmpdir instead.");
			try {
				File tempDirectory = new File(System.getProperty("java.io.tmpdir"));				
				this.schemaDirectory = new File(tempDirectory,basedir);
				this.schemaDirectory.mkdir();
			} catch (Exception e2) {
				throw new RuntimeException(
						"Unable to create a directory to store the temporary schema files!!");
			}
		}
	}

	protected ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		try {

			FileUploadBean fileUploadBean = (FileUploadBean) command;
			MultipartFile files = fileUploadBean.getFile();
			String tmpFileName = String.valueOf(UUID.randomUUID());
			File mvt = new File(this.schemaDirectory, tmpFileName);
			files.transferTo(mvt);

			// TODO find a better way to return filename to client
			response.setContentType("text/plain");
			response.getWriter().print(
					FILENAME_TAG_START + mvt.toString() + FILENAME_TAG_END);
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
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		super.initBinder(request, binder);
	}
}