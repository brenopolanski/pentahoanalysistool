/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.rpc.dto.Axis;
import org.pentaho.pat.rpc.dto.Axis.Standard;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class DimensionDropWidget extends LayoutComposite {

    private Standard dimAxis;
    private LayoutPanel baseLayoutPanel;
    private DimensionFlexTable dimensionTable;
    /**
     *TODO JAVADOC
     * @param unused 
     * @param string 
     *
     */
    public DimensionDropWidget(String labelText, Standard targetAxis) {
        this.dimAxis = targetAxis;
        baseLayoutPanel = getLayoutPanel();
        init(labelText, dimAxis);

    }
   
    /**
     * Initialization.
     *
     * @param labelText the label text
     * @param targetAxis the target axis
     */
    public final void init(final String labelText, final Axis targetAxis) {
           
            LayoutPanel scrollLayoutPanel = new ScrollLayoutPanel();
            
            dimensionTable = new DimensionFlexTable(/*DimensionPanel.getTableRowDragController()*/);
            dimensionTable.setWidth("100%"); //$NON-NLS-1$
            final Label dropLabel = new Label(labelText);
            dropLabel.setStyleName("dropLabel"); //$NON-NLS-1$
            dimensionTable.setStyleName("dropTable"); //$NON-NLS-1$
            
            Grid dimensionGrid = new Grid(2,1);
            dimensionGrid.setWidget(0, 0, dropLabel);

            dimensionGrid.setWidget(1, 0, dimensionTable);
            dimensionGrid.setWidth("100%"); //$NON-NLS-1$
            
            scrollLayoutPanel.add(dimensionGrid);
            
            baseLayoutPanel.add(scrollLayoutPanel);
            
            dimensionTable.populateDimensionTable(dimAxis);
            }

}
