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
package org.pentaho.pat.client.deprecated.ui.panels;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.deprecated.ui.panels.ConnectionManagerPanel;
import org.pentaho.pat.client.deprecated.util.ConnectionItem;
import org.pentaho.pat.client.deprecated.util.factory.ConstantFactory;
import org.pentaho.pat.client.deprecated.util.factory.GlobalConnectionFactory;
import org.pentaho.pat.client.deprecated.util.factory.MessageFactory;
import org.pentaho.pat.client.deprecated.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * The Class ConnectXmlaPanel.
 *
 * @author pstoellberger
 */

public class ConnectXmlaPanel extends LayoutComposite {

	/** Label Suffix. */
	private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$

	/** Panel Height. */
	private static final String HEIGHT = "280px"; //$NON-NLS-1$

	/** Panel Width. */
	private static final String WIDTH = "620px"; //$NON-NLS-1$

	/** Textbox for connection name. */
	private final TextBox nameTextBox;

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
		super(new BorderLayout());
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		connectButton = new Button(ConstantFactory.getInstance().save());
		// catalogTextBox = new TextBox();
		urlTextBox = new TextBox();
		userTextBox = new TextBox();
		nameTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		// this.setLayout(new BorderLayout());
		onInitialize();
		// this.add(onInitialize());
	}

	/**
	 * Setup Cube Connection.
	 *
	 * @return the cube connection
	 */
	private CubeConnection getCubeConnection() {
		final CubeConnection cc = new CubeConnection(ConnectionType.XMLA);
		cc.setName(nameTextBox.getText());
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
		final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
				+ "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
				// "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
		"p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
		final PanelBuilder builder = new PanelBuilder(layout);
		builder.addLabel(ConstantFactory.getInstance().name() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
		builder.add(nameTextBox, CellConstraints.xyw(3, 1, 5));
		builder.addLabel(ConstantFactory.getInstance().xmlaUrl() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
		builder.add(urlTextBox, CellConstraints.xyw(3, 3, 5));
		builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 5));
		builder.add(userTextBox, CellConstraints.xy(3, 5));
		builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 5));
		builder.add(passwordTextBox, CellConstraints.xy(7, 5));
		// builder.addLabel(ConstantFactory.getInstance().catalog() +
		// LABEL_SUFFIX, CellConstraints.xy(1, 8));
		// builder.add(catalogTextBox, CellConstraints.xyw(3,8,5));

		connectButton.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				connectButton.setEnabled(false);
                // TODO replace with new RPC functions
//				ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new AsyncCallback<Object>() {
//					public void onFailure(final Throwable arg0) {
//						MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().noConnectionParam(arg0.getLocalizedMessage()));
//						connectButton.setEnabled(true);
//					}
//
//					public void onSuccess(final Object o) {
//						connectButton.setEnabled(true);
//						MessageBox.info(ConstantFactory.getInstance().success(), ConstantFactory.getInstance().connectionEstablished());
//						setConnectionEstablished(true);
//						GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionMade(ConnectXmlaPanel.this);
//						// TODO change this once saving connections is possible
//						ConnectionManagerPanel.addConnection(new ConnectionItem("1234",getCubeConnection().getName(),false));
//					}
//				});
			}
		});

		builder.add(connectButton, CellConstraints.xyw(3, 9, 5));

		final LayoutPanel layoutPanel = builder.getPanel();
		layoutPanel.setPadding(15);
		this.getLayoutPanel().add(layoutPanel);
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
