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
import org.pentaho.pat.rpc.exceptions.RpcException;
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

	public void clearSelection(String sessionId, String dimensionName, 
			List<String> memberNames) 
	{
		this.queryService.clearSelection(getCurrentUserId(), 
				sessionId, dimensionName, memberNames);
	}

	public void createSelection(String sessionId, String dimensionName,
			List<String> memberNames, String selectionType) throws RpcException {
		try {
            this.queryService.createSelection(getCurrentUserId(), sessionId, 
            		dimensionName, memberNames, org.olap4j.query.Selection.Operator.valueOf(selectionType));
        } catch (OlapException e) {
            throw new RpcException("Unable to perform the members selection.",e);
        }
	}

	public void moveDimension(String sessionId, Axis axis, 
			String dimensionName) 
	{
		this.queryService.moveDimension(
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
