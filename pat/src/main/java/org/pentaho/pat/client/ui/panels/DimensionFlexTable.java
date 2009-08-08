/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.MessageBox;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.Axis;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class DimensionFlexTable extends FlexTable {

    public DimensionFlexTable(/*final FlexTableRowDragController tableRowDragController*/) {
        addStyleName("demo-flextable"); //$NON-NLS-1$

        //this.trdc = tableRowDragController;
        final Label spacerLabel = new Label(""); //$NON-NLS-1$
        spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER"); //$NON-NLS-1$


        setWidget(0, 0, spacerLabel);
}

    public void populateDimensionTable(final Axis targetAxis) {
        this.clear();
        
        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), OlapPanel.getQueryId(), targetAxis, new AsyncCallback<String[]>() {

                public void onFailure(final Throwable arg0) {
                        // TODO use standardized message dialog when implemented
                        MessageBox.error("Error", "Failed to get Dim List");
                }

                public void onSuccess(final String[] arg0) {
                        for (int row = 0; row < arg0.length; row++) {
                                final Label handle = new Label(arg0[row]);
                                handle.addStyleName("drag-Dimension"); //$NON-NLS-1$
                                setWidget(row, 0, handle);
                                //trdc.makeDraggable(handle);
                        }

                }
        });

}

    public void clearDimensionTable() {
        this.clear();
        final Label spacerLabel = new Label(""); //$NON-NLS-1$
        spacerLabel.setStylePrimaryName("CSS_DEMO_INDEXED_PANEL_EXAMPLE_SPACER"); //$NON-NLS-1$
}

}
