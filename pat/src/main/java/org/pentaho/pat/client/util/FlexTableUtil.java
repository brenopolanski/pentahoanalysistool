package org.pentaho.pat.client.util;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.MemberSelectionLabel;
import org.pentaho.pat.client.ui.widgets.SelectionModePopup;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.beans.Axis;
import org.pentaho.pat.rpc.beans.StringTree;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class FlexTableUtil {
	
	 
	 public FlexTableUtil() {
		
	 }
	/**
	 * Copy an entire FlexTable from one FlexTable to another. Each element is
	 * copied by creating a new {@link HTML} widget by calling
	 * {@link FlexTable#getHTML(int, int)} on the source table.
	 * 
	 * @param sourceTable
	 *            the FlexTable to copy a row from
	 * @param targetTable
	 *            the FlexTable to copy a row to
	 * @param sourceRow
	 *            the index of the source row
	 * @param targetRow
	 *            the index before which to insert the copied row
	 */
	public static void copyRow(FlexTable sourceTable, FlexTable targetTable, int sourceRow, int targetRow) {
		targetTable.insertRow(targetRow);
		for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
			HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
			targetTable.setWidget(targetRow, col, html);
		}
		copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
	}

	/**
	 * Move an entire FlexTable from one FlexTable to another. Elements are
	 * moved by attempting to call {@link FlexTable#getWidget(int, int)} on the
	 * source table. If no widget is found (because <code>null</code> is
	 * returned), a new {@link HTML} is created instead by calling
	 * {@link FlexTable#getHTML(int, int)} on the source table.
	 * 
	 * @param sourceTable
	 *            the FlexTable to move a row from
	 * @param targetTable
	 *            the FlexTable to move a row to
	 * @param sourceRow
	 *            the index of the source row
	 * @param targetRow
	 *            the index before which to insert the moved row
	 */
	public static void moveRow(final FlexTable sourceTable, final FlexTable targetTable, int sourceRow, final int targetRow, Axis targetAxis) {
		// targetRow = targetTable.getRowCount();
		if (sourceTable != targetTable) {
			if (sourceTable == targetTable && sourceRow >= targetRow) {
				sourceRow++;
			}
			targetTable.insertRow(targetRow);

			for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
				final int col2 = col;
				final Widget w = sourceTable.getWidget(sourceRow, col);
				if (w != null) {
					if (w instanceof Label == true) {
						ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), targetAxis, w.getElement().getInnerText().trim(),
								new AsyncCallback() {

									public void onFailure(Throwable arg0) {
										MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionSet(arg0.getLocalizedMessage()));
									}

									public void onSuccess(Object arg0) {
										ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), w.getElement().getInnerText().trim(),
												new AsyncCallback<StringTree>() {

													public void onFailure(Throwable arg0) {
														// TODO Auto-generated
														// method stub

													}

													public void onSuccess(StringTree arg0) {
														// TODO Auto-generated
														// method stub
														final Tree dimTree = new Tree(){
															@Override
														public void onBrowserEvent(final Event event) {
													        if (getSelectedItem() != null) {
													          if (event.getTypeInt() == Event.ONCONTEXTMENU) {
													            DOM.eventPreventDefault(event);
													            final SelectionModePopup test = new SelectionModePopup();
													            //test.showContextMenu(event, getSelectedItem().getText(), getSelectedItem().getTree());
													            test.showContextMenu(event, getSelectedItem());
													            test.setPopupPositionAndShow(new PositionCallback() {
													                public void setPosition(int offsetWidth, int offsetHeight) {
													                  test.setPopupPosition(event.getClientX(), event.getClientY());
													                }
													              });
													          }
													        }
													        super.onBrowserEvent(event);
													      }
															@Override
													      protected void setElement(Element elem) {
													        super.setElement(elem);
													        sinkEvents(Event.ONCONTEXTMENU);
													      }
														};
														// TreeItem tn = new
														// TreeItem(w.getElement().getInnerText().trim());
														// dimTree.addItem(tn);

														StringTree memberTree = arg0;
														Label rootLabel = new Label(memberTree.getValue());
														TreeItem root = new TreeItem(rootLabel);
														for (int i = 0; i < memberTree.getChildren().size(); i++) {
															root = createPathForMember(root, memberTree.getChildren().get(i));
														}
														dimTree.addItem(root);
														targetTable.setWidget(targetRow, col2, dimTree);

													}

												});
									}

								});
					} else
						targetTable.setWidget(targetRow, col, w);

				} else {
					final HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
					targetTable.setWidget(targetRow, col, html);
					ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), targetAxis, html.getText().trim(), new AsyncCallback() {

						public void onFailure(Throwable arg0) {
							MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedDimensionList(arg0.getLocalizedMessage()));
						}

						public void onSuccess(Object arg0) {

						}

					});
				}
			}
			copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
			sourceTable.removeRow(sourceRow);
		}

	}

	/**
	 *TODO JAVADOC
	 *
	 * @param parent
	 * @param node
	 * @return
	 */
	protected static TreeItem createPathForMember(TreeItem parent, StringTree node) {
		MemberSelectionLabel memberLabel = new MemberSelectionLabel(node.getValue());

		TreeItem childItem = new TreeItem(memberLabel);
		
		memberLabel.setTreeItem(childItem);
		parent.addItem(childItem);
		for (int i = 0; i < node.getChildren().size(); i++) {
			createPathForMember(childItem, node.getChildren().get(i));
		}
		return parent;
	}	
	
	  /**
	   * @param targetLabel
	   * @return
	   */
	  protected static String getDimensionName(MemberSelectionLabel targetLabel) {
	    Tree tree = (Tree) targetLabel.getParent();
	    TreeItem rootItem = tree.getItem(0);
	    Label rootLabel = (Label) rootItem.getWidget();
	    return rootLabel.getText();
	  }

	  

	/**
	 * Copies the CSS style of a source row to a target row.
	 * 
	 * @param sourceTable
	 * @param targetTable
	 * @param sourceRow
	 * @param targetRow
	 */
	private static void copyRowStyle(FlexTable sourceTable, FlexTable targetTable, int sourceRow, int targetRow) {
		String rowStyle = sourceTable.getRowFormatter().getStyleName(sourceRow);
		targetTable.getRowFormatter().setStyleName(targetRow, rowStyle);
	}



}
