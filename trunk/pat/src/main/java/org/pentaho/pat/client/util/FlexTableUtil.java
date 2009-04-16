package org.pentaho.pat.client.util;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.infopanel.TrayInfoPanelNotifier;
import org.olap4j.Axis;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

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
		if (sourceTable != targetTable){
	    if (sourceTable == targetTable && sourceRow >= targetRow) {
	      sourceRow++;
	    }
	    targetTable.insertRow(targetRow);
	    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
	      Widget w = sourceTable.getWidget(sourceRow, col);
	      if (w != null) {
	    	/*  if (w instanceof Label == true){
	    		  final Label w2 = new Label(){
	    			  @Override
	    		      public void onBrowserEvent(Event event) {
	    		          if (event.getTypeInt() == Event.ONMOUSEOVER) {
	    		            DOM.eventPreventDefault(event);
	    		            showContextMenu(event);
	    		          }
	    		        
	    		        super.onBrowserEvent(event);
	    		      }
	    		  };
	    		  
	    		  w2.setText(((Label) w).getText());
	    		  targetTable.setWidget(targetRow, col, w2);
	    		  }
	    	  else*/ targetTable.setWidget(targetRow, col, w);
	    	  
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
	    
	  }
	  
	  /**
	   * Make a command that we will execute from all menu items.
	   */
	  private static Command cmd = new Command() {
	    public void execute() {
	      TrayInfoPanelNotifier.notifyTrayEvent("Menu Button", "You selected a menu item!");
	    }
	  };
	
	  private static PopupMenu contextMenu;
	  
	  private static void showContextMenu(final Event event) {
		    if (contextMenu == null) {
		      contextMenu = new PopupMenu();

		      contextMenu.addItem("MenuItem 1", cmd);
		      contextMenu.addItem("MenuItem 2", cmd);

		      contextMenu.addSeparator();

		      contextMenu.addItem("MenuItem 3", cmd);
		      contextMenu.addItem("MenuItem 4", cmd);
		    }

		    contextMenu.setPopupPositionAndShow(new PositionCallback() {
		      public void setPosition(int offsetWidth, int offsetHeight) {
		        contextMenu.setPopupPosition(event.getClientX(), event.getClientY());
		      }
		    });
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
