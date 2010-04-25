package org.pentaho.pat.client.ui.widgets;

import java.util.List;

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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Data Cell Panel for the OLAPTable.
 * @author tom(at)wamonline.org.uk
 *
 */
public class DataCellPanel extends HorizontalPanel implements ITableListener {

    private Number cellNum;
    
    private List<Integer> coordinates = null;

    private final static String DATA_CELL_PANEL = "pat-DataCellPanel"; //$NON-NLS-1$
        
    
    
    /**
     * Creates a DataCellPanel.
     * @param parentColMember
     * @param parentRowMember
     * @param rawNumber
     */
    public DataCellPanel(final MemberCell parentColMember, final MemberCell parentRowMember, Number rawNumber, List<Integer> coordinates) {
        super();
        this.coordinates = coordinates;
        sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
        cellNum = rawNumber;
        this.setStyleName(DATA_CELL_PANEL);
        GlobalConnectionFactory.getOperationInstance().addTableListener(this);
        
        this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
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

    public void onOperationExecuted(Operation operation) {
        ImageButton collapseBtn3 = new ImageButton(Caption.IMAGES.toolCollapseDown());
        collapseBtn3.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                ServiceFactory.getQueryInstance().drillThrough(Pat.getSessionID(), Pat.getCurrQuery(),coordinates, new AsyncCallback<String[][]>() {
                    
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
                if(cellNum != null) {
                    this.add(collapseBtn3);

                }
        }
        
        if (Operation.DISABLE_DRILLTHROUGH.equals(operation)) {
            this.clear();
        }
        
    }

}
