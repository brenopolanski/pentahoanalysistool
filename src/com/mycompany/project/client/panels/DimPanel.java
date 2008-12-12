package com.mycompany.project.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.Tree;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.mycompany.project.client.listeners.ConnectionListener;
import com.mycompany.project.client.util.DraggableTree;
import com.mycompany.project.client.util.GuidFactory;
import com.mycompany.project.client.util.ListBoxDragController;
import com.mycompany.project.client.util.MessageFactory;
import com.mycompany.project.client.util.ServiceFactory;
import com.mycompany.project.client.util.StringTree;
import com.mycompany.project.client.widgets.MemberSelectionLabel;
import com.mycompany.project.client.widgets.MouseListBox;
import com.gwtext.client.data.*;

public class DimPanel extends FlexTable implements ConnectionListener{
	  public static final String AXIS_NONE = "none"; //$NON-NLS-1$
	  public static final String AXIS_UNUSED = "UNUSED"; //$NON-NLS-1$
	  public static final String AXIS_FILTER = "FILTER"; //$NON-NLS-1$
	  public static final String AXIS_COLUMNS = "COLUMNS"; //$NON-NLS-1$
	  public static final String AXIS_ROWS = "ROWS"; //$NON-NLS-1$
	  public static final String AXIS_PAGES = "PAGES"; //$NON-NLS-1$
	  public static final String AXIS_CHAPTERS = "CHAPTERS"; //$NON-NLS-1$
	  public static final String AXIS_SECTIONS = "SECTIONS"; //$NON-NLS-1$
	private TreePanel tree;
	ComboBox cubeListBox;
	 SelectionModePopup selectionModePopup;
	 MouseListBox dimensionsList;
	 ListBoxDragController dragController;
	 VerticalPanel verticalPanel;
	 Store store;
	 MemoryProxy proxy;
	 TreeNode dimensions = new TreeNode("Dims");
	 RecordDef recordDef;
	public DimPanel() {
		super();
		init();
		
		 
	}
	 private void init() {
		 selectionModePopup = new SelectionModePopup(GuidFactory.getGuid());
		    tree = new TreePanel();
		    
		    
		  proxy = new MemoryProxy(getProxyData());  
		          recordDef = new RecordDef(  
		                  new FieldDef[]{  
		                          new StringFieldDef("noo"),  
		                          new StringFieldDef("nam") 
		                  }  
		          );  
		    
		    ArrayReader reader = new ArrayReader(recordDef);  
		    store = new Store(proxy, reader);  
		    store.load();  
		    cubeListBox = new ComboBox();
		    cubeListBox.setFieldLabel("Cube");
		    cubeListBox.setStore(store);
		    cubeListBox.setDisplayField("nam");  
		    cubeListBox.setMode(ComboBox.LOCAL); 
		    cubeListBox.addListener(new ComboBoxListenerAdapter() {
		    	
		      public void onSelect(ComboBox comboBox, Record record, int index) {
		    	  System.out.println(comboBox.getValueAsString());
		    	  ServiceFactory.getInstance().setCube((String) comboBox.getValueAsString(), GuidFactory.getGuid(), new AsyncCallback() {
		          public void onSuccess(Object result) {
		            populateDimensions();
		          }         
		          public void onFailure(Throwable caught) {}
		        });
		      }
		     
		    });
		              
		    
		    this.setText(0, 1, MessageFactory.getInstance().select_cube());
		    
		    
	        FormPanel form = new FormPanel();  
		    form.setLabelWidth(70);  
		    form.setBorder(false);  
		    form.add(cubeListBox);  
		     
		    
		    this.setWidget(0, 0, form);
		    final VerticalPanel verticalPanel = new VerticalPanel();
		    
		    tree.setRootNode(dimensions);
		    tree.setWidth(300);
		    verticalPanel.add(tree);
		    this.setWidget(1, 0, verticalPanel);
			
			
			
		    
	 }

	 
	  public void populateDimensions() {
			List axis = new ArrayList();
			axis.add(AXIS_NONE);
			axis.add(AXIS_ROWS);
			axis.add(AXIS_COLUMNS);
			axis.add(AXIS_FILTER);
			populateDimensions(axis);
		}
	  
