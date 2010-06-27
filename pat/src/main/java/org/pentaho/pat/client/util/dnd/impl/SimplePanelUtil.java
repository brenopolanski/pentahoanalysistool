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
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.enums.SelectionType;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class SimplePanelUtil {

    private static void addNewDropTargets(DragContext context, ObjectType ot) {
    	int[] coordinate = ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getCoord();
        FlexTable ft = ((FlexTable) ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getParent());
    	
        
        if (((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis() == IAxis.ROWS) {
        	 if(ot.equals(ObjectType.MEMBER)|| ot.equals(ObjectType.MEASURE)){
             	ft.insertRow(coordinate[0]+1);
         		ft.insertCell(coordinate[0]+1, coordinate[1]);
         		ft.setWidget(coordinate[0]+1, coordinate[1], new DimensionSimplePanel(IAxis.ROWS));
         	}
        	 if(!ft.isCellPresent(coordinate[0]-1, coordinate[1]+1) || ft.getWidget(coordinate[0]-1, coordinate[1]+1) instanceof DimensionSimplePanel
        			 && ((DimensionSimplePanel)ft.getWidget(coordinate[0]-1, coordinate[1]+1)).getAxis().equals(IAxis.COLUMNS)){
        	for (int i = 0; i < ft.getRowCount(); i++) {
                ft.insertCell(i, coordinate[1] + 1);
            }
            ft.setWidget(coordinate[0], coordinate[1] + 1, new DimensionSimplePanel(IAxis.ROWS));
        	 }
           
        }

        else if (((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis() == IAxis.COLUMNS) {

        	 if(ot.equals(ObjectType.MEMBER)|| ot.equals(ObjectType.MEASURE)){
         		ft.insertCell(coordinate[0], coordinate[1]+1);
         		ft.setWidget(coordinate[0], coordinate[1]+1, new DimensionSimplePanel(IAxis.COLUMNS));
         	}
        	 if(!ft.isCellPresent(coordinate[0], coordinate[1]-1)|| !(ft.getWidget(coordinate[0], coordinate[1]-1) instanceof DimensionSimplePanel)){
            ft.insertRow(coordinate[0] + 1);
            ft.setWidget(coordinate[0] + 1, coordinate[1], new DimensionSimplePanel(IAxis.COLUMNS));
        	 }

           
        }

        else if (((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis() == IAxis.FILTER) {
            
        	if(ot.equals(ObjectType.MEMBER)|| ot.equals(ObjectType.MEASURE)){
            	ft.insertRow(coordinate[0]+1);
        		ft.insertCell(coordinate[0]+1, coordinate[1]);
        		ft.setWidget(coordinate[0]+1, coordinate[1], new DimensionSimplePanel(IAxis.FILTER));
        	}
        	 if(!ft.isCellPresent(coordinate[0]-1, coordinate[1]+1) || ft.getWidget(coordinate[0]-1, coordinate[1]+1) instanceof DimensionSimplePanel
        			 && ((DimensionSimplePanel)ft.getWidget(coordinate[0]-1, coordinate[1]+1)).getAxis().equals(IAxis.COLUMNS)){
        	for (int i = 0; i < ft.getRowCount(); i++) {
                ft.insertCell(i, coordinate[1] + 1);
            }
            ft.setWidget(coordinate[0], coordinate[1] + 1, new DimensionSimplePanel(IAxis.FILTER));
            
        	 }
        }

    }

    public static void clearDimension(DragContext context, Widget draggable, final int[] is, final IAxis iAxis) {
        final MeasureLabel label = ((MeasureLabel) draggable);
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), IAxis.UNUSED,
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        // TODO Auto-generated method stub

                    }

                    public void onSuccess(Object arg0) {
                        ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                label.getActualName(), label.getCurrentSelection(), new AsyncCallback<Object>() {

                                    public void onFailure(Throwable arg0) {
                                        // TODO Auto-generated method stub

                                    }

                                    public void onSuccess(Object arg0) {
                                        GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                .fireSelectionCleared(Pat.getCurrQuery(), label, is, iAxis);
                                    }

                                });

                    }

                });

    }

    public static void clearHierarchy(DragContext context, Widget draggable, final int[] coord, final IAxis axis) {

        final MeasureLabel label = ((MeasureLabel) draggable);
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), IAxis.UNUSED,
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        // TODO Auto-generated method stub

                    }

                    public void onSuccess(Object arg0) {
                        ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                label.getActualName(), label.getCurrentSelection(), new AsyncCallback<Object>() {

                                    public void onFailure(Throwable arg0) {
                                        // TODO Auto-generated method stub

                                    }

                                    public void onSuccess(Object arg0) {
                                        GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                .fireSelectionCleared(Pat.getCurrQuery(), label, coord, axis);
                                    }

                                });

                    }

                });

    }

    public static void clearLevel(DragContext context, Widget draggable, final int[] coord, final IAxis axis) {
        final MeasureLabel label = ((MeasureLabel) draggable);
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), IAxis.UNUSED,
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        // TODO Auto-generated method stub

                    }

                    public void onSuccess(Object arg0) {
                        ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                label.getActualName(), label.getCurrentSelection(), new AsyncCallback<Object>() {

                                    public void onFailure(Throwable arg0) {
                                        // TODO Auto-generated method stub

                                    }

                                    public void onSuccess(Object arg0) {
                                        GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                .fireSelectionCleared(Pat.getCurrQuery(), label, coord, axis);
                                    }

                                });

                    }

                });

    }

    public static void moveDimension(final DragContext context, final MeasureLabel label, final Widget w,
            final boolean createSelection, final int[] is, final IAxis iAxis, boolean createnewdroptarget) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(),
                ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis(), label.getText(),
                new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        // TODO Auto-generated method stub

                    }

                    public void onSuccess(Object arg0) {
                        final List<String> dimension = new ArrayList<String>();
                        dimension.add(label.getText());
                        if (createSelection) {

                            label.setSelectionType(SelectionType.MEMBER);
                            label.setAxis(((DimensionSimplePanel) context.finalDropController.getDropTarget())
                                    .getAxis());
                            ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                    label.getActualName(), ObjectType.DIMENSION, SelectionType.MEMBER,
                                    new AsyncCallback<List<String>>() {

                                        public void onFailure(Throwable arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void onSuccess(List<String> arg0) {
                                            label.setCurrentSelection(arg0);
                                            ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                                    label.getActualName(), ObjectType.DIMENSION, label.getSelectionType(), new AsyncCallback<StringTree>() {

                                                        public void onFailure(Throwable arg0) {
                                                            // TODO Auto-generated method stub

                                                        }

                                                        public void onSuccess(StringTree arg0) {

                                                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                                    .fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");

                                                        }

                                                    });
                                        }

                                    });

                        } else {
                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionCleared(
                                    Pat.getCurrQuery(), label, is, iAxis);
                            
                            ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                    label.getActualName(), ObjectType.DIMENSION, label.getSelectionType(), new AsyncCallback<StringTree>() {

                                        public void onFailure(Throwable arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void onSuccess(StringTree arg0) {

                                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                    .fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");

                                        }

                                    });
                        }
                        

                    }

                });

        if(createnewdroptarget)
        addNewDropTargets(context, label.getType());

    }

    public static void moveHierarchy(final DragContext context, final MeasureLabel label,
            final boolean createSelection, final int[] is, final IAxis iAxis, boolean createnewdroptarget) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(),
                ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis(),
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error("Error", "move to axis failed");

                    }

                    public void onSuccess(Object arg0) {
                        label.setSelectionType(SelectionType.MEMBER);
                        label.setAxis(((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis());
                        if (createSelection) {
                            ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                    label.getActualName(), ObjectType.HIERARCHY, SelectionType.MEMBER,
                                    new AsyncCallback<List<String>>() {

                                        public void onFailure(Throwable arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void onSuccess(List<String> arg0) {
                                            label.setCurrentSelection(arg0);
                                            ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                                    label.getActualName(), ObjectType.HIERARCHY, label.getSelectionType(),
                                                    new AsyncCallback<StringTree>() {

                                                        public void onFailure(Throwable arg0) {
                                                            // TODO Auto-generated method stub

                                                        }

                                                        public void onSuccess(StringTree arg0) {
                                                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                                    .fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");

                                                        }

                                                    });
                                        }

                                    });

                        } else {
                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionCleared(
                                    Pat.getCurrQuery(), label, is, iAxis);
                            ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                    label.getActualName(), ObjectType.HIERARCHY, label.getSelectionType(),
                                    new AsyncCallback<StringTree>() {

                                        public void onFailure(Throwable arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void onSuccess(StringTree arg0) {
                                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                    .fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");

                                        }

                                    });
                        }
                       

                    }

                });
        if(createnewdroptarget)
        addNewDropTargets(context, label.getType());

    }

    public static void moveLevel(final DragContext context, final MeasureLabel label, final boolean createSelection,
            final int[] is, final IAxis iAxis, boolean createnewdroptarget) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(),
                ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis(),
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error("Error", "move to axis failed");

                    }

                    public void onSuccess(Object arg0) {
                        if (createSelection) {
                            ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                    label.getActualName(), ObjectType.LEVEL, SelectionType.CHILDREN,
                                    new AsyncCallback<List<String>>() {

                                        public void onFailure(Throwable arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void onSuccess(List<String> arg0) {
                                            label.setCurrentSelection(arg0);
                                            label.setSelectionType(SelectionType.CHILDREN);
                                            label.setAxis(((DimensionSimplePanel) context.finalDropController
                                                    .getDropTarget()).getAxis());

                                            ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                                    label.getActualName(), ObjectType.LEVEL, label.getSelectionType(),
                                                    new AsyncCallback<StringTree>() {

                                                        public void onFailure(Throwable arg0) {
                                                            // TODO Auto-generated method stub

                                                        }

                                                        public void onSuccess(StringTree arg0) {
                                                            label.setAxis(((DimensionSimplePanel) context.finalDropController
                                                                    .getDropTarget()).getAxis());
                                                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                                    .fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");
                                                            
                                                        }

                                                    });

                                        }

                                    });
                        } else {
                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireSelectionCleared(
                                    Pat.getCurrQuery(), label, is, iAxis);
                            ServiceFactory.getQueryInstance().getSpecificMembers(Pat.getSessionID(), Pat.getCurrQuery(),
                                    label.getActualName(), ObjectType.LEVEL, label.getSelectionType(),
                                    new AsyncCallback<StringTree>() {

                                        public void onFailure(Throwable arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void onSuccess(StringTree arg0) {
                                            label.setAxis(((DimensionSimplePanel) context.finalDropController
                                                    .getDropTarget()).getAxis());
                                            GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                    .fireSelectionChanged(Pat.getCurrQuery(), label, arg0, "MEMBER");
                                            
                                        }

                                    });

                        }
                        if(label.getSelectionType() == null){
                            label.setSelectionType(SelectionType.CHILDREN);
                        }
                                            
                      
                    }
                });
        if(createnewdroptarget)
        addNewDropTargets(context, label.getType());

    }

    public static void moveMeasure(final DragContext context, final MeasureLabel label, final boolean createSelection,
            final int[] is, final IAxis iAxis, final boolean createnewdroptarget) {
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(),
                ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis(),
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {

                    }

                    public void onSuccess(Object arg0) {
                        if (label.getText().equals("Measures")) {
                            if (createSelection) {
                                final List<String> list = new ArrayList<String>();
                                list.add("Measures");
                                ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(),
                                        Pat.getCurrQuery(), label.getActualName(), ObjectType.MEASURE, SelectionType.MEMBER,
                                        new AsyncCallback<List<String>>() {

                                            public void onFailure(Throwable arg0) {
                                                MessageBox.error("Error", "oops");

                                            }

                                            public void onSuccess(List<String> arg0) {
                                                label.setCurrentSelection(arg0);
                                                label.setSelectionType(SelectionType.MEMBER);
                                                label.setAxis(((DimensionSimplePanel) context.finalDropController
                                                        .getDropTarget()).getAxis());
                                                ServiceFactory.getQueryInstance().getSpecificMembers(
                                                        Pat.getSessionID(), Pat.getCurrQuery(), label.getActualName(),
                                                        ObjectType.MEASURE, SelectionType.MEMBER, new AsyncCallback<StringTree>() {

                                                            public void onFailure(Throwable arg0) {
                                                                // TODO Auto-generated method stub

                                                            }

                                                            public void onSuccess(StringTree arg0) {
                                                                GlobalConnectionFactory.getSelectionInstance()
                                                                        .getQueryListeners().fireSelectionChanged(
                                                                                Pat.getCurrQuery(), label, arg0,
                                                                                "MEMBER");

                                                            }

                                                        });

                                            }

                                        });
                            }
                        } else {
                            if (!createSelection)
                                GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                        .fireSelectionCleared(Pat.getCurrQuery(), label, is, iAxis);
                            else {
                                List<String> list = new ArrayList<String>();
                                list.add("dummy");
                                list.add(label.getActualName());
                                ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(),
                                        Pat.getCurrQuery(), label.getActualName(), ObjectType.MEASURE, SelectionType.MEMBER,
                                        new AsyncCallback<List<String>>() {

                                            public void onFailure(Throwable arg0) {
                                                // TODO Auto-generated method stub

                                            }

                                            public void onSuccess(List<String> arg0) {
                                                label.setCurrentSelection(arg0);
                                                label.setSelectionType(SelectionType.MEMBER);
                                                label.setAxis(((DimensionSimplePanel) context.finalDropController
                                                        .getDropTarget()).getAxis());
                                            }

                                        });
                            }
                        }
                        if(createnewdroptarget)
                        addNewDropTargets(context, label.getType());
                    }
                });

    }

    public static void moveMember(final DragContext context, final MeasureLabel label, final boolean createSelection,
            final int[] is, final IAxis iAxis, boolean createnewdroptarget) {

        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(),
                ((DimensionSimplePanel) context.finalDropController.getDropTarget()).getAxis(),
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error("Error", "move to axis failed");

                    }

                    public void onSuccess(Object arg0) {
                        if (createSelection) {
                            ServiceFactory.getQueryInstance().createSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                    label.getActualName(), ObjectType.MEMBER, SelectionType.MEMBER,
                                    new AsyncCallback<List<String>>() {

                                        public void onFailure(Throwable arg0) {
                                            // TODO Auto-generated method stub

                                        }

                                        public void onSuccess(List<String> arg0) {
                                            label.setCurrentSelection(arg0);
                                            label.setSelectionType(SelectionType.MEMBER);
                                            label.setAxis(((DimensionSimplePanel) context.finalDropController
                                                    .getDropTarget()).getAxis());

                                        }

                                    });
                        }
                    }
                });

        if(createnewdroptarget)
        addNewDropTargets(context, label.getType());

    }

    public static void clearMember(DragContext context, Widget draggable, final int[] coord, final IAxis axis) {
        final MeasureLabel label = ((MeasureLabel) draggable);
        ServiceFactory.getQueryInstance().moveDimension(Pat.getSessionID(), Pat.getCurrQuery(), IAxis.UNUSED,
                label.getActualName(), new AsyncCallback<Object>() {

                    public void onFailure(Throwable arg0) {
                        // TODO Auto-generated method stub

                    }

                    public void onSuccess(Object arg0) {
                        ServiceFactory.getQueryInstance().clearSelection(Pat.getSessionID(), Pat.getCurrQuery(),
                                label.getActualName(), label.getCurrentSelection(), new AsyncCallback<Object>() {

                                    public void onFailure(Throwable arg0) {
                                        // TODO Auto-generated method stub

                                    }

                                    public void onSuccess(Object arg0) {
                                        GlobalConnectionFactory.getSelectionInstance().getQueryListeners()
                                                .fireSelectionCleared(Pat.getCurrQuery(), label, coord, axis);
                                    }

                                });

                    }

                });

    }

    public static void pullUp(DragContext context, MeasureLabel originalLabel, final int[] currentPos, final int[] newPos, IAxis axis, final boolean firelistener) {
        if(axis.equals(IAxis.COLUMNS)){
        ServiceFactory.getQueryInstance().pullUpDimension(Pat.getSessionID(), Pat.getCurrQuery(), originalLabel.getAxis(), currentPos[0], newPos[0], new AsyncCallback<Object>(){

            public void onFailure(Throwable arg0) {
                MessageBox.error("Bugger", "Bugger");
                
            }

            public void onSuccess(Object arg0) {
            	if(firelistener)
            		GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireMoveRow(Pat.getCurrQuery(), currentPos[0], newPos[0]);
            }
            
        });
        }
        else if(axis.equals(IAxis.ROWS)){
            ServiceFactory.getQueryInstance().pullUpDimension(Pat.getSessionID(), Pat.getCurrQuery(), originalLabel.getAxis(), currentPos[1], newPos[1], new AsyncCallback<Object>(){

                public void onFailure(Throwable arg0) {
                    MessageBox.error("Bugger", "Bugger");
                    
                }

                public void onSuccess(Object arg0) {
                	if(firelistener)
                		GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireMoveCol(Pat.getCurrQuery(), currentPos[1], newPos[1]);
                }
                
            });
        }
    }

    public static void pushDown(DragContext context, MeasureLabel originalLabel, final int[] coord, final int[] coord2, IAxis axis, final boolean firelistener) {

        if(axis.equals(IAxis.COLUMNS)){
            ServiceFactory.getQueryInstance().pushDownDimension(Pat.getSessionID(), Pat.getCurrQuery(), originalLabel.getAxis(), coord[0], coord2[0], new AsyncCallback<Object>(){

                public void onFailure(Throwable arg0) {
                    MessageBox.error("Bugger", "Bugger");
                    
                }

                public void onSuccess(Object arg0) {
                	if(firelistener)
                		GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireMoveRow(Pat.getCurrQuery(), coord[0], coord2[0]);
                }
                
            });
            }
            else if(axis.equals(IAxis.ROWS)){
                ServiceFactory.getQueryInstance().pushDownDimension(Pat.getSessionID(), Pat.getCurrQuery(), originalLabel.getAxis(), coord[1], coord2[1], new AsyncCallback<Object>(){

                    public void onFailure(Throwable arg0) {
                        MessageBox.error("Bugger", "Bugger");
                        
                    }

                    public void onSuccess(Object arg0) {
                    	if(firelistener)
                    		GlobalConnectionFactory.getSelectionInstance().getQueryListeners().fireMoveCol(Pat.getCurrQuery(), coord[1], coord2[1]);
                    }
                    
                });
            }
    }

}
