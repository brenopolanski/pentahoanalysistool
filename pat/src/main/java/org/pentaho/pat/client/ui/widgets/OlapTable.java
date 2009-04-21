package org.pentaho.pat.client.ui.widgets;

import java.util.Iterator;

import org.pentaho.pat.client.i18n.PatMessages;
import org.pentaho.pat.client.ui.panels.CellFormatPopup;
import org.pentaho.pat.client.util.CellInfo;
import org.pentaho.pat.client.util.CellSpanInfo;
import org.pentaho.pat.client.util.OlapUtils;
import org.pentaho.pat.rpc.beans.OlapData;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * Creates the OLAP flextable used for displaying the data
 * @author wseyler
 *
 */
public class OlapTable extends FlexTable { // NOPMD by bugg on 21/04/09 05:55

/**
  * The Class ClickCellCommand.
  */
 public class ClickCellCommand implements ClickListener {

		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
		 */
     		/**
     		 * @param sender the sender
     		 */
		public final void onClick(final Widget sender) {
			cellFormatPopup =  new CellFormatPopup(sender.getAbsoluteTop(), sender.getAbsoluteLeft(), sender);
			cellFormatPopup.show();
			//sender.addStyleName(cellFormatPopup.getReturnStyle());

		};
	}

	/** The Constant OLAP_ROW_HEADER_LABEL. */
	private static final String OLAP_ROW_HEADER_LABEL = "olap-row-header-label"; //$NON-NLS-1$ // NOPMD by bugg on 21/04/09 05:55

	/** The Constant OLAP_ROW_HEADER_CELL. */
	private static final String OLAP_ROW_HEADER_CELL = "olap-row-header-cell"; //$NON-NLS-1$ // NOPMD by bugg on 21/04/09 05:55

	/** The Constant OLAP_COL_HEADER_CELL. */
	private static final String OLAP_COL_HEADER_CELL = "olap-col-header-cell"; //$NON-NLS-1$ // NOPMD by bugg on 21/04/09 05:55

	/** The Constant OLAP_COL_HEADER_LABEL. */
	private static final String OLAP_COL_HEADER_LABEL = "olap-col-header-label"; //$NON-NLS-1$ // NOPMD by bugg on 21/04/09 05:55

	/** The Constant USED. */
	private static final char USED = 'u';

	/** The Constant SPANNED. */
	private static final char SPANNED = 's';

	/** The Constant FREE. */
	private static final char FREE = 'f';

	/** The olap data. */
	private transient OlapData olapData = null;

	/** The show parent members. */
	private boolean showParentMembers = true;


	/** The group headers. */
	private boolean groupHeaders = true;

	/** The cell format popup. */
	private transient CellFormatPopup cellFormatPopup;


	/**
	 * The Constructor.
	 *
	 * @param patMessages the pat messages
	 */
	public OlapTable(final PatMessages patMessages) {
		super();


		addStyleName("olap-table"); //$NON-NLS-1$
	}

	/**
	 * Instantiates a new olap table.
	 *
	 * @param messages the messages
	 * @param showParentMembers the show parent members
	 */
	public OlapTable(final PatMessages messages, final boolean showParentMembers) {
		this(messages);
		this.showParentMembers = showParentMembers;
	}

