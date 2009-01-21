/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.query.Selection.Operator;
import org.pentaho.pat.rpc.Query;
import org.pentaho.pat.server.services.QueryService;

/**
 * @author luc Boudreau
 *
 */
public class QueryServlet extends GenericServlet implements Query {

	private static final long serialVersionUID = 1L;
	
	private QueryService queryService;

	public Boolean clearSelection(String dimensionName, List<String> memberNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean createSelection(String dimensionName,
			List<String> memberNames, Operator selectionType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean moveDimension(Axis axis, String dimensionName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setQueryService(QueryService service) {
		this.queryService = service;
	}
	
}
