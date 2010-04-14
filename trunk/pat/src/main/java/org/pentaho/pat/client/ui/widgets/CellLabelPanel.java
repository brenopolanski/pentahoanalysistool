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
import org.pentaho.pat.client.ui.popups.CellModeMenu;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * Creates the panel for Cell Labels.
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class CellLabelPanel extends HorizontalPanel {
    private MemberCell memCell = null;

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
            ServiceFactory.getQueryInstance().drillThrough(Pat.getSessionID(), Pat.getCurrQuery(),new AsyncCallback<String[][]>() {
                
                public void onSuccess(String[][] arg0) {
//
//                    if (arg0 != null && arg0[0] != null) {
//                    TableDataSet ts = new TableDataSet(arg0[0].length,arg0.length);
//                    ts.setTableHeader(arg0[0]);
//                    ts.setTableBody(arg0);
//                    DrillThroughWindow.display(ts);
//                    }
                    GlobalConnectionFactory.getOperationInstance().getTableListeners().fireDrillThroughExecuted(CellLabelPanel.this, Pat.getCurrQuery(), arg0);
                }
                
                public void onFailure(Throwable arg0) {
                    MessageBox.alert("error", "drillthrough error");
                    
                }
            });
            break;
        case Event.ONCONTEXTMENU:
            final CellModeMenu test = new CellModeMenu();
            test.showContextMenu(event, this);
            test.setPopupPositionAndShow(new PositionCallback() {
                public void setPosition(final int offsetWidth, final int offsetHeight) {
                    test.setPopupPosition(event.getClientX(), event.getClientY());
                }
            });
        default:
            break;
        }
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