	  public void populateDimensions( List axis) {
		    if (axis.contains(AXIS_NONE)) {
	ServiceFactory.getInstance().getDimensions(AXIS_NONE, GuidFactory.getGuid(), new AsyncCallback() {
		public void onSuccess(Object result) {
			
	          String[] dimStrs = (String[]) result;
//	          dimensionsList.clear();
	          
	      /*    ArrayList<Widget> widgetsList = dimensionsList.widgetList();
	          for (Widget widget: widgetsList) 
	            dimensionsList.remove(widget);
	        */  
	          //for (int i=0; i<dimStrs.length; i++) {
	            //dimensionsList.addItem(dimStrs[i]);
	            
	        	tree = getDimTree(dimStrs);
	        	
	            
	            /*  label.addClickListener(new ClickListener() {

	              public void onClick(Widget arg0) {
	                updateMoveButtons();
	              }
	              
	            });*/
	            
	          //}
	         /* updateMoveButtons();*/
	        }
	        
	        public void onFailure(Throwable caught) {
	          // TODO Auto-generated method stub
	          
	        }
		
	}
	);
		    }
	  }
	  public void getCubes() {
		    ServiceFactory.getInstance().getCubes(GuidFactory.getGuid(), new AsyncCallback() {
		      public void onSuccess(Object result1) {
		        if (result1 != null) {
		        	store.removeAll();
	                store.commitChanges();
		        	System.out.println(store.getCount());	
		        	String[][] cubeNames = (String[][]) result1;
		        	  for (int i = 0; i < cubeNames.length; i++) {
	                    store.add(recordDef.createRecord(cubeNames[i]));
	                }
	                store.commitChanges();
		        }
		        ServiceFactory.getInstance().setCube(cubeListBox.getText(), GuidFactory.getGuid(), new AsyncCallback() {
		          public void onSuccess(Object result2) {
		            populateDimensions();
		          }         
		          public void onFailure(Throwable caught) {}
		        });
		      }      
		      public void onFailure(Throwable caught) {}
		    });
		    
		  }
	  public void addItemToMouseListBox (MouseListBox mouseList, Widget item) {
		    if (mouseList.getCapacitySize() <= mouseList.getWidgetCount()){
		      mouseList.increaseGridSize();
		    }
		    mouseList.add(item);
		  }
	  
	  
	  protected TreePanel getDimTree(String[] dimStrs) {
		    final TreePanel dimTree = new TreePanel();
		    for (int i=0; i<dimStrs.length; i++) {
		    ServiceFactory.getInstance().getMembers(dimStrs[i], GuidFactory.getGuid(), new AsyncCallback() {
		      public void onSuccess(Object result) {
		    	   
		        StringTree memberTree = (StringTree) result;
		        TreeNode root = new TreeNode(memberTree.getValue());
		        
		        for (int i=0; i<memberTree.getChildren().size(); i++) {
		          root = createPathForMember(root, (StringTree)memberTree.getChildren().get(i));
		        }
		        dimensions.appendChild(root);
		      }
		      
		     public void onFailure(Throwable caught) {
		        // TODO Auto-generated method stub
		        
		      }
		    });
		    }
		    return dimTree;
		  }
	  
	  protected TreeNode createPathForMember(TreeNode parent, StringTree node ) {
		    String memberLabel = new String(node.getValue());
		    /*memberLabel.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		        selectionModePopup.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
		        selectionModePopup.setSource(sender);
		        selectionModePopup.show();
		      }
		      
		    });*/
		    TreeNode childItem = new TreeNode(memberLabel);
		  //  memberLabel.setTreeItem(childItem);
		    parent.appendChild(childItem);
		    for (int i=0; i<node.getChildren().size(); i++) {
		      createPathForMember(childItem, (StringTree)node.getChildren().get(i));
		    }
		    return parent;
		  }
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}
	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		getCubes();
		
	}
	private String[][] getProxyData() {  
		         String[][] tom ={
		        		 new String[]{"naught", "No Cubes"},
		        		 new String[]{"1", "No more Cubes"}
		         };
		         return tom;
		         }
	
		    }