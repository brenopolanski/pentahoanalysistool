/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;


import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Query;
import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.OlapData;
import org.pentaho.pat.server.services.QueryService;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author luc Boudreau
 *
 */
public class QueryServlet extends AbstractServlet implements Query, InitializingBean {

	private static final long serialVersionUID = 1L;
	
	private QueryService queryService;
	
	public void setQueryService(QueryService service) {
		this.queryService = service;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (this.queryService==null)
			throw new Exception("A queryService is required.");
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
			getCurrentUserId(), sessionId, org.olap4j.Axis.Standard.valueOf(axis.getCaption()), dimensionName);
	}

	public OlapData executeQuery(String sessionId) {
		try {
			return this.queryService.executeQuery(getCurrentUserId(), sessionId);
		} catch (OlapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
