package com.mycompany.project.client.panels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.RecordDef;
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

	public DimPanel() {
		super();
		init();

	}

	private void init() {
		tree = new TreePanel();
		// this.setText(0, 1, MessageFactory.getInstance().select_cube());
		tree.setRootNode(dimensions);
		tree.setSize("100%", "100%");
		verticalPanel = new VerticalPanel();
		verticalPanel.add(tree);
		verticalPanel.setSize("100%", "100%");
		this.setWidget(1, 0, verticalPanel);

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