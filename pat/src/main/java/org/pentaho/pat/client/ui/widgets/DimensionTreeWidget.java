package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IQueryListener;
import org.pentaho.pat.client.listeners.ISelectionListener;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.MemberLabelItem;
import org.pentaho.pat.rpc.dto.StringTree;
import org.pentaho.pat.rpc.dto.enums.ObjectType;

import com.google.gwt.gen2.commonevent.shared.BeforeOpenEvent;
import com.google.gwt.gen2.commonevent.shared.BeforeOpenHandler;
import com.google.gwt.gen2.complexpanel.client.FastTree;
import com.google.gwt.gen2.complexpanel.client.FastTreeItem;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class DimensionTreeWidget extends LayoutComposite implements
		ISelectionListener {

	private SimplePanelDragControllerImpl dragController;
	private FastTree t;
	private DimensionPanel parentPanel;
	public DimensionTreeWidget(SimplePanelDragControllerImpl dragController, DimensionPanel dimensionPanel) {
		this.parentPanel = dimensionPanel;
		this.dragController = dragController;
		this.getLayoutPanel().add(onInitialize());
	}

	FastTreeItem parentItem;
	
	protected Widget onInitialize() {
		t = new FastTree();
		lazyCreateChild(t.getTreeRoot(), 0, 50);

		t.addBeforeOpenHandler(new BeforeOpenHandler<FastTreeItem>() {
			
			public void onBeforeOpen(BeforeOpenEvent<FastTreeItem> event) {
				parentItem = (FastTreeItem) event.getTarget();
				if (event.isFirstTime()) {
					lazyCreateChilds();
				}
			}
		});


			
		final ScrollPanel panel = new ScrollPanel();
		panel.add(t);

		return panel;
			}

	private FastTreeItem createChildLabel(String name, String caption, ObjectType objecttype,  boolean isuniquename, SimplePanelDragControllerImpl controller, 
			boolean draggable){
		
		FastTreeItem fti = new FastTreeItem();
		
		MeasureLabel label = new MeasureLabel(name, caption, objecttype, fti, isuniquename);
		
		label.setDragController(controller);
		
		if(draggable){
			label.makeDraggable();
		}
		if(objecttype.equals(ObjectType.MEASURE)||objecttype.equals(ObjectType.MEMBER)){
			fti.becomeLeaf();
		}
		else{
			fti.becomeInteriorNode();
		}
		fti.setWidget(label);
		return fti;
		
	}
	private void lazyCreateChilds() {
				try {
					final MeasureLabel parentlabel = (MeasureLabel) parentItem
							.getWidget();
					ObjectType labelType = parentlabel.getType();
					if(labelType == null){
						
					}
					else if (labelType == ObjectType.DIMENSION) {
						ServiceFactory.getDiscoveryInstance().getHierarchies(
								Pat.getSessionID(), Pat.getCurrQuery(),
								parentlabel.getText(),
								new AsyncCallback<List<MemberLabelItem>>() {

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(List<MemberLabelItem> arg0) {
										for (int i = 0; i < arg0.size(); i++) {
											
											
											parentItem.addItem(createChildLabel(arg0.get(i).getName(), arg0.get(i).getCaption(), ObjectType.HIERARCHY, parentPanel.isUniqueNameLabel(), dragController,
													parentlabel.isDraggable()));
											
										}

									}

								});
					} else if (labelType == ObjectType.HIERARCHY) {
						//MeasureLabel
						final MeasureLabel w =(MeasureLabel) parentItem.getWidget();
						String name = w.getActualName();
	
						ServiceFactory.getDiscoveryInstance().getLevels(
								Pat.getSessionID(), Pat.getCurrQuery(),
								name,
								new AsyncCallback<ArrayList<MemberLabelItem>>() {

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(ArrayList<MemberLabelItem> arg0) {
										for (int i = 0; i < arg0.size(); i++) {
											parentItem.addItem(createChildLabel(arg0.get(i).getName(), arg0.get(i).getCaption(), ObjectType.LEVEL, parentPanel.isUniqueNameLabel(), dragController,
													parentlabel.isDraggable()));
										}
									
									}
								});
					} else if (labelType == ObjectType.LEVEL) {
						final MeasureLabel w =(MeasureLabel) parentItem.getWidget();

						ServiceFactory.getDiscoveryInstance().getLevelMembers(
								Pat.getSessionID(),
								Pat.getCurrQuery(),
								w.getActualName(),
								new AsyncCallback<List<MemberLabelItem>>() {

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub

									}

									public void onSuccess(List<MemberLabelItem> arg0) {
										for (int i = 0; i < arg0.size(); i++) {
											parentItem.addItem(createChildLabel(arg0.get(i).getName(), arg0.get(i).getCaption(), ObjectType.MEMBER, parentPanel.isUniqueNameLabel(), dragController,
													parentlabel.isDraggable()));
										}

									}

								});
					} else if (labelType == ObjectType.ALLMEMBER) {

					} else if (labelType == ObjectType.MEASURE) {
						ServiceFactory.getDiscoveryInstance().getMeasures(Pat.getSessionID(), Pat.getCurrQuery(),
								new AsyncCallback<List<MemberLabelItem>>(){

							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub
								
							}

							public void onSuccess(List<MemberLabelItem> arg0) {
								List<String> list = new ArrayList<String>();
								list.add("Measures");
								for (int i = 0; i< arg0.size(); i++){
									parentItem.addItem(createChildLabel(arg0.get(i).getName(), arg0.get(i).getCaption(), ObjectType.MEASURE, parentPanel.isUniqueNameLabel(), dragController,
											parentlabel.isDraggable()));
								}
							}
							
						});
					} else {
						MessageBox.alert("Error", "Unknown cell type");
					}

				} finally {
					parentItem.removeStyleName("gwt-FastTreeItem-loading");
				}
			}

			
		


	private void lazyCreateChild(final FastTreeItem parent, final int index,
			final int children) {

	    for ( IAxis.Standard  axis : IAxis.Standard.values()) {
	        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(),
	                Pat.getCurrQuery(), axis,
	                new AsyncCallback<String[]>() {

	                    public void onFailure(Throwable arg0) {
	                        // TODO Auto-generated method stub

	                    }

	                    public void onSuccess(String[] arg0) {

	                        for (int i = 0; i < arg0.length; i++) {
	                            ArrayList<String> path = new ArrayList<String>();
	                            path.add(arg0[i]);
	                            MeasureLabel label;
	                            final FastTreeItem item = new FastTreeItem();
	                            if(arg0[i].equals("Measures")){
	                                label = new MeasureLabel(arg0[i], arg0[i],
	                                        ObjectType.MEASURE, item, parentPanel.isUniqueNameLabel());
	                            }else{
	                                label = new MeasureLabel(arg0[i], arg0[i],
	                                        ObjectType.DIMENSION, item, parentPanel.isUniqueNameLabel());
	                            }
	                            label.setDragController(dragController);
	                            label.makeDraggable();
	                            item.setWidget(label);
	                            item.becomeInteriorNode();
	                            parent.addItem(item);
	                        }

	                    }

	                });
	    }
	}

	public void onMoveCol(String currQuery, int oldcol, int newcol) {
		// TODO Auto-generated method stub
		
	}

	public void onMoveRow(String currQuery, int oldrow, int newrow) {
		// TODO Auto-generated method stub
		
	}

	public void onSelectionChange(String queryID, Widget sender,
			StringTree tree, String type) {
		// TODO Auto-generated method stub
		
	}

	public void onSelectionCleared(String currQuery, MeasureLabel label,
			int[] is, IAxis iAxis) {
		// TODO Auto-generated method stub
		
	}
}
