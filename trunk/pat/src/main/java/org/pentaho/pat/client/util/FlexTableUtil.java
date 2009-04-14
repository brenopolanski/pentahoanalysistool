package org.pentaho.pat.client.util;

import org.gwt.mosaic.ui.client.MessageBox;
import org.olap4j.Axis;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class FlexTableUtil {


	  /**
	   * Copy an entire FlexTable from one FlexTable to another. Each element is
	   * copied by creating a new {@link HTML} widget by calling
	   * {@link FlexTable#getHTML(int, int)} on the source table.
	   * 
	   * @param sourceTable the FlexTable to copy a row from
	   * @param targetTable the FlexTable to copy a row to
	   * @param sourceRow the index of the source row
	   * @param targetRow the index before which to insert the copied row
	   */
	  public static void copyRow(FlexTable sourceTable, FlexTable targetTable, int sourceRow,
	      int targetRow) {
	    targetTable.insertRow(targetRow);
	    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
	      HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
	      targetTable.setWidget(targetRow, col, html);
	    }
	    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
	  }

	  /**
	   * Move an entire FlexTable from one FlexTable to another. Elements are moved
	   * by attempting to call {@link FlexTable#getWidget(int, int)} on the source
	   * table. If no widget is found (because <code>null</code> is returned), a
	   * new {@link HTML} is created instead by calling
	   * {@link FlexTable#getHTML(int, int)} on the source table.
	   * 
	   * @param sourceTable the FlexTable to move a row from
	   * @param targetTable the FlexTable to move a row to
	   * @param sourceRow the index of the source row
	   * @param targetRow the index before which to insert the moved row
	   */
	  public static void moveRow(final FlexTable sourceTable, final FlexTable targetTable, int sourceRow,
	      int targetRow, org.pentaho.pat.rpc.beans.Axis targetAxis) {
		  targetRow = targetTable.getRowCount();
	    if (sourceTable == targetTable && sourceRow >= targetRow) {
	      sourceRow++;
	    }
	    targetTable.insertRow(targetRow);
	    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
	      final Widget w = sourceTable.getWidget(sourceRow, col);
	      if (w != null) {
	    	  targetTable.setWidget(targetRow, col, w);
	        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), targetAxis, w.getElement().getInnerText().trim(), new AsyncCallback(){

				public void onFailure(Throwable arg0) {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().failedDimensionSet());
				}

				public void onSuccess(Object arg0) {
			        
				}
	        	
	        });

	      } else {
	    	  final HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
	    	  targetTable.setWidget(targetRow, col, html);
	    	  ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), targetAxis, html.getText().trim(), new AsyncCallback(){

				public void onFailure(Throwable arg0) {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().failedDimensionSet());
				}

				public void onSuccess(Object arg0) {				      
					
				}
	        	
	        });
	      }
	    }
	    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
	    sourceTable.removeRow(sourceRow);
	    
	    
	  }

	  /**
	   * Copies the CSS style of a source row to a target row.
	   * 
	   * @param sourceTable
	   * @param targetTable
	   * @param sourceRow
	   * @param targetRow
	   */
	  private static void copyRowStyle(FlexTable sourceTable, FlexTable targetTable, int sourceRow,
	      int targetRow) {
	    String rowStyle = sourceTable.getRowFormatter().getStyleName(sourceRow);
	    targetTable.getRowFormatter().setStyleName(targetRow, rowStyle);
	  }

}
