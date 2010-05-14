package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.core.client.DOM;
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
		 DOM.setStyleAttribute(trashPanel.getElement(), "background", "red");
		 DOM.setStyleAttribute(this.getElement(), "background", "red");
		 trashPanel.setSize("100", "100");
		 this.getLayoutPanel().setSize("100", "100");
	     //trashPanel.setWidget(Pat.IMAGES.stock_delete().createImage());
	     fTblRowDropCont = new SimplePanelDropControllerImpl(trashPanel, true);
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
