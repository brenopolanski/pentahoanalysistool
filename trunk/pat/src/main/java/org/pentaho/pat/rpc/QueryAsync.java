package org.pentaho.pat.rpc;

import java.util.List;

import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.exceptions.RpcException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This interface defines the operations permitted on a query
 * object that was previously instanciated through the Session service.
 * @author Luc Boudreau
 */
@SuppressWarnings("unchecked")
public interface QueryAsync {
	
    public void moveDimension(
            String sessionId,
            String queryId,
            Axis axis, 
            String dimensionName,
            AsyncCallback callback);
	
	public void createSelection(
	        String sessionId,
	        String queryId,
	        String dimensionName, 
	        List<String> memberNames, 
	        String selectionType,
	        AsyncCallback callback);	
	
	
	public void clearSelection(
	        String sessionId,
	        String queryId,
	        String dimensionName, 
	        List<String> memberNames,
	        AsyncCallback callback);
	
	public void executeQuery(
	        String sessionId, 
            String queryId,
            AsyncCallback<CellDataSet> callback);
	
	// TODO is this the way we want mdx to work?
	public void executeMdxQuery(
	        String sessionId, 
            String connectionId, 
            String mdx,
			AsyncCallback<CellDataSet> callback);
	
	public void createNewQuery(String sessionId, String connectionId, String cubeName, AsyncCallback<String> callback);
    
    public void getQueries(String sessionId, AsyncCallback<String[]> callback);

    public void deleteQuery(String sessionId, String queryId, AsyncCallback callback);
    
    public void getMdxForQuery(
            String sessionId, 
            String queryId,
            AsyncCallback<String> callback) throws RpcException;
}
