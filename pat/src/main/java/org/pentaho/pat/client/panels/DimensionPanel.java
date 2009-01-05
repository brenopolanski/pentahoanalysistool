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
import com.gwtext.client.core.EventObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
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


import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridDragData;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.tree.TreeDragData;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.DropNodeCallback;

/**
 * @author Tom Barber
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

	private static  TreeNode dimensions = new TreeNode(MessageFactory.getInstance().dimensions());
	private static TreePanel dimTree; 
	private static  RecordDef recordDef;
	private static  Store treeStore;
	private ColumnConfig columns;
	private RecordDef recDef;
	private MemoryProxy proxy;
	private Store colStore;
	private Store rowStore;
	private  GridPanel gridCols;
	private  GridPanel gridRows; 
	private  Panel gridWrapperPanel;
	private Panel wrapperPanel;
	private DropTargetConfig dtc;
	private DropTarget tg;
	public DimensionPanel() {
		super();

		init();
	}
	public void init(){
		this.setAutoScroll(true);
		wrapperPanel = new Panel();  
		wrapperPanel.setAutoScroll(true);
		wrapperPanel.setWidth("100%");
		wrapperPanel.setHeight(300);
		colStore = new SimpleStore(new String[] { "tags" },
				new String[][] {});
		colStore.load();
		rowStore = new SimpleStore(new String[] { "tags" },
				new String[][] {});
		rowStore.load();

		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("tags") });
		proxy = new MemoryProxy(getProxyData());

		ArrayReader reader = new ArrayReader(recordDef);
		treeStore = new Store(proxy, reader);
		treeStore.load();

		ColumnConfig[] columns2 = { new ColumnConfig("Columns", "tags", 90) };
		ColumnConfig[] columns = { new ColumnConfig("Rows", "tags", 90) };
		ColumnModel columnModel = new ColumnModel(columns);
		ColumnModel columnModel2 = new ColumnModel(columns2);
		
		
		dimTree = new TreePanel();
		dimTree.setRootNode(dimensions);
		dimTree.setDdGroup("myDDGroup");
		dimTree.setAnimate(true);
		dimTree.setEnableDD(true);
		dimTree.setContainerScroll(true);
		dimTree.setEnableDrop(true);
		dimTree.setRootVisible(true);
		dimTree.setAutoWidth(true);
		dimTree.setAutoHeight(true);
		dimTree.setDisabled(true);
		// add trip tree listener that handles move / copy logic
		dimTree.addListener(new TreePanelListenerAdapter() {
			
			public boolean doBeforeNodeDrop(TreePanel treePanel,
					TreeNode target, DragData dragData, String point,
					DragDrop source, TreeNode dropNode,
					DropNodeCallback dropDropNodeCallback) {
				if (dragData instanceof GridDragData) {
					GridDragData gridDragData = (GridDragData) dragData;
					Record[] records = gridDragData.getSelections();
					TreeNode[] copyNodes = new TreeNode[records.length];
					for (int i = 0; i < records.length; i++) {
						Record record = records[i];
						String dimension = record.getAsString("tags");
						TreeNode copyNode = new TreeNode(dimension);
						copyNode.setId(dimension);

						copyNodes[i] = copyNode;
						target.appendChild(copyNode);

						GridPanel grid = gridDragData.getGrid();
						Store store = grid.getStore();
						store.remove(record);
						store.commitChanges();

						rowStore.add(record);
						rowStore.commitChanges();

					}
				}
				return true;
			}
		});
		
		gridWrapperPanel = new Panel();  
		gridWrapperPanel.setAutoScroll(true);
		gridWrapperPanel.setWidth("100%");
		gridWrapperPanel.setHeight(110);
		gridWrapperPanel.setLayout(new HorizontalLayout(2)); 
		
		gridCols = new GridPanel();
		gridCols.setColumnModel(columnModel2);
		gridCols.setEnableDragDrop(true);
		gridCols.setEnableColumnResize(true);
		gridCols.setStore(colStore);
		gridCols.setDdGroup("myDDGroup");
		gridCols.setHeight(100);
		gridCols.setAutoWidth(true);
		gridCols.setAutoHeight(true);
		//gridCols.setDisabled(true);
		
		gridRows= new GridPanel();
		gridRows.setColumnModel(columnModel);
		gridRows.setEnableDragDrop(true);
		gridRows.setEnableColumnResize(true);
		gridRows.setStore(rowStore);
		gridRows.setDdGroup("myDDGroup");
		gridRows.setHeight(100);
		gridRows.setAutoWidth(true);
		gridRows.setAutoHeight(true);
		//gridRows.setDisabled(true);
		gridWrapperPanel.add(gridRows);
		gridWrapperPanel.add(gridCols);
		dtc = new DropTargetConfig();
		dtc.setdDdGroup("myDDGroup");

		tg = new DropTarget(gridRows, dtc) {
			
			public boolean notifyDrop(DragSource source,
				EventObject e, DragData data) {
				if (data instanceof GridDragData)
					return false;

				TreeDragData treeDragData = (TreeDragData) data;
				TreeNode treeNode = treeDragData.getTreeNode();
				String country = treeNode.getText();

				int index = treeStore.find("tags", country, 0, true, true);
				Record record = treeStore.getAt(index);
				if (record != null) {
					rowStore.add(record);
					rowStore.commitChanges();

					treeStore.remove(record);
					treeStore.commitChanges();
				}
				TreeNode node = dimTree.getNodeById(country);
				dimensions.removeChild(node);
				moveDimension(country, "ROWS");
				return true;
			}

			public String notifyOver(DragSource source, EventObject e,
					DragData data) {
				return "x-dd-drop-ok";
			}
		};
		
		wrapperPanel.add(dimTree);
		this.add(wrapperPanel);
		this.add(gridWrapperPanel);
	}
	
	private static void getDimTree(String[] dimStrs) {

		for (int i = 0; i < dimStrs.length; i++) {

			ServiceFactory.getInstance().getMembers(dimStrs[i],
					GuidFactory.getGuid(), new AsyncCallback() {
						public void onSuccess(Object result) {
							StringTree memberTree = (StringTree) result;
							TreeNode root = new TreeNode(memberTree.getValue());
							root.setId(memberTree.getValue());
							for (int i = 0; i < memberTree.getChildren().size(); i++) {
								root = createPathForMember(root, memberTree
										.getChildren().get(i));
							}
							dimensions.appendChild(root);

						}
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});
		}
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
								loadupStoreWithDimensions((String[])result);
								getDimTree((String[])result);
								dimTree.setDisabled(false);
								//gridCols.setDisabled(false);
								//gridRows.setDisabled(false);
							}

							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

						});
			}
		}

		private static ArrayList loadupStoreWithChildDimensions(TreeNode parent,
				StringTree node) {
			String memberLabel = new String(node.getValue());

			final ArrayList<String> listing2 = new ArrayList<String>();
			listing2.add(memberLabel);

			TreeNode childItem = new TreeNode(memberLabel);
			parent.appendChild(childItem);
			for (int i = 0; i < node.getChildren().size(); i++) {
				listing2.addAll(loadupStoreWithChildDimensions(childItem, node
						.getChildren().get(i)));

			}
			return listing2;
		}

		private static void loadupStoreWithDimensions(String[] dimStrs) {
			final ArrayList<String> listing = new ArrayList<String>();

			for (int i = 0; i < dimStrs.length; i++) {

				ServiceFactory.getInstance().getMembers(dimStrs[i],
						GuidFactory.getGuid(), new AsyncCallback() {
							public void onSuccess(Object result) {

								String dimStr[][] = null;
								StringTree memberTree = (StringTree) result;
								TreeNode root = new TreeNode(memberTree.getValue());
								listing.clear();
								listing.add(memberTree.getValue());

								for (int i = 0; i < memberTree.getChildren().size(); i++) {

									ArrayList listing2 = (loadupStoreWithChildDimensions(
											root, memberTree.getChildren().get(i)));
									listing.addAll(listing2);
								}

								if (listing.size() > 0) {

									dimStr = new String[listing.size()][1];

									for (int k = 0; k < listing.size(); k++) {
										dimStr[k][0] = listing.get(k);
									}

								}

								for (int j = 0; j < dimStr.length; j++) {
									treeStore.add(recordDef.createRecord(dimStr[j]));
									treeStore.commitChanges();
								}
							}

							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});

			}

		}

		/*
		 * Horrible Horrible Code That Sorts Out Stores For Drag N Drop, drink plenty of beer before reading below this line... You HAVE Been Warned!!
		 *Currently Broken..
		 */
				public void moveDimension(String dim, String axis){
			    final String finalAxis = axis;
				  System.out.println("init");
			    ServiceFactory.getInstance().moveDimension(axis, dim, GuidFactory.getGuid(), new AsyncCallback() {
			      public void onSuccess(Object result) {
			    	  System.out.println("success");
			        boolean success = ((Boolean)result).booleanValue();
			      if (success) {
			        List axisList = new ArrayList();
			        axisList.add(DimensionPanel.AXIS_NONE);
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
	dimTree.setDisabled(true);
	//gridCols.setDisabled(true);
	//gridRows.setDisabled(true);
	}

	public void onConnectionMade(Widget sender) {
	
	}

}
