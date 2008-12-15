package com.mycompany.project.client.panels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.tree.AsyncTreeNode;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.mycompany.project.client.listeners.ConnectionListener;
import com.mycompany.project.client.util.GuidFactory;
import com.mycompany.project.client.util.ListBoxDragController;
import com.mycompany.project.client.util.ServiceFactory;
import com.mycompany.project.client.util.StringTree;
import com.mycompany.project.client.widgets.MouseListBox;

public class DimPanel extends FlexTable implements ConnectionListener {

	public static TreePanel tree;
	SelectionModePopup selectionModePopup;
	MouseListBox dimensionsList;
	ListBoxDragController dragController;
	VerticalPanel verticalPanel;
	static TreeNode dimensions = new TreeNode("Dims");
	RecordDef recordDef;
	GridPanel dragndropPanel;
	Store dragndropStore;
	ColumnConfig columns;
	Store store;
	MemoryProxy proxy;
	Button execute;
	
	public DimPanel() {
		super();
		init();

	}

	private void init() {
		execute = new Button();
		execute.setText("Execute");
		final GridPanel dragndropPanel = new GridPanel();
		//dragndropStore = new Store();
		proxy = new MemoryProxy(getProxyData());
		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("rows"),
				new StringFieldDef("cols") });

		ArrayReader reader = new ArrayReader(recordDef);
		dragndropStore = new Store(proxy, reader);
		dragndropStore.load();
		
		ColumnConfig[] columns = {  
		new ColumnConfig("Rows", "rows", 90),  
		new ColumnConfig("Columns", "cols", 90)  
		};  
		ColumnModel columnModel = new ColumnModel(columns);
		dragndropPanel.setColumnModel(columnModel); 
		tree = new TreePanel();
		// this.setText(0, 1, MessageFactory.getInstance().select_cube());
		tree.setRootNode(dimensions);
		tree.setDdGroup("myDDGroup");
		tree.setAnimate(true);
		tree.setEnableDD(true);
		tree.setEnableDrop(true);
		tree.setContainerScroll(true);
		
		dragndropPanel.setEnableDragDrop(true);
		dragndropPanel.setEnableColumnResize(true);
		dragndropPanel.setStore(dragndropStore);
		dragndropPanel.setDdGroup("myDDGroup");
		this.setWidget(0, 0, tree);
		this.getCellFormatter().setHeight(0,0, "80%");
		this.setWidget(1, 0, dragndropPanel);
		this.getCellFormatter().setHeight(1, 0, "20%");
		this.setWidget(2,0,execute);
		this.getCellFormatter().setHeight(2,0,"20px");
	}

	private String[][] getProxyData() {
		// TODO Auto-generated method stub
		String[][] tom = { new String[] { "Empty", "Empty" }};
		return tom;
	}

	protected static TreePanel getDimTree(String[] dimStrs) {
		final TreePanel dimTree = new TreePanel();
		for (int i = 0; i < dimStrs.length; i++) {
			ServiceFactory.getInstance().getMembers(dimStrs[i],
					GuidFactory.getGuid(), new AsyncCallback() {
						public void onSuccess(Object result) {

							StringTree memberTree = (StringTree) result;
							TreeNode root = new TreeNode(memberTree.getValue());

							for (int i = 0; i < memberTree.getChildren().size(); i++) {
								root = createPathForMember(root,
										memberTree.getChildren()
												.get(i));
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

	protected static TreeNode createPathForMember(TreeNode parent,
			StringTree node) {
		String memberLabel = new String(node.getValue());
		/*
		 * memberLabel.addClickListener(new ClickListener() { public void
		 * onClick(Widget sender) {
		 * selectionModePopup.setPopupPosition(sender.getAbsoluteLeft(),
		 * sender.getAbsoluteTop()); selectionModePopup.setSource(sender);
		 * selectionModePopup.show(); }
		 * 
		 * });
		 */
		TreeNode childItem = new TreeNode(memberLabel);
		// memberLabel.setTreeItem(childItem);
		parent.appendChild(childItem);
		for (int i = 0; i < node.getChildren().size(); i++) {
			createPathForMember(childItem, node.getChildren().get(
					i));
		}
		return parent;
	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub

	}
}