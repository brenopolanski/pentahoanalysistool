/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Apr 23, 2009 
 * @author Paul Stoellberger
 */
package org.pentaho.pat.client.ui.widgets;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.GlobalConnectionListeners;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

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
 * Connection Panel for Mondrian Connections.
 *
 * @author Paul Stoellberger
 */

public class ConnectMondrianPanel extends LayoutPanel {

	/** Form element name of the file component. */
	private static final String FORM_NAME_FILE = "file"; //$NON-NLS-1$

	/** Encoding type for the Connection form. */
	private static final String FORM_ENCODING = "multipart/form-data"; //$NON-NLS-1$

	/** Submit method of the Connection form. */
	private static final String FORM_METHOD = "POST"; //$NON-NLS-1$

	/** Defines the action of the form. */
	private static final String FORM_ACTION = "schemaupload"; //$NON-NLS-1$

	/** Height of the panel. */
	private static final String HEIGHT = "280px"; //$NON-NLS-1$

	/** Width of the Panel. */
	private static final String WIDTH = "620px"; //$NON-NLS-1$

	/** Suffix for label constants. */
	private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$

	/** Custom start tag for recognizing the returned filename from the backend. Has to match the one defined in the backend */
	private static final String FILENAME_TAG_START = "pat_schema_filename_start"; //$NON-NLS-1$

	/** Custom end tag for recognizing the returned filename from the backend. Has to match the one defined in the backend. */
	private static final String FILENAME_TAG_END = "pat_schema_filename_end"; //$NON-NLS-1$

	/** Listbox for drivers. */
	private final ListBox driverListBox;

	/** Textbox for url. */
	private final TextBox urlTextBox;

	/** Textbox for user. */
	private final TextBox userTextBox;

	/** Textbox for Password. */
	private final PasswordTextBox passwordTextBox;

	/** File upload widget. */
	private final FileUpload fileUpload;

	/** Schema upload button. */
	private final Button uploadButton;

	/** Connect button. */
	private final Button connectButton;

	/** Schema path string. */
	private String schemaPath;

	/** Connect status. */
	private boolean connectionEstablished = false;

	/**
	 * ConnectMondrianPanel Constructor.
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
	 * Driver Listbox setup.
	 *
	 * @return the list box
	 */
	private ListBox createDriverListComboBox() {
		final ListBox listBox = new ListBox();

		ServiceFactory.getDiscoveryInstance().getDrivers(new AsyncCallback<String[]>() {
			public void onFailure(final Throwable arg0) {
				MessageBox.error(ConstantFactory.getInstance().error(), arg0.getMessage());
			}

			public void onSuccess(final String[] arg0) {

				if (arg0 != null && arg0.length > 0) {
					for (final String element2 : arg0) {
						listBox.addItem(element2);
					}
				} else {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().nojdbcdriverfound());
				}
			}
		});
		return listBox;
	}

	/**
	 * Empty form contents.
	 */
	public final void emptyForm() {
		urlTextBox.setText(""); //$NON-NLS-1$
		userTextBox.setText(""); //$NON-NLS-1$
		passwordTextBox.setText(""); //$NON-NLS-1$
		schemaPath = ""; //$NON-NLS-1$
	}

	/**
	 * Cube connection setup.
	 *
	 * @return the cube connection
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

	/**
	 * Returns connection status.
	 *
	 * @return true, if checks if is connection established
	 */
	public final boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	/**
	 * Run on panel initialize.
	 */
	private void onInitialize() {

		final FormPanel formPanel = new FormPanel();
		formPanel.setWidth(WIDTH);
		formPanel.setHeight(HEIGHT);
		formPanel.setAction(FORM_ACTION);
		formPanel.setMethod(FORM_METHOD);
		formPanel.setEncoding(FORM_ENCODING);

		formPanel.addFormHandler(new FormHandler() {
			public void onSubmit(final FormSubmitEvent arg0) {
				// TODO add Submit Action - Probably validation? And mask UI
				// Window.alert("onSubmit:" + arg0.toString());
			}

			public void onSubmitComplete(final FormSubmitCompleteEvent arg0) {
				// TODO Replace filename handling with stored schema handling
				// when implemented
				if (arg0 != null && arg0.getResults() != null && arg0.getResults().length() > 0) {
					if (arg0.getResults().contains(FILENAME_TAG_START)) {
						final String tmp = arg0.getResults().substring(arg0.getResults().indexOf(FILENAME_TAG_START) + FILENAME_TAG_START.length(),
								arg0.getResults().indexOf(FILENAME_TAG_END));
						schemaPath = tmp;
						connectButton.setEnabled(true);
						// TODO remove this later
						MessageBox.info("File uploaded", "Success"); //$NON-NLS-1$
					} else {
						MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().fileuploadfailed());
					}
				} else {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().checkerrorlog());
				}
			}
		});
		final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
				+ "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
				// "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
		"p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
		final PanelBuilder builder = new PanelBuilder(layout);
		builder.addLabel(ConstantFactory.getInstance().jdbcdriver() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
		builder.add(driverListBox, CellConstraints.xyw(3, 1, 5));
		builder.addLabel(ConstantFactory.getInstance().jdbcurl() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
		builder.add(urlTextBox, CellConstraints.xyw(3, 3, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 5));
		builder.add(userTextBox, CellConstraints.xy(3, 5));
		builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 5));
		builder.add(passwordTextBox, CellConstraints.xy(7, 5));
		builder.addLabel(ConstantFactory.getInstance().schemafile() + LABEL_SUFFIX, CellConstraints.xy(1, 7));
		fileUpload.setName(FORM_NAME_FILE);
		builder.add(fileUpload, CellConstraints.xyw(3, 7, 5));

		uploadButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final String filename = fileUpload.getFilename();
				if (filename == null || filename.length() == 0) {
					MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance().fileuploadnofile());
				} else {
					formPanel.submit();
				}
			}
		});

		builder.add(uploadButton, CellConstraints.xyw(3, 9, 5));
		connectButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new AsyncCallback<Object>() {
					public void onFailure(final Throwable arg0) {
						MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().noconnectionparam(arg0.getLocalizedMessage()));
						connectButton.setEnabled(true);
					}

					public void onSuccess(final Object o) {
						MessageBox.info(ConstantFactory.getInstance().success(), ConstantFactory.getInstance().connectionestablished());
						setConnectionEstablished(true);
						GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionMade(ConnectMondrianPanel.this);
					}
				});
			}
		});

		connectButton.setEnabled(false);
		builder.add(connectButton, CellConstraints.xyw(3, 11, 5));

		final LayoutPanel layoutPanel = builder.getPanel();
		layoutPanel.setPadding(15);
		formPanel.add(layoutPanel);
		this.add(formPanel);
	}

	/**
	 * Set connection status.
	 *
	 * @param connectionEstablished the connection established
	 */
	public final void setConnectionEstablished(final boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

}
