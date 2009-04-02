/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.LoadingPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.beans.CubeConnection;
import org.pentaho.pat.rpc.beans.CubeConnection.ConnectionType;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author pstoellberger
 *
 */

public class ConnectMondrianPanel extends WindowPanel implements
			SourcesConnectionEvents {

	// TODO Finish this Widget
	
	private static final String FORM_NAME_FILE = "file";
	private static final String FORM_ENCODING = "multipart/form-data";
	private static final String FORM_METHOD = "POST";
	private static final String FORM_ACTION = "schemaupload";
	private static final String HEIGHT = "300";
	private static final String WIDTH = "700";
	private static final String REGISTER_NEW_MONDRIAN_CONNECTION = "Register new Mondrian Connection";
	private static final String LABEL_SUFFIX = ":";
	private static final String FILENAME_TAG_START = "pat_schema_filename_start";
	private static final String FILENAME_TAG_END = "pat_schema_filename_end";
	

	private final ListBox driverListBox;
	private final TextBox urlTextBox;
	private final TextBox userTextBox;
	private final PasswordTextBox passwordTextBox;
	private final FileUpload fileUpload;
	private final Button uploadButton;
	private final Button connectButton;
	private String schemaPath;
	
	public ConnectMondrianPanel() {
		super();
		this.setTitle(REGISTER_NEW_MONDRIAN_CONNECTION);
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);

		connectButton = new Button(ConstantFactory.getInstance().connect());
		uploadButton = new Button(ConstantFactory.getInstance().upload());
		driverListBox = createDriverListComboBox();
		urlTextBox = new TextBox();
		userTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		fileUpload = new FileUpload();
		schemaPath ="";
		

		this.setWidget(onInitialize());
	}


	private Widget onInitialize() {
		final FormPanel formPanel;
		formPanel = new FormPanel();
		formPanel.setAction(FORM_ACTION);
		formPanel.setMethod(FORM_METHOD);
		formPanel.setEncoding(FORM_ENCODING);

		formPanel.addFormHandler(new FormHandler() {
			public void onSubmitComplete(FormSubmitCompleteEvent arg0) {
				// TODO Replace filename handling with stored schema handling when implemented
				if (arg0 != null || arg0.getResults() != null || arg0.getResults().length() > 0) {
					if (arg0.getResults().contains(FILENAME_TAG_START))
					{
						String tmp = arg0.getResults().substring(arg0.getResults().indexOf(FILENAME_TAG_START)+FILENAME_TAG_START.length(),arg0.getResults().indexOf(FILENAME_TAG_END));
						schemaPath = tmp;
						connectButton.setEnabled(true);
						uploadButton.setEnabled(false);
						MessageBox.info("File uploaded","Filename" + tmp);
					}
					else {
						// TODO use standardized message dialog when implemented
						MessageBox.error("Error", "Schema Upload failed");	
					}
				}
				else
					// TODO use standardized message dialog when implemented
					MessageBox.error("Error", "Error occured. See Server log for details");
			}
			
			public void onSubmit(FormSubmitEvent arg0) {
				// TODO add Submit Action - Probably validation? And mask UI
				// Window.alert("onSubmit:" + arg0.toString());
			}
		});
		final FormLayout layout = new FormLayout(
				"right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
				+ "right:[40dlu,pref], 3dlu, 70dlu",
		"12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");

		final PanelBuilder builder = new PanelBuilder(layout);
		
		builder.addLabel(ConstantFactory.getInstance().jdbc_driver() + LABEL_SUFFIX, CellConstraints.xy(1, 2));
		builder.add(driverListBox, CellConstraints.xyw(3, 2, 5));
		builder.addLabel(ConstantFactory.getInstance().jdbc_url() + LABEL_SUFFIX, CellConstraints.xy(1, 4));
		builder.add(urlTextBox, CellConstraints.xyw(3, 4, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 6));
		builder.add(userTextBox, CellConstraints.xy(3, 6));
		builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 6));
		builder.add(passwordTextBox, CellConstraints.xy(7, 6));
		builder.addLabel(ConstantFactory.getInstance().schema_file() + LABEL_SUFFIX, CellConstraints.xy(1, 8));
		fileUpload.setName(FORM_NAME_FILE);
		builder.add(fileUpload, CellConstraints.xyw(3,8,5));

		uploadButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				String filename = fileUpload.getFilename();
				if (filename == null || filename.length() == 0) {
					// TODO use standardized message dialog when implemented
					MessageBox.error("Error","No file selected");
				} else {
					formPanel.submit();
				}
			}
		});
		
		builder.add(uploadButton, CellConstraints.xyw(3,10,5));
		connectButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				// TODO implement Connection Routine
				// .connect(getCubeConnection())
				connectButton.setEnabled(false);
				
				ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new AsyncCallback<Boolean>() {
					public void onSuccess(Boolean arg0) {
						// TODO Auto-generated method stub
						if (arg0 == true) {
							MessageBox.info("Success","Connect established!");
							ConnectMondrianPanel.this.hide();
						}
						else
							MessageBox.error("Error", "Connect Failed");
					}
					public void onFailure(Throwable arg0) {
						// TODO use standardized message dialog when implemented
						MessageBox.error("Error", "Connect Failed:" + arg0.getLocalizedMessage());
						
					}
				});
			}
		});
		
		connectButton.setEnabled(false);
		builder.add(connectButton, CellConstraints.xyw(3,12,5));

		formPanel.add(builder.getPanel());
		return formPanel;
	}

	private ListBox createDriverListComboBox() {
	    final ListBox listBox = new ListBox();
	    final LoadingPanel loadingPanel = LoadingPanel.show(listBox,"Loading...");

	    ServiceFactory.getDiscoveryInstance().getDrivers(new AsyncCallback<String[]>() {
			public void onSuccess(String[] arg0) {
				loadingPanel.hide();
				if (arg0 != null && arg0.length > 0) {
					for(int i=0;i < arg0.length;i++) {
						listBox.addItem(arg0[i]);
					}
				}
				else {
					// TODO use standardized message dialog when implemented
					MessageBox.error("Error", "No installed JDBC Drivers found");	
				}
			}
			public void onFailure(Throwable arg0) {
				loadingPanel.hide();
				// TODO use standardized message dialog when implemented
				MessageBox.error("Error", "Error occured. See Server log for details");	
			}
		});
	    return listBox;
	  }

	private CubeConnection getCubeConnection() {
		final CubeConnection cc = new CubeConnection(ConnectionType.Mondrian);
		cc.setDriverClassName(driverListBox.getItemText(driverListBox.getSelectedIndex()));
		cc.setUrl(urlTextBox.getText());
		cc.setUsername(userTextBox.getText());
		cc.setPassword(passwordTextBox.getText());
		cc.setSchemaPath(schemaPath);
		return cc;
	}
	public void addConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}


	public void removeConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

}
