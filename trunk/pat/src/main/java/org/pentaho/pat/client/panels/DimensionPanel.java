/**
 * 
 */
package org.pentaho.pat.client.panels;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;
import org.pentaho.pat.client.util.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragDrop;
import com.gwtext.client.dd.DragSource;
import com.gwtext.client.dd.DropTarget;
import com.gwtext.client.dd.DropTargetConfig;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridDragData;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.tree.DropNodeCallback;
import com.gwtext.client.widgets.tree.TreeDragData;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * @author root
 *
 */
public class DimensionPanel extends Panel implements ConnectionListener  {

	 /*TODO
	 * The Dimension Panel Needs to Handle the listing of the dimension tree and adding the dimensions
	 * to the rows/columns grids in a manner that also allows selecting of children etc(check Original halogen for ideas).
	 * There also needs to be an execute button of some kind, and preferable a 2nd(hidden pane) that users can select to show and
	 * insert MDX code.
	 */
	private static final String AXIS_NONE = "none"; //$NON-NLS-1$
	private static final String AXIS_UNUSED = "UNUSED"; //$NON-NLS-1$
	private static final String AXIS_FILTER = "FILTER"; //$NON-NLS-1$
	private static final String AXIS_COLUMNS = "COLUMNS"; //$NON-NLS-1$
	private static final String AXIS_ROWS = "ROWS"; //$NON-NLS-1$
	private static final String AXIS_PAGES = "PAGES"; //$NON-NLS-1$
	private static final String AXIS_CHAPTERS = "CHAPTERS"; //$NON-NLS-1$
	private static final String AXIS_SECTIONS = "SECTIONS"; //$NON-NLS-1$
	
	private static  TreeNode rowNode = new TreeNode("Rows");
	private static  TreeNode columnNode = new TreeNode("Columns");
	private TreePanel rowTree;
	private TreePanel colTree; 
	private static  RecordDef recordDef;
	private static  Store dimensionStore;
	private MemoryProxy proxy;
	private Store colStore;
	private Store rowStore;
	private GridPanel gridDimensions;
	private Panel gridWrapperPanel;
	private Panel rowWrapperPanel;
	private Panel colWrapperPanel;
	private DropTargetConfig dimensionDTC;
	private DropTarget dimensionTg;
	private DropTargetConfig colDTC;
	private DropTarget colTg;
	
	public DimensionPanel() {
		super();

		init();
	}
	
