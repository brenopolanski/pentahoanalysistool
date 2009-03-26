/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.ui.client.WindowPanel;

import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GuidFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;

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
 * @author pstoellberger
 *
 */

public class ConnectMondrianPanel extends WindowPanel implements
			SourcesConnectionEvents {

	// TODO Finish this Widget
		Button connectButton;
		TextBox serverTextBox;
		Label serverLabel;
		TextBox portTextBox;
		Label portLabel;
		TextBox databaseTextBox;
		Label databaseLabel;
		TextBox userTextBox;
		Label userLabel;
		PasswordTextBox passwordTextBox;
		Label passwordLabel;
		FileUpload fileUpload;
		Button uploadButton;
		FormPanel formPanel;
		
		
		 public ConnectMondrianPanel() {
			
			this.setTitle("Register new Mondrian Connection");
			ConstantFactory.getInstance().disconnect();
			connectButton = new Button(ConstantFactory.getInstance().connect());
			uploadButton = new Button(ConstantFactory.getInstance().upload());
			serverTextBox = new TextBox();
			portTextBox = new TextBox();
			databaseTextBox = new TextBox();
			userTextBox = new TextBox();
			passwordTextBox = new PasswordTextBox();
			fileUpload = new FileUpload();
			this.setWidget(onInitialize());
			this.setWidth("700");
			this.setHeight("300");
			
		}
		
		
		  protected Widget onInitialize() {
			  formPanel = new FormPanel();
			  formPanel.setAction("schemaupload");
			  formPanel.setMethod("POST");
			  formPanel.setEncoding("multipart/form-data");
			  
			  formPanel.addFormHandler(new FormHandler() {
				 
				  public void onSubmitComplete(FormSubmitCompleteEvent arg0) {
					// TODO Auto-generated method stub
					  if (arg0.getResults().contains("#filename#"))
					  {
						  String tmp = arg0.getResults().substring(arg0.getResults().indexOf("#filename#")+10,arg0.getResults().indexOf("#/filename#"));
						  Window.alert(tmp);
					  }
					  else
						  Window.alert("Schema Upload failed");
					
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
			  builder.addLabel(ConstantFactory.getInstance().server() + ":", CellConstraints.xy(1, 2));
			  builder.add(serverTextBox, CellConstraints.xy(3, 2));
			  builder.addLabel(ConstantFactory.getInstance().port() + ":", CellConstraints.xy(5, 2));
			  builder.add(portTextBox, CellConstraints.xy(7, 2));
			  builder.addLabel(ConstantFactory.getInstance().database() + ":", CellConstraints.xy(1, 4));
			  builder.add(databaseTextBox, CellConstraints.xyw(3, 4, 5));
			  builder.addLabel(ConstantFactory.getInstance().username() + ":", CellConstraints.xy(1, 6));
			  builder.add(userTextBox, CellConstraints.xy(3, 6));
			  builder.addLabel(ConstantFactory.getInstance().password() + ":", CellConstraints.xy(5, 6));
			  builder.add(passwordTextBox, CellConstraints.xy(7, 6));
			  builder.addLabel(ConstantFactory.getInstance().schema_file() + ":", CellConstraints.xy(1, 8));
			  fileUpload.setName("file");
			  
			  builder.add(fileUpload, CellConstraints.xyw(3,8,5));
			  		  
			  uploadButton.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			        String filename = fileUpload.getFilename();
			        if (filename.length() == 0) {
			          Window.alert("No File");
			        } else {
			        	formPanel.submit();
			        	connectButton.setEnabled(true);
			        	uploadButton.setEnabled(false);
			        	//Window.alert(filename);
			        }
			      }
			    });
			    builder.add(uploadButton, CellConstraints.xyw(3,10,5));
			    connectButton.addClickListener(new ClickListener(){
			    	public void onClick(Widget sender) {

			    		//Connection Routine
			    		
			    	}
			    });
			    connectButton.setEnabled(false);
			    builder.add(connectButton, CellConstraints.xyw(3,12,5));
			    
			    
			    formPanel.add(builder.getPanel());
			    return formPanel;
		  		}


		public void addConnectionListener(ConnectionListener listener) {
			// TODO Auto-generated method stub

		}


		public void removeConnectionListener(ConnectionListener listener) {
			// TODO Auto-generated method stub
			
		}

	}
