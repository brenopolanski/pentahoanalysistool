package org.pentaho.pat.client.util.dnd.impl;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.popups.SelectionModeMenu;
import org.pentaho.pat.client.ui.widgets.DimensionSimplePanel;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class SimplePanelUtil {

	public static void moveDimension(final DragContext context, MeasureLabel label) {
		//Set selection

	    /*final SelectionModeMenu smm = new SelectionModeMenu(((DimensionSimplePanel)context.finalDropController.getDropTarget()), label);
	    
	    
        smm.setPopupPositionAndShow(new PositionCallback() {
            public void setPosition(final int offsetWidth, final int offsetHeight) {
                smm.setPopupPosition(context.mouseX, context.mouseY);
            }
        });
*/
		//Move dimension
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), 
        		((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis(), label.getValue(), new AsyncCallback(){

			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(Object arg0) {
				
				
			}
        	
        });
        

		//Get members
		ServiceFactory.getDiscoveryInstance().getMembers(Pat.getSessionID(), Pat.getCurrQuery(), label.getValue(), new AsyncCallback<StringTree>(){

			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(StringTree arg0) {
				int[] coordinate = ((DimensionSimplePanel)context.finalDropController.getDropTarget()).getCoord();
				FlexTable ft = ((FlexTable)((DimensionSimplePanel)context.finalDropController.getDropTarget()).getParent());
				if(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis()==IAxis.ROWS){
				ft.setText(coordinate[0]+1,coordinate[1], arg0.getValue());
				for(int i = 0; i<ft.getRowCount(); i++){
				ft.insertCell(i, coordinate[1]+1);
				}
				ft
				.setWidget(coordinate[0], coordinate[1]+1, new DimensionSimplePanel(IAxis.ROWS, new int[]{coordinate[0], coordinate[1]+1}));
				}
				
			}
			
		});
		
		

			}

	
}
