package org.pentaho.pat.client.util.dnd.impl;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DimensionSimplePanel;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.IAxis;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;

public class SimplePanelUtil {

	public static void moveDimension(final DragContext context, final MeasureLabel label) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), 
        		((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis(), label.getText(), new AsyncCallback<Object>(){

			public void onFailure(Throwable arg0) {
				MessageBox.error("Error", "move to axis failed");
				
			}

			public void onSuccess(Object arg0) {
				
				label.setAxis(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis());
				
				label.makeNotDraggable();
			}
        	
        });
        
        
		
    	addNewDropTargets(context);
		

			}


		public static void moveHierarchy(final DragContext context, final MeasureLabel label){
		ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), 
        		((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis(), label.getValue().get(0), new AsyncCallback<Object>(){

			public void onFailure(Throwable arg0) {
				MessageBox.error("Error", "move to axis failed");
				
			}

			public void onSuccess(Object arg0) {
				
				label.setAxis(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis());
				label.makeNotDraggable();
			}
        	
        });
        
		addNewDropTargets(context);
		

	}

	public static void moveLevel(final DragContext context, final MeasureLabel label){
		ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), 
        		((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis(), label.getValue().get(0), new AsyncCallback<Object>(){

			public void onFailure(Throwable arg0) {
				MessageBox.error("Error", "move to axis failed");
				
			}

			public void onSuccess(Object arg0) {
				
				label.setAxis(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis());
				label.makeNotDraggable();
			}
        	
        });
        
		addNewDropTargets(context);
		

	}
	private static void addNewDropTargets(DragContext context){
		int[] coordinate = ((DimensionSimplePanel)context.finalDropController.getDropTarget()).getCoord();
        FlexTable ft = ((FlexTable)((DimensionSimplePanel)context.finalDropController.getDropTarget()).getParent());
    	int numberofcols = ft.getCellCount(0);
		if(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis()==IAxis.ROWS){
			for(int i = 0; i<ft.getRowCount(); i++){
				for(int j = 1; j<numberofcols+1; j++){
				if(ft.isCellPresent(i, coordinate[1]+j) && ft.getWidget(i, coordinate[1]+j) instanceof DimensionSimplePanel){
					((DimensionSimplePanel)ft.getWidget(i, coordinate[1]+j)).setCoord(new int[] {i, coordinate[1]+(j+1)});
				}
				}
			ft.insertCell(i, coordinate[1]+1);
			}
			ft
			.setWidget(coordinate[0], coordinate[1]+1, new DimensionSimplePanel(IAxis.ROWS, new int[]{coordinate[0], coordinate[1]+1}));
			}
		
		else if(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis()==IAxis.COLUMNS){
			for(int i=0; i<numberofcols+1; i++){
				for(int j =coordinate[0]+1; j<ft.getRowCount(); j++){
					if(ft.isCellPresent(coordinate[0]+j, i) && ft.getWidget(coordinate[0]+j, i) instanceof DimensionSimplePanel){
						((DimensionSimplePanel)ft.getWidget(coordinate[0]+j, i)).setCoord(new int[] {coordinate[0]+(j+1), i});
					}	
				}
			}
			ft.insertRow(coordinate[0]+1);
			ft
			.setWidget(coordinate[0]+1, coordinate[1], new DimensionSimplePanel(IAxis.COLUMNS, new int[]{coordinate[0]+1, coordinate[1]}));
			
			}
		
	}

}
