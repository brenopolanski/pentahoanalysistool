/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import org.pentaho.pat.client.ui.widgets.DataWidget;

import com.google.gwt.user.client.ui.Widget;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class OlapPanel extends DataWidget {

    private String panelName;

    /**
     *TODO JAVADOC
     *
     */
    public OlapPanel(String name) {
        panelName = name;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#getDescription()
     */
    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return panelName;
    }

    /* (non-Javadoc)
     * @see org.pentaho.pat.client.ui.widgets.DataWidget#onInitialize()
     */
    @Override
    protected Widget onInitialize() {
        // TODO Auto-generated method stub
        return null;
    }

}
