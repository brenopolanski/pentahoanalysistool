package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDropControllerImpl;
import org.pentaho.pat.rpc.dto.IAxis;

public class QueryTrashWidget extends LayoutComposite{

	private SimplePanelDragControllerImpl tblRowDragCont = Application.SimplePanelDrgCont;
	

	SimplePanelDropControllerImpl fTblRowDropCont;
    
	public QueryTrashWidget(){
		 DimensionSimplePanel trashPanel = new DimensionSimplePanel(false, IAxis.UNUSED);
		 trashPanel.setSize("50", "50");
		 this.getLayoutPanel().setSize("50", "50");
	     trashPanel.setWidget(Pat.IMAGES.bin_empty().createImage());
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
