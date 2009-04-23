/**
 * 
 */
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.pentaho.pat.client.events.SourcesConnectionEvents;
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paul
 * 
 */
public class ConnectPanel extends WindowPanel implements SourcesConnectionEvents, ConnectionListener {

	/**
	 *TODO JAVADOC
	 */
	private final transient Button connectBtn;
	/**
	 *TODO JAVADOC
	 */
	private final transient TextBox serverTB;
	/**
	 *TODO JAVADOC
	 */
	private final transient TextBox portTB;
	/**
	 *TODO JAVADOC
	 */
	private final transient TextBox databaseTB;
	/**
	 *TODO JAVADOC
	 */
	private final transient TextBox userTB;
	/**
	 *TODO JAVADOC
	 */
	private final transient PasswordTextBox passwordTB;;
	/**
	 *TODO JAVADOC
	 */
	private final transient FileUpload fileUpload;
	/**
	 *TODO JAVADOC
	 */
	private final transient Button uploadButton;
	/**
	 *TODO JAVADOC
	 */
	private transient FormPanel fpanel;

	/**
	 *TODO JAVADOC
	 *
	 */
	public ConnectPanel() {
		super();
		this.setTitle("Register new Mondrian Connection"); //$NON-NLS-1$
		ConstantFactory.getInstance().disconnect();
		connectBtn = new Button(ConstantFactory.getInstance().connect());
		uploadButton = new Button(ConstantFactory.getInstance().upload());
		serverTB = new TextBox();
		portTB = new TextBox();
		databaseTB = new TextBox();
		userTB = new TextBox();
		passwordTB = new PasswordTextBox();
		fileUpload = new FileUpload();
		this.setWidget(onInitialize());
		this.setWidth("700"); //$NON-NLS-1$
		this.setHeight("300"); //$NON-NLS-1$

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#addConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void addConnectionListener(final ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionBroken(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionBroken(final Widget sender) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.listeners.ConnectionListener#onConnectionMade(com.google.gwt.user.client.ui.Widget)
	 */
	public void onConnectionMade(final Widget sender) {
		// TODO Auto-generated method stub
		System.out.println("ConnectPanel on Connection Made"); //$NON-NLS-1$
	}

	/**
	 *TODO JAVADOC
	 *
	 * @return fpanel
	 */
	protected Widget onInitialize() {
		fpanel = new FormPanel();
		fpanel.setAction("schemaupload"); //$NON-NLS-1$
		fpanel.setMethod("POST"); //$NON-NLS-1$
		fpanel.setEncoding("multipart/form-data"); //$NON-NLS-1$

		fpanel.addFormHandler(new FormHandler() {

			public void onSubmit(final FormSubmitEvent arg0) {
				// TODO Auto-generated method stub
				// Window.alert(arg0.toString());

			}

			public void onSubmitComplete(final FormSubmitCompleteEvent arg0) {
				if (arg0.getResults().contains("#filename#")) //$NON-NLS-1$
				{
					final String tmp = arg0.getResults().substring(arg0.getResults().indexOf("#filename#") + 10, arg0.getResults().indexOf("#/filename#")); //$NON-NLS-1$ //$NON-NLS-2$
					Window.alert(tmp);
				} else {
					Window.alert("Schema Upload failed"); //$NON-NLS-1$
				}

			}
		});
		final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
				+ "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
		"12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px"); //$NON-NLS-1$

		final PanelBuilder builder = new PanelBuilder(layout);
		this.setTitle("title"); //$NON-NLS-1$
		builder.addLabel(ConstantFactory.getInstance().server() + ":", CellConstraints.xy(1, 2)); //$NON-NLS-1$
		builder.add(serverTB, CellConstraints.xy(3, 2));
		builder.addLabel(ConstantFactory.getInstance().port() + ":", CellConstraints.xy(5, 2)); //$NON-NLS-1$
		builder.add(portTB, CellConstraints.xy(7, 2));
		builder.addLabel(ConstantFactory.getInstance().database() + ":", CellConstraints.xy(1, 4)); //$NON-NLS-1$
		builder.add(databaseTB, CellConstraints.xyw(3, 4, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + ":", CellConstraints.xy(1, 6)); //$NON-NLS-1$
		builder.add(userTB, CellConstraints.xy(3, 6));
		builder.addLabel(ConstantFactory.getInstance().password() + ":", CellConstraints.xy(5, 6)); //$NON-NLS-1$
		builder.add(passwordTB, CellConstraints.xy(7, 6));
		builder.addLabel(ConstantFactory.getInstance().schemafile() + ":", CellConstraints.xy(1, 8)); //$NON-NLS-1$
		fileUpload.setName("file"); //$NON-NLS-1$

		builder.add(fileUpload, CellConstraints.xyw(3, 8, 5));

		uploadButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final String filename = fileUpload.getFilename();
				if (filename.length() == 0) {
					Window.alert("No File"); //$NON-NLS-1$
				} else {
					fpanel.submit();
					connectBtn.setEnabled(true);
					uploadButton.setEnabled(false);
					// Window.alert(filename);
				}
			}
		});
		builder.add(uploadButton, CellConstraints.xyw(3, 10, 5));
		connectBtn.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {

				// Connection Routine

			}
		});
		connectBtn.setEnabled(false);
		builder.add(connectBtn, CellConstraints.xyw(3, 12, 5));

		fpanel.add(builder.getPanel());
		return fpanel;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.pat.client.events.SourcesConnectionEvents#removeConnectionListener(org.pentaho.pat.client.listeners.ConnectionListener)
	 */
	public void removeConnectionListener(final ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

}
