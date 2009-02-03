/**
 * 
 */
package org.pentaho.pat.client.panels;

import java.util.ArrayList;
import java.util.List;


import org.pentaho.pat.client.images.SelectionModeImageBundle;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.tree.TreeNode;

/**
 * @author root
 *
 */
public class DimensionContextMenu extends Menu{
	 private TreeNode ctxNode;
	 private String guid;
	 protected SelectionModeImageBundle selectionImageBundle;
	public DimensionContextMenu(TreeNode node, String guid){
		super();
		selectionImageBundle = (SelectionModeImageBundle)GWT.create(SelectionModeImageBundle.class);
		this.ctxNode = node;
		this.guid = guid;
		init();
		
	}
	
	public void init(){
		
		this.setShadow(true);  
		this.setMinWidth(10); 
		
		Item memberItem = new Item("Member", new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {  
					  String[] target = getFullPath();
					  String dimName = getDimensionName();
					  int sel = 0;
					  ServiceFactory.getInstance().createSelection(dimName, target, sel, guid, new AsyncCallback() {
					        public void onFailure(Throwable caught) {
					          Window.alert(MessageFactory.getInstance().no_selection_set(caught.getLocalizedMessage()));
					        }
					        public void onSuccess(Object result) {
					          if (((Boolean)result).booleanValue()) {
					            ctxNode.setIcon(selectionImageBundle.member_select_icon().createImage().getUrl());
					          }
					        }         
					      }); 
			   }
		}); 
		  
		this.addItem(memberItem);  
		   
		Item childrenItem = new Item("Children", new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {  
				  String[] target = getFullPath();
				  String dimName = getDimensionName();
				  int sel = 1;
				  ServiceFactory.getInstance().createSelection(dimName, target, sel, guid, new AsyncCallback() {
				        public void onFailure(Throwable caught) {
				          Window.alert(MessageFactory.getInstance().no_selection_set(caught.getLocalizedMessage()));
				        }
				        public void onSuccess(Object result) {
				          if (((Boolean)result).booleanValue()) {
				            ctxNode.setIcon(selectionImageBundle.children_select_icon().createImage().getUrl());
				          }
				        }         
				      }); 
		   }
	});  
		this.addItem(childrenItem);  
		  
		Item includeChildren = new Item("Include Children", new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {  
				  String[] target = getFullPath();
				  String dimName = getDimensionName();
				  int sel = 2;
				  ServiceFactory.getInstance().createSelection(dimName, target, sel, guid, new AsyncCallback() {
				        public void onFailure(Throwable caught) {
				          Window.alert(MessageFactory.getInstance().no_selection_set(caught.getLocalizedMessage()));
				        }
				        public void onSuccess(Object result) {
				          if (((Boolean)result).booleanValue()) {
				            ctxNode.setIcon(selectionImageBundle.include_children_select_icon().createImage().getUrl());
				          }
				        }         
				      }); 
		   }
	});    
		this.addItem(includeChildren);  
		  
		Item siblingsItem = new Item("Siblings", new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {  
				  String[] target = getFullPath();
				  String dimName = getDimensionName();
				  int sel = 3;
				  ServiceFactory.getInstance().createSelection(dimName, target, sel, guid, new AsyncCallback() {
				        public void onFailure(Throwable caught) {
				          Window.alert(MessageFactory.getInstance().no_selection_set(caught.getLocalizedMessage()));
				        }
				        public void onSuccess(Object result) {
				          if (((Boolean)result).booleanValue()) {
				            ctxNode.setIcon(selectionImageBundle.siblings_select_icon().createImage().getUrl());
				          }
				        }         
				      }); 
		   }
	});    
		this.addItem(siblingsItem);
		
		Item clearItem = new Item("Clear", new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {  
				  String[] target = getFullPath();
				  String dimName = getDimensionName();
				  ServiceFactory.getInstance().clearSelection(dimName, target, guid, new AsyncCallback() {
				        public void onFailure(Throwable caught) {
				          Window.alert(MessageFactory.getInstance().no_selection_set(caught.getLocalizedMessage()));
				        }
				        public void onSuccess(Object result) {
				          if (((Boolean)result).booleanValue()) {
				            ctxNode.setIcon(null);
				            
				          }
				        }         
				      }); 
		   }
	});   
		this.addItem(clearItem);  	
	}
	 
		  /**
		   * @return
		   */
		  private String[] getFullPath() {
			TreeNode currentTreeItem =  ctxNode;
		    List pathList = new ArrayList();
		    pathList.add(currentTreeItem.getText());
		    while (currentTreeItem.getParentNode() != null && currentTreeItem.getId()!="top") {
		    currentTreeItem = (TreeNode) currentTreeItem.getParentNode();
		      pathList.add(0, (currentTreeItem.getText()));
		    }
		    
		    pathList.remove(0);
		    String[] values = new String[pathList.size()];
		    return (String[]) pathList.toArray(values);
		  }
		  
		  /**
		   * @return
		   */
		  private String getDimensionName() {
			TreeNode currentTreeItem =  ctxNode;
		    while (currentTreeItem.getParentNode() != null ) {
		    currentTreeItem = (TreeNode) currentTreeItem.getParentNode();
		    }
		    currentTreeItem = (TreeNode) currentTreeItem.getFirstChild();
		    return (String)currentTreeItem.getText();
		  }
		private int getSelectionNumber(String press){
			  int ret = 0;
			  if (press == "Children") ret=1;
			  else if (press == "Include Children") ret=2;
			  else if (press == "Siblings") ret=3;
			  else if (press == "Clear") ret=-1;
			  
			  return ret;
		}

		   }
