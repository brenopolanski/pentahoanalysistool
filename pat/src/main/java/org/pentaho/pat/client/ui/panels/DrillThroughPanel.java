/*
 * Copyright (C) 2009 Paul Stoellberger
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
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.ui.widgets.ResultSetTable;
import org.pentaho.pat.rpc.dto.TableDataSet;

/**
 * Panel for Drillthrough Table
 * @created Apr 14, 2010 
 * @since 0.7
 * @author Paul Stoellberger
 * 
 */
public class DrillThroughPanel extends LayoutComposite {

    private ResultSetTable rTable = new ResultSetTable();
    
    /**
     * 
     */
    public DrillThroughPanel(TableDataSet tableData) {
        initializeWidget();
        rTable.setData(tableData);
        this.layout();
       
    }

    public DrillThroughPanel() {
        initializeWidget();
        this.layout();
       
    }
    
    protected void initializeWidget() {

        final LayoutPanel baselayoutPanel = getLayoutPanel();
        baselayoutPanel.clear();
        baselayoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        // FIXME remove that and use style
        DOM.setStyleAttribute(baselayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        baselayoutPanel.add(rTable, new BoxLayoutData(FillStyle.BOTH));

    }
    
    public void setData(TableDataSet tableData) {
        rTable.setData(tableData);
        this.layout();
    }
    
    public void refresh() {
        rTable.refresh();
        initializeWidget();
        this.layout();
    }

}
