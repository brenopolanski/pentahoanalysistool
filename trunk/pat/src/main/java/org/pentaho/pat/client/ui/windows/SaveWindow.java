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
import org.pentaho.pat.client.ui.panels.SaveMenuPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
        this.setFooter(buttonBar());
        this.layout();   
    }
    
    
    public LayoutPanel buttonBar(){
        Button okButton = new Button(ConstantFactory.getInstance().ok());
        okButton.addClickHandler(new ClickHandler(){

            public void onClick(ClickEvent arg0) {
                
                saveMenuPanel.save();
                
            }
            
        });
        Button cancelButton = new Button(ConstantFactory.getInstance().cancel());
        
        return ButtonBarFactory.buildOKCancelBar(okButton, cancelButton);
    }
}
