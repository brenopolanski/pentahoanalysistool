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

public class ConnectMondrianPanel extends LayoutPanel implements SourcesConnectionEvents {

	// TODO Finish this Widget

	/**
	 *TODO JAVADOC
	 */
	private static final String FORM_NAME_FILE = "file"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String FORM_ENCODING = "multipart/form-data"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String FORM_METHOD = "POST"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String FORM_ACTION = "schemaupload"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String HEIGHT = "280px"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String WIDTH = "620px"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String FILENAME_TAG_START = "pat_schema_filename_start"; //$NON-NLS-1$
	/**
	 *TODO JAVADOC
	 */
	private static final String FILENAME_TAG_END = "pat_schema_filename_end"; //$NON-NLS-1$

	/**
	 *TODO JAVADOC
	 */
	private final ListBox driverListBox;
	/**
	 *TODO JAVADOC
	 */
	private final TextBox urlTextBox;
	/**
	 *TODO JAVADOC
	 */
	private final TextBox userTextBox;
	/**
	 *TODO JAVADOC
	 */
	private final PasswordTextBox passwordTextBox;
	/**
	 *TODO JAVADOC
	 */
	private FileUpload fileUpload;
	/**
	 *TODO JAVADOC
	 */
	private final Button uploadButton;
	/**
	 *TODO JAVADOC
	 */
	private final Button connectButton;
	/**
	 *TODO JAVADOC
	 */
	private String schemaPath;
	/**
	 *TODO JAVADOC
	 */
	private boolean connectionEstablished = false;
	/**
	 *TODO JAVADOC
	 */
	private ConnectionListenerCollection connectionListeners;

	/**
	 *TODO JAVADOC
	 *
	 */
	public ConnectMondrianPanel() {
		super();

		this.setLayout(new BorderLayout());

		connectButton = new Button(ConstantFactory.getInstance().connect());
		uploadButton = new Button(ConstantFactory.getInstance().upload());
		driverListBox = createDriverListComboBox();
		urlTextBox = new TextBox();
		userTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		fileUpload = new FileUpload();
		schemaPath = ""; //$NON-NLS-1$

		onInitialize();

	}

	/**
	 *TODO JAVADOC
	 *
	 */
	private void onInitialize() {

		final FormPanel formPanel = new FormPanel();
		formPanel.setWidth(WIDTH);
		formPanel.setHeight(HEIGHT);
		formPanel.setAction(FORM_ACTION);
		formPanel.setMethod(FORM_METHOD);
		formPanel.setEncoding(FORM_ENCODING);

		formPanel.addFormHandler(new FormHandler() {
			public void onSubmitComplete(FormSubmitCompleteEvent arg0) {
				// TODO Replace filename handling with stored schema handling
				// when implemented
				if (arg0 != null && arg0.getResults() != null && arg0.getResults().length() > 0) {
					if (arg0.getResults().contains(FILENAME_TAG_START)) {
						String tmp = arg0.getResults().substring(arg0.getResults().indexOf(FILENAME_TAG_START) + FILENAME_TAG_START.length(),
								arg0.getResults().indexOf(FILENAME_TAG_END));
						schemaPath = tmp;
						connectButton.setEnabled(true);
						// TODO remove this later
						MessageBox.info("File uploaded", tmp); //$NON-NLS-1$
					} else {
						MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().file_upload_failed());
					}
				} else
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().check_error_log());
			}

			public void onSubmit(FormSubmitEvent arg0) {
				// TODO add Submit Action - Probably validation? And mask UI
				// Window.alert("onSubmit:" + arg0.toString());
			}
		});
		final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
				+ "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
				// "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
				"p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
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
		builder.add(fileUpload, CellConstraints.xyw(3, 7, 5));

		uploadButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				String filename = fileUpload.getFilename();
				if (filename == null || filename.length() == 0) {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().file_upload_no_file());
				} else {
					formPanel.submit();
				}
			}
		});

		builder.add(uploadButton, CellConstraints.xyw(3, 9, 5));
		connectButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new AsyncCallback<Object>() {
					public void onSuccess(Object o) {
						MessageBox.info(ConstantFactory.getInstance().success(), ConstantFactory.getInstance().connection_established());
						setConnectionEstablished(true);
						connectionListeners.fireConnectionMade(ConnectMondrianPanel.this);
					}

					public void onFailure(Throwable arg0) {
						MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().no_connection_param(arg0.getLocalizedMessage()));
						connectButton.setEnabled(true);
					}
				});
			}
		});

		connectButton.setEnabled(false);
		builder.add(connectButton, CellConstraints.xyw(3, 11, 5));

		LayoutPanel layoutPanel = builder.getPanel();
		layoutPanel.setPadding(15);
		formPanel.add(layoutPanel);
		this.add(formPanel);
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	public boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @param connectionEstablished
	 */
	public void setConnectionEstablished(boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	private ListBox createDriverListComboBox() {
		final ListBox listBox = new ListBox();

		ServiceFactory.getDiscoveryInstance().getDrivers(new AsyncCallback<String[]>() {
			public void onSuccess(String[] arg0) {

				if (arg0 != null && arg0.length > 0) {
					for (int i = 0; i < arg0.length; i++) {
						listBox.addItem(arg0[i]);
					}
				} else {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().no_jdbc_driver_found());
				}
			}

			public void onFailure(Throwable arg0) {
				MessageBox.error(ConstantFactory.getInstance().error(), arg0.getMessage());
			}
		});
		return listBox;
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return
	 */
	private CubeConnection getCubeConnection() {
		final CubeConnection cc = new CubeConnection(ConnectionType.Mondrian);
		cc.setDriverClassName(driverListBox.getItemText(driverListBox.getSelectedIndex()));
		cc.setUrl(urlTextBox.getText());
		if (userTextBox.getText() != null && userTextBox.getText().length() > 0) {
			cc.setUsername(userTextBox.getText());
		} else {
			cc.setUsername(null);
		}
		if (passwordTextBox.getText() != null && passwordTextBox.getText().length() > 0) {
			cc.setPassword(passwordTextBox.getText());
		} else {
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
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#addConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
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
	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#removeConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		if (connectionListeners != null) {
			connectionListeners.remove(listener);
		}
	}

	/**
	 *TODO JAVADOC
	 *
	 */
	public void emptyForm() {
		urlTextBox.setText(""); //$NON-NLS-1$
		userTextBox.setText(""); //$NON-NLS-1$
		passwordTextBox.setText(""); //$NON-NLS-1$
		schemaPath = ""; //$NON-NLS-1$
	}

}
