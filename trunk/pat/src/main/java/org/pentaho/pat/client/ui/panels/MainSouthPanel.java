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
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.ITableListener;
import org.pentaho.pat.client.util.Operation;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.rpc.dto.TableDataSet;
import org.pentaho.pat.rpc.dto.enums.DrillType;

/**
 * Collapsable Southpanel for MainTabPanel
 * @created Apr 14, 2010 
 * @since 0.7
 * @author Paul Stoellberger
 * 
 */
public class MainSouthPanel extends CaptionLayoutPanel implements ITableListener {

    private DrillThroughPanel dtp = new DrillThroughPanel();
    private LayoutPanel baselayoutPanel = getLayoutPanel();
    private LayoutPanel parent = null;
    private String queryId;
    private LayoutPanel simple  = new LayoutPanel(new BorderLayout());
    
    /**
     * 
     */
    public MainSouthPanel(LayoutPanel _parent) {
        parent = _parent;
        queryId = Pat.getCurrQuery();
        initializeWidget();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (parent != null) {
            parent.layout();
        }
        GlobalConnectionFactory.getOperationInstance().addTableListener(MainSouthPanel.this);
    };
    
   

    @Override
    protected void onUnload() {
        super.onUnload();
        if (parent != null) {
            parent.layout();
        }
       
        GlobalConnectionFactory.getOperationInstance().removeTableListener(MainSouthPanel.this);
    };

    private void initializeWidget() {
        this.setLayout(new BoxLayout(Orientation.VERTICAL));
        // FIXME remove that and use style
        DOM.setStyleAttribute(baselayoutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        
//        final ImageButton collapseBtn = new ImageButton(Caption.IMAGES.toolCollapseDown());
//        this.getHeader().add(collapseBtn, CaptionRegion.RIGHT);
//
//        ClickHandler collapseClick = new ClickHandler() {
//            public void onClick(final ClickEvent event) {
//                MainSouthPanel.this.setCollapsed(!MainSouthPanel.this.isCollapsed());
//                if (MainSouthPanel.this.isCollapsed())
//                    collapseBtn.setImage(Caption.IMAGES.toolCollapseDown().createImage());
//                else
//                    collapseBtn.setImage(Caption.IMAGES.toolCollapseUp().createImage());
//                
//                MainSouthPanel.this.layout();
//                
//            }
//        };
//        MainSouthPanel.this.getHeader().addClickHandler(collapseClick);
        
        this.add(simple,new BoxLayoutData(FillStyle.BOTH));
    }
    
    
    /* (non-Javadoc)
     * @see org.pentaho.pat.client.listeners.ITableListener#onOperationExecuted(org.pentaho.pat.client.util.Operation)
     */
    public void onOperationExecuted(String queryId, Operation operation) {
        if (this.queryId.equals(queryId) && this.isAttached()) {
            if (operation.equals(Operation.ENABLE_DRILLTHROUGH)) {
                this.getHeader().setText(ConstantFactory.getInstance().drillThroughPanel());
                simple.clear();
                simple.add(dtp);
                parent.setCollapsed(this,false);
                this.setVisible(true);
                WidgetHelper.revalidate(this);
            }

            if (operation.equals(Operation.DISABLE_DRILLTHROUGH)) {
                this.remove(dtp);
                this.getHeader().setText("");
                parent.setCollapsed(this,false);
                this.setVisible(false);
                WidgetHelper.revalidate(this);
            }
        }
    }

    public void onDrillThroughExecuted(String queryId, String[][] result) {
        if (this.queryId.equals(queryId)  && this.isAttached()) {
            TableDataSet tds = new TableDataSet();
            tds.setData(result);
            dtp.setData(tds);
            WidgetHelper.revalidate(this);
        }
    }

    public void onDrillStyleChanged(String queryId, DrillType drillType) {
        // TODO Auto-generated method stub
        
    }

}
