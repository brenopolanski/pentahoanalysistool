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
package org.pentaho.pat.client.util.dnd.impl;

import org.pentaho.pat.client.ui.widgets.DimensionSimplePanel;
import org.pentaho.pat.client.ui.widgets.DimensionTreeWidget;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.dnd.SimplePanelDropController;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.enums.ObjectType;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * FlexTableRowDropConroller allows flextable cell drops.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class SimplePanelDropControllerImpl extends SimpleDropController implements SimplePanelDropController {

    private final DimensionSimplePanel dropTarget;

    private boolean trash;
    
    public SimplePanelDropControllerImpl(DimensionSimplePanel dropTarget, boolean trash) {
        super(dropTarget);
        this.dropTarget = dropTarget;
        this.trash = trash;
    }

    @Override
    public void onDrop(final DragContext context) {
        MeasureLabel originalLabel = ((MeasureLabel) context.draggable.getParent().getParent());
        if (trash) {
            DimensionSimplePanel panel = (DimensionSimplePanel) originalLabel.getParent();
            context.draggable.removeFromParent();

            originalLabel.makeDraggable();


            if (originalLabel.getType() == ObjectType.DIMENSION) {
                SimplePanelUtil.clearDimension(context, originalLabel, panel.getCoord(), panel.getAxis());
            } else if (originalLabel.getType() == ObjectType.HIERARCHY) {
                SimplePanelUtil.clearHierarchy(context, originalLabel, panel.getCoord(), panel.getAxis());
            } else if (originalLabel.getType() == ObjectType.LEVEL) {
                SimplePanelUtil.clearLevel(context, originalLabel, panel.getCoord(), panel.getAxis());
            } else if (originalLabel.getType() == ObjectType.MEMBER) {
                SimplePanelUtil.clearMember(context, originalLabel, panel.getCoord(), panel.getAxis());
            } else if (originalLabel.getType() == ObjectType.MEASURE) {
                SimplePanelUtil.clearDimension(context, originalLabel, panel.getCoord(), panel.getAxis());
            }

        } else if (originalLabel.getParent() instanceof DimensionSimplePanel && 
        		(((MeasureLabel)context.draggable.getParent().getParent()).getAxis() != ((DimensionSimplePanel)this.dropTarget).getAxis())) {
        	DimensionSimplePanel dp = null;
        	boolean createdrop = true;
            DimensionSimplePanel panel = (DimensionSimplePanel) originalLabel.getParent();
            if(dropTarget.getWidget()==null){
            dropTarget.setWidget(originalLabel);
            }
            else{
            	FlexTable ft = ((FlexTable) ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getParent());
            	int row = dropTarget.getCoord()[0];
        		int col = dropTarget.getCoord()[1];
        		
            	if(dropTarget.getAxis().equals(IAxis.ROWS)|| dropTarget.getAxis().equals(IAxis.FILTER)){
            		for(int i = 0; i<ft.getRowCount(); i++){
            		ft.insertCell(i,col);
            		}
            		dp = new DimensionSimplePanel(dropTarget.getAxis());
        			dp.setWidget(originalLabel);
            		ft.setWidget(row, col, dp);
            		createdrop = false;
            	}
            	else if(dropTarget.getAxis().equals(IAxis.COLUMNS)){
            		ft.insertRow(row);
            		dp = new DimensionSimplePanel(dropTarget.getAxis());
        			dp.setWidget(originalLabel);
        			ft.setWidget(row, col, dp);
        			createdrop = false;
            	}
            }
            
            
            if (originalLabel.getType() == ObjectType.DIMENSION) {
                originalLabel.setAxis(((DimensionSimplePanel) this.dropTarget).getAxis());
                SimplePanelUtil.moveDimension(context, originalLabel, originalLabel, false, panel.getCoord(), panel.getAxis(), createdrop);
                if(dp!=null){
                	if(dp.getCoord()[0]<dropTarget.getCoord()[0]||dp.getCoord()[1]<dropTarget.getCoord()[1]){
                		SimplePanelUtil.pullUp(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                	else{
                		SimplePanelUtil.pushDown(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                }
            } else if (originalLabel.getType() == ObjectType.HIERARCHY) {
                originalLabel.setAxis(((DimensionSimplePanel) this.dropTarget).getAxis());
                SimplePanelUtil.moveHierarchy(context, originalLabel, false, panel.getCoord(), panel.getAxis(), createdrop);
                if(dp!=null){
                	if(dp.getCoord()[0]<dropTarget.getCoord()[0]||dp.getCoord()[1]<dropTarget.getCoord()[1]){
                		SimplePanelUtil.pullUp(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                	else{
                		SimplePanelUtil.pushDown(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                }
            } else if (originalLabel.getType() == ObjectType.LEVEL) {
                originalLabel.setAxis(((DimensionSimplePanel) this.dropTarget).getAxis());
                SimplePanelUtil.moveLevel(context, originalLabel, false, panel.getCoord(), panel.getAxis(), false);
                if(dp!=null){
                	if(dp.getCoord()[0]<dropTarget.getCoord()[0]||dp.getCoord()[1]<dropTarget.getCoord()[1]){
                		SimplePanelUtil.pullUp(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                	else{
                		SimplePanelUtil.pushDown(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                }
            } else if (originalLabel.getType() == ObjectType.MEMBER) {
                originalLabel.setAxis(((DimensionSimplePanel) this.dropTarget).getAxis());
                SimplePanelUtil.moveMember(context, originalLabel, false, panel.getCoord(), panel.getAxis(), createdrop);
                if(dp!=null){
                	if(dp.getCoord()[0]<dropTarget.getCoord()[0]||dp.getCoord()[1]<dropTarget.getCoord()[1]){
                		SimplePanelUtil.pullUp(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                	else{
                		SimplePanelUtil.pushDown(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                }
            } else if (originalLabel.getType() == ObjectType.MEASURE) {
                originalLabel.setAxis(((DimensionSimplePanel) this.dropTarget).getAxis());
                DimensionTreeWidget dtw = (DimensionTreeWidget) originalLabel.getParentNode().getTree().getParent().getParent().getParent();
            	dtw.setMeasureAxis(dropTarget.getAxis());
                SimplePanelUtil.moveMeasure(context, originalLabel, false, panel.getCoord(), panel.getAxis(), createdrop);
                if(dp!=null){
                	if(dp.getCoord()[0]<dropTarget.getCoord()[0]||dp.getCoord()[1]<dropTarget.getCoord()[1]){
                		SimplePanelUtil.pullUp(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                	else{
                		SimplePanelUtil.pushDown(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                }
            }

        } else if(originalLabel.getParent() instanceof DimensionSimplePanel && (((MeasureLabel)context.draggable.getParent().getParent()).getAxis() == ((DimensionSimplePanel)this.dropTarget).getAxis())){
            DimensionSimplePanel panel = ((DimensionSimplePanel)originalLabel.getParent());
            if(panel.getAxis().equals(IAxis.COLUMNS)){
            if(panel.getCoord()[0] > ((DimensionSimplePanel)this.dropTarget).getCoord()[0]){
            	if(originalLabel.getType().equals(ObjectType.MEASURE) || originalLabel.getType().equals(ObjectType.MEMBER)){
            		SimplePanelUtil.pullUpMeasember(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.COLUMNS, true);
            	}
            	else{
                //pushup
                SimplePanelUtil.pullUp(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.COLUMNS, true);
            	}
            } 
            else {
            	if(originalLabel.getType().equals(ObjectType.MEASURE) || originalLabel.getType().equals(ObjectType.MEMBER)){
            		SimplePanelUtil.pushdownMeasember(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.COLUMNS, true);
            	}
            	else{
                //pulldown
                SimplePanelUtil.pushDown(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.COLUMNS, true);
            	}
            }
        }
            else if(panel.getAxis().equals(IAxis.ROWS)){
                if(panel.getCoord()[1] > ((DimensionSimplePanel)this.dropTarget).getCoord()[1]){
                	if(originalLabel.getType().equals(ObjectType.MEASURE) || originalLabel.getType().equals(ObjectType.MEMBER)){
                		SimplePanelUtil.pullUpMeasember(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.ROWS, true);
                	}
                	else{
                    //pushup
                    SimplePanelUtil.pullUp(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.ROWS, true);
                	}
                } 
                else {
                	if(originalLabel.getType().equals(ObjectType.MEASURE)||originalLabel.getType().equals(ObjectType.MEMBER)){
                		SimplePanelUtil.pushdownMeasember(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.ROWS, true);
                	}
                	else{
                    //pulldown
                    SimplePanelUtil.pushDown(context, originalLabel, panel.getCoord(), ((DimensionSimplePanel)this.dropTarget).getCoord(), IAxis.ROWS, true);
                	}
                }   
            }
        }
        else {

            MeasureLabel label = new MeasureLabel(originalLabel.getActualName(),
                    originalLabel.getText(), originalLabel.getType(), originalLabel.getParentNode(), originalLabel
                            .isUniqueName());
            label.setDragController(originalLabel.getDragController());
            label.makeDraggable();
            label.setDownButtonVisible(true);
            dropTarget.setWidget(label);

            originalLabel.makeNotDraggable();

            if (originalLabel.getType() == ObjectType.DIMENSION) {
                SimplePanelUtil.moveDimension(context, label, originalLabel, true, null, null, true);
            } else if (originalLabel.getType() == ObjectType.HIERARCHY) {
                SimplePanelUtil.moveHierarchy(context, label, true, null, null, true);
            } else if (originalLabel.getType() == ObjectType.LEVEL) {
                SimplePanelUtil.moveLevel(context, label, true, null, null, true);
            } else if (originalLabel.getType() == ObjectType.MEMBER) {
                SimplePanelUtil.moveMember(context, label, true, null, null, true);
            } else if (originalLabel.getType() == ObjectType.MEASURE) {
            	DimensionTreeWidget dtw = (DimensionTreeWidget) originalLabel.getParentNode().getTree().getParent().getParent().getParent();
            	dtw.setMeasureAxis(dropTarget.getAxis());
                SimplePanelUtil.moveMeasure(context, label, true, null, null, true);
            }

        }
        super.onDrop(context);
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
       /** if (dropTarget.getWidget() != null) {
            throw new VetoDragException();
        }*/
    	MeasureLabel originalLabel = ((MeasureLabel) context.draggable.getParent().getParent());
    	DimensionTreeWidget dtw = (DimensionTreeWidget)originalLabel.getParentNode().getTree().getParent().getParent().getParent();
    	
    	
    	if(dtw.getMeasureAxis()!=IAxis.UNUSED &&(dtw.getMeasureAxis()!=dropTarget.getAxis()) && originalLabel.getType().equals(ObjectType.MEASURE)){
    		
    		throw new VetoDragException();
    	}
        super.onPreviewDrop(context);
    }

    public void SetWidgetDropController(SimplePanel dropTarget) {
        // TODO Auto-generated method stub

    }

    
}
