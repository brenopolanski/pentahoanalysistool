/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */

package org.pentaho.pat.client.ui.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.ISelectionListener;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
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

/**
 * 
 * @author tom(at)wamonline.org.uk
 *
 */
public class DimensionTreeWidget extends LayoutComposite implements
		ISelectionListener {

	/*
	 * THE DIMENSIONLOCATIONS HASHMAP HAS TO BE SYNC'D WITH ANY LOADED QUERY.
	 */
	HashMap<String, IAxis> dimensionLocations = new HashMap<String, IAxis>();
	private SimplePanelDragControllerImpl dragController;
	private FastTree t;
	private DimensionPanel parentPanel;
	public DimensionTreeWidget(SimplePanelDragControllerImpl dragController, DimensionPanel dimensionPanel) {
		this.parentPanel = dimensionPanel;
		this.dragController = dragController;
		this.getLayoutPanel().add(onInitialize());
	}

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onLoad()
     */
    @Override
    public void onLoad(){
        GlobalConnectionFactory.getSelectionInstance().addSelectionListener(DimensionTreeWidget.this);
    }
    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onUnload()
     */
    @Override
    public void onUnload(){
        GlobalConnectionFactory.getSelectionInstance().removeSelectionListener(DimensionTreeWidget.this);
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
	                            dimensionLocations.put(arg0[i], IAxis.UNUSED);
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

	private void enableDrag(FastTreeItem fti) {
        ((MeasureLabel) fti.getWidget()).makeDraggable();
        for (int i = 0; i < fti.getChildCount(); i++) {

            ((MeasureLabel) fti.getChild(i).getWidget()).makeDraggable();
            for (int j = 0; j < fti.getChild(i).getChildCount(); j++) {
                enableDrag(fti.getChild(i).getChild(j));
            }
        }
    }

    private void disableDrag(FastTreeItem fti) {
        ((MeasureLabel) fti.getWidget()).makeNotDraggable();
        for (int i = 0; i < fti.getChildCount(); i++) {

            ((MeasureLabel) fti.getChild(i).getWidget()).makeNotDraggable();
            for (int j = 0; j < fti.getChild(i).getChildCount(); j++) {
                disableDrag(fti.getChild(i).getChild(j));
            }
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
		String name=((MeasureLabel)sender).getActualName();
		
		String[] split = name.split("[*.?]");
		split[0]=split[0].substring(1);

		
		for(int i =0; i<t.getChildCount(); i++){
			MeasureLabel l = (MeasureLabel)t.getChild(i).getWidget();
			if(l.getActualName().equals(split[0])){
				l.makeNotDraggable();
				for (int j =0; j<t.getChild(i).getChildCount(); j++){
					disableDrag(t.getChild(i).getChild(j));
				}
			}
		}
		
	}

	public void onSelectionCleared(String currQuery, MeasureLabel label,
			int[] is, IAxis iAxis) {
		String name=label.getDimensionName();
		
		for(int i =0; i<t.getChildCount(); i++){
			MeasureLabel l = (MeasureLabel)t.getChild(i).getWidget();
			if(l.getActualName().equals(name)){
				l.makeDraggable();
				for (int j =0; j<t.getChild(i).getChildCount(); j++){
					enableDrag(t.getChild(i).getChild(j));
				}
			}
		}
		
	}

	public void setDimensionLocation(IAxis axis, String dimension){
		
		IAxis ax = dimensionLocations.get(dimension);
		if(ax != null){
			dimensionLocations.put(dimension, axis);
		}
	}
	
	public IAxis getDimensionLocation(String dimension){
		return dimensionLocations.get(dimension);
	}
}
