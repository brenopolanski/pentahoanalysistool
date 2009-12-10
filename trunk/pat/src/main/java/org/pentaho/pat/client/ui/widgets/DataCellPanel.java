package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
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

public class DataCellPanel extends HorizontalPanel {

    private final MemberCell pcm;

    private final MemberCell prm;
    
    private Number cellNum;

    public DataCellPanel(final MemberCell parentColMember, final MemberCell parentRowMember, Number rawNumber) {
        super();
        sinkEvents(Event.ONDBLCLICK);
        pcm = parentColMember;
        prm = parentRowMember;
        cellNum = rawNumber;
    }

    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);

        if (DOM.eventGetType(event) == Event.ONDBLCLICK) {
            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                    Pat.getCurrDrillType(), pcm, new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {
                            // TODO Auto-generated method stub
                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                    .failedDrill(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final Object arg0) {
                            ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                                    Pat.getCurrDrillType(), prm, new AsyncCallback<Object>() {

                                        public void onFailure(final Throwable arg0) {
                                            MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory
                                                    .getInstance().failedDrill(arg0.getLocalizedMessage()));

                                        }

                                        public void onSuccess(final Object arg0) {
                                            ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(),
                                                    Pat.getCurrQuery(), new AsyncCallback<CellDataSet>() {

                                                        public void onFailure(final Throwable arg0) {
                                                            MessageBox.error(ConstantFactory.getInstance().error(),
                                                                    MessageFactory.getInstance().failedQuery(
                                                                            arg0.getLocalizedMessage()));

                                                        }

                                                        public void onSuccess(final CellDataSet arg0) {
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
    }

    public Number getCellNum() {
	return cellNum;
    }
    
    public void setCellNum(Number num) {
	this.cellNum = num;
    }
}
