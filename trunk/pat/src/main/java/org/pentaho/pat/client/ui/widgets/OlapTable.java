/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import java.util.Iterator;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.i18n.PatMessages;
import org.pentaho.pat.client.ui.panels.CellFormatPopup;
import org.pentaho.pat.client.util.CellInfo;
import org.pentaho.pat.client.util.CellSpanInfo;
import org.pentaho.pat.client.util.OlapUtils;
import org.pentaho.pat.rpc.beans.OlapData;
import org.pentaho.pat.server.messages.Messages;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
/**
 * @author wseyler
 *
 */
public class OlapTable extends FlexTable {
 
  private static final String OLAP_ROW_HEADER_LABEL = "olap-row-header-label"; //$NON-NLS-1$
  private static final String OLAP_ROW_HEADER_CELL = "olap-row-header-cell"; //$NON-NLS-1$
  private static final String OLAP_COL_HEADER_CELL = "olap-col-header-cell"; //$NON-NLS-1$
  private static final String OLAP_COL_HEADER_LABEL = "olap-col-header-label"; //$NON-NLS-1$
  private static final char USED = 'u';
  private static final char SPANNED = 's';
  private static final char FREE = 'f';
 
  private transient OlapData olapData = null;
  private boolean showParentMembers = true;
  private boolean groupHeaders = true;

 
  private transient CellFormatPopup cellFormatPopup;
 
  /**
   * @param patMessages
   */
  public OlapTable(final PatMessages patMessages) {
    super();
    
   
    addStyleName("olap-table"); //$NON-NLS-1$
  }

 
  public OlapTable(final PatMessages messages, final boolean showParentMembers) {
    this(messages);
    this.showParentMembers = showParentMembers;
  }
 
  public void setData(final OlapData olapData) {
    setData(olapData, true);
  }
 
  public void setData(final OlapData olapData, final boolean refresh) {
    this.olapData = olapData;
    if (refresh) {
      refresh();
    }
  }
 
  public OlapData getData() {
    return olapData;
  }
 
  public void refresh() {
    removeAllRows();

    if (olapData != null) {
        createColumnHeaders();
      createRowHeaders();
      populateData();
    }  
  }

  protected void removeAllRows() {
    while (this.getRowCount() > 0) {
      this.removeRow(0);
    }
  }
 
  protected void createColumnHeaders() {
    final FlexCellFormatter cellFormatter = getFlexCellFormatter();
   
    CellInfo[][] headerData;
    if (showParentMembers) {
        headerData = olapData.getColumnHeaders().getColumnHeaderMembers();
    } else {
        headerData = new CellInfo[1][olapData.getColumnHeaders().getAcrossCount()];
        headerData[0] = olapData.getColumnHeaders().getColumnHeaderMembers()[olapData.getColumnHeaders().getDownCount()-1];
    }
    if (groupHeaders && showParentMembers) {  
      for (int row=0; row<headerData.length; row++) {
        int currentColumn = 0;
        final Iterator iter = OlapUtils.getCellSpans(OlapUtils.extractRow(headerData, row)).iterator();
        while (iter.hasNext()) {
                final CellSpanInfo spanInfo = (CellSpanInfo) iter.next();
                final Label label = new Label(spanInfo.getInfo().getFormattedValue());
                label.addStyleName(OLAP_COL_HEADER_LABEL);
                setWidget(row, currentColumn + olapData.getRowHeaders().getAcrossCount(), label);
                cellFormatter.addStyleName(row, currentColumn + olapData.getRowHeaders().getAcrossCount(), OLAP_COL_HEADER_CELL);
                cellFormatter.setColSpan(row, currentColumn + olapData.getRowHeaders().getAcrossCount(), spanInfo.getSpan());
                currentColumn ++;
        }
      }
    } else {
        for (int row=0; row<headerData.length; row++) {
        for (int column=0; column<headerData[row].length; column++) {
               final CellInfo cellInfo = headerData[row][column];
                if (cellInfo != null) {
                       final Label label = new Label(cellInfo.getFormattedValue());
                        label.addStyleName(OLAP_COL_HEADER_LABEL);
                        cellFormatter.addStyleName(row, showParentMembers ? column + olapData.getRowHeaders().getAcrossCount() : column + 1, OLAP_COL_HEADER_CELL);
                        // aki tinha +1 antes do label (em cima tbm, no correspondente)
                        setWidget(row, showParentMembers ? column + olapData.getRowHeaders().getAcrossCount() : column + 1, label);
                }
        }
      }
    }
  }
 
