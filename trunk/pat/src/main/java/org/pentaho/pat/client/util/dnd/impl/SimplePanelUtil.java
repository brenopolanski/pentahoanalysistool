package org.pentaho.pat.client.util.dnd.impl;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.DimensionSimplePanel;
import org.pentaho.pat.client.ui.widgets.MeasureLabel;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.IAxis;
import org.pentaho.pat.rpc.dto.StringTree;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class SimplePanelUtil {

	public static void moveDimension(final DragContext context, final MeasureLabel label, final Widget w) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), 
        		((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis(), label.getText(), new AsyncCallback<Object>(){

			public void onFailure(Throwable arg0) {
				MessageBox.error("Error", "move to axis failed");
				
			}

			public void onSuccess(Object arg0) {
				final List<String> dimension = new ArrayList<String>();
				dimension.add(label.getText());
				ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(), label.getText(), dimension, "dimension",
			            "MEMBER", new AsyncCallback<List<String>>(){

							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub
								
							}

							public void onSuccess(List<String> arg0) {
								label.setCurrentSelection(arg0);
								label.setSelectionType("MEMBER");
								ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(), 
										label.getText(), dimension, "dimension", "MEMBER", new AsyncCallback<StringTree>(){

											public void onFailure(Throwable arg0) {
												// TODO Auto-generated method stub
												
											}

											public void onSuccess(StringTree arg0) {
												
												GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionChanged(Pat.getCurrQuery(), label, 
														arg0, "MEMBER");
												
												
											}
									
								});
								
							}
					
				});
				
				
				label.setAxis(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis());
				
				//label.makeNotDraggable();
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
				
				ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(), label.getValue().get(0), label.getValue(), "hierarchy",
			            "MEMBER", new AsyncCallback<List<String>>(){

							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub
								
							}

							public void onSuccess(List<String> arg0) {
								label.setCurrentSelection(arg0);
								label.setSelectionType("MEMBER");
								ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(), label.getValue().get(0), label.getValue(), "hierarchy",
							            "MEMBER", new AsyncCallback<StringTree>(){

											public void onFailure(Throwable arg0) {
												// TODO Auto-generated method stub
												
											}

											public void onSuccess(
													StringTree arg0) {
												GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");
												
											}
									
								});
								
								
							}
					
				});
				
				
				label.setAxis(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis());
				
				//label.makeNotDraggable();
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
				ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(), label.getText(), label.getValue(), "level",
			            "MEMBER", new AsyncCallback<List<String>>(){

							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub
								
							}

							public void onSuccess(List<String> arg0) {
								label.setCurrentSelection(arg0);
								label.setSelectionType("MEMBER");
								ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(), label.getText(), label.getValue(), "level",
							            "MEMBER", new AsyncCallback<StringTree>(){

											public void onFailure(Throwable arg0) {
												// TODO Auto-generated method stub
												
											}

											public void onSuccess(
													StringTree arg0) {
												GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");
												
											}
									
								});
								
								
							}
					
				});
				
				
				label.setAxis(((DimensionSimplePanel)context.finalDropController.getDropTarget()).getAxis());
				
				//label.makeNotDraggable();
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


	public static void clearDimension(DragContext context, Widget draggable, final int[] is, final IAxis iAxis) {
		final MeasureLabel label = ((MeasureLabel)draggable);
		ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), IAxis.UNUSED, 
				label.getValue().get(0), new AsyncCallback(){

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(Object arg0) {
						ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(), label.getValue().get(0),
								label.getCurrentSelection(), new AsyncCallback(){

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub
										
									}

									public void onSuccess(Object arg0) {
										StringTree tree = null;
										GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionCleared(Pat.getCurrQuery(), 
												label, is, iAxis);
									}
							
						});	
						
					}
			
		});
		
	}


	public static void clearHierarchy(DragContext context, Widget draggable,
			final int[] coord, final IAxis axis) {

		final MeasureLabel label = ((MeasureLabel)draggable);
		ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), IAxis.UNUSED, 
				label.getValue().get(0), new AsyncCallback(){

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(Object arg0) {
						ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(), label.getValue().get(0),
								label.getCurrentSelection(), new AsyncCallback(){

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub
										
									}

									public void onSuccess(Object arg0) {
										StringTree tree = null;
										GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionCleared(Pat.getCurrQuery(), 
												label, coord, axis);
									}
							
						});	
						
					}
			
		});

		
	}


	public static void clearLevel(DragContext context, Widget draggable,
			final int[] coord, final IAxis axis) {
		final MeasureLabel label = ((MeasureLabel)draggable);
		ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), IAxis.UNUSED, 
				label.getValue().get(0), new AsyncCallback(){

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(Object arg0) {
						ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(), label.getValue().get(0),
								label.getCurrentSelection(), new AsyncCallback(){

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub
										
									}

									public void onSuccess(Object arg0) {
										StringTree tree = null;
										GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionCleared(Pat.getCurrQuery(), 
												label, coord, axis);
									}
							
						});	
						
					}
			
		});

		
	}

}
