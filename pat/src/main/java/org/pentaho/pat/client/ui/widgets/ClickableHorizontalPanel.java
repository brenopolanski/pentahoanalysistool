package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.DrillType;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ClickableHorizontalPanel extends HorizontalPanel {

    
    private MemberCell pcm;
    private MemberCell prm;

    public ClickableHorizontalPanel(MemberCell parentColMember, MemberCell parentRowMember){
	super();
	sinkEvents(Event.ONDBLCLICK);
	pcm = parentColMember;
	prm = parentRowMember;
    }
    
    @Override
    public void onBrowserEvent(Event e){
	super.onBrowserEvent(e);
	
	if(DOM.eventGetType(e)== Event.ONDBLCLICK){
	    ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(), DrillType.POSITION, pcm, new AsyncCallback(){

		public void onFailure(Throwable arg0) {
		    // TODO Auto-generated method stub
		    MessageBox.error("Error", "Error");
		}

		public void onSuccess(Object arg0) {
		   ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(), DrillType.POSITION, prm, new AsyncCallback(){

		    public void onFailure(Throwable arg0) {
			MessageBox.error("Error", "Error");
			
		    }

		    public void onSuccess(Object arg0) {
			ServiceFactory.getQueryInstance().executeQuery(Pat.getSessionID(), Pat.getCurrQuery(), new AsyncCallback<CellDataSet>(){

			    public void onFailure(Throwable arg0) {
				MessageBox.error("Error", "Error");
				
			    }

			    public void onSuccess(CellDataSet arg0) {
				 GlobalConnectionFactory.getQueryInstance().getQueryListeners().fireQueryExecuted(
		                                ClickableHorizontalPanel.this, Pat.getCurrQuery(), arg0);
				
			    }
			    
			});
			
		    }
		       
		   });
		    
		}
		
	    });
	}
    }
}
