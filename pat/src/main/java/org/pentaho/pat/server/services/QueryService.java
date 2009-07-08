package org.pentaho.pat.server.services;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.query.Query;
import org.olap4j.query.Selection;
import org.pentaho.pat.rpc.dto.Matrix;
import org.springframework.security.annotation.Secured;

/**
 * This interface defines the operations permitted on a query
 * object.
 * @author Luc Boudreau
 */
public interface QueryService extends Service {

    /**
     * Creates a new query for a given session.
     * @param userId The owner of the query to create.
     * @param sessionId The session id into which we want to create a new query.
     * @return A unique query identification number.
     * @throws OlapException If creating the query fails.
     */
    @Secured ({"ROLE_USER"})
    public String createNewQuery(String userId, String sessionId) throws OlapException;
    
    /**
     * Creates a new query for a given session using a supplied MDX query.
     * @param userId The owner of the query to create.
     * @param sessionId The session id into which we want to create a new query.
     * @param mdx The initial MDX to initialize the query object with.
     * @return A unique query identification number.
     * @throws OlapException If creating the query fails or the supplied MDX cannot
     * be parsed as a Query object.
     */
    @Secured ({"ROLE_USER"})
    public String createNewQuery(String userId, String sessionId, String mdx) throws OlapException;
    
    @Secured ({"ROLE_USER"})
    Query getQuery(String userId, String sessionId, String queryId);
    
    /**
     * Returns a list of the currently created queries inside a
     * given session.
     * @param userId The owner of the session and queries. 
     * @param sessionId The unique id of the session for which we want the current opened queries.
     * @return A list of query names.
     */
    @Secured ({"ROLE_USER"})
    public List<String> getQueries(String userId, String sessionId);
    
    /**
     * Releases and closes a query inside a given session.
     * @param userId The owner of the query.
     * @param sessionId The unique session id for which we want to close and delete a query.
     * @param queryId The unique id of the query to close and release.
     */
    @Secured ({"ROLE_USER"})
    public void releaseQuery(String userId, String sessionId, String queryId);

    /**
     * Moves a dimension from an axis to another.
     * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
     * @param axis The axis into which we want the dimension to be put. 
     * Null to remove it from the current query.
     * @param dimensionName The name of the dimension to move.
     */
	@Secured ({"ROLE_USER"})
	public void moveDimension(
		String userId, 
		String sessionId,
		Axis.Standard axis, 
		String dimensionName);
		

	/**
	 * Creates a selection of members on a given dimension.
	 * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
	 * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
	 * @param dimensionName The name of the dimension onto which to
	 * apply the selection.
	 * @param memberNames A list of member names to select.
	 * @param selectionType The type of selection to perform.
	 * @throws OlapException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public void createSelection(
		String userId, 
		String sessionId,
		String dimensionName, 
		List<String> memberNames, 
		Selection.Operator selectionType)
	    throws OlapException;	
	

	/**
	 * Unselects members from a dimension inside a query.
	 * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
	 * @param dimensionName The name of the dimension that contains the
	 * members to deselect.
	 * @param memberNames A list of member names to deselect.
	 */
	@Secured ({"ROLE_USER"})
	public void clearSelection(
		String userId, 
		String sessionId,
		String dimensionName, 
		List<String> memberNames);
	
	/**
	 * Executes a query.
	 * You must first specify onto which query to perform the
     * operation via Session.setCurrentQuery().
     * @param userId The owner of the query.
     * @param sessionId The session id into which the query is stored.
	 * @return The resultset of the query as a OlapData object.
	 * @throws OlapException If something goes sour.
	 */
	@Secured ({"ROLE_USER"})
	public Matrix executeQuery2(String userId, 
			String sessionId) throws OlapException;
}
