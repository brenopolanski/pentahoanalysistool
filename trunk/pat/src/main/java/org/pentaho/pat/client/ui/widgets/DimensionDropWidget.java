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

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.listeners.QueryListener;
import org.pentaho.pat.client.ui.panels.DimensionPanel;
import org.pentaho.pat.client.util.FlexTableRowDropController;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.Axis.Standard;

import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public class DimensionDropWidget extends LayoutComposite implements QueryListener{

    private Standard dimAxis;

    private LayoutPanel baseLayoutPanel;

    private DimensionFlexTable dimensionTable;

    private CaptionLayoutPanel captionLayoutPanel;

    private Boolean horizontal = false;

    private FlexTableRowDropController flexTableRowDropController1;

    /**
     *TODO JAVADOC
     * 
     * @param unused
     * @param string
     *  
     */
    public DimensionDropWidget(final String labelText, final Standard targetAxis) {
        this.dimAxis = targetAxis;
        
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captionLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

    }

    public DimensionDropWidget(final String labelText, final Standard targetAxis, final Boolean orientation) {
        super();
        horizontal = orientation;
        this.dimAxis = targetAxis;
        
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);
        baseLayoutPanel.add(captionLayoutPanel);
        GlobalConnectionFactory.getQueryInstance().addQueryListener(DimensionDropWidget.this);

    }

    /**
     * Initialization.
     * 
     * @param labelText
     *            the label text
     * @param targetAxis
     *            the target axis
     */
    public final void init(final String labelText, final Axis targetAxis) {

        captionLayoutPanel = new CaptionLayoutPanel(labelText);

        dimensionTable = new DimensionFlexTable(DimensionPanel.getTableRowDragController(), horizontal);
        dimensionTable.addStyleName("pat-dropTable"); //$NON-NLS-1$

        captionLayoutPanel.add(dimensionTable);

        dimensionTable.populateDimensionTable(dimAxis);
    }

    @Override
    public void onLoad() {
        flexTableRowDropController1 = new FlexTableRowDropController(dimensionTable, dimAxis);
        DimensionPanel.getTableRowDragController().registerDropController(flexTableRowDropController1);

    }

    @Override
    public void onUnload() {
        DimensionPanel.getTableRowDragController().unregisterDropController(flexTableRowDropController1);
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onMemberMoved(com.google.gwt.user.client.ui.Widget)
     */
    public void onMemberMoved(Widget sender) {
        dimensionTable.populateDimensionTable(dimAxis);
        this.invalidate();
        this.layout();
        captionLayoutPanel.invalidate();
        captionLayoutPanel.layout();
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryChange(com.google.gwt.user.client.ui.Widget)
     */
    public void onQueryChange(Widget sender) {
        // TODO Auto-generated method stub
        dimensionTable.populateDimensionTable(dimAxis);
        this.invalidate();
        this.layout();
        captionLayoutPanel.invalidate();
        captionLayoutPanel.layout();
 
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.QueryListener#onQueryExecuted(java.lang.String, org.pentaho.pat.rpc.dto.CellDataSet)
     */
    public void onQueryExecuted(String queryId, CellDataSet matrix) {
        // TODO Auto-generated method stub
        
    }

    /**
     *TODO JAVADOC
     * @return the horizontal
     */
    public Boolean getHorizontal() {
        return horizontal;
    }

}
