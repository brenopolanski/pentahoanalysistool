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

import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.dnd.SimplePanelDropController;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * FlexTableRowDropConroller allows flextable cell drops.
 * 
 * @author tom(at)wamonline.org.uk
 */
public class SimplePanelDropControllerImpl extends SimpleDropController implements SimplePanelDropController {

	 private final SimplePanel dropTarget;

	 
	  public SimplePanelDropControllerImpl(SimplePanel dropTarget) {
	    super(dropTarget);
	    this.dropTarget = dropTarget;
	  }

	  @Override
	  public void onDrop(final DragContext context) {
		MeasureLabel label = new MeasureLabel(((MeasureLabel)context.draggable).getText(), ((MeasureLabel)context.draggable).getType());
	    dropTarget.setWidget(label);
	    SimplePanelUtil.moveDimension(context, label);
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

}
