package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class SelectionModePopup extends PopupPanel{
	  public static final int MEMBER = 0;
	  public static final int CHILDREN = 1;
	  public static final int INCLUDE_CHILDREN = 2;
	  public static final int SIBLINGS = 3;
	  public static final int CLEAR = -1;

	  Integer selectionValue = new Integer(0); // Member

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
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().member(), new SelectionModeCommand(MEMBER)));
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().children(), new SelectionModeCommand(CHILDREN)));
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().include_children(), new SelectionModeCommand(INCLUDE_CHILDREN)));
	    menuBar.addItem(new MenuItem(ConstantFactory.getInstance().siblings(), new SelectionModeCommand(SIBLINGS)));
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
	    protected int selectionMode = -1;
	    /**
	     * @param selectionMode 
	     * @param member
	     */
	    public SelectionModeCommand(int selectionMode) {
	      this.selectionMode = selectionMode;
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
	      final MemberSelectionLabel targetLabel = (MemberSelectionLabel)getSource();
	      String dimName = getDimensionName(targetLabel);
	  
	      List<String> dimSelections = Arrays.asList(targetLabel.getFullPath());
	
	      
	      /*String dimName = getDimensionName(tItem);
			List<String> dimSelections = new ArrayList<String>();
			dimSelections.add(tText);*/
			//String test = targetLabel.getFullPath()[0];
	      String selection = setSelectionMode(selectionMode);
	      ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), dimName, dimSelections,
					selection, new AsyncCallback(){

				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					SelectionModePopup.this.hide();
					MessageBox.info(ConstantFactory.getInstance().error(), MessageFactory.getInstance().no_selection_set(arg0.getLocalizedMessage()));
				       //Window.alert(MessageFactory.getInstance().no_selection_set(caught.getLocalizedMessage()));			
				}

				public void onSuccess(Object arg0) {
					// TODO Auto-generated method stub
					targetLabel.setSelectionMode(selectionMode);
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

	/**
	 *TODO JAVADOC
	 *
	 * @param event
	 * @param selectedItem
	 */
	public void showContextMenu(Event event, TreeItem selectedItem) {
		if (contextMenu == null) {
	     init();
	    }
		setSource(selectedItem.getWidget());
		
	}

	  public String setSelectionMode(int selectionMode) { 
		  String selection = "";
	  switch (selectionMode) { 
	  case 0: selection = "MEMBER"; 
	  break; 
	  case 1: selection = "CHILDREN"; 
	  break; 
	  case 2: selection = "INCLUDE_CHILDREN"; 
	  break;
	  case 3: selection = "SIBLINGS"; 
	  }
	   return selection;
	  
	  }
}
