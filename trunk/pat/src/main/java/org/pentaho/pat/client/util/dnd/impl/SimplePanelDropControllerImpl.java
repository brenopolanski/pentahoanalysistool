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
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.query.IAxis;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * SimplePanelDropConterllerImpl, allows users to drop labels, 
 * on DimensionSimplePanels to alter the query.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class SimplePanelDropControllerImpl extends SimpleDropController implements SimplePanelDropController {

    private static final String CSS_DEMO_BIN_DRAGGABLE_ENGAGE = null;

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
        DimensionTreeWidget dtw = (DimensionTreeWidget) originalLabel.getParentNode().getTree().getParent().getParent().getParent();
        /*
         * If the widget is dropped on the trash can.
         * 
         */
        if (trash) {
            DimensionSimplePanel panel = (DimensionSimplePanel) originalLabel.getParent();
            context.draggable.removeFromParent();

            originalLabel.makeDraggable();

            /*
             * Remove the widget.
             */
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

            dtw.setDimensionLocation(panel.getAxis(), originalLabel.getDimensionName());
        } 
        /*
         * If the widget is dropped and the target axis != the widgets current axis. IE move dimension from rows to columns.
         */
        else if (originalLabel.getParent() instanceof DimensionSimplePanel && 
        		(((MeasureLabel)context.draggable.getParent().getParent()).getAxis() != ((DimensionSimplePanel)this.dropTarget).getAxis())) {
        	DimensionSimplePanel dp = null;
        	boolean createdrop = true;
            DimensionSimplePanel panel = (DimensionSimplePanel) originalLabel.getParent();
            /*
             * If the drop target does not contain a widget then add the original label.
             */
            if(dropTarget.getWidget()==null){
            dropTarget.setWidget(originalLabel);
            }
            /*
             * Else create a new drop target.
             */
            else{
            	FlexTable ft = ((FlexTable) ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getParent());
            	int row = dropTarget.getCoord()[0];
        		int col = dropTarget.getCoord()[1];
        		
            	if(dropTarget.getAxis().equals(IAxis.ROWS)|| dropTarget.getAxis().equals(IAxis.FILTER)){
            		for(int i = 0; i<ft.getRowCount(); i++){
            		ft.insertCell(i,col);
            		}
            		dp = new DimensionSimplePanel(false, dropTarget.getAxis());
        			dp.setWidget(originalLabel);
            		ft.setWidget(row, col, dp);
            		createdrop = false;
            	}
            	else if(dropTarget.getAxis().equals(IAxis.COLUMNS)){
            		ft.insertRow(row);
            		dp = new DimensionSimplePanel(false, dropTarget.getAxis());
        			dp.setWidget(originalLabel);
        			ft.setWidget(row, col, dp);
        			createdrop = false;
            	}
            }
            
            /*
             * If the widget is a Dimension.
             */
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
            } 
            /*
             * If the widget is a hierarchy.
             */
            else if (originalLabel.getType() == ObjectType.HIERARCHY) {
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
            } 
            /*
             * If the widget is a level.
             */
            else if (originalLabel.getType() == ObjectType.LEVEL) {
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
            } 
            /*
             * If the widget is a Member.
             */
            else if (originalLabel.getType() == ObjectType.MEMBER) {
                originalLabel.setAxis(((DimensionSimplePanel) this.dropTarget).getAxis());
                SimplePanelUtil.moveMember(context, originalLabel, false, panel.getCoord(), panel.getAxis(), createdrop);
                if(dp!=null){
                	if(dp.getCoord()[0]<dropTarget.getCoord()[0]||dp.getCoord()[1]<dropTarget.getCoord()[1]){
                		SimplePanelUtil.pullUpMeasember(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                	else{
                		SimplePanelUtil.pushdownMeasember(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                }
            } 
            /*
             * if the widget is a measure.
             */
            else if (originalLabel.getType() == ObjectType.MEASURE) {
                originalLabel.setAxis(((DimensionSimplePanel) this.dropTarget).getAxis());
            	//dtw.setMeasureAxis(dropTarget.getAxis());
                SimplePanelUtil.moveMeasure(context, originalLabel, false, panel.getCoord(), panel.getAxis(), createdrop);
                if(dp!=null){
                	if(dp.getCoord()[0]<dropTarget.getCoord()[0]||dp.getCoord()[1]<dropTarget.getCoord()[1]){
                		SimplePanelUtil.pullUpMeasember(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                	else{
                		SimplePanelUtil.pushdownMeasember(context, originalLabel, dropTarget.getCoord(), dp.getCoord(), dropTarget.getAxis(),false);
                	}
                }
            }
            
            dtw.setDimensionLocation(dropTarget.getAxis(), originalLabel.getDimensionName());

        } 
        /*
         * If the drop is on the same axis, then pushdown or pull up.
         */
        else if(originalLabel.getParent() instanceof DimensionSimplePanel && 
        		(((MeasureLabel)context.draggable.getParent().getParent()).getAxis() == this.dropTarget.getAxis())){
            		DimensionSimplePanel panel = ((DimensionSimplePanel)originalLabel.getParent());
            /*
             * For Columns.
             */
            if(panel.getAxis().equals(IAxis.COLUMNS)){
            if(panel.getCoord()[0] > ((DimensionSimplePanel)this.dropTarget).getCoord()[0]){
            	if(originalLabel.getType().equals(ObjectType.MEASURE) || originalLabel.getType().equals(ObjectType.MEMBER)){
            		SimplePanelUtil.pullUpMeasember(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.COLUMNS, true);
            	}
            	else{
                //pushup
                SimplePanelUtil.pullUp(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.COLUMNS, true);
            	}
            } 
            else {
            	if(originalLabel.getType().equals(ObjectType.MEASURE) || originalLabel.getType().equals(ObjectType.MEMBER)){
            		SimplePanelUtil.pushdownMeasember(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.COLUMNS, true);
            	}
            	else{
                //pulldown
                SimplePanelUtil.pushDown(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.COLUMNS, true);
            	}
            }
        }
            /*
             * For Rows.
             */
            else if(panel.getAxis().equals(IAxis.ROWS)){
                if(panel.getCoord()[1] > ((DimensionSimplePanel)this.dropTarget).getCoord()[1]){
                	if(originalLabel.getType().equals(ObjectType.MEASURE) || originalLabel.getType().equals(ObjectType.MEMBER)){
                		SimplePanelUtil.pullUpMeasember(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.ROWS, true);
                	}
                	else{
                    //pushup
                    SimplePanelUtil.pullUp(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.ROWS, true);
                	}
                } 
                else {
                	if(originalLabel.getType().equals(ObjectType.MEASURE)||originalLabel.getType().equals(ObjectType.MEMBER)){
                		SimplePanelUtil.pushdownMeasember(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.ROWS, true);
                	}
                	else{
                    //pulldown
                    SimplePanelUtil.pushDown(context, originalLabel, panel.getCoord(), this.dropTarget.getCoord(), IAxis.ROWS, true);
                	}
                }   
            }
        }
        /*
         * If a standard widget is dropped on an empty drop target.
         */
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
            	
            	//dtw.setMeasureAxis(dropTarget.getAxis());
                SimplePanelUtil.moveMeasure(context, label, true, null, null, true);
            }

            dtw.setDimensionLocation(dropTarget.getAxis(), originalLabel.getDimensionName());
        }
        super.onDrop(context);
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
       
    	MeasureLabel originalLabel = ((MeasureLabel) context.draggable.getParent().getParent());
    	DimensionTreeWidget dtw = (DimensionTreeWidget)originalLabel.getParentNode().getTree().getParent().getParent().getParent();
    	
    	/*
    	 * If the a measure widget is dropped and the axis is not the same axis as the Measure Dimension.
    	 */
    	if(dropTarget.getAxis()==IAxis.UNUSED && (originalLabel.getAxis()!=(null))){
    	    
    	}
    	else if(dropTarget.isTrash() && originalLabel.getAxis()==null){
            throw new VetoDragException();
        }

    	else if(dtw.getDimensionLocation("Measures")!=IAxis.UNUSED &&(dtw.getDimensionLocation("Measures")!=dropTarget.getAxis()) && originalLabel.getType().equals(ObjectType.MEASURE)){
    		
    		throw new VetoDragException();
    	}
    	else if(dtw.getDimensionLocation(originalLabel.getDimensionName())!=IAxis.UNUSED && !dropTarget.isMeasurebox() && (originalLabel.getType().equals(ObjectType.MEASURE)|| originalLabel.getType().equals(ObjectType.MEMBER)) && originalLabel.getParentNode().getParentItem()!=null){
    		throw new VetoDragException();
    	}
    	else if(dropTarget.getDimensionAssociation()!=null && !dropTarget.getDimensionAssociation().equals(originalLabel.getDimensionName())){
    		throw new VetoDragException();
    	}
    	
    	
        super.onPreviewDrop(context);
    }

    public void SetWidgetDropController(SimplePanel dropTarget) {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void onEnter(DragContext context){
        super.onEnter(context);
        for (Widget widget : context.selectedWidgets) {
          //widget.addStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
        }
        if(dropTarget.isTrash())
        dropTarget.setEngaged(true);        
    }

    @Override
    public void onLeave(DragContext context) {
      for (Widget widget : context.selectedWidgets) {
       // widget.removeStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
      }
      if(dropTarget.isTrash())
      dropTarget.setEngaged(false);
      
      super.onLeave(context);
    }
}
