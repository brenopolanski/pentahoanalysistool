package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.ITableListener;
import org.pentaho.pat.client.util.Operation;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Data Cell Panel for the OLAPTable.
 * @author tom(at)wamonline.org.uk
 *
 */
public class DataCellPanel extends HorizontalPanel implements ITableListener {

    private Number cellNum;

    private final static String DATA_CELL_PANEL = "pat-DataCellPanel"; //$NON-NLS-1$
    
    /**
     * Creates a DataCellPanel.
     * @param parentColMember
     * @param parentRowMember
     * @param rawNumber
     */
    public DataCellPanel(final MemberCell parentColMember, final MemberCell parentRowMember, Number rawNumber) {
        super();
        sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
        cellNum = rawNumber;
        this.setStyleName(DATA_CELL_PANEL);
        GlobalConnectionFactory.getOperationInstance().addTableListener(this);
    }
    
    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
     */
//    @Override
//    public void onBrowserEvent(final Event event) {
//        super.onBrowserEvent(event);
//        if (DOM.eventGetType(event) == Event.ONDBLCLICK) {
//            LogoPanel.spinWheel(true);
//            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
//                    Pat.getCurrDrillType(), pcm, new AsyncCallback<Object>() {
//
//                        public void onFailure(final Throwable arg0) {
//                            LogoPanel.spinWheel(false);
//                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
//                                    .failedDrill(arg0.getLocalizedMessage()));
//                        }
//
//                        public void onSuccess(final Object arg0) {
//                            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
//                                    Pat.getCurrDrillType(), prm, new AsyncCallback<Object>() {
//
//                                        public void onFailure(final Throwable arg0) {
//                                            LogoPanel.spinWheel(false);
//                                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
//                                                    .getInstance().failedDrill(arg0.getLocalizedMessage()));
//
//                                        }
//
//                                        public void onSuccess(final Object arg0) {
//                                            ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(),
//                                                    Pat.getCurrQuery(), new AsyncCallback<CellDataSet>() {
//
//                                                        public void onFailure(final Throwable arg0) {
//                                                            LogoPanel.spinWheel(false);
//                                                            MessageBox.error(ConstantFactory.getInstance().error(),
//                                                                    MessageFactory.getInstance().failedQuery(
//                                                                            arg0.getLocalizedMessage()));
//
//                                                        }
//
//                                                        public void onSuccess(final CellDataSet arg0) {
//                                                            LogoPanel.spinWheel(false);
//                                                            GlobalConnectionFactory.getQueryInstance()
//                                                                    .getQueryListeners().fireQueryExecuted(
//                                                                            DataCellPanel.this, Pat.getCurrQuery(),
//                                                                            arg0);
//
//                                                        }
//
//                                                    });
//
//                                        }
//
//                                    });
//
//                        }
//
//                    });
//            
//            
//        }
//        else if (DOM.eventGetType(event) == Event.ONCLICK) {
//            
//              NewValueWidget.display();
//              
//
//        }
//    }

  

    /**
     * Return the cells number.
     * @return
     */
    public Number getCellNum() {
	return cellNum;
    }
    
    /**
     * Set the cells number. 
     */
    public void setCellNum(Number num) {
	this.cellNum = num;
    }

    public void onDrillThroughExecuted(String queryId, String[][] drillThroughResult) {
        // TODO Auto-generated method stub
        
    }

    public void onOperationExecuted(Operation operation) {
        ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES.toolCollapseDown());
        collapseBtn3.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                ServiceFactory.getQueryInstance().drillThrough(Pat.getSessionID(), Pat.getCurrQuery(),new AsyncCallback<String[][]>() {
                    
                    public void onSuccess(String[][] arg0) {
                        GlobalConnectionFactory.getOperationInstance().getTableListeners().fireDrillThroughExecuted(DataCellPanel.this, Pat.getCurrQuery(), arg0);
                    }
                    
                    public void onFailure(Throwable arg0) {
                        MessageBox.alert("error", "drillthrough error");
                        
                    }
                });

                
            }
        });
        
     
        if (Operation.ENABLE_DRILLTHROUGH.equals(operation)) {
                this.add(collapseBtn3);
        }
        
        if (Operation.DISABLE_DRILLTHROUGH.equals(operation)) {
            if ( this.getWidgetCount() > 1)

                    this.remove(1);
//            this.getChildren().remove(collapseBtn3);
//            this.remove(collapseBtn3);
        }
        
    }
}
