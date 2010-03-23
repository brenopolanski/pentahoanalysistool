package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Data Cell Panel for the OLAPTable.
 * @author tom(at)wamonline.org.uk
 *
 */
public class DataCellPanel extends HorizontalPanel {

    private final MemberCell pcm;

    private final MemberCell prm;
    
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
        pcm = parentColMember;
        prm = parentRowMember;
        cellNum = rawNumber;
        this.setStyleName(DATA_CELL_PANEL);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);

        if (DOM.eventGetType(event) == Event.ONDBLCLICK) {
            LogoPanel.spinWheel(true);
            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                    Pat.getCurrDrillType(), pcm, new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {
                            LogoPanel.spinWheel(false);
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedDrill(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final Object arg0) {
                            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                                    Pat.getCurrDrillType(), prm, new AsyncCallback<Object>() {

                                        public void onFailure(final Throwable arg0) {
                                            LogoPanel.spinWheel(false);
                                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                    .getInstance().failedDrill(arg0.getLocalizedMessage()));

                                        }

                                        public void onSuccess(final Object arg0) {
                                            ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(),
                                                    Pat.getCurrQuery(), new AsyncCallback<CellDataSet>() {

                                                        public void onFailure(final Throwable arg0) {
                                                            LogoPanel.spinWheel(false);
                                                            MessageBox.error(ConstantFactory.getInstance().error(),
                                                                    MessageFactory.getInstance().failedQuery(
                                                                            arg0.getLocalizedMessage()));

                                                        }

                                                        public void onSuccess(final CellDataSet arg0) {
                                                            LogoPanel.spinWheel(false);
                                                            GlobalConnectionFactory.getQueryInstance()
                                                                    .getQueryListeners().fireQueryExecuted(
                                                                            DataCellPanel.this, Pat.getCurrQuery(),
                                                                            arg0);

                                                        }

                                                    });

                                        }

                                    });

                        }

                    });
            
            
        }
        else if (DOM.eventGetType(event) == Event.ONCLICK) {
            
              NewValueWidget.display();
              

        }
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
}