package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.images.widgets.WidgetImages;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.rpc.beans.Axis;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DimensionDropWidget extends Grid{
/**
 * Widget for allowing user to drop dimension rows/columns and mdx filters onto canvas
 * 
 * @param labelText
 * 
 * @author tom(at)wamonline.org.uk
 * @return 
 */
	private Axis dimAxis;
	private DimensionFlexTable	table1;
	public DimensionDropWidget(String labelText, Axis targetAxis){
		
		super(2,1);
		
		this.dimAxis=targetAxis;
		init(labelText, dimAxis);
	}
	
	public void init(String labelText, Axis targetAxis){
/*
 * 
 * Needs Completely reworking once cube and dimension panels are sorted.
 */
		table1 = new DimensionFlexTable(DimensionPanel.getDragController());

		FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(table1, targetAxis);
	    DimensionPanel.getDragController().registerDropController(flexTableRowDropController1);
	    Label dropLabel = new Label(labelText);
	    dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
	    table1.setStyleName("dropTable"); //$NON-NLS-1$
	    this.setWidget(0, 0, dropLabel);
	    
	    this.setWidget(1, 0, table1);
	/*	final WidgetImages IMAGES = (WidgetImages) GWT
		.create(WidgetImages.class);
		
		
		
		Label dropLabel = new Label();
		
		dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
		//FlexTableRowDragController tableRowDragController = new FlexTableRowDragController(Application.getPanel());
		 
		
		
		//FlexTableRowDragController tableRowDragController = new FlexTableRowDragController(Application.getPanel());
		
		DimensionFlexTable rowTable = new DimensionFlexTable(DimensionPanel.getDragController());
		
		FlexTableRowDropController flexTableRowDropController1 = new FlexTableRowDropController(rowTable);
	    DimensionPanel.getDragController().registerDropController(flexTableRowDropController1);
	    
		
		rowTable.setStyleName("dropTable"); //$NON-NLS-1$
		//TODO Rework the drop stuff
        
		 this.setStyleName("dropGrid"); //$NON-NLS-1$
		 
	//	 this.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
	//	 this.setWidget(0, 0, IMAGES.flexend().createImage());
		 this.setWidget(0, 1, new HTML(labelText));
	//	 this.getCellFormatter().setStyleName(0, 1, "dropLabel"); //$NON-NLS-1$
		 this.setWidget(0, 2, rowTable);
	//	 this.setWidget(0, 3, IMAGES.flexend3().createImage());
		 
		 */
	}

	public void populateDimensionTable() {
		
		table1.populateDimensionTable(dimAxis);
		
	}
}
