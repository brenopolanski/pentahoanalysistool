package org.pentaho.pat.server.servlet;

import javax.servlet.ServletContext;

import org.gwtwidgets.server.spring.GWTSpringController;

public class GenericServlet extends GWTSpringController {

	private static final long serialVersionUID = 1L;

	@Override
	public ServletContext getServletContext() {
		// That's a weird freakin thing but at least it freakin works...
		// I don't know why but GWT returns a null context.
		if (super.getServletContext() ==null)
			return super.getServletConfig().getServletContext();
		else
			return super.getServletContext();
	}
}
