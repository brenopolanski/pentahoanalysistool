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
import org.pentaho.pat.rpc.dto.Selection;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.messages.Messages;
import org.pentaho.pat.server.services.QueryService;
import org.pentaho.pat.server.services.SessionService;


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

	public String createNewQuery(String sessionId, String connectionId, String cubeName) throws RpcException
    {
        try {
            return queryService.createNewQuery(getCurrentUserId(), sessionId, connectionId, cubeName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantCreateQuery"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantCreateQuery"), e); //$NON-NLS-1$
        }
    }

    public void deleteQuery(String sessionId, String queryId) throws RpcException
    {
        queryService.releaseQuery(getCurrentUserId(), sessionId, queryId);
    }

    public String[] getQueries(String sessionId) throws RpcException
    {
        List<String> list = queryService.getQueries(getCurrentUserId(), sessionId);
        return list.toArray(new String[list.size()]);
    }

    public void clearSelection(
            String sessionId,
            String queryId,
            String dimensionName, 
            List<String> memberNames) throws RpcException
	{
		this.queryService.clearSelection(getCurrentUserId(), 
				sessionId, queryId, dimensionName, memberNames);
	}

    public void createSelection(
            String sessionId,
            String queryId,
            String dimensionName, 
            List<String> memberNames, 
            String selectionType) throws RpcException
    {
		try {
            this.queryService.createSelection(getCurrentUserId(), sessionId, 
            		queryId, dimensionName, memberNames, org.olap4j.query.Selection.Operator.valueOf(selectionType));
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantSelectMembers"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantSelectMembers")); //$NON-NLS-1$
        }
	}

    public String[][] getSelection(
            String sessionId,
            String queryId,
            String dimensionName) throws RpcException
    {
		try {
           return this.queryService.getSelection(getCurrentUserId(), sessionId, 
            		queryId, dimensionName);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantGetMembers"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantGetMembers")); //$NON-NLS-1$
        }
		
	}
    public void moveDimension(
            String sessionId,
            String queryId,
            Axis axis, 
            String dimensionName) throws RpcException
	{
		this.queryService.moveDimension(
			getCurrentUserId(), 
			sessionId, 
			queryId,
			(axis.equals(Axis.UNUSED))?null:org.olap4j.Axis.Standard.valueOf(axis.name()), 
			dimensionName);
	}

    public CellDataSet executeQuery(String sessionId, String queryId) throws RpcException
	{
		try {
			return this.queryService.executeQuery(getCurrentUserId(), sessionId, queryId);
		} catch (OlapException e) {
		    log.error(Messages.getString("Servlet.Query.CantExecuteQuery"),e); //$NON-NLS-1$
			throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
		}
	}
	
	// TODO is this the way we want mdx to work?
    public CellDataSet executeMdxQuery(String sessionId, String connectionId, String mdx) throws RpcException
	{
		try {
			return this.queryService.executeMdxQuery(getCurrentUserId(), sessionId, connectionId, mdx);
		} catch (OlapException e) {
		    log.error(Messages.getString("Servlet.Query.CantExecuteQuery"),e); //$NON-NLS-1$
			throw new RpcException(Messages.getString("Servlet.Query.CantExecuteQuery")); //$NON-NLS-1$
		}
	}

    public String getMdxForQuery(String sessionId, String queryId)
            throws RpcException {
        try {
            return this.queryService.getMdxForQuery(getCurrentUserId(), sessionId, queryId);
        } catch (OlapException e) {
            log.error(Messages.getString("Servlet.Query.CantFetchMdx"),e); //$NON-NLS-1$
            throw new RpcException(Messages.getString("Servlet.Query.CantFetchMdx")); //$NON-NLS-1$
        }
    }

}
