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
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDragControllerImpl;
import org.pentaho.pat.client.util.dnd.impl.SimplePanelDropControllerImpl;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.user.client.ui.SimplePanel;

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
    
    private final static String TABLE_CSS_NAME = "pat-DimensionFlexTable"; //$NON-NLS-1$

    private SimplePanelDragControllerImpl tblRowDragCont = Application.SimplePanelDrgCont;
    
    private int[] coord;
    
    SimplePanelDropControllerImpl fTblRowDropCont;
    /**
     * Create a flextable widget for the DimensionDropWidget.
     * 
     */
    public DimensionSimplePanel(final IAxis axis, int[] coord) {
        super();
        this.setStyleName(TABLE_CSS_NAME);
        this.setSize("100", "100");
        fTblRowDropCont = new SimplePanelDropControllerImpl(DimensionSimplePanel.this);
        this.setAxis(axis);
        this.setCoord(coord);
    }

    /**
     * Create a flextable widget for the DimensionDropWidget.
     * @param orientation
     * @param axis
     */
    public DimensionSimplePanel(final Boolean orientation, final IAxis axis) {
        super();
        this.setStyleName(TABLE_CSS_NAME);
        this.setAxis(axis);
        horizontal = orientation;

    }

    @Override
    protected void onLoad(){
    	
        tblRowDragCont.registerDropController(fTblRowDropCont);
    }
    
    @Override
    protected void onUnload(){
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
     * @param axis
     */
    public void setAxis(final IAxis axis) {
	this.axis = axis;
    }

    /**
     * Get the mondrian axis for this widget.
     * @return
     */
    public IAxis getAxis() {
	return axis;
    }

	public void setCoord(int[] coord) {
		this.coord = coord;
	}

	public int[] getCoord() {
		return coord;
	}
  
}
