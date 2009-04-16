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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paul
 * 
 */
public class ConnectPanel extends WindowPanel implements
		SourcesConnectionEvents, ConnectionListener {

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

	protected Widget onInitialize() {
		fpanel = new FormPanel();
		fpanel.setAction("schemaupload"); //$NON-NLS-1$
		fpanel.setMethod("POST"); //$NON-NLS-1$
		fpanel.setEncoding("multipart/form-data"); //$NON-NLS-1$

		fpanel.addFormHandler(new FormHandler() {

			public void onSubmitComplete(FormSubmitCompleteEvent arg0) {
				if (arg0.getResults().contains("#filename#")) //$NON-NLS-1$
				{
					String tmp = arg0
							.getResults()
							.substring(
									arg0.getResults().indexOf("#filename#") + 10, arg0.getResults().indexOf("#/filename#")); //$NON-NLS-1$ //$NON-NLS-2$
					Window.alert(tmp);
				} else
					Window.alert("Schema Upload failed"); //$NON-NLS-1$

			}

			public void onSubmit(FormSubmitEvent arg0) {
				// TODO Auto-generated method stub
				// Window.alert(arg0.toString());

			}
		});
		FormLayout layout = new FormLayout(
				"right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
						+ "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
				"12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px"); //$NON-NLS-1$

		PanelBuilder builder = new PanelBuilder(layout);
		this.setTitle("title"); //$NON-NLS-1$
		builder
				.addLabel(
						ConstantFactory.getInstance().server() + ":", CellConstraints.xy(1, 2)); //$NON-NLS-1$
		builder.add(serverTB, CellConstraints.xy(3, 2));
		builder
				.addLabel(
						ConstantFactory.getInstance().port() + ":", CellConstraints.xy(5, 2)); //$NON-NLS-1$
		builder.add(portTB, CellConstraints.xy(7, 2));
		builder
				.addLabel(
						ConstantFactory.getInstance().database() + ":", CellConstraints.xy(1, 4)); //$NON-NLS-1$
		builder.add(databaseTB, CellConstraints.xyw(3, 4, 5));
		builder
				.addLabel(
						ConstantFactory.getInstance().username() + ":", CellConstraints.xy(1, 6)); //$NON-NLS-1$
		builder.add(userTB, CellConstraints.xy(3, 6));
		builder
				.addLabel(
						ConstantFactory.getInstance().password() + ":", CellConstraints.xy(5, 6)); //$NON-NLS-1$
		builder.add(passwordTB, CellConstraints.xy(7, 6));
		builder
				.addLabel(
						ConstantFactory.getInstance().schema_file() + ":", CellConstraints.xy(1, 8)); //$NON-NLS-1$
		fileUpload.setName("file"); //$NON-NLS-1$

		builder.add(fileUpload, CellConstraints.xyw(3, 8, 5));

		uploadButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				String filename = fileUpload.getFilename();
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
			public void onClick(Widget sender) {

				// Connection Routine

			}
		});
		connectBtn.setEnabled(false);
		builder.add(connectBtn, CellConstraints.xyw(3, 12, 5));

		fpanel.add(builder.getPanel());
		return fpanel;
	}

	public void addConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

	public void removeConnectionListener(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		System.out.println("ConnectPanel on Connection Made"); //$NON-NLS-1$
	}

}
