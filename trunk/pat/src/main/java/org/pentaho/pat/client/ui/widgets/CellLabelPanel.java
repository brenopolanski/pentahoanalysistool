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

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.listeners.ITableListener;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.ui.popups.CellModeMenu;
import org.pentaho.pat.client.util.Operation;
import org.pentaho.pat.client.util.PanelUtil.PanelType;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;
import org.pentaho.pat.rpc.dto.enums.DrillType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * Creates the panel for Cell Labels.
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class CellLabelPanel extends HorizontalPanel implements ITableListener {
    private MemberCell memCell = null;

    private Image drillButton;
    
    private final static String CELL_LABEL_PANEL = "pat-cellLabel"; //$NON-NLS-1$
    
    /**
     * Create the CellLabelPanel.
     * @param memCell
     */
    public CellLabelPanel(final MemberCell memCell) {
        super();
        this.memCell = memCell;
        this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
        this.setStyleName(CELL_LABEL_PANEL);
        
        GlobalConnectionFactory.getOperationInstance().addTableListener(this);
        
        drillButton = null;
        if (memCell != null && memCell.getRawValue() != null && PanelType.QM.equals(Pat.getCurrPanelType())) {
            drillButton = new Image() {

                public void onBrowserEvent(final Event event) {
                    if (DOM.eventGetType(event) == Event.ONCLICK) {
                        LogoPanel.spinWheel(true);
                        ServiceFactory.getQueryInstance().drillPosition(Pat.getSessionID(), Pat.getCurrQuery(),
                                Pat.getCurrDrillType(), memCell, new AsyncCallback<Object>() {

                            public void onFailure(Throwable arg0) {
                                LogoPanel.spinWheel(false);
                                MessageBox.alert(Pat.CONSTANTS.error(), MessageFactory
                                        .getInstance().failedDrill(arg0.getLocalizedMessage()));
                            }

                            public void onSuccess(Object arg0) {
                                Pat.executeQuery(CellLabelPanel.this,Pat.getCurrQuery());
                            }

                        });
                    }
                }
            };

            setDrillIcon(Pat.getCurrDrillType());
            this.add(drillButton);

        }
        
        
        
    }

    private void setDrillIcon(DrillType drillType) {

        if (drillButton != null) {
            boolean setIcon = false;
            if (drillType != null && memCell.getRawValue() != null) {
                if (drillType.equals(DrillType.POSITION) && memCell.getChildMemberCount() > 0) {
                    if (memCell.isExpanded()) {
                        drillButton.setUrl(GWT.getModuleBaseURL() + "closeButton.png"); //$NON-NLS-1$
                    } else {
                        drillButton.setUrl(GWT.getModuleBaseURL() + "drill.png"); //$NON-NLS-1$
                    }
                    setIcon = true;
                }
                if (memCell.getChildMemberCount() > 0 && drillType.equals(DrillType.REPLACE)) {
                    drillButton.setUrl(GWT.getModuleBaseURL() + "arrow_down.png"); //$NON-NLS-1$
                    setIcon = true;
                }

                if (memCell.getParentMember() != null && drillType.equals(DrillType.UP)) {
                    drillButton.setUrl(GWT.getModuleBaseURL() + "arrow_up.png"); //$NON-NLS-1$
                    setIcon = true;
                }

                if (drillType.equals(DrillType.NONE)) {
                    setIcon = false;
                }
            }

            drillButton.setVisible(setIcon);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
        case Event.ONCLICK:
            break;
        case Event.ONCONTEXTMENU:
            final CellModeMenu test = new CellModeMenu();
            test.showContextMenu(event, this);
            test.setPopupPositionAndShow(new PositionCallback() {
                public void setPosition(final int offsetWidth, final int offsetHeight) {
                    test.setPopupPosition(event.getClientX(), event.getClientY());
                }
            });
            break;
        default:
            break;
        }
    }


    public void onDrillStyleChanged(String queryId, DrillType drillType) {
        if (Pat.getCurrQuery().equals(queryId)) {
            setDrillIcon(drillType);
        }
        
    }

    public void onDrillThroughExecuted(String queryId, String[][] drillThroughResult) {
        // TODO Auto-generated method stub
        
    }

    public void onOperationExecuted(String queryId, Operation operation) {
        // TODO Auto-generated method stub
        
    }

    /**
     * Return this panels member cell.
     * 
     * @return the mc
     */
    public MemberCell getMc() {
        return memCell;
    }

}
