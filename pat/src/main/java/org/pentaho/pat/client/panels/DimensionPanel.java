/**
 * 
 */
package org.pentaho.pat.client.panels;

import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.ServiceFactory;
import org.pentaho.pat.client.util.StringTree;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
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
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.tree.DropNodeCallback;

/**
 * @author Tom Barber
 *
 */
public class DimensionPanel extends Panel implements ConnectionListener  {
	static /*TODO
	 * The Dimension Panel Needs to Handle the listing of the dimension tree and adding the dimensions
	 * to the rows/columns grids in a manner that also allows selecting of children etc(check Original halogen for ideas).
	 * There also needs to be an execute button of some kind, and preferable a 2nd(hidden pane) that users can select to show and
	 * insert MDX code.
	 */
	TreeNode dimensions = new TreeNode("Dims");
	FlexTable flexTable;
	TreePanel dimTree; 
	public DimensionPanel() {
		super();

		init();
	}
	
	public void init(){
		flexTable = new FlexTable();
		this.add(flexTable);
		
		
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

						//store2.add(record);
						//store2.commitChanges();

					}
				}
				return true;
			}
		});

		
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


	

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		
	}

}
