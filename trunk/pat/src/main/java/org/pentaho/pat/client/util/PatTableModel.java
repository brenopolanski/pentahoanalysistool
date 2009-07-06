package org.pentaho.pat.client.util;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.client.ui.widgets.cells.ColumnLabel;
import org.pentaho.pat.rpc.dto.CellInfo;
import org.pentaho.pat.rpc.dto.OlapData;


public class PatTableModel {

	private final char SPANNED = 's';
	private final char FREE = 'f';
	private final boolean showParentMembers=true;
	private final boolean groupHeaders=true;
	private final OlapData olapData = null;

	public PatTableModel(){



	}


	protected char[][] createMatrix(final int row, final int column) {

		final char m[][] = new char[row][column];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				m[i][j] = FREE;
			}
		}

		return m;
	}

	/**
	 * 
	 *Get the amount of columns in the dataset
	 *
	 * @return olapData.getCellData().getAcrossCount();
	 */
	public int getColumnCount(){
		return olapData.getCellData().getAcrossCount();
	}

	public void getColumnHeaders(){
		CellInfo[][] headerData;
		final List<List<ColumnLabel>> colHeaderData = new ArrayList<List<ColumnLabel>>();
		if (showParentMembers) {
			headerData = olapData.getColumnHeaders().getColumnHeaderMembers();
		} else {
			headerData = new CellInfo[1][olapData.getColumnHeaders()
			                             .getAcrossCount()];
			headerData[0] = olapData.getColumnHeaders()
			.getColumnHeaderMembers()[olapData.getColumnHeaders()
			                          .getDownCount() - 1];
		}
		if (groupHeaders && showParentMembers) {

			for (int row = 0; row < headerData.length; row++) {

				final List<CellSpanInfo> infos = OlapUtils
				.getCellSpans(OlapUtils.extractRow(headerData, row));
				final List<ColumnLabel> colRowData = new ArrayList<ColumnLabel>();
				for (int currentColumn = 0, n = infos.size(); currentColumn < n; currentColumn++) {
					final CellSpanInfo spanInfo = infos.get(currentColumn);
					colRowData.add(new ColumnLabel(spanInfo.getInfo().getFormattedValue(), olapData.getRowHeaders().getAcrossCount()));
				}
				colHeaderData.add(colRowData);
			}
		} else {
			for (final CellInfo[] element : headerData) {
				final List<ColumnLabel> colRowData = new ArrayList<ColumnLabel>();
				for (int column = 0; column < element.length; column++) {
					final CellInfo cellInfo = element[column];
					if (cellInfo != null) {
						colRowData.add(new ColumnLabel(cellInfo.getFormattedValue(), olapData.getRowHeaders().getAcrossCount()));
						/*headerTable.setWidget(row, showParentMembers ? column
								+ olapData.getRowHeaders().getAcrossCount()
								: column + 1, label);*/
					}
				}
			}
		}


	}

	/**
	 * 
	 * Get the amount of data rows
	 * 
	 * @return olapData.getCellData().getDownCount();
	 */
	public int getRowCount(){
		return olapData.getCellData().getDownCount();
	}

	public List getRowData(){

		final int columnHeadersHeight = 0;

		final int rowHeadersHeight = olapData.getRowHeaders().getDownCount();
		final int rowHeadersWidth = olapData.getRowHeaders().getAcrossCount();
		CellInfo[][] headerData;

		new ArrayList();
		if (showParentMembers) {
			headerData = olapData.getRowHeaders().getRowHeaderMembers();
		} else {
			headerData = new CellInfo[rowHeadersHeight][1];
			for (int row = 0; row < rowHeadersHeight; row++) {
				headerData[row][0] = olapData.getRowHeaders().getCell(row,
						rowHeadersWidth - 1);
			}
		}
		createMatrix(headerData.length + columnHeadersHeight,
				headerData[0].length);

		//Old code need rewriting to work on a row based output
		if (groupHeaders) {
			for (int row = 0; row < rowHeadersHeight; row++){
				OlapUtils.getCellSpans(OlapUtils.extractRow(headerData, row));

			}
		}

		//Once Row Header calculated the row data needs inserting

		return null;

	}

	protected void spanMatrixRow(final char matrix[][], final int row, final int column, final int span) {
		for (int i = 1; i < span; i++) {
			matrix[row + i][column] = SPANNED;
		}
	}

}