	public void init(){
		ColumnConfig[] columns = { new ColumnConfig("Dimensions", "tags", 90) };
		ColumnModel columnModel = new ColumnModel(columns);
		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("tags") });
		proxy = new MemoryProxy(getProxyData());

		ArrayReader reader = new ArrayReader(recordDef);
		dimensionStore = new Store(proxy, reader);
		dimensionStore.load();
		
		colStore = new SimpleStore(new String[] { "tags" },
				new String[][] {});
		colStore.load();
		rowStore = new SimpleStore(new String[] { "tags" },
				new String[][] {});
		rowStore.load();
		
		this.setAutoScroll(false);
		
		
		rowWrapperPanel = new Panel();  
		rowWrapperPanel.setAutoScroll(true);
		rowWrapperPanel.setWidth("100%");
		rowWrapperPanel.setAutoWidth(true);
		rowWrapperPanel.setHeight(200);
		 
		colWrapperPanel = new Panel();  
		colWrapperPanel.setAutoScroll(true);
		colWrapperPanel.setWidth("100%");
		colWrapperPanel.setAutoWidth(true);
		colWrapperPanel.setHeight(200);
		
		gridWrapperPanel = new Panel();  
		gridWrapperPanel.setAutoScroll(true);
		gridWrapperPanel.setWidth("100%");
		gridWrapperPanel.setHeight(110);
		//gridWrapperPanel.setLayout(new HorizontalLayout(1)); 
		
		gridDimensions = new GridPanel();
		gridDimensions.setColumnModel(columnModel);
		gridDimensions.setEnableDragDrop(true);
		gridDimensions.setEnableColumnResize(true);
		gridDimensions.setStore(dimensionStore);
		gridDimensions.setDdGroup("myDDGroup");
		gridDimensions.setHeight(100);
		gridDimensions.setAutoWidth(true);
		gridDimensions.setAutoHeight(true);
		
		
		dimensionDTC = new DropTargetConfig();
		dimensionDTC.setdDdGroup("myDDGroup");

		dimensionTg = new DropTarget(gridDimensions, dimensionDTC) {
			
			public boolean notifyDrop(DragSource source,
				EventObject e, DragData data) {
				if (data instanceof GridDragData)
					return false;

				TreeDragData treeDragData = (TreeDragData) data;
				TreeNode treeNode = treeDragData.getTreeNode();
				String nodeText = treeNode.getText();

				int index = rowStore.find("tags", nodeText, 0, true, true);
				Record record = rowStore.getAt(index);
				if (record != null) {
					dimensionStore.add(record);
					dimensionStore.commitChanges();

					rowStore.remove(record);
					rowStore.commitChanges();
				}
				TreeNode node = rowTree.getNodeById(nodeText);
				rowNode.removeChild(node);
				//moveDimension(nodeText, "ROWS");
				return true;
			}

			public String notifyOver(DragSource source, EventObject e,
					DragData data) {
				return "x-dd-drop-ok";
			}
		};
		gridWrapperPanel.add(gridDimensions);
		
		
		rowTree = new TreePanel();
		rowTree.setRootNode(rowNode);
		rowTree.setDdGroup("myDDGroup");
		rowTree.setAnimate(true);
		rowTree.setEnableDD(true);
		rowTree.setContainerScroll(true);
		rowTree.setEnableDrop(true);
		rowTree.setRootVisible(true);
		rowTree.setAutoWidth(true);
		rowTree.setAutoHeight(true);
		
		//rowTree.setDisabled(true);
		// add trip tree listener that handles move / copy logic
		rowTree.addListener(new TreePanelListenerAdapter() {
			
			public boolean doBeforeNodeDrop(TreePanel treePanel,
					TreeNode target, DragData dragData, String point,
					DragDrop source, TreeNode dropNode,
					DropNodeCallback dropDropNodeCallback) {
				if (dragData instanceof GridDragData) {
					GridDragData gridDragData = (GridDragData) dragData;
					Record[] records = gridDragData.getSelections();
					for (int i = 0; i < records.length; i++) {
						Record record = records[i];
						String dimension = record.getAsString("tags");
						GridPanel grid = gridDragData.getGrid();
						Store store = grid.getStore();
						store.remove(record);
						store.commitChanges();

						rowStore.add(record);
						rowStore.commitChanges();
						moveDimension(dimension, "ROWS");
					}
				}
				return true;
			}
		});
		
		rowTree.addListener(new TreePanelListenerAdapter() {
		     public void onContextMenu(TreeNode node, EventObject e) {
		         // logic to create context menu depending on which node was clicked
		         
		        Menu menu = new Menu();
		        // add menu items
		        
		        //display menu where node was right clicked
		        menu.showAt(e.getXY());
		    }

		}); 
		
		colTree = new TreePanel();
		colTree.setRootNode(columnNode);
		colTree.setDdGroup("myDDGroup");
		colTree.setAnimate(true);
		colTree.setEnableDD(true);
		colTree.setContainerScroll(true);
		colTree.setEnableDrop(true);
		colTree.setRootVisible(true);
		colTree.setAutoWidth(true);
		colTree.setAutoHeight(true);
		//colTree.setDisabled(true);
		// add trip tree listener that handles move / copy logic
		colTree.addListener(new TreePanelListenerAdapter() {
			
			public boolean doBeforeNodeDrop(TreePanel treePanel,
					TreeNode target, DragData dragData, String point,
					DragDrop source, TreeNode dropNode,
					DropNodeCallback dropDropNodeCallback) {
				if (dragData instanceof GridDragData) {
					GridDragData gridDragData = (GridDragData) dragData;
					Record[] records = gridDragData.getSelections();
					
					for (int i = 0; i < records.length; i++) {
						Record record = records[i];
						String dimension = record.getAsString("tags");
						GridPanel grid = gridDragData.getGrid();
						Store store = grid.getStore();
						store.remove(record);
						store.commitChanges();

						colStore.add(record);
						colStore.commitChanges();
						moveDimension(dimension, "COLUMNS");
					}
				}
				return true;
			}
		});

		colTree.addListener(new TreePanelListenerAdapter() {
		     public void onContextMenu(TreeNode node, EventObject e) {
		         // logic to create context menu depending on which node was clicked
		         
		        Menu menu = new Menu();
		        // add menu items
		        
		        //display menu where node was right clicked
		        menu.showAt(e.getXY());
		    }

		}); 
		rowWrapperPanel.add(rowTree);
		colWrapperPanel.add(colTree);
		this.add(gridWrapperPanel);
		this.add(rowWrapperPanel);
		this.add(colWrapperPanel);
		
	}
	
	private static TreeNode getDimTree(String dimStrs) {
		final TreeNode parent = new TreeNode();

			ServiceFactory.getInstance().getMembers(dimStrs,
					GuidFactory.getGuid(), new AsyncCallback() {
						public void onSuccess(Object result) {
							StringTree memberTree = (StringTree) result;
							TreeNode root = new TreeNode(memberTree.getValue());
							parent.setId(memberTree.getValue());
							parent.setText(memberTree.getValue());
							for (int i = 0; i < memberTree.getChildren().size(); i++) {
								root = createPathForMember(root, memberTree
										.getChildren().get(i));
							}
							//rows.appendChild(root);
							parent.appendChild(root);

						}
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});
			return parent;
		}
	
		private static TreeNode createPathForMember(TreeNode parent,
				StringTree node) {
			String memberLabel = new String(node.getValue());

			TreeNode childItem = new TreeNode(memberLabel);
			childItem.setId(memberLabel);
			parent.appendChild(childItem);
			for (int i = 0; i < node.getChildren().size(); i++) {
				createPathForMember(childItem, node.getChildren().get(i));

			}
			return parent;
		}
		
		private String[][] getProxyData() {
			// TODO Auto-generated method stub
			String[][] list = { new String[] { "Empty", "Empty" } };
			return list;
		}


		public static void populateDimensions() {
			List axis = new ArrayList();
			axis.add(AXIS_NONE);
			axis.add(AXIS_ROWS);
			axis.add(AXIS_COLUMNS);
			axis.add(AXIS_FILTER);
			populateDimensions(axis);
		}

		public static void populateDimensions(List axis) {
			if (axis.contains(AXIS_NONE)) {
				ServiceFactory.getInstance().getDimensions(AXIS_NONE,
						GuidFactory.getGuid(), new AsyncCallback() {
							public void onSuccess(Object result) {
								String[] dimStrs = (String[]) result;
								String dimStr[][] = null;
								dimensionStore.removeAll();
								if (dimStrs.length > 0) {
									dimStr = new String[dimStrs.length][1];
									for (int k = 0; k < dimStrs.length; k++) {
										dimStr[k][0] = dimStrs[k];
									}

								}
								for (int j = 0; j < dimStr.length; j++) {
									
									dimensionStore.add(recordDef.createRecord(dimStr[j]));
									dimensionStore.commitChanges();
								}
								/*loadupStoreWithDimensions((String[])result);
								getDimTree((String[])result);
								dimTree.setDisabled(false);*/
								//gridCols.setDisabled(false);
								//gridRows.setDisabled(false);
							}

							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

						});
			}
		    if (axis.contains(AXIS_ROWS)) {
		      ServiceFactory.getInstance().getDimensions(AXIS_ROWS, GuidFactory.getGuid(), new AsyncCallback() {
		  
		        public void onSuccess(Object result) {
		        	String[] dimStrs = (String[]) result; 
		        	for (int i=0; i<dimStrs.length; i++) {
		        	rowNode.appendChild(getDimTree(dimStrs[i]));
		        	}
		        }
		        
		        public void onFailure(Throwable caught) {
		          // TODO Auto-generated method stub
		        }
		      });
		    }
		    
		    if (axis.contains(AXIS_COLUMNS)) {
		      ServiceFactory.getInstance().getDimensions(AXIS_COLUMNS, GuidFactory.getGuid(), new AsyncCallback() {
		  
		        public void onSuccess(Object result) {
		        	String[] dimStrs = (String[]) result; 
		        	for (int i=0; i<dimStrs.length; i++) {
		        	columnNode.appendChild(getDimTree(dimStrs[i]));
		        }
		        }
		        
		        public void onFailure(Throwable caught) {
		          // TODO Auto-generated method stub
		        }
		      });
		    }
		    
		    if (axis.contains(AXIS_FILTER)) {
		      ServiceFactory.getInstance().getDimensions(AXIS_FILTER, GuidFactory.getGuid(), new AsyncCallback() {

		        public void onSuccess(Object result) {
		        
		        }
		        
		        public void onFailure(Throwable caught) {
		          // TODO Auto-generated method stub
		          
		        }     
		      });
		    }
		}

		
		
				public void moveDimension(String dim, String axis){
			    final String finalAxis = axis;
				  System.out.println("init");
			    ServiceFactory.getInstance().moveDimension(axis, dim, GuidFactory.getGuid(), new AsyncCallback() {
			      public void onSuccess(Object result) {
			    	  System.out.println("success");
			        boolean success = ((Boolean)result).booleanValue();
			      if (success) {
			        List axisList = new ArrayList();
			        axisList.add(AXIS_NONE);
			        axisList.add(finalAxis);
			        populateDimensions(axisList);
			        System.out.println("success");
			      }
			      }

			      public void onFailure(Throwable arg0) {
			        // TODO Auto-generated method stub
			    	  System.out.println(arg0);
			      }
			    });  
			  }
				
				
	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}

}
