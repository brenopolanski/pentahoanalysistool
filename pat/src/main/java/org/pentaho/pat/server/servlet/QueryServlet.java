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

import com.mysql.jdbc.Messages;

/**
 * @author luc Boudreau
 *
 */
public class QueryServlet extends AbstractServlet implements Query {

	private static final long serialVersionUID = 1L;
	
	private QueryService queryService;
	
	public void init() throws ServletException {
		super.init();
		queryService = (QueryService)applicationContext.getBean("queryService"); //$NON-NLS-1$
		if (queryService==null)
		    throw new ServletException(Messages.getString("Servlet.QueryServiceNotFound")); //$NON-NLS-1$
	}

	public void clearSelection(String sessionId, String dimensionName, 
			List<String> memberNames) throws RpcException
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
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
	}

	public void moveDimension(String sessionId, Axis axis, 
			String dimensionName) throws RpcException
	{
		this.queryService.moveDimension(
			getCurrentUserId(), 
			sessionId, 
			(axis.equals(Axis.UNUSED))?null:org.olap4j.Axis.Standard.valueOf(axis.name()), 
			dimensionName);
	}

	public OlapData executeQuery(String sessionId) throws RpcException 
	{
		try {
			return this.queryService.executeQuery(getCurrentUserId(), sessionId);
		} catch (OlapException e) {
			throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
		}
	}
}
