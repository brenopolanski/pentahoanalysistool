package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CellDataSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A widget to allow you to add a new value to a cell, for use with mondrian scenarios.
 * @author tom(at)wamonline.org.uk
 *
 */
public class NewValueWidget extends WindowPanel {

  
    private static int ordinal;

	private final LayoutPanel winContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    
    private static final String WINDOW_TITLE = "Change Value";
    
    private final static NewValueWidget CBW = new NewValueWidget();
    
    private TextBox input = new TextBox();
    
    public NewValueWidget(){
	
	 super(WINDOW_TITLE);
	  
	  LayoutPanel upload = new LayoutPanel();
	  upload.add(createUploadFileContent(upload));
	        winContentpanel.add(upload, new BoxLayoutData(FillStyle.BOTH));
	        this.setWidget(winContentpanel);
	        this.setFooter(buttonBar());
	        this.layout();
	        
	       
    }
    
    public static void display(int ord) {
    	ordinal = ord;
        CBW.setSize("450px", "300px"); //$NON-NLS-1$ //$NON-NLS-2$
        CBW.showModal(false);
        CBW.layout();
    }

    private VerticalPanel createUploadFileContent(LayoutPanel upload2) {
	 VerticalPanel vPanel = new VerticalPanel();

	    // Add a label
	    vPanel.add(new HTML("Enter a new value"));

	    
	    
	    vPanel.add(input);
	    
	return vPanel;
    }
    
    public LayoutPanel buttonBar() {
        final Button okButton = new Button(Pat.CONSTANTS.ok());
        okButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
        ServiceFactory.getQueryInstance().alterCell(Pat.getSessionID(), Pat.getCurrQuery(), Pat.getCurrConnectionId(),Pat.getCurrScenario(), ordinal, Integer.parseInt(input.getText()), new AsyncCallback<CellDataSet>(){

      		public void onFailure(Throwable arg0) {
      		    // TODO Auto-generated method stub
      		    
      		}

      		public void onSuccess(CellDataSet arg0) {
                Pat.executeQuery(NewValueWidget.this, Pat.getCurrQuery());
      		}
      		
      	    });
        }

        });
        final Button cancelButton = new Button(Pat.CONSTANTS.cancel());
        cancelButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
               
        
            }

        });
        return ButtonBarFactory.buildOKCancelBar(okButton, cancelButton);
    }
}
