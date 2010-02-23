package org.pentaho.pat.client.util.dnd;

import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.rpc.dto.IAxis;

public interface FlexTableUtil {

	public abstract void moveRow(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRoworCol, final boolean isSourceRow, final int targetRow, final int targetCol, final IAxis targetAxis);
	
	public abstract void copyRow(final DimensionFlexTable sourceTable, final DimensionFlexTable targetTable,
            final int sourceRow, final int targetRow);
}