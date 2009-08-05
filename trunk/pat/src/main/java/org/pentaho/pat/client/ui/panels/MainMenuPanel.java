/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

/**
 *TODO JAVADOC
 *
 * @author tom(at)wamonline.org.uk
 *
 */
public class MainMenuPanel extends LayoutComposite {

    private LayoutPanel rootPanel = getLayoutPanel();
    
    public MainMenuPanel(){
        super();
        rootPanel.setLayout(new BoxLayout());        
    }
    
    
}
