package org.pentaho.pat.client.ui.widgets;

import java.util.List;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.IOperationListener;
import org.pentaho.pat.client.util.Operation;
import org.pentaho.pat.client.util.factory.EventFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.DataCell;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Data Cell Panel for the OLAPTable.
 * @author tom(at)wamonline.org.uk
 *
 */
public class DataCellPanel extends HorizontalPanel implements IOperationListener {

    private Number cellNum;
    
    private String queryId; 
    
    private List<Integer> coordinates = null;

	private int ordinal;

    private final static String DATA_CELL_PANEL = "pat-DataCellPanel"; //$NON-NLS-1$
        
    
    
    /**
     * Creates a DataCellPanel.
     * @param parentColMember
     * @param parentRowMember
     * @param rawNumber
     */
    public DataCellPanel(final MemberCell parentColMember, final MemberCell parentRowMember, Number rawNumber, List<Integer> coordinates, final int ordinal) {
        super();
        this.setOrdinal(ordinal);
        this.coordinates = coordinates;
        sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
        cellNum = rawNumber;
        this.setStyleName(DATA_CELL_PANEL);
        EventFactory.getOperationInstance().addOperationListener(this);
        queryId = Pat.getCurrQuery();
        this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
            }
    

  

    public DataCellPanel(final int ordinal, DataCell dataCell) {
    	this.ordinal = ordinal;
    	Button b = new Button("click");
        b.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent arg0) {
				NewValueWidget.display(ordinal);
								
			}
        	
        });
        this.add(b);

	}




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

    public void onOperationExecuted(String _queryId, Operation operation) {
        if (this.queryId.equals(_queryId)) {
            ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES.toolCollapseDown());
            collapseBtn3.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent arg0) {
                    ServiceFactory.getQueryInstance().drillThrough(Pat.getSessionID(), Pat.getCurrQuery(),coordinates, new AsyncCallback<String[][]>() {

                        public void onSuccess(String[][] arg0) {
                            EventFactory.getOperationInstance().getOperationListeners().fireDrillThroughExecuted(DataCellPanel.this, Pat.getCurrQuery(), arg0);
                        }

                        public void onFailure(Throwable arg0) {
                            MessageBox.alert(Pat.CONSTANTS.error(), MessageFactory.getInstance().failedDrillThrough(arg0.getMessage()));

                        }
                    });


                }
            });


            if (Operation.ENABLE_DRILLTHROUGH.equals(operation)) {
                if(cellNum != null) {
                    this.add(collapseBtn3);

                }
            }

            if (Operation.DISABLE_DRILLTHROUGH.equals(operation)) {
                this.clear();
            }
        }
    }




    public void onDrillStyleChanged(String queryId, DrillType drillType) {
        // TODO Auto-generated method stub
        
    }




	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}




	public int getOrdinal() {
		return ordinal;
	}

}
