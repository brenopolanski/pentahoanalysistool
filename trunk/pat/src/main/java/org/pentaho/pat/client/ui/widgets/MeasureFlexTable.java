package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class MeasureFlexTable {

    private final static String TABLE_CSS_NAME = "dropFlexTable2"; //$NON-NLS-1$
    CaptionLayoutPanel captLayoutPanel = new CaptionLayoutPanel("Measures");
    ScrollLayoutPanel scrollLayoutPanel = new ScrollLayoutPanel();
    FlexTable flexTable = new FlexTable();
    public MeasureFlexTable(DragController dragController) {
	
	//addStyleName(TABLE_CSS_NAME);
	
	setupTable();
	
	dragController.makeDraggable(captLayoutPanel, captLayoutPanel.getHeader());
	
    }
    
    private void setupTable(){
	
	scrollLayoutPanel.add(flexTable);
	
	captLayoutPanel.add(scrollLayoutPanel);
    }
    
    public void setWidget(int row, int col, Widget widget){
	flexTable.setWidget(row, col, widget);
    }
    
    public void clear(){
	flexTable.clear();
    }

}
