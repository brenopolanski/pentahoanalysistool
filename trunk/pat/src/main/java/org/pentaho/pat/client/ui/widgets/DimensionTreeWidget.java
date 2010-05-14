package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.ui.widgets.MeasureLabel.LabelType;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.MemberLabelItem;

import com.google.gwt.gen2.commonevent.shared.BeforeOpenEvent;
import com.google.gwt.gen2.commonevent.shared.BeforeOpenHandler;
import com.google.gwt.gen2.complexpanel.client.FastTree;
import com.google.gwt.gen2.complexpanel.client.FastTreeItem;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class DimensionTreeWidget extends LayoutComposite implements
		IQueryListener {

	private SimplePanelDragControllerImpl dragController;

	public DimensionTreeWidget(SimplePanelDragControllerImpl dragController) {
		this.dragController = dragController;
		this.getLayoutPanel().add(onInitialize());
	}

	protected Widget onInitialize() {
		final FastTree t = new FastTree();
		lazyCreateChild(t.getTreeRoot(), 0, 50);

		t.addBeforeOpenHandler(new BeforeOpenHandler<FastTreeItem>() {
			FastTreeItem parentItem;

			private Timer t = new Timer() {
				public void run() {
					lazyCreateChilds();
				}
			};

			private void lazyCreateChilds() {
				try {
					MeasureLabel parentlabel = (MeasureLabel) parentItem
							.getWidget();
					LabelType labelType = parentlabel.getType();

					if (labelType != null && labelType == LabelType.DIMENSION) {
						ServiceFactory.getDiscoveryInstance().getHierarchies(
								Pat.getSessionID(), Pat.getCurrQuery(),
								parentlabel.getText(),
								new AsyncCallback<List<MemberLabelItem>>() {

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(List<MemberLabelItem> arg0) {
										for (int i = 0; i < arg0.size(); i++) {
											MeasureLabel label = new MeasureLabel(arg0.get(i).getParents(), arg0.get(i).getName(),
													arg0.get(i).getCaption(),
													MeasureLabel.LabelType.HIERARCHY);
											label.setDragController(dragController);
											label.makeDraggable();
											FastTreeItem fti = new FastTreeItem(
													label);
											fti.becomeInteriorNode();
											parentItem.addItem(fti);
										}

									}

								});
					} else if (labelType != null
							&& labelType == LabelType.HIERARCHY) {
						//MeasureLabel
						Widget w =parentItem.getWidget();
						String name = ((MeasureLabel)w).getActualName();
						String dimname = ((MeasureLabel)w).getValue().get(0);
						ServiceFactory.getDiscoveryInstance().getLevels(
								Pat.getSessionID(), Pat.getCurrQuery(),
								dimname,
								name,
								new AsyncCallback<ArrayList<MemberLabelItem>>() {

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(ArrayList<MemberLabelItem> arg0) {
										for (int i = 0; i < arg0.size(); i++) {
											MeasureLabel label = new MeasureLabel(arg0.get(i).getParents(), arg0.get(i).getName(),
													arg0.get(i).getCaption(),
													MeasureLabel.LabelType.LEVEL);
											label.setDragController(dragController);
											label.makeDraggable();
											FastTreeItem fti = new FastTreeItem(
													label);
											fti.becomeInteriorNode();
											parentItem.addItem(fti);
										}
									}

								});
					} else if (labelType != null
							&& labelType == LabelType.LEVEL) {
						Widget w =parentItem.getWidget();
						String name = ((MeasureLabel)w).getActualName();
						String dimname = ((MeasureLabel)w).getValue().get(0);
						String hiername = ((MeasureLabel)w).getValue().get(1);
						ServiceFactory.getDiscoveryInstance().getLevelMembers(
								Pat.getSessionID(),
								Pat.getCurrQuery(),
								dimname,
								hiername,
								name,
								new AsyncCallback<String[]>() {

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(String[] arg0) {
										for (int i = 0; i < arg0.length; i++) {
											ArrayList<String> path = new ArrayList<String>();
											path.add(parentItem.getParentItem().getParentItem()
											.getText());
											path.add(parentItem.getParentItem().getText());
											path.add(arg0[i]);
											MeasureLabel label = new MeasureLabel(path, "",
													arg0[i],
													MeasureLabel.LabelType.MEMBER);
											
											FastTreeItem fti = new FastTreeItem(
													label);
											fti.becomeInteriorNode();
											parentItem.addItem(fti);
										}

									}

								});
					} else if (labelType != null
							&& labelType == LabelType.ALLMEMBER) {

					} else if (labelType != null
							&& labelType == LabelType.MEASURE) {

					} else {
						MessageBox.alert("Error", "Unknown cell type");
					}

				} finally {
					parentItem.removeStyleName("gwt-FastTreeItem-loading");
				}
			}

			public void onBeforeOpen(BeforeOpenEvent<FastTreeItem> event) {
				parentItem = (FastTreeItem) event.getTarget();
				if (event.isFirstTime()) {
					parentItem.addStyleName("gwt-FastTreeItem-loading");
					t.schedule(333);
				}
			}
		});

		final ScrollPanel panel = new ScrollPanel();
		panel.add(t);

		return panel;
	}

	private void lazyCreateChild(final FastTreeItem parent, final int index,
			final int children) {

		ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(),
				Pat.getCurrQuery(), IAxis.UNUSED,
				new AsyncCallback<String[]>() {

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(String[] arg0) {

						for (int i = 0; i < arg0.length; i++) {
							ArrayList<String> path = new ArrayList<String>();
							path.add(arg0[i]);
							MeasureLabel label = new MeasureLabel(path ,arg0[i], arg0[i],
									MeasureLabel.LabelType.DIMENSION);
							label.setDragController(dragController);
							label.makeDraggable();
							final FastTreeItem item = new FastTreeItem(label);
							item.becomeInteriorNode();
							parent.addItem(item);
						}

					}

				});
	}

	public void onQueryChange(Widget sender, int sourceRow,
			boolean isSourceRow, IAxis sourceAxis, IAxis targetAxis) {
		// TODO Auto-generated method stub

	}

	public void onQueryExecuted(String queryId, CellDataSet matrix) {
		// TODO Auto-generated method stub

	}

	public void onQueryPivoted(String queryId) {
		// TODO Auto-generated method stub

	}

}
