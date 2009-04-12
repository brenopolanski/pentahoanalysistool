package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.images.widgets.WidgetImages;
import org.pentaho.pat.client.util.FlexTableCellDragController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class DimensionDropWidget extends Grid {
/**
 * Widget for allowing user to drop dimension rows/columns and mdx filters onto canvas
 * 
 * @param labelText
 * 
 * @author tom(at)wamonline.org.uk
 */

	public DimensionDropWidget(String labelText){
		
		super(1,4);
		init(labelText);
	}
	
	public void init(String labelText){
/*
 * 
 * Needs Completely reworking once cube and dimension panels are sorted.
 */
		final WidgetImages IMAGES = (WidgetImages) GWT
		.create(WidgetImages.class);
		
		
		FlexTableCellDragController tableRowDragController = new FlexTableCellDragController(Application.getPanel());
		
		Label dropLabel = new Label();
		dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
		
		 DimensionFlexTable rowTable = new DimensionFlexTable(tableRowDragController);
		 rowTable.setStyleName("dropTable"); //$NON-NLS-1$
		 
		 this.setStyleName("dropGrid"); //$NON-NLS-1$
		 
		 this.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		 this.setWidget(0, 0, IMAGES.flexend().createImage());
		 this.setWidget(0, 1, new HTML(labelText));
		 this.getCellFormatter().setStyleName(0, 1, "dropLabel"); //$NON-NLS-1$
		 this.setWidget(0, 2, rowTable);
		 this.setWidget(0, 3, IMAGES.flexend3().createImage());
		 
		 
	}
}
