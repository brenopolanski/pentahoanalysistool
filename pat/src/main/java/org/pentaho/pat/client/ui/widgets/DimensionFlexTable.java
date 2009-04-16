/*
 * Copyright 2008 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.FlexTableRowDragController;
import org.pentaho.pat.client.util.ServiceFactory;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

/**
 * Table to demonstrate draggable rows and columns.
 */
public final class DimensionFlexTable extends FlexTable {

	

  /**
   * Creates a FlexTable with the desired number of rows and columns, making
   * each row draggable via the provided drag controller.
   * 
   * 
   * @param tableRowDragController the drag controller to enable dragging of
   *            table rows
   *            
   * @author tom(at)wamonline.org.uk
   */
	private FlexTableRowDragController trdc;
  public DimensionFlexTable(FlexTableRowDragController tableRowDragController) {	  
	  addStyleName("demo-flextable"); //$NON-NLS-1$
	  
	  this.trdc=tableRowDragController;
    HTML empty = new HTML("EMPTY"); //$NON-NLS-1$
    empty.addStyleName("drag-Dimension"); //$NON-NLS-1$
    
	 setWidget(0,0, empty);
	 
	
	      }

  public void populateDimensionTable(Axis targetAxis){
	 this.clear();
	  
	  ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), targetAxis, new AsyncCallback<String[]>() {

			public void onFailure(Throwable arg0) {
				// TODO use standardized message dialog when implemented
				Window.alert("Dimension Listing Failed:" + arg0.getLocalizedMessage()); //$NON-NLS-1$
			}

			public void onSuccess(String[] arg0) {
				 for (int row = 0; row < arg0.length; row++) {
				        Label handle = new Label(arg0[row]);
				        handle.addStyleName("drag-Dimension"); //$NON-NLS-1$
				        setWidget(row, 0, handle);
				        trdc.makeDraggable(handle);	
			}
	    	
	    }
		});

  }
}


  

