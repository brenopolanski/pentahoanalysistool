package org.pentaho.pat.client.util;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.rpc.dto.celltypes.CellInfo;

/**
 * The Class OlapUtils.
 */
public class OlapUtils {
	
	/**
	 * Extract column.
	 * 
	 * @param cellInfoGrid the cell info grid
	 * @param column the column
	 * 
	 * @return the cell info[]
	 */
	public static CellInfo[] extractColumn(final CellInfo[][] cellInfoGrid, final int column) {
		final CellInfo[] values = new CellInfo[cellInfoGrid.length];
		for (int row = 0; row < cellInfoGrid.length; row++) {
			values[row] = cellInfoGrid[row][column];
		}
		return values;
	}

	/**
	 * Extract row.
	 * 
	 * @param cellInfoGrid the cell info grid
	 * @param row the row
	 * 
	 * @return the cell info[]
	 */
	public static CellInfo[] extractRow(final CellInfo[][] cellInfoGrid, final int row) {
		return cellInfoGrid[row];
	}

	/**
	 * Gets the cell spans.
	 * 
	 * @param cellInfos the cell infos
	 * 
	 * @return the cell spans
	 */
	public static List<CellSpanInfo> getCellSpans(final CellInfo[] cellInfos) {
		final List<CellSpanInfo> spans = new ArrayList<CellSpanInfo>();
		CellInfo holdValue = cellInfos != null && cellInfos.length > 0 ? cellInfos[0] : null;
		int span = 1;

		for (int i = 1; i < cellInfos.length; i++) {
			if (cellInfos[i].getFormattedValue().equals(holdValue == null ? null : holdValue.getFormattedValue())) {
				span++;
			} else {
				spans.add(new CellSpanInfo(holdValue, span));
				span = 1;
				holdValue = cellInfos[i];
			}
		}
		spans.add(new CellSpanInfo(holdValue, span));

		return spans;
	}
}
