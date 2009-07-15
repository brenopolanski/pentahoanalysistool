/**
 * 
 */
package org.pentaho.pat.server.servlet;

import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.olap4j.OlapException;
import org.pentaho.pat.rpc.Query;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.BaseCell;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.Constants;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.util.Matrix;


/**
 * @author luc Boudreau
 *
 */
public class QueryServlet extends AbstractServlet implements Query {

	private static final long serialVersionUID = 1L;
	
	private transient QueryService queryService;
	
	private transient SessionService sessionService;
	
	private transient Logger log = Logger.getLogger(QueryServlet.class);
	
	public void init() throws ServletException {
		super.init();
		queryService = (QueryService)applicationContext.getBean("queryService"); //$NON-NLS-1$
		sessionService = (SessionService)applicationContext.getBean("sessionService"); //$NON-NLS-1$
		if (queryService==null)
		    throw new ServletException(Messages.getString("Servlet.QueryServiceNotFound")); //$NON-NLS-1$
		if (sessionService==null)
            throw new ServletException(Messages.getString("Servlet.SessionServiceNotFound")); //$NON-NLS-1$
	}

	public String createNewQuery(String sessionId)  throws RpcException
    {
        try {
            return queryService.createNewQuery(getCurrentUserId(), sessionId);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantCreateQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    public void deleteQuery(String sessionId, String queryId) throws RpcException
    {
        queryService.releaseQuery(getCurrentUserId(), sessionId, queryId);
    }

    public String getCurrentQuery(String sessionId) throws RpcException
    {
        return (String)sessionService.getUserSessionVariable(
                getCurrentUserId(), sessionId, Constants.CURRENT_QUERY_NAME);
    }

    public String[] getQueries(String sessionId) throws RpcException
    {
        List<String> list = queryService.getQueries(getCurrentUserId(), sessionId);
        return list.toArray(new String[list.size()]);
    }

    public void setCurrentQuery(String sessionId, String queryId) throws RpcException
    {
        sessionService.saveUserSessionVariable(getCurrentUserId(), 
                sessionId, Constants.CURRENT_QUERY_NAME, queryId);
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
            log.error(Messages.getString("Servlet.Query.CantSelectMembers"),e); //$NON-NLS-1$
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

	public CellDataSet executeQuery2(String sessionId) throws RpcException 
	{
		try {
			return this.queryService.executeQuery2(getCurrentUserId(), sessionId);
		} catch (OlapException e) {
		    log.error(Messages.getString("Servlet.Query.CantExecuteQuery"),e); //$NON-NLS-1$
			throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
		}
	}
}
