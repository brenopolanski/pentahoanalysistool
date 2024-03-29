/*
 * Copyright (C) 2009 Tom Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDropControllerImpl;
import org.pentaho.pat.rpc.dto.query.IAxis;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionSimplePanel extends SimplePanel {

    private Boolean horizontal = false;

    private IAxis axis;

    private final static String TABLE_CSS_NAME = "pat-DimensionSimplePanel"; //$NON-NLS-1$

    private SimplePanelDragControllerImpl tblRowDragCont = Application.INSTANCE.getSimplePanelDrgCont();

    SimplePanelDropControllerImpl fTblRowDropCont;

	private boolean measurebox;

	private String dimensionAssociation;

    private boolean isTrash;
    
    Image nonengagedimage = Pat.IMAGES.bin_empty().createImage();
    
    Image engagedimage = Pat.IMAGES.bin_full().createImage();
    
    /**
     * Create a flextable widget for the DimensionDropWidget.
     * 
     */
    public DimensionSimplePanel(final boolean measurebox, final IAxis axis) {
        super();
        this.setStyleName(TABLE_CSS_NAME);
        this.setSize("100", "50");
        fTblRowDropCont = new SimplePanelDropControllerImpl(DimensionSimplePanel.this, false);
        this.setAxis(axis);
        this.setMeasurebox(measurebox);
        if(axis==IAxis.ROWS){
        this.setWidget(Pat.IMAGES.arrow_right().createImage());
        }
        else if(axis ==IAxis.COLUMNS){
            this.setWidget(Pat.IMAGES.arrow_down().createImage());
        }
        if(measurebox==true){
            this.setWidget(new Label("Measure/Member"));
        }
    }
    
    public DimensionSimplePanel(final boolean measurebox, final String dimensionAssociation, final IAxis axis) {
        this(measurebox,axis);
        this.setDimensionAssociation(dimensionAssociation);
    }
    
    public void removeSize(){
        this.setSize(null, null);
    }

    @Override
    protected void onLoad() {

        tblRowDragCont.registerDropController(fTblRowDropCont);
    }

    @Override
    protected void onUnload() {
        tblRowDragCont.unregisterDropController(fTblRowDropCont);
    }

    /**
     * Return true if the widget is in horizontal orientation.
     * 
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

    /**
     * Set the mondrian axis for this widget.
     * 
     * @param axis
     */
    public void setAxis(final IAxis axis) {
        this.axis = axis;
    }

    /**
     * Get the mondrian axis for this widget.
     * 
     * @return
     */
    public IAxis getAxis() {
        return axis;
    }

    public int[] getCoord() {
        return getWidgetRow(this, ((FlexTable) this.getParent()));
    }

    private int[] getWidgetRow(Widget widget, FlexTable table) {

        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getCellCount(row); col++) {
                Widget w = table.getWidget(row, col);
                if (w == widget) {
                    return new int[] {row, col};
                }
            }
        }

        return null;
    }

	public void setMeasurebox(boolean measurebox) {
		this.measurebox = measurebox;
	}

	public boolean isMeasurebox() {
		return measurebox;
	}

	public void setDimensionAssociation(String dimensionAssociation) {
		this.dimensionAssociation = dimensionAssociation;
	}

	public String getDimensionAssociation() {
		return dimensionAssociation;
	}

    public void setTrash(boolean isTrash) {
        this.isTrash = isTrash;
        if(isTrash){
            this.setWidget(nonengagedimage);
        }
        else{
            this.remove(nonengagedimage);
        }
    }

    public boolean isTrash() {
        return isTrash;
    }

    public void setEngaged(boolean b) {
        if(b){
            this.remove(nonengagedimage);
            this.setWidget(engagedimage);
        }
        else{
            this.remove(engagedimage);
            this.setWidget(nonengagedimage);
        }
        
    }
}
