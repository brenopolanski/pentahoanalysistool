package org.pentaho.pat.server.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.util.Iterator;

import org.springframework.metadata.commons.CommonsAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class FileUploadController extends AbstractController {

	private File basedir;
	
	public File getBasedir() {
		return basedir;
	}
	public void setBasedir(File basedir) {
		this.basedir = basedir;
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(arg0);
		Iterator it = items.iterator();
		String fn = new Date().toString();
		
		
		while(it.hasNext())
		{
			
			FileItem item = (FileItem)it.next();
			if(!item.isFormField()) {
			byte[] data = item.get();
			
			fn = "" + new Date().getTime();
			if (basedir.mkdir()) System.out.print("Created Schema_temp Directory\n");
			FileWriter fw = new FileWriter(new File(basedir,fn));
			BufferedWriter out = new BufferedWriter(fw);
			
			out.write(new String(data));
			out.close();
			//arg1.getOutputStream().println(fn);
			System.out.print("Uploaded Schema file:" + fn);
			arg1.getWriter().print(fn);
			}
		}
		arg1.setStatus(HttpServletResponse.SC_OK);
		
		
		//arg1.getOutputStream().flush();
		arg1.getWriter().flush();
		return null;
	}

}
