package com.mycompany.project.client.panels;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.dd.DragSource;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragDrop;

import com.gwtext.client.dd.DropTarget;
import com.gwtext.client.dd.DropTargetConfig;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridDragData;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.tree.TreeDragData;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import com.mycompany.project.client.listeners.ConnectionListener;
import com.mycompany.project.client.util.GuidFactory;
import com.mycompany.project.client.util.ListBoxDragController;
import com.mycompany.project.client.util.ServiceFactory;
import com.mycompany.project.client.util.StringTree;
import com.mycompany.project.client.widgets.MouseListBox;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.DropNodeCallback;

public class DimPanel extends FlexTable implements ConnectionListener {

	public static TreePanel dimTree;
	SelectionModePopup selectionModePopup;
	MouseListBox dimensionsList;
	ListBoxDragController dragController;
	VerticalPanel verticalPanel;
	static TreeNode dimensions = new TreeNode("Dims");
	static RecordDef recordDef;
	static Store store3;
	ColumnConfig columns;
	RecordDef recDef;
	MemoryProxy proxy;
	Button execute;

	public DimPanel() {
		super();
		init();

	}

	private void init() {
		execute = new Button();
		execute.setText("Execute");
		final Store store4 = new SimpleStore(new String[] { "tags" },
				new String[][] {});
		store4.load();
		final Store store2 = new SimpleStore(new String[] { "tags" },
				new String[][] {});
		store2.load();

		proxy = new MemoryProxy(getProxyData());
		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("tags") });

		ArrayReader reader = new ArrayReader(recordDef);
		store3 = new Store(proxy, reader);
		store3.load();

		ColumnConfig[] columns2 = { new ColumnConfig("Columns", "tags", 90) };
		ColumnConfig[] columns = { new ColumnConfig("Rows", "tags", 90) };
		ColumnModel columnModel = new ColumnModel(columns);
		ColumnModel columnModel2 = new ColumnModel(columns2);

		final GridPanel dragndropGridCols = new GridPanel();
		dragndropGridCols.setColumnModel(columnModel2);
		dragndropGridCols.setEnableDragDrop(true);
		dragndropGridCols.setEnableColumnResize(true);
		dragndropGridCols.setStore(store4);
		dragndropGridCols.setDdGroup("myDDGroup");
		// dragndropGridCols.setWidth("50%");
		dragndropGridCols.setHeight(100);

		final GridPanel dragndropGrid = new GridPanel();
		dragndropGrid.setColumnModel(columnModel);
		dragndropGrid.setEnableDragDrop(true);
		dragndropGrid.setEnableColumnResize(true);
		dragndropGrid.setStore(store2);
		dragndropGrid.setDdGroup("myDDGroup");
		// dragndropGrid.setWidth("50%");
		dragndropGrid.setHeight(100);

		dimTree = new TreePanel();
		dimTree.setRootNode(dimensions);
		dimTree.setDdGroup("myDDGroup");
		dimTree.setAnimate(true);
		dimTree.setEnableDD(true);
		dimTree.setContainerScroll(true);
		dimTree.setTitle("Selected Dimensions");
		dimTree.setEnableDrop(true);
		dimTree.setRootVisible(true);
		dimTree.setHeight(300);
		// add trip tree listener that handles move / copy logic
		dimTree.addListener(new TreePanelListenerAdapter() {
			@Override
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
						String country = record.getAsString("tags");
						TreeNode copyNode = new TreeNode(country);
						copyNode.setId(country);

						copyNodes[i] = copyNode;
						target.appendChild(copyNode);

						GridPanel grid = gridDragData.getGrid();
						Store store = grid.getStore();
						store.remove(record);
						store.commitChanges();

						store2.add(record);
						store2.commitChanges();

					}
				}
				return true;
			}
		});

		DropTargetConfig dtc = new DropTargetConfig();
		dtc.setdDdGroup("myDDGroup");

		DropTarget tg = new DropTarget(dragndropGrid, dtc) {
			@Override
			public boolean notifyDrop(DragSource source,
					com.gwtext.client.core.EventObject e, DragData data) {
				if (data instanceof GridDragData)
					return false;

				TreeDragData treeDragData = (TreeDragData) data;
				TreeNode treeNode = treeDragData.getTreeNode();
				String country = treeNode.getText();

				int index = store3.find("tags", country, 0, true, true);
				Record record = store3.getAt(index);
				if (record != null) {
					store2.add(record);
					store2.commitChanges();

					store3.remove(record);
					store3.commitChanges();
				}
				TreeNode node = dimTree.getNodeById(country);
				dimensions.removeChild(node);
				return true;
			}

			@Override
			public String notifyOver(DragSource source, EventObject e,
					DragData data) {
				return "x-dd-drop-ok";
			}
		};

		DropTargetConfig dtc2 = new DropTargetConfig();
		dtc.setdDdGroup("myDDGroup");

		DropTarget tg2 = new DropTarget(dragndropGridCols, dtc) {
			@Override
			public boolean notifyDrop(DragSource source,
					com.gwtext.client.core.EventObject e, DragData data) {
				if (data instanceof GridDragData)
					return false;

				TreeDragData treeDragData = (TreeDragData) data;
				TreeNode treeNode = treeDragData.getTreeNode();
				String country = treeNode.getText();

				int index = store3.find("tags", country, 0, true, true);
				Record record = store3.getAt(index);
				if (record != null) {
					store4.add(record);
					store4.commitChanges();

					store3.remove(record);
					store3.commitChanges();
				}
				TreeNode node = dimTree.getNodeById(country);
				dimensions.removeChild(node);
				return true;
			}

			@Override
			public String notifyOver(DragSource source, EventObject e,
					DragData data) {
				return "x-dd-drop-ok";
			}
		};
		this.setWidget(0, 0, dimTree);
		this.getCellFormatter().setHeight(0, 0, "50%");
		this.setWidget(1, 0, dragndropGrid);
		this.getFlexCellFormatter().setColSpan(0, 0, 2);
		this.getCellFormatter().setHeight(1, 0, "25%");
		this.setWidget(1, 1, dragndropGridCols);
		this.getCellFormatter().setHeight(1, 1, "25%");
		this.setWidget(2, 0, execute);
		this.getCellFormatter().setHeight(2, 0, "20px");
		this.getFlexCellFormatter().setColSpan(2, 0, 2);
	}

	private String[][] getProxyData() {
		// TODO Auto-generated method stub
		String[][] tom = { new String[] { "Empty", "Empty" } };
		return tom;
	}

	protected static void getDimTree(String[] dimStrs) {

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

	protected static TreeNode createPathForMember(TreeNode parent,
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
/*
 * Horrible Horrible Code That Sorts Out Stores For Drag N Drop, drink plenty of beer before reading below this line... You HAVE Been Warned!!
 */
	protected static ArrayList loadupStoreWithChildDimensions(TreeNode parent,
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

	public static void loadupStoreWithDimensions(String[] dimStrs) {
		final ArrayList<String> listing = new ArrayList<String>();

		for (int i = 0; i < dimStrs.length; i++) {

			ServiceFactory.getInstance().getMembers(dimStrs[i],
					GuidFactory.getGuid(), new AsyncCallback() {
						public void onSuccess(Object result) {

							String str[][] = null;
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

								str = new String[listing.size()][1];

								for (int k = 0; k < listing.size(); k++) {
									str[k][0] = listing.get(k);
								}

							}

							for (int j = 0; j < str.length; j++) {
								store3.add(recordDef.createRecord(str[j]));
								store3.commitChanges();
							}
						}

						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});

		}

		// return store3;
	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub

	}
}