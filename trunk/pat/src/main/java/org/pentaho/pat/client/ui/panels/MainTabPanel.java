/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 * 
 * @author tom(at)wamonline.org.uk
 * 
 */
public class MainTabPanel extends LayoutComposite {

    private final static DecoratedTabLayoutPanel contentWrapper = new DecoratedTabLayoutPanel();

    /**
     *TODO JAVADOC
     * 
     */
    public MainTabPanel() {
        super();
        final LayoutPanel rootLayoutPanel = getLayoutPanel();

        contentWrapper.addStyleName("pat-content-wrapper"); //$NON-NLS-1$
        contentWrapper.addSelectionHandler(new SelectionHandler<Integer>() {
            public void onSelection(final SelectionEvent<Integer> selectEvent) {
                final Widget widget = contentWrapper.getWidget(selectEvent.getSelectedItem());

               /* if (widget instanceof QueryPanel) {

                }*/

            }
        });

        rootLayoutPanel.add(contentWrapper);
    }

}
