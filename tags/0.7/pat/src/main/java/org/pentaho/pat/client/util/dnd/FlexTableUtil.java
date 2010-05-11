package org.pentaho.pat.client.util.dnd;

import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.rpc.dto.IAxis;

/**
 * 
 * @created Feb 25, 2009
 * @since 0.6.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public interface FlexTableUtil {

	/**
	 * Move row from dim drop widget a to dim drop widget b.
	 * @param sourceTable
	 * @param targetTable
	 * @param sourceRoworCol
	 * @param isSourceRow
	 * @param targetRow
	 * @param targetCol
	 * @param targetAxis
	 */
	public abstract void moveRow(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRoworCol, final boolean isSourceRow, final int targetRow, final int targetCol, final IAxis targetAxis);
	
	/**
	 * Copy row from dim drop widget a to dim drop widget b.
	 * @param sourceTable
	 * @param targetTable
	 * @param sourceRow
	 * @param targetRow
	 */
	public abstract void copyRow(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRow, final int targetRow);
}