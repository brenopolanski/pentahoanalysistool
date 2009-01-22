package org.pentaho.pat.rpc;

import java.util.List;

import org.olap4j.Axis;
import org.olap4j.query.Selection;

/**
 * This interface defines the operations permitted on a query
 * object that was previously instanciated through the Session service.
 * @author Luc Boudreau
 */
public interface Query {
	
	/**
	 * Moves a dimension to a different axis.
	 * @param axis The destination axis.
	 * @param dimensionName The name of the dimension to move.
	 * @return True if all is good.
	 */
	public Boolean moveDimension(
		Axis axis, 
		String dimensionName);
	
	/**
	 * Performs a selection of certain members in a dimension.
	 * @param dimensionName The name of the dimension on which we want to 
	 * select members.
	 * @param memberNames The actual names of the members to perform a 
	 * selection on.
	 * @param selectionType The type of selection to perform.
	 * @return True if all is well.
	 */
	public Boolean createSelection(
		String dimensionName, 
		List<String> memberNames, 
		Selection.Operator selectionType);	
	
	/**
	 * Removes the selection status of members inside a given dimension.
	 * @param dimensionName Name of the dimension that includes the 
	 * members to remove selection status.
	 * @param memberNames The actual member names of which we want to 
	 * remove the selection status.
	 * @return True if all is well.
	 */
	public Boolean clearSelection(
		String dimensionName, 
		List<String> memberNames);
}
