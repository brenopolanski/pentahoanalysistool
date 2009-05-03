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
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ConnectXmlaPanel.
 *
 * @author pstoellberger
 */

public class ConnectXmlaPanel extends LayoutPanel {

	/** Label Suffix. */
	private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$

	/** Panel Height. */
	private static final String HEIGHT = "280px"; //$NON-NLS-1$

	/** Panel Width. */
	private static final String WIDTH = "620px"; //$NON-NLS-1$

	/** Url Textbox. */
	private final TextBox urlTextBox;
	// private final TextBox catalogTextBox;
	/** User Textbox. */
	private final TextBox userTextBox;

	/** Password Textbox. */
	private final PasswordTextBox passwordTextBox;

	/** Connect button. */
	private final Button connectButton;

	/** Connection Status. */
	private boolean connectionEstablished = false;

	/**
	 * ConnectXmlaPanel Constructor.
	 */
	public ConnectXmlaPanel() {
		super();
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		connectButton = new Button(ConstantFactory.getInstance().connect());
		// catalogTextBox = new TextBox();
		urlTextBox = new TextBox();
		userTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		this.setLayout(new BorderLayout());
		onInitialize();
		// this.add(onInitialize());
	}


	/**
	 * Empty the form.
	 */
	public final void emptyForm() {
		urlTextBox.setText(""); //$NON-NLS-1$
		userTextBox.setText(""); //$NON-NLS-1$
		passwordTextBox.setText(""); //$NON-NLS-1$
		// catalogTextbox.setText("");
	}

	/**
	 * Setup Cube Connection.
	 *
	 * @return the cube connection
	 */
	private CubeConnection getCubeConnection() {
		final CubeConnection cc = new CubeConnection(ConnectionType.XMLA);
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
		// if(catalogTextBox.getText() != null &&
		// catalogTextBox.getText().length() > 0) {
		// cc.setCatalog(catalogTextBox.getText());
		// }
		// else {
		// cc.setCatalog(null);
		// }

		return cc;
	}

	/**
	 * Return the connection Status.
	 *
	 * @return true, if checks if is connection established
	 */
	public final boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	/**
	 * Initialize the panel.
	 */
	private void onInitialize() {

		// final FormPanel formPanel = new FormPanel();
		// formPanel.setWidth(WIDTH);
		// formPanel.setHeight(HEIGHT);

		final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
				+ "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
				// "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
		"p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
		final PanelBuilder builder = new PanelBuilder(layout);

		builder.addLabel(ConstantFactory.getInstance().xmlaurl() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
		builder.add(urlTextBox, CellConstraints.xyw(3, 1, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
		builder.add(userTextBox, CellConstraints.xy(3, 3));
		builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 3));
		builder.add(passwordTextBox, CellConstraints.xy(7, 3));
		// builder.addLabel(ConstantFactory.getInstance().catalog() +
		// LABEL_SUFFIX, CellConstraints.xy(1, 8));
		// builder.add(catalogTextBox, CellConstraints.xyw(3,8,5));

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
						GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionMade(ConnectXmlaPanel.this);
					}
				});
			}
		});

		builder.add(connectButton, CellConstraints.xyw(3, 9, 5));

		final LayoutPanel layoutPanel = builder.getPanel();
		layoutPanel.setPadding(15);
		this.add(layoutPanel);
	}


	/**
	 * Set the connection status
	 *
	 * @param connectionEstablished the connection established
	 */
	public final void setConnectionEstablished(final boolean connectionEstablished) {
		this.connectionEstablished = connectionEstablished;
	}

}
