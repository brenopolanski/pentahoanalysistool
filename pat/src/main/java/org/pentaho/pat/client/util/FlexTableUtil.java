package org.pentaho.pat.client.util;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.infopanel.TrayInfoPanelNotifier;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.StringTree;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
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
	      final int targetRow, Axis targetAxis) {
		//  targetRow = targetTable.getRowCount();
		if (sourceTable != targetTable){
	    if (sourceTable == targetTable && sourceRow >= targetRow) {
	      sourceRow++;
	    }
	    targetTable.insertRow(targetRow);
	    
	    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
	    	final int col2 = col;
	      final Widget w = sourceTable.getWidget(sourceRow, col);
	      if (w != null) {
	    	  if (w instanceof Label == true){
/*	    		  final Label w2 = new Label(){
	    			  public void onBrowserEvent(Event event) {
	    				  	super.onBrowserEvent(event);
	    					if(DOM.eventGetType(event)== Event.ONDBLCLICK){
	    						DOM.eventPreventDefault(event);
	    						showContextMenu(event);
	    					}
	    			  } 

	    		  };
	    		  w2.sinkEvents(Event.ONDBLCLICK);
	    		  w2.setText(((Label) w).getText());
*/	    		  
	    	  
	        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), targetAxis, w.getElement().getInnerText().trim(), new AsyncCallback(){

				public void onFailure(Throwable arg0) {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().failedDimensionSet());
				}

				public void onSuccess(Object arg0) {
			        ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), w.getElement().getInnerText().trim(), new AsyncCallback<StringTree>(){

						public void onFailure(Throwable arg0) {
							// TODO Auto-generated method stub
							
						}

						public void onSuccess(StringTree arg0) {
							// TODO Auto-generated method stub
							final Tree dimTree = new Tree();
							//TreeItem tn = new TreeItem(w.getElement().getInnerText().trim());
							//dimTree.addItem(tn);
														
							 StringTree memberTree = (StringTree) arg0;
						        Label rootLabel = new Label(memberTree.getValue());
						        TreeItem root = new TreeItem(rootLabel);
						        for (int i=0; i<memberTree.getChildren().size(); i++) {
						          root = createPathForMember(root, (StringTree)memberTree.getChildren().get(i));
						        }
						        dimTree.addItem(root);
						        targetTable.setWidget(targetRow, col2, dimTree);

						}
			        	
			        });
				}
	        	
	        });
    		  }
	    	  else targetTable.setWidget(targetRow, col, w);
	
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
	  protected static TreeItem createPathForMember(TreeItem parent, StringTree node ) {
		    MemberSelectionLabel memberLabel = new MemberSelectionLabel(node.getValue());
		    
		    memberLabel.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		   /*     selectionModePopup.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
		        selectionModePopup.setSource(sender);
		        selectionModePopup.show();*/
		      }
		      
		    });
		    TreeItem childItem = new TreeItem(memberLabel);
		    memberLabel.setTreeItem(childItem);
		    parent.addItem(childItem);
		    for (int i=0; i<node.getChildren().size(); i++) {
		      createPathForMember(childItem, (StringTree)node.getChildren().get(i));
		    }
		    return parent;
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
		     // contextMenu.addItem(ConstantFactory.getInstance().member(), new SelectionModeCommand(MEMBER));     
		  /*    contextMenu.addItem(new MenuItem(MessageFactory.getInstance().children(), new SelectionModeCommand(CHILDREN)));     
		      contextMenu.addItem(new MenuItem(MessageFactory.getInstance().include_children(), new SelectionModeCommand(INCLUDE_CHILDREN)));    
		      contextMenu.addItem(new MenuItem(MessageFactory.getInstance().siblings(), new SelectionModeCommand(SIBLINGS)));   
		      contextMenu.addItem(new MenuItem(MessageFactory.getInstance().clear_selections(), new SelectionModeClearCommand()));*/ 
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
