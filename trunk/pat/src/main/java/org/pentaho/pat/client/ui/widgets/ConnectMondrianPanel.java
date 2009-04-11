/**
 * 
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.listeners.ConnectionListenerCollection;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
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

public class ConnectMondrianPanel extends LayoutPanel implements
SourcesConnectionEvents {

	// TODO Finish this Widget

	private static final String FORM_NAME_FILE = "file";
	private static final String FORM_ENCODING = "multipart/form-data";
	private static final String FORM_METHOD = "POST";
	private static final String FORM_ACTION = "schemaupload";
	private static final String HEIGHT = "340px";
	private static final String WIDTH = "750px";
	private static final String TITLE = ConstantFactory.getInstance().register_new_mondrian_connection();
	private static final String LABEL_SUFFIX = ":";
	private static final String FILENAME_TAG_START = "pat_schema_filename_start";
	private static final String FILENAME_TAG_END = "pat_schema_filename_end";


	private final ListBox driverListBox;
	private final TextBox urlTextBox;
	private final TextBox userTextBox;
	private final PasswordTextBox passwordTextBox;
	private FileUpload fileUpload;
	private final Button uploadButton;
	private final Button connectButton;
	private String schemaPath;
	private boolean connectionEstablished = false;
	private ConnectionListenerCollection connectionListeners;

	public ConnectMondrianPanel() {
		super();
		this.setTitle(TITLE);
	
		this.setLayout(new BorderLayout());
		
		

		connectButton = new Button(ConstantFactory.getInstance().connect());
		uploadButton = new Button(ConstantFactory.getInstance().upload());
		driverListBox = createDriverListComboBox();
		urlTextBox = new TextBox();
		userTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		fileUpload = new FileUpload();
		schemaPath ="";

		onInitialize();

	}


	private void onInitialize() {

		final FormPanel formPanel = new FormPanel();
		formPanel.setWidth(WIDTH);
		formPanel.setHeight(HEIGHT);
		formPanel.setAction(FORM_ACTION);
		formPanel.setMethod(FORM_METHOD);
		formPanel.setEncoding(FORM_ENCODING);

		formPanel.addFormHandler(new FormHandler() {
			public void onSubmitComplete(FormSubmitCompleteEvent arg0) {
				// TODO Replace filename handling with stored schema handling when implemented
				if (arg0 != null && arg0.getResults() != null && arg0.getResults().length() > 0) {
					if (arg0.getResults().contains(FILENAME_TAG_START))
					{
						String tmp = arg0.getResults().substring(arg0.getResults().indexOf(FILENAME_TAG_START)+FILENAME_TAG_START.length(),arg0.getResults().indexOf(FILENAME_TAG_END));
						schemaPath = tmp;
						connectButton.setEnabled(true);
						// TODO remove this later
						MessageBox.info("File uploaded",tmp);
					}
					else {
						MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().file_upload_failed());	
					}
				}
				else
					MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().check_error_log());
			}

			public void onSubmit(FormSubmitEvent arg0) {
				// TODO add Submit Action - Probably validation? And mask UI
				// Window.alert("onSubmit:" + arg0.toString());
			}
		});
		final FormLayout layout = new FormLayout(
				"right:[40dlu,pref], 3dlu, 70dlu, 7dlu, "
				+ "right:[40dlu,pref], 3dlu, 70dlu",
		//"12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
			"p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p");	
		final PanelBuilder builder = new PanelBuilder(layout);
		builder.addLabel(ConstantFactory.getInstance().jdbc_driver() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
		builder.add(driverListBox, CellConstraints.xyw(3, 1, 5));
		builder.addLabel(ConstantFactory.getInstance().jdbc_url() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
		builder.add(urlTextBox, CellConstraints.xyw(3, 3, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 5));
		builder.add(userTextBox, CellConstraints.xy(3, 5));
		builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 5));
		builder.add(passwordTextBox, CellConstraints.xy(7, 5));
		builder.addLabel(ConstantFactory.getInstance().schema_file() + LABEL_SUFFIX, CellConstraints.xy(1, 7));
		fileUpload.setName(FORM_NAME_FILE);
		builder.add(fileUpload, CellConstraints.xyw(3,7,5));

		uploadButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				String filename = fileUpload.getFilename();
				if (filename == null || filename.length() == 0) {
					MessageBox.error(ConstantFactory.getInstance().error(),MessageFactory.getInstance().file_upload_no_file());
				} else {
					formPanel.submit();
				}
			}
		});

		builder.add(uploadButton, CellConstraints.xyw(3,9,5));
		connectButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
					ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new AsyncCallback<Object>() {
					public void onSuccess(Object o) {
						MessageBox.info(ConstantFactory.getInstance().success(),MessageFactory.getInstance().connection_established());
						setConnectionEstablished(true);
						connectionListeners.fireConnectionMade(ConnectMondrianPanel.this);
					}
					public void onFailure(Throwable arg0) {
						MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().no_connection_param(arg0.getMessage()));
						connectButton.setEnabled(true);
					}
				});
			}
		});

		connectButton.setEnabled(false);
		builder.add(connectButton, CellConstraints.xyw(3,11,5));

		formPanel.add(builder.getPanel());
		this.add(formPanel);
	}

	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	private ListBox createDriverListComboBox() {
		final ListBox listBox = new ListBox();


		ServiceFactory.getDiscoveryInstance().getDrivers(new AsyncCallback<String[]>() {
			public void onSuccess(String[] arg0) {

				if (arg0 != null && arg0.length > 0) {
					for(int i=0;i < arg0.length;i++) {
						listBox.addItem(arg0[i]);
					}
				}
				else {
					MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().no_jdbc_driver_found());	
				}
			}
			public void onFailure(Throwable arg0) {
				MessageBox.error(ConstantFactory.getInstance().error(), arg0.getMessage());	
			}
		});
		return listBox;
	}

	private CubeConnection getCubeConnection() {
		final CubeConnection cc = new CubeConnection(ConnectionType.Mondrian);
		cc.setDriverClassName(driverListBox.getItemText(driverListBox.getSelectedIndex()));
		cc.setUrl(urlTextBox.getText());
		if(userTextBox.getText() != null && userTextBox.getText().length() > 0) {
			cc.setUsername(userTextBox.getText());
		}
		else {
			cc.setUsername(null);
		}
		if(passwordTextBox.getText() != null && passwordTextBox.getText().length() > 0) {
			cc.setPassword(passwordTextBox.getText());	
		}
		else {
			cc.setPassword(null);
		}
		cc.setSchemaPath(schemaPath);
		return cc;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * addConnectionListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	public void addConnectionListener(ConnectionListener listener) {
		if (connectionListeners == null) {
			connectionListeners = new ConnectionListenerCollection();
		}
		connectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.pentaho.halogen.client.listeners.SourcesConnectionEvents#
	 * removeClickListener
	 * (org.pentaho.halogen.client.listeners.ConnectionListener)
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}
	
	public void emptyForm() {
		urlTextBox.setText("");
		userTextBox.setText("");
		passwordTextBox.setText("");
		schemaPath ="";
	}

}
