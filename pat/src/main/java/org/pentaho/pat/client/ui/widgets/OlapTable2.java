/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * @created Jan 3, 2008
 * @author wseyler
 */

package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.table.ScrollTable2;
import org.pentaho.pat.client.i18n.PatMessages;
import org.pentaho.pat.client.util.CellSpanInfo;
import org.pentaho.pat.client.util.OlapUtils;
import org.pentaho.pat.rpc.dto.CellInfo;
import org.pentaho.pat.rpc.dto.OlapData;

import com.google.gwt.gen2.table.client.AbstractScrollTable;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author wseyler
 * @hacked tom barber ;)
 */
@SuppressWarnings("deprecation")
public class OlapTable2 extends LayoutComposite {

	private static final String OLAP_ROW_HEADER_LABEL = "olap-row-header-label"; //$NON-NLS-1$
	private static final String OLAP_ROW_HEADER_CELL = "olap-row-header-cell"; //$NON-NLS-1$
	private static final String OLAP_COL_HEADER_CELL = "olap-col-header-cell"; //$NON-NLS-1$
	private static final String OLAP_COL_HEADER_LABEL = "olap-col-header-label"; //$NON-NLS-1$
	private static final char USED = 'u';
	private static final char SPANNED = 's';
	private static final char FREE = 'f';

	private OlapData olapData = null;
	private boolean showParentMembers = true;
	private boolean groupHeaders = true;
	
	private PatMessages messages = null;
	private CellFormatPopup cellFormatPopup;

	private ScrollTable2 scrollTable = null;

	private boolean initialized = false;

	/**
	 * The header portion of the <code>ScrollTable</code>
	 */
	private static FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
	
	private FixedWidthGrid dataTable = new FixedWidthGrid();
	
	private String parentName;
	/**
	 * @param messages
	 */
	public OlapTable2(PatMessages messages) {
		
		super();
		this.setMessages(messages);

		this.setSize("100%", "100%");
		
	}

	 /**
	 *TODO JAVADOC
	 *
	 */
	public OlapTable2() {
		super();
		this.setSize("100%", "100%");
	}

	@Override
	  protected void onLoad() {
	    super.onLoad();
		 
	  }
	 
	public final boolean isInitialized() {
		return initialized;
	}
	public void setData(OlapData olapData) {
		setData(olapData, true);
	}

	/**
	 * @param olapData
	 * @param refresh
	 */
	public void setData(final OlapData olapData, final boolean refresh) {
		initialized = true;
		this.olapData = olapData;
		if (refresh) {
			refresh();
		}
	}

	/**
	 * @return
	 */
	public OlapData getData() {
		return olapData;
	}