	/*
	 * There's no clone() method in GWT, so it's done by hand.
	 */
	/**
	 * Copy matrix.
	 *
	 * @param source the source
	 * @param destination the destination
	 */
	protected final void copyMatrix(final char[][] source, final char[][] destination)
	{
		if (source.length > destination.length
			|| source[0].length > destination[0].length) {
			throw new IndexOutOfBoundsException(
				"The destination[" + destination.length + "][" + destination[0].length + "]" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"is smaller than source[" + source.length + "][" + source[0].length + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		for (int i = 0; i < source.length; i++) {


			for (int j = 0; j < source[0].length; j++) {
				destination[i][j] = source[i][j];
			}

		}

	}

	/**
	 * Creates the column headers.
	 */
	protected final void createColumnHeaders() { // NOPMD by bugg on 21/04/09 05:55
		final FlexCellFormatter cellFormatter = getFlexCellFormatter();

		CellInfo[][] headerData;
		if (showParentMembers) {
			headerData = olapData.getColumnHeaders().getColumnHeaderMembers();
		} else {
			headerData = new CellInfo[1][olapData.getColumnHeaders().getAcrossCount()];
			headerData[0] = olapData.getColumnHeaders().getColumnHeaderMembers()[olapData.getColumnHeaders().getDownCount() - 1];
		}
		if (groupHeaders && showParentMembers) {
			for (int row = 0; row < headerData.length; row++) {
				int currentColumn = 0;
				final Iterator iter = OlapUtils.getCellSpans(OlapUtils.extractRow(headerData, row)).iterator();
				while (iter.hasNext()) {
					final CellSpanInfo spanInfo = (CellSpanInfo) iter.next();
					final Label label = new Label(spanInfo.getInfo().getFormattedValue()); // NOPMD by bugg on 21/04/09 05:55
					label.addStyleName(OLAP_COL_HEADER_LABEL);
					setWidget(row, currentColumn + olapData.getRowHeaders().getAcrossCount(), label);
					cellFormatter.addStyleName(row, currentColumn + olapData.getRowHeaders().getAcrossCount(), OLAP_COL_HEADER_CELL);
					cellFormatter.setColSpan(row, currentColumn + olapData.getRowHeaders().getAcrossCount(), spanInfo.getSpan());
					currentColumn++;
				}
			}
		} else {
			for (int row = 0; row < headerData.length; row++) {
				for (int column = 0; column < headerData[row].length; column++) {
					final CellInfo cellInfo = headerData[row][column];
					if (cellInfo != null) {
						final Label label = new Label(cellInfo.getFormattedValue()); // NOPMD by bugg on 21/04/09 05:56
						label.addStyleName(OLAP_COL_HEADER_LABEL);
						cellFormatter.addStyleName(row, showParentMembers ? column + olapData.getRowHeaders().getAcrossCount() : column + 1, OLAP_COL_HEADER_CELL);
						// aki tinha +1 antes do label (em cima tbm, no correspondente)
						setWidget(row, showParentMembers ? column + olapData.getRowHeaders().getAcrossCount() : column + 1, label);
					}
				}
			}
		}
	}

	/**
	 * Creates the matrix.
	 *
	 * @param row the row
	 * @param column the column
	 *
	 * @return the char[][]
	 */
	protected final char[][] createMatrix(final int row, final int column) {
		final char[][] m = new char[row][column]; // NOPMD by bugg on 21/04/09 05:56
		for (int i = 0; i < m.length; i++) {


			for (int j = 0; j < m[0].length; j++) {
				m[i][j] = FREE;
			}

		}
		return m;
	}

	/**
	 * Creates the row headers.
	 */
	protected final void createRowHeaders() { // NOPMD by bugg on 21/04/09 05:56
		final FlexCellFormatter cellFormatter = getFlexCellFormatter();
		final int columnHeadersHeight = showParentMembers ? olapData.getColumnHeaders().getDownCount() : 2; // NOPMD by bugg on 21/04/09 05:56

		final int rowHeadersHeight = olapData.getRowHeaders().getDownCount();
		final int rowHeadersWidth = olapData.getRowHeaders().getAcrossCount();
		int offset = 0;

		CellInfo[][] headerData;
		if (showParentMembers) {
			headerData = olapData.getRowHeaders().getRowHeaderMembers();
		} else {
			headerData = new CellInfo[rowHeadersHeight][1];
			for (int row = 0; row < rowHeadersHeight; row++) {
				headerData[row][0] = olapData.getRowHeaders().getCell(row, rowHeadersWidth - 1);
			}
		}
		final char[][] matrix = createMatrix(headerData.length + columnHeadersHeight, headerData[0].length);

		if (groupHeaders) {
			for (int column = 0; column < headerData[0].length; column++) { // columns

				final CellInfo[] actualColumn = OlapUtils.extractColumn(headerData, column);
				if (actualColumn == null || actualColumn.length == 0) {
					continue;
				}
				final Iterator iter = OlapUtils.getCellSpans(actualColumn).iterator();
				int actualRow = 0; // the current row (considering just the headerData, excluding column headers)
				while (iter.hasNext()) {
					if (!showParentMembers) {
						actualRow--;
					}
					final CellSpanInfo spanInfo = (CellSpanInfo) iter.next();
					//Prepares the label
					final Label label = new Label(spanInfo.getInfo().getFormattedValue()); // NOPMD by bugg on 21/04/09 05:58
					label.addStyleName(OLAP_ROW_HEADER_LABEL);

					final int newColumn = offset;
					matrix[columnHeadersHeight + actualRow][column] = USED;
					spanMatrixRow(matrix, columnHeadersHeight + actualRow, newColumn, spanInfo.getSpan());

					cellFormatter.setRowSpan(columnHeadersHeight + actualRow ,
							newColumn - getSpanInRow(matrix, columnHeadersHeight +actualRow), spanInfo.getSpan());
					cellFormatter.addStyleName(columnHeadersHeight + actualRow ,
							newColumn - getSpanInRow(matrix, columnHeadersHeight +actualRow), OLAP_ROW_HEADER_CELL);
					setWidget(columnHeadersHeight + actualRow ,
							newColumn - getSpanInRow(matrix, columnHeadersHeight + actualRow), label);

					actualRow += spanInfo.getSpan();

					if (showParentMembers) {
						actualRow++;
					}

				}
				offset++;
			}
		} else {
			final int rowAddition = showParentMembers ? columnHeadersHeight : 1;
			for (int row = 0; row < headerData.length; row++) {
				for (int column = 0; column < headerData[row].length; column++) {
					final CellInfo cellInfo = headerData[row][column];
					if (cellInfo != null) {
						final Label label = new Label(cellInfo.getFormattedValue()); // NOPMD by bugg on 21/04/09 05:58
						label.addStyleName(OLAP_ROW_HEADER_CELL);
						cellFormatter.addStyleName(row + rowAddition, column, OLAP_ROW_HEADER_CELL);
						// TODO Code Review : why is this commented out??
						//showParentMembers ? column + olapData.getRowHeaders().getAcrossCount() : column + 1, OLAP_COL_HEADER_CELL);
						setWidget(row + rowAddition,
								showParentMembers ? column : column, label);
					}
				}
			} //for
		} //else
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public final OlapData getData() {
		return olapData;
	}

	/**
	 * Gets the first unused column for row.
	 *
	 * @param row the row
	 *
	 * @return the first unused column for row
	 */
	public final int getFirstUnusedColumnForRow(final int row) {
		int column = 0;

		try {
			while (true) {
				final Widget widget = getWidget(row, column);
				final String text = getText(row, column);
				if ((text == null || text.length() < 1) && widget == null) {
					return column; // NOPMD by bugg on 21/04/09 05:58
				}
				column++;
			}
		} catch (final IndexOutOfBoundsException e) {
			return column;
		}
	}

	/*
  private void printTable()
  {
          int j = 0;
          System.out.println("==============");
          for (int i = 0; i < getRowCount(); i++)
          {
                for (j = 0; j < getCellCount(i); j++)
                {
                        if (isCellPresent (i,j))
                                System.out.print("P ");
                        else
                                System.out.print("n ");
                }
                System.out.println((j));
          }
          System.out.println("==============");
  }
	 */

	/**
	 * Gets the span in column.
	 *
	 * @param matrix the matrix
	 * @param column the column
	 *
	 * @return the span in column
	 */
	protected final int getSpanInColumn(final char[][] matrix, final int column) {
		int result = 0;
		for (final char[] element2 : matrix) {
			if (element2[column] == SPANNED) {
				result++;
			}
		}
		return result;
	}

	/**
	 * Gets the span in row.
	 *
	 * @param matrix the matrix
	 * @param row the row
	 *
	 * @return the span in row
	 */
	protected final int getSpanInRow(final char[][] matrix, final int row) {
		int result = 0;
		for (int i = 0; i < matrix[row].length; i++) {
			if (matrix[row][i] == SPANNED) {
				result++;
			}
		}
		return result;
	}


	/**
	 * Checks if is group headers.
	 *
	 * @return true, if is group headers
	 */
	public final boolean isGroupHeaders() {
		return groupHeaders;
	}

	/**
	 * Checks if is show parent members.
	 *
	 * @return true, if is show parent members
	 */
	public final boolean isShowParentMembers() {
		return showParentMembers;
	}


	/**
	 * Populate data.
	 */
	protected final void populateData() {
		for (int row = 0; row < olapData.getCellData().getDownCount(); row++) {
			for (int column = 0; column < olapData.getCellData().getAcrossCount(); column++) {
				final CellInfo cellInfo = olapData.getCellData().getCell(row, column);
				if (cellInfo != null) {
					final  Label label = new Label(cellInfo.getFormattedValue()); // NOPMD by bugg on 21/04/09 05:58
					label.addStyleName("olap-cell-label"); //$NON-NLS-1$
					final String colorValueStr = cellInfo.getColorValue();
					if (colorValueStr != null) {
						DOM.setElementAttribute(label.getElement(), "style", "background-color: "+cellInfo.getColorValue()+";");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
					}
					label.addClickListener(new ClickCellCommand()); // NOPMD by bugg on 21/04/09 05:58
					setWidget(showParentMembers ? row + olapData.getColumnHeaders().getDownCount() : row + 1, showParentMembers ? getFirstUnusedColumnForRow(row + olapData.getColumnHeaders().getDownCount())/*column + olapData.getRowHeaders().getAcrossCount() */: column + 1, label);
				}
			}
		}
	}

	/**
	 * Prints the matrix.
	 *
	 * @param m the m
	 */
	protected final void printMatrix(final char[][] m) // NOPMD by bugg on 21/04/09 05:58
	{
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				System.out.print(" [" + i + "][" + j + "]=" + m[i][j]);  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			}
			//   System.out.println();
		}

	}

	/**
	 * Refresh.
	 */
	public final void refresh() {
		removeAllRows();

		if (olapData != null) {
			createColumnHeaders();
			createRowHeaders();
			populateData();
		}
	}


	/**
	 * Removes the all rows.
	 */
	protected final void removeAllRows() {
		while (this.getRowCount() > 0) {
			this.removeRow(0);
		}
	}

	/**
	 * Sets the data.
	 *
	 * @param olapData the new data
	 */
	public final void setData(final OlapData olapData) {
		setData(olapData, true);
	}

	/**
	 * Sets the data.
	 *
	 * @param olapData the olap data
	 * @param refresh the refresh
	 */
	public final void setData(final OlapData olapData, final boolean refresh) {
		this.olapData = olapData;
		if (refresh) {
			refresh();
		}
	}

	/**
	 * Sets the group headers.
	 *
	 * @param groupHeaders the new group headers
	 */
	public final void setGroupHeaders(final boolean groupHeaders) {
		setGroupHeaders(groupHeaders, true);
	}

	/**
	 * Sets the group headers.
	 *
	 * @param groupHeaders the group headers
	 * @param refresh the refresh
	 */
	public final void setGroupHeaders(final boolean groupHeaders, final boolean refresh) {
		this.groupHeaders = groupHeaders;
		if (refresh) {
			refresh();
		}
	}

	/**
	 * Sets the show parent members.
	 *
	 * @param showParentMembers the new show parent members
	 */
	public final void setShowParentMembers(final boolean showParentMembers) {
		setShowParentMembers(showParentMembers, true);
	}

	/**
	 * Sets the show parent members.
	 *
	 * @param showParentMembers the show parent members
	 * @param refresh the refresh
	 */
	public final void setShowParentMembers(final boolean showParentMembers, final boolean refresh) {
		this.showParentMembers = showParentMembers;
		if (refresh) {
			refresh();
		}
	}

	/**
	 * Span matrix row.
	 *
	 * @param matrix the matrix
	 * @param row the row
	 * @param column the column
	 * @param span the span
	 */
	protected final void spanMatrixRow(final char[][] matrix, final int row, final int column, final int span)
	{
		for (int i = 1; i < span; i++){
			matrix[row  + i][column] = SPANNED;
		}
	}
}
