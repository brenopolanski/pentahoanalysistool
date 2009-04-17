package org.pentaho.pat.client.ui.panels;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.MemberSelectionLabel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class SelectionModePopup extends PopupPanel{
	  static Widget source;
	  MenuBar menuBar;
	  Tree tItem;
	  public SelectionModePopup(){
		  //super(false,true);
		  init();
	  }
	  
	  /**
	   * 
	   */
	  protected void init() {
	    menuBar = new MenuBar(true);
	    menuBar.setAutoOpen(true);
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().member(), new SelectionModeCommand("MEMBER")));
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().children(), new SelectionModeCommand("CHILDREN")));
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().include_children(), new SelectionModeCommand("INCLUDE_CHILDREN")));
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().siblings(), new SelectionModeCommand("SIBLINGS")));
	    //menuBar.addItem(new MenuItem(ConstantFactory.getInstance().clear_selections(), new SelectionModeClearCommand()));
	    
	    this.setWidget(menuBar);
	  }

	public static void setSource(Widget source2) {
		source = source2;
	  }


	  public static Widget getSource() {
		    return source;
		  }

	 /**
	   * @author wseyler
	   *
	   */
	  public class SelectionModeCommand implements Command {
	    protected String selectionMode = "";
	    /**
	     * @param member
	     */
	    public SelectionModeCommand(String string) {
	      this.selectionMode = string;
	    }
	    
	    /**
	     * @param targetLabel
	     * @return
	     */
	    protected String getDimensionName(MemberSelectionLabel targetLabel) {
	      Tree tree = (Tree) targetLabel.getParent();
	      TreeItem rootItem = tree.getItem(0);
	      Label rootLabel = (Label) rootItem.getWidget();
	      return rootLabel.getText();
	    }
	   
	    /**
	     * @param targetLabel
	     * @return
	     */
	    protected String getDimensionName(Tree targetLabel) {
	      //Tree tree = (Tree) targetLabel.getParent();
	      TreeItem rootItem = targetLabel.getItem(0);
	      Label rootLabel = (Label) rootItem.getWidget();
	      return rootLabel.getText();
	    }
	   
	    /* (non-Javadoc)
	     * @see com.google.gwt.user.client.Command#execute()
	     */
	    public void execute() {
	      //final MemberSelectionLabel targetLabel = (MemberSelectionLabel)getSource();
	      String dimName = getDimensionName(tItem);
			List<String> dimSelections = new ArrayList<String>();
			dimSelections.add(tText);
			//String test = targetLabel.getFullPath()[0];
	      ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), dimName, dimSelections,
					selectionMode, new AsyncCallback(){

				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
				       //Window.alert(MessageFactory.getInstance().no_selection_set(caught.getLocalizedMessage()));			
				}

				public void onSuccess(Object arg0) {
					// TODO Auto-generated method stub
				//targetLabel.setSelectionMode(selectionMode);
					MessageBox.info("Set", "whoop");
					SelectionModePopup.this.hide();
				}
		
	});
	   
	      //SelectionModePopup.this.hide();
	    }
	  }

	  private PopupMenu contextMenu;
	private String tText;
	  
	  public void showContextMenu(final Event event, String text, Tree treeItem) {
		    if (contextMenu == null) {
		    	this.tItem = treeItem;
		    	this.tText = text;
		     init();
		    }
		    
		  }

	
}
