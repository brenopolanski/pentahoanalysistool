/**
 * TODO JAVADOC
 */
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;

/**
 *TODO JAVADOC
 *
 * @author bugg
 *
 */
public class SaveWindow extends WindowPanel{

    private static final String TITLE = "Save As....";
    
    private final SaveMenuPanel saveMenuPanel = new SaveMenuPanel();

    private final LayoutPanel windowContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    
    private final static SaveWindow cbw = new SaveWindow();
    
    final LayoutPanel buttonBar = ButtonBarFactory.buildOKCancelBar(new Button("OK"), new Button("Cancel"));
    
    public static void display() {
        cbw.setSize("450px", "300px"); //$NON-NLS-1$ //$NON-NLS-2$
        
        cbw.showModal(true);
        cbw.layout();
    }
    
    public SaveWindow(){
        super(TITLE);
        windowContentpanel.add(saveMenuPanel, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(windowContentpanel);
        this.setFooter(buttonBar);
        this.layout();   
    }
    
    

}