	public void initScrollTable(){
		scrollTable = new ScrollTable2(dataTable, headerTable);
	}
	public void refresh() {
		
	
			
		removeAllRows();
		
		if (olapData != null) {
			createColumnHeaders();
			createDataTable();
			createRowHeaders();
			populateData();
			
			
			  // Setup the formatting
		    scrollTable.setCellPadding(3);
		    scrollTable.setCellSpacing(0);
		    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);
		    
		    for(int i=0; i < scrollTable.getHeaderTable().getColumnCount(); i++)
		    {
		    	scrollTable.setColumnTruncatable(i, false);
		    }
		    final LayoutPanel layoutPanel = getLayoutPanel();
		    layoutPanel.add(scrollTable);
		    scrollTable.layout();
			this.layout();
		}
	}

	private void removeAllRows(){
		headerTable.clear();
		dataTable.clear();
	}

	private FixedWidthGrid createDataTable() {
		     
		    dataTable.resize(olapData.getCellData().getDownCount(), olapData.getRowHeaders().getAcrossCount()+olapData.getCellData().getAcrossCount());
		    dataTable.setSelectionPolicy(SelectionPolicy.MULTI_ROW);
		    return dataTable;
	}
	  
	
	
	public AbstractScrollTable getScrollTable() {
	    return scrollTable;
	  }
	
	protected void createColumnHeaders() {
		
		FlexCellFormatter headerFormatter = headerTable.getFlexCellFormatter();

		CellInfo[][] headerData;
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
				for (int currentColumn = 0, n = infos.size(); currentColumn < n; currentColumn++) {
					final CellSpanInfo spanInfo = infos.get(currentColumn);
					final Label label = new Label(spanInfo.getInfo()
							.getFormattedValue());
					label.addStyleName(OLAP_COL_HEADER_LABEL);
					headerTable.setWidget(row, currentColumn
							+ olapData.getRowHeaders().getAcrossCount(), label);
		/*			headerFormatter.addStyleName(row, currentColumn
							+ olapData.getRowHeaders().getAcrossCount(),
							OLAP_COL_HEADER_CELL);*/
					headerFormatter.setColSpan(row, currentColumn
							+ olapData.getRowHeaders().getAcrossCount(),
							spanInfo.getSpan());
				}
			}
		} else {
			for (int row = 0; row < headerData.length; row++) {
				for (int column = 0; column < headerData[row].length; column++) {
					CellInfo cellInfo = headerData[row][column];
					if (cellInfo != null) {
						Label label = new Label(cellInfo.getFormattedValue());
						label.addStyleName(OLAP_COL_HEADER_LABEL);
						headerFormatter.addStyleName(row,
								showParentMembers ? column
										+ olapData.getRowHeaders()
												.getAcrossCount() : column + 1,
								OLAP_COL_HEADER_CELL);
						// aki tinha +1 antes do label (em cima tbm, no
						// correspondente)
						headerTable.setWidget(row, showParentMembers ? column
								+ olapData.getRowHeaders().getAcrossCount()
								: column + 1, label);
					}
				}
			}
		}

	}

	protected void createRowHeaders() {
		int columnHeadersHeight = 0;

		int rowHeadersHeight = olapData.getRowHeaders().getDownCount();
		int rowHeadersWidth = olapData.getRowHeaders().getAcrossCount();
		int offset = 0;

		CellInfo[][] headerData;
		if (showParentMembers) {
			headerData = olapData.getRowHeaders().getRowHeaderMembers();
		} else {
			headerData = new CellInfo[rowHeadersHeight][1];
			for (int row = 0; row < rowHeadersHeight; row++) {
				headerData[row][0] = olapData.getRowHeaders().getCell(row,
						rowHeadersWidth - 1);
			}
		}
		char matrix[][] = createMatrix(headerData.length + columnHeadersHeight,
				headerData[0].length);

		if (groupHeaders) {
			for (int column = 0; column < headerData[0].length; column++) { // columns

				final List<CellSpanInfo> infos = OlapUtils
					.getCellSpans(OlapUtils.extractColumn(headerData,column));
				for (int actualRow = 0, n = infos.size(); actualRow < n; actualRow++) {
					if (showParentMembers == false)
						actualRow--;
					CellSpanInfo spanInfo = (CellSpanInfo) infos.get(actualRow);
					// Prepares the label
					Label label = new Label(spanInfo.getInfo()
							.getFormattedValue());
					label.addStyleName(OLAP_ROW_HEADER_LABEL);

					int newColumn = offset;
					matrix[columnHeadersHeight + actualRow][column] = USED;
					spanMatrixRow(matrix, columnHeadersHeight + actualRow,
							newColumn, spanInfo.getSpan());

							dataTable.setWidget(columnHeadersHeight + actualRow,
							newColumn, label);
						
					if (showParentMembers == false)
						actualRow++;

				}
				
				for (int i=0; i<dataTable.getRowCount();i++){
				
				Widget widget = dataTable.getWidget(i, offset);
				String text = dataTable.getText(i, offset);
		//Whilst this is here, don't use 1character column descriptors, because it wont see them!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if ((text == null || text.trim().length() < 2) && widget == null) {
					dataTable.setText(i, column, "___");
	                
				}
				
		}
				offset++;
			}
		} else {
			int rowAddition = showParentMembers ? columnHeadersHeight : 1;

			for (int row = 0; row < headerData.length; row++) {
				for (int column = 0; column < headerData[row].length; column++) {
					CellInfo cellInfo = headerData[row][column];
					if (cellInfo != null) {
						Label label = new Label(cellInfo.getFormattedValue());
						label.addStyleName(OLAP_ROW_HEADER_CELL);
						dataTable.setWidget(row + rowAddition,
								showParentMembers ? column : column, label);
					}
				}
			}// for
		}// else
	}

	protected void populateData() {
	  
		    for (int row=0; row<olapData.getCellData().getDownCount(); row++) {
		        for (int column=0; column<olapData.getCellData().getAcrossCount(); column++) {
		                CellInfo cellInfo = olapData.getCellData().getCell(row, column);
		                if (cellInfo != null) {
		                        Label label = new Label(cellInfo.getFormattedValue());
		                //label.addStyleName("olap-cell-label"); //$NON-NLS-1$
		                String colorValueStr = cellInfo.getColorValue();
		                if (colorValueStr != null) {
		                  DOM.setElementAttribute(label.getElement(), "style", "background-color: "+cellInfo.getColorValue()+";");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		                }
		                label.addClickListener(new ClickCellCommand());
		                System.out.println("actual row=" + row);
		                dataTable.setWidget(showParentMembers ? row: row + 1, 
		                					showParentMembers ? getFirstUnusedColumnForRow(row): column + 1, 
		                							label);
		                }
		        }
		    }   
		  }

	public boolean isShowParentMembers() {
		return showParentMembers;
	}

	public void setShowParentMembers(boolean showParentMembers, boolean refresh) {
		this.showParentMembers = showParentMembers;
		if (refresh) {
			refresh();
		}
	}

	public void setShowParentMembers(boolean showParentMembers) {
		setShowParentMembers(showParentMembers, true);
	}

	public boolean isGroupHeaders() {
		return groupHeaders;
	}

	public void setGroupHeaders(boolean groupHeaders) {
		setGroupHeaders(groupHeaders, true);
	}

	/**
	 * @param groupHeaders
	 * @param refresh
	 */
	public void setGroupHeaders(boolean groupHeaders, boolean refresh) {
		this.groupHeaders = groupHeaders;
		if (refresh) {
			refresh();
		}
	}

	public int getFirstUnusedColumnForRow(int row) {
		
		int column = 0;

		try {
			while (true) {
				Widget widget = dataTable.getWidget(row, column);
				String text = dataTable.getText(row, column);
		//Whilst this is here, don't use 1character column descriptors, because it wont see them!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if ((text == null || text.trim().length() < 2) && widget == null) {
					return column;
	                
				}
				column++;
			}
		} catch (IndexOutOfBoundsException e) {
			return column;
		}
	}

	protected char[][] createMatrix(int row, int column) {

		char m[][] = new char[row][column];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				m[i][j] = FREE;

		return m;
	}

	/*
	 * There's no clone() method in GWT, so it's done by hand.
	 */
	protected void copyMatrix(char source[][], char destination[][]) {
		if (source.length > destination.length
				| source[0].length > destination[0].length) {
			throw new IndexOutOfBoundsException(
					"The destination[" + destination.length + "][" + destination[0].length + "]" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							"is smaller than source[" + source.length
							+ "][" + source[0].length + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		for (int i = 0; i < source.length; i++)
			for (int j = 0; j < source[0].length; j++)
				destination[i][j] = source[i][j];

	}

	protected void spanMatrixRow(char matrix[][], int row, int column, int span) {
		for (int i = 1; i < span; i++)
			matrix[row + i][column] = SPANNED;
	}

	protected void printMatrix(char m[][]) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++)
				System.out.print(" [" + i + "][" + j + "]=" + m[i][j]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			System.out.println();
		}

	}

	protected int getSpanInRow(char[][] matrix, int row) {

		int result = 0;
		for (int i = 0; i < matrix[row].length; i++)
			if (matrix[row][i] == SPANNED)
				result++;

		return result;
	}

	protected int getSpanInColumn(char[][] matrix, int column) {

		int result = 0;
		for (int i = 0; i < matrix.length; i++)
			if (matrix[i][column] == SPANNED)
				result++;

		return result;
	}

	public class ClickCellCommand implements ClickListener {
		public void onClick(Widget sender) {
			cellFormatPopup = new CellFormatPopup(sender.getAbsoluteTop(),
					sender.getAbsoluteLeft(), sender);
			cellFormatPopup.show();
			// sender.addStyleName(cellFormatPopup.getReturnStyle());

		};
	}

	public void setMessages(PatMessages messages) {
		this.messages = messages;
	}
	
	public void setParentName(String parent){
		this.parentName = parent;
	}
	
	public String getParentName(){
		return parentName;
	}
	
	public FixedWidthGrid getDataTable(){
		return dataTable;
	}
	
	public void setDataTable(FixedWidthGrid table){
		this.dataTable = table;
	}
	
	public FixedWidthFlexTable getHeaderTable(){
	 return headerTable;
	}
	
	public void setHeaderTable(FixedWidthFlexTable headerTable){
		this.headerTable=headerTable;
	}
	
	public PatMessages getMessages(){
		return messages;
	}
}
