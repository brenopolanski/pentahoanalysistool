/**
 * 
 */
package org.pentaho.pat.client.panels;


import org.gwt.mosaic.ui.client.WindowPanel;

import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.MessageFactory;
import org.pentaho.pat.client.util.ServiceFactory;

import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;

/**
 * @author paul
 *
 */
public class ConnectPanel extends WindowPanel implements
		SourcesConnectionEvents {

	Button connectBtn;
	TextBox serverTB;
	Label serverLbl;
	TextBox portTB;
	Label portLbl;
	TextBox databaseTB;
	Label databaseLbl;
	TextBox userTB;
	Label userLbl;
	PasswordTextBox passwordTB;
	Label passwordLbl;
	FileUpload fileUpload;
	Button uploadButton;
	FormPanel fpanel;
	
	
	public ConnectPanel() {
		
		this.setTitle("Register new Mondrian Connection");
		MessageFactory.getInstance().disconnect();
		connectBtn = new Button(MessageFactory.getInstance().connect());
		uploadButton = new Button(MessageFactory.getInstance().upload());
		serverTB = new TextBox();
		portTB = new TextBox();
		databaseTB = new TextBox();
		userTB = new TextBox();
		passwordTB = new PasswordTextBox();
		fileUpload = new FileUpload();
		this.setWidget(onInitialize());
		this.setWidth("700");
		this.setHeight("300");
		
	}
	
	
	  protected Widget onInitialize() {
		  fpanel = new FormPanel();
		  fpanel.setAction("schemaupload");
		  fpanel.addFormHandler(new FormHandler() {
			 
			  public void onSubmitComplete(FormSubmitCompleteEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			 public void onSubmit(FormSubmitEvent arg0) {
				// TODO Auto-generated method stub
				 //Window.alert(arg0.toString());
				
			}
		  });
		  FormLayout layout = new FormLayout(
			        "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
			            + "right:[40dlu,pref], 3dlu, 70dlu",
			        "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");

		  PanelBuilder builder = new PanelBuilder(layout);
		  this.setTitle("title");
		  builder.addLabel(MessageFactory.getInstance().server() + ":", CellConstraints.xy(1, 2));
		  builder.add(serverTB, CellConstraints.xy(3, 2));
		  builder.addLabel(MessageFactory.getInstance().port() + ":", CellConstraints.xy(5, 2));
		  builder.add(portTB, CellConstraints.xy(7, 2));
		  builder.addLabel(MessageFactory.getInstance().database() + ":", CellConstraints.xy(1, 4));
		  builder.add(databaseTB, CellConstraints.xyw(3, 4, 5));
		  builder.addLabel(MessageFactory.getInstance().username() + ":", CellConstraints.xy(1, 6));
		  builder.add(userTB, CellConstraints.xy(3, 6));
		  builder.addLabel(MessageFactory.getInstance().password() + ":", CellConstraints.xy(5, 6));
		  builder.add(passwordTB, CellConstraints.xy(7, 6));
		  builder.addLabel(MessageFactory.getInstance().schema_file() + ":", CellConstraints.xy(1, 8));
		  builder.add(fileUpload, CellConstraints.xyw(3,8,5));
		  		  
		  uploadButton.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		        String filename = fileUpload.getFilename();
		        if (filename.length() == 0) {
		          Window.alert("No File");
		        } else {
		        	fpanel.submit();
		        	connectBtn.setEnabled(true);
		        	uploadButton.setEnabled(false);
		        	Window.alert(filename);
		        }
		      }
		    });
		    builder.add(uploadButton, CellConstraints.xyw(3,10,5));
		    
		    connectBtn.setEnabled(false);
		    builder.add(connectBtn, CellConstraints.xyw(3,12,5));
		    
		    
		    fpanel.add(builder.getPanel());
		    return fpanel;
	  		}


	public void addConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

	public void removeClickListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

}