  protected void createRowHeaders() {
        final FlexCellFormatter cellFormatter = getFlexCellFormatter();
        final int columnHeadersHeight = showParentMembers ? olapData.getColumnHeaders().getDownCount() : 2;
       
        final int rowHeadersHeight = olapData.getRowHeaders().getDownCount();
        final int rowHeadersWidth = olapData.getRowHeaders().getAcrossCount();
        int offset = 0;
       
        CellInfo[][] headerData;
    if (showParentMembers) {
        headerData = olapData.getRowHeaders().getRowHeaderMembers();
    } else {
        headerData = new CellInfo[rowHeadersHeight][1];
        for (int row = 0; row<rowHeadersHeight; row++) {
                headerData[row][0] = olapData.getRowHeaders().getCell(row, rowHeadersWidth - 1);
        }
    }
    char matrix[][] = createMatrix(headerData.length + columnHeadersHeight, headerData[0].length);
   
    if (groupHeaders) {
        for (int column=0; column < headerData[0].length; column++) { // columns

                final CellInfo actualColumn[] = OlapUtils.extractColumn(headerData, column);
                if (actualColumn == null || actualColumn.length == 0) 
                	{
                	continue;                                      
                	}
                final Iterator iter = OlapUtils.getCellSpans(actualColumn).iterator();
                int actualRow = 0; // the current row (considering just the headerData, excluding column headers)
                while (iter.hasNext())
                {
                        if (showParentMembers == false)
                                {
                        	actualRow--;
                                }
                        final CellSpanInfo spanInfo = (CellSpanInfo) iter.next();
                        //Prepares the label
                       final Label label = new Label(spanInfo.getInfo().getFormattedValue());
                        label.addStyleName(OLAP_ROW_HEADER_LABEL);

                        final int newColumn = offset;
                        matrix[columnHeadersHeight + actualRow][column] = USED;
                        spanMatrixRow(matrix, columnHeadersHeight + actualRow, newColumn, spanInfo.getSpan());
                       
                        cellFormatter.setRowSpan(columnHeadersHeight + actualRow ,
                                        newColumn - getSpanInRow(matrix, columnHeadersHeight +actualRow), spanInfo.getSpan());
                        cellFormatter.addStyleName(columnHeadersHeight + actualRow ,
                                        newColumn - getSpanInRow(matrix, columnHeadersHeight +actualRow), OLAP_ROW_HEADER_CELL);
                        setWidget (columnHeadersHeight + actualRow ,
                                        newColumn - getSpanInRow(matrix, columnHeadersHeight + actualRow), label);
                       
                        actualRow+=spanInfo.getSpan();
                       
                        if (showParentMembers == false)
                                {
                        	actualRow++;
                                }
                       
                }
                offset++;
        }
    }
    else
    {
        final int rowAddition = showParentMembers ? columnHeadersHeight : 1;
        final int columnAddition = showParentMembers ? 0 : -1 ;
        for (int row=0; row<headerData.length; row++) {
                for (int column=0; column<headerData[row].length; column++) {
                        final CellInfo cellInfo = headerData[row][column];
                        if (cellInfo != null) {
                               final Label label = new Label(cellInfo.getFormattedValue());
                                label.addStyleName(OLAP_ROW_HEADER_CELL);
                                cellFormatter.addStyleName(row + rowAddition,column, OLAP_ROW_HEADER_CELL);
                                // TODO Code Review : why is this commented out??
                                  //showParentMembers ? column + olapData.getRowHeaders().getAcrossCount() : column + 1, OLAP_COL_HEADER_CELL);
                                setWidget(row + rowAddition,
                                                showParentMembers ? column : column, label);
                        }
                }
         }//for
    }//else
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
 
  protected void populateData() {
    for (int row=0; row<olapData.getCellData().getDownCount(); row++) {
        for (int column=0; column<olapData.getCellData().getAcrossCount(); column++) {
                final CellInfo cellInfo = olapData.getCellData().getCell(row, column);
                if (cellInfo != null) {
                      final  Label label = new Label(cellInfo.getFormattedValue());
                label.addStyleName("olap-cell-label"); //$NON-NLS-1$
                final String colorValueStr = cellInfo.getColorValue();
                if (colorValueStr != null) {
                  DOM.setElementAttribute(label.getElement(), "style", "background-color: "+cellInfo.getColorValue()+";");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                }
                label.addClickListener(new ClickCellCommand());
                setWidget(showParentMembers ? row + olapData.getColumnHeaders().getDownCount() : row + 1, showParentMembers ? getFirstUnusedColumnForRow(row + olapData.getColumnHeaders().getDownCount())/*column + olapData.getRowHeaders().getAcrossCount() */: column + 1, label);
                }
        }
    }  
  }
 
  public boolean isShowParentMembers() {
    return showParentMembers;
  }


  public void setShowParentMembers(final boolean showParentMembers, final boolean refresh) {
    this.showParentMembers = showParentMembers;
    if (refresh) {
      refresh();
    }
  }
 
  public void setShowParentMembers(final boolean showParentMembers) {
    setShowParentMembers(showParentMembers, true);
  }


  public boolean isGroupHeaders() {
    return groupHeaders;
  }

  public void setGroupHeaders(final boolean groupHeaders) {
    setGroupHeaders(groupHeaders, true);
  }
 
  /**
   * @param groupHeaders2
   * @param b
   */
  public void setGroupHeaders(final boolean groupHeaders, final boolean refresh) {
    this.groupHeaders = groupHeaders;
    if (refresh) {
      refresh();
    }
  }


  public int getFirstUnusedColumnForRow(final int row) {
        int column = 0;
       
        try {
                while (true) {
                       final Widget widget = getWidget(row, column);
                        final String text = getText(row, column);
                        if ((text == null || text.length() < 1) && widget == null) {
                                return column;
                        }
                        column ++;
                }
        } catch (IndexOutOfBoundsException e) {
                return column;
        }
  }
 
  protected char[][] createMatrix (final int row, final int column)
  {
          char m[][] = new char[row][column];
          for (int i = 0; i < m.length; i++)
          {
        	  
          
                  for (int j = 0; j < m[0].length; j++){
                	    m[i][j] = FREE;
                  }
                      
          }
          return m;
  }
 
  /*
   * There's no clone() method in GWT, so it's done by hand.
   */
  protected void copyMatrix( final char source[][], char destination[][])
  {
          if (source.length > destination.length ||
                  source[0].length > destination[0].length)
          {
                  throw new IndexOutOfBoundsException
                  ("The destination[" + destination.length + "][" + destination[0].length +"]" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                 "is smaller than source[" + source.length + "][" + source[0].length +"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          }

          for (int i = 0; i < source.length; i++){
        	  
          
                  for (int j = 0; j < source[0].length; j++){
                	    destination[i][j]=source[i][j];
                  }
                      
          }       
 
  }
 
  protected void spanMatrixRow(final char matrix[][], final int row, final int column, final int span)
  {
          for (int i = 1; i < span; i++){
                  matrix[row  + i][column] = SPANNED;
          }
  }

  protected void printMatrix (char m[][])
  {
          for (int i = 0; i < m.length; i++)
          {
                  for (int j = 0; j < m[0].length; j++){
                          System.out.print( " [" + i + "][" + j +"]=" + m[i][j]);  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
                  }
               //   System.out.println();
          }
         
  }
 
  protected int getSpanInRow(final char[][] matrix, final int row)
  {
          int result = 0;
          for (int i = 0; i < matrix[row].length; i++){
                  if (matrix[row][i] == SPANNED){
                          result++;
          }
          }
          return result;
  }
 
  protected int getSpanInColumn(final char[][] matrix, final int column)
  {
          int result = 0;
          for (int i = 0; i < matrix.length; i++){
                  if (matrix[i][column] == SPANNED){
                          result++;
                  }
          }
          return result;
  }
 
  public class ClickCellCommand implements ClickListener{
          public void onClick(final Widget sender){
                  cellFormatPopup =  new CellFormatPopup(sender.getAbsoluteTop(), sender.getAbsoluteLeft(), sender );
                  cellFormatPopup.show();
                  //sender.addStyleName(cellFormatPopup.getReturnStyle());
                 
          };
  }
}
