package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDropControllerImpl;

import com.google.gwt.user.client.ui.SimplePanel;

public class QueryTrashWidget extends LayoutComposite{

	private SimplePanelDragControllerImpl tblRowDragCont = Application.SimplePanelDrgCont;
	

	SimplePanelDropControllerImpl fTblRowDropCont;
    
	public QueryTrashWidget(){
		 SimplePanel trashPanel = new SimplePanel();
		 trashPanel.setSize("50", "50");
		 this.getLayoutPanel().setSize("50", "50");
	     //trashPanel.setWidget(Pat.IMAGES.stock_delete().createImage());
	     fTblRowDropCont = new SimplePanelDropControllerImpl(trashPanel, true);
	     trashPanel.setStylePrimaryName("pat-QueryTrashWidget");
	     this.getLayoutPanel().add(trashPanel);
	     
	}
	

    @Override
    protected void onLoad(){
    	
        tblRowDragCont.registerDropController(fTblRowDropCont);
    }
    
    @Override
    protected void onUnload(){
    	tblRowDragCont.unregisterDropController(fTblRowDropCont);
    }
}
