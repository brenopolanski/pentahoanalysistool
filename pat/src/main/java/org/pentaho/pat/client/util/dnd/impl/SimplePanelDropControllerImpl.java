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
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.dnd.SimplePanelDropController;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.gen2.complexpanel.client.FastTreeItem;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * FlexTableRowDropConroller allows flextable cell drops.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class SimplePanelDropControllerImpl extends SimpleDropController
		implements SimplePanelDropController {

	private final SimplePanel dropTarget;
	private boolean trash;

	public SimplePanelDropControllerImpl(SimplePanel dropTarget, boolean trash) {
		super(dropTarget);
		this.dropTarget = dropTarget;
		this.trash = trash;
	}

	@Override
	public void onDrop(final DragContext context) {
		MeasureLabel originalLabel = ((MeasureLabel) context.draggable);
		if (trash) {
			DimensionSimplePanel panel = (DimensionSimplePanel) originalLabel
					.getParent();
			context.draggable.removeFromParent();
			
			FastTreeItem fti = originalLabel.getParentNode();
			((MeasureLabel) fti.getWidget()).makeDraggable();
			for (int i = 0; i < fti.getChildCount(); i++) {
				
				enableDrag(fti.getChild(i));
				
			}

			originalLabel.makeDraggable();

			if (((MeasureLabel) context.draggable).getType() == MeasureLabel.LabelType.DIMENSION) {
				SimplePanelUtil.clearDimension(context, context.draggable,
						panel.getCoord(), panel.getAxis());
			} else if (((MeasureLabel) context.draggable).getType() == MeasureLabel.LabelType.HIERARCHY) {
				SimplePanelUtil.clearHierarchy(context, context.draggable,
						panel.getCoord(), panel.getAxis());
			} else if (((MeasureLabel) context.draggable).getType() == MeasureLabel.LabelType.LEVEL) {
				SimplePanelUtil.clearLevel(context, context.draggable, panel
						.getCoord(), panel.getAxis());
			}
			else if (((MeasureLabel) context.draggable).getType() == MeasureLabel.LabelType.MEASURE) {
				SimplePanelUtil.clearDimension(context, context.draggable,
						panel.getCoord(), panel.getAxis());
			}
		} else {
			MeasureLabel label = new MeasureLabel(originalLabel.getValue(),
					originalLabel.getActualName(), originalLabel.getText(),
					originalLabel.getType(), originalLabel.getParentNode());
			label.setDragController(originalLabel.getDragController());
			label.makeDraggable();
			label.enableSinkEvents();
			dropTarget.setWidget(label);
			FastTreeItem fti = originalLabel.getParentNode();
			((MeasureLabel) fti.getWidget()).makeNotDraggable();
			for (int i = 0; i < fti.getChildCount(); i++) {
				
				disableDrag(fti.getChild(i));
				
			}

			originalLabel.makeNotDraggable();

			if (originalLabel.getType() == MeasureLabel.LabelType.DIMENSION) {
				SimplePanelUtil
						.moveDimension(context, label, context.draggable);
			} else if (originalLabel.getType() == MeasureLabel.LabelType.HIERARCHY) {
				SimplePanelUtil.moveHierarchy(context, label);
			} else if (originalLabel.getType() == MeasureLabel.LabelType.LEVEL) {
				SimplePanelUtil.moveLevel(context, label);
			}
			else if (originalLabel.getType() == MeasureLabel.LabelType.MEMBER) {
						SimplePanelUtil.moveMember(context, label);
			}
			 else if (originalLabel.getType() == MeasureLabel.LabelType.MEASURE){
				SimplePanelUtil.moveMeasure(context, label);
			}

		}
		super.onDrop(context);
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		if (dropTarget.getWidget() != null) {
			throw new VetoDragException();
		}
		super.onPreviewDrop(context);
	}

	public void SetWidgetDropController(SimplePanel dropTarget) {
		// TODO Auto-generated method stub

	}

	private void enableDrag(FastTreeItem fti){
		((MeasureLabel)fti.getWidget()).makeDraggable();
		for (int i = 0; i < fti.getChildCount(); i++) {

		((MeasureLabel) fti.getChild(i).getWidget()).makeDraggable();
		for(int j =0; j<fti.getChild(i).getChildCount(); j++){
			enableDrag(fti.getChild(j));
		}
		}
	}
	
	private void disableDrag(FastTreeItem fti){
		((MeasureLabel)fti.getWidget()).makeNotDraggable();
		for (int i = 0; i < fti.getChildCount(); i++) {

		((MeasureLabel) fti.getChild(i).getWidget()).makeNotDraggable();
		for(int j =0; j<fti.getChild(i).getChildCount(); j++){
			enableDrag(fti.getChild(j));
		}
		}
	}

}
