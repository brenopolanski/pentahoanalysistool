/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.widgets;

import org.pentaho.pat.client.ui.popups.CellModeMenu;
import org.pentaho.pat.rpc.dto.celltypes.MemberCell;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class CellLabelPanel extends HorizontalPanel {
    MemberCell mc = null;
    public CellLabelPanel(MemberCell mc){
        super();
        this.mc = mc;
        this.sinkEvents(NativeEvent.BUTTON_LEFT | NativeEvent.BUTTON_RIGHT | Event.ONCONTEXTMENU);
    }
    
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
        default:
            break;
        }
    }

    /**
     *TODO JAVADOC
     * @return the mc
     */
    public MemberCell getMc() {
        return mc;
    }

}
