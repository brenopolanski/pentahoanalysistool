/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.client.ui.panels.windows;

import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * XMLA connections panel
 * 
 * @created Apr 10, 2009
 * @since 0.2.0
 * @author Paul Stoellberger
 * 
 */
public class ConnectXmlaPanel extends LayoutComposite {

    /** Label Suffix. */
    private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$

    /** Height of the panel. */
    private static final Integer HEIGHT = 280;

    /** Width of the Panel. */
    private static final Integer WIDTH = 620;

    /** Textbox for connection name. */
    private  final TextBox nameTextBox;

    /** Url Textbox. */
    private  final TextBox urlTextBox;

    private  final TextBox catalogTextBox;

    /** User Textbox. */
    private  final TextBox userTextBox;

    /** Password Textbox. */
    private  final PasswordTextBox passwordTextBox;

    /** Connect button. */
    private  final Button connectButton;

    /** Cancel button. */
    private  final Button cancelButton;

    /**
     * ConnectXmlaPanel Constructor.
     */
    public ConnectXmlaPanel() {
        super(new BorderLayout());
        connectButton = new Button(ConstantFactory.getInstance().save());
        cancelButton = new Button(ConstantFactory.getInstance().cancel());
        urlTextBox = new TextBox();
        userTextBox = new TextBox();
        nameTextBox = new TextBox();
        passwordTextBox = new PasswordTextBox();
        catalogTextBox = new TextBox();
        init();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    /**
     * Setup Cube Connection.
     * 
     * @return the cube connection
     */
    private CubeConnection getCubeConnection() {
        final CubeConnection cubeConn = new CubeConnection(ConnectionType.XMLA);
        cubeConn.setName(nameTextBox.getText());
        cubeConn.setUrl(urlTextBox.getText());
        if (userTextBox.getText() != null && userTextBox.getText().length() > 0) {
            cubeConn.setUsername(userTextBox.getText());
        } else {
            cubeConn.setUsername(null);
        }
        if (passwordTextBox.getText() != null && passwordTextBox.getText().length() > 0) {
            cubeConn.setPassword(passwordTextBox.getText());
        } else {
            cubeConn.setPassword(null);
        }

        if (catalogTextBox.getText() != null && catalogTextBox.getText().length() > 0) {
            cubeConn.setCatalog(catalogTextBox.getText());
        }
        return cubeConn;
    }

    /**
     * Initialize the panel.
     */
    private void init() {
        final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                // "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
                "p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.addLabel(ConstantFactory.getInstance().name() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
        builder.add(nameTextBox, CellConstraints.xyw(3, 1, 5));
        builder.addLabel(ConstantFactory.getInstance().xmlaUrl() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
        builder.add(urlTextBox, CellConstraints.xyw(3, 3, 5));
        builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 5));
        builder.add(userTextBox, CellConstraints.xy(3, 5));
        builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 5));
        builder.add(passwordTextBox, CellConstraints.xy(7, 5));
        builder.addLabel(ConstantFactory.getInstance().catalog() + LABEL_SUFFIX, CellConstraints.xy(1, 7));
        builder.add(catalogTextBox, CellConstraints.xyw(3, 7, 5));

        connectButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                connectButton.setEnabled(false);
                ServiceFactory.getSessionInstance().saveConnection(Pat.getSessionID(), getCubeConnection(),
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable arg0) {
                                MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedLoadConnection(arg0.getLocalizedMessage()));
                                connectButton.setEnabled(true);
                            }

                            public void onSuccess(final String object) {
                                connectButton.setEnabled(true);
                                ConnectionManagerWindow.closeTabs();
                            }
                        });
            }
        });

        builder.add(connectButton, CellConstraints.xy(3, 11));

        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                ConnectionManagerWindow.closeTabs();
            }
        });
        builder.add(cancelButton, CellConstraints.xy(7, 11));
        final LayoutPanel layoutPanel = builder.getPanel();
        layoutPanel.setPadding(15);
        this.getLayoutPanel().add(layoutPanel);
    }

}
