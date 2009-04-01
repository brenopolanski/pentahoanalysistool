/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Query;
import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.OlapData;
import org.pentaho.pat.server.services.QueryService;

/**
 * @author luc Boudreau
 *
 */
public class QueryServlet extends AbstractServlet implements Query {

	private static final long serialVersionUID = 1L;
	
	private QueryService queryService;
	
	public void init() throws ServletException {
		super.init();
		queryService = (QueryService)applicationContext.getBean("queryService");
		if (queryService==null)
		    throw new ServletException("No query service was found in the application context.");
	}

	public Boolean clearSelection(String sessionId, String dimensionName, 
			List<String> memberNames) 
	{
		return this.queryService.clearSelection(getCurrentUserId(), 
				sessionId, dimensionName, memberNames);
	}

	public Boolean createSelection(String sessionId, String dimensionName,
			List<String> memberNames, String selectionType) {
		return this.queryService.createSelection(getCurrentUserId(), sessionId, 
				dimensionName, memberNames, org.olap4j.query.Selection.Operator.valueOf(selectionType));
	}

	public Boolean moveDimension(String sessionId, Axis axis, 
			String dimensionName) 
	{
		return this.queryService.moveDimension(
			getCurrentUserId(), sessionId, org.olap4j.Axis.valueOf(axis.getCaption()), dimensionName);
	}

	public OlapData executeQuery(String sessionId) {
		try {
			return this.queryService.executeQuery(getCurrentUserId(), sessionId);
		} catch (OlapException e) {
			log.error("There was an error while executing the query.", e);
			return null;
		}
	}
}
