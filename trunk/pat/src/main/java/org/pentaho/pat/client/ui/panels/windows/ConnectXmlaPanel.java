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
import org.pentaho.pat.client.i18n.IGuiConstants;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Hidden;
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

    private final Hidden idHidden;
    
    /** Textbox for connection name. */
    private final TextBox nameTextBox;

    /** Url Textbox. */
    private final TextBox urlTextBox;

    private final TextBox catalogTextBox;

    /** User Textbox. */
    private final TextBox userTextBox;

    /** Password Textbox. */
    private final PasswordTextBox passwordTextBox;
    
    private final TextBox roleTextBox;

    private final CheckBox startupCheckbox;

    /** Connect button. */
    private final Button saveButton;

    /** Cancel button. */
    private final Button cancelButton;

    private final static String CONNECT_XMLA_PANEL = "pat-ConnectXmlaPanel"; //$NON-NLS-1$
    /**
     * ConnectXmlaPanel Constructor.
     */
    public ConnectXmlaPanel() {
        super(new BorderLayout());
        idHidden = new Hidden();
        saveButton = new Button(Pat.CONSTANTS.save());
        cancelButton = new Button(Pat.CONSTANTS.cancel());
        urlTextBox = new TextBox();
        userTextBox = new TextBox();
        nameTextBox = new TextBox();
        passwordTextBox = new PasswordTextBox();
        roleTextBox = new TextBox();
        catalogTextBox = new TextBox();
        startupCheckbox = new CheckBox(Pat.CONSTANTS.connectStartup());
        this.setStyleName(CONNECT_XMLA_PANEL);
        init();
    }

    public ConnectXmlaPanel(CubeConnection cc) {
        this();
        idHidden.setValue(cc.getId());
        nameTextBox.setText(cc.getName());
        urlTextBox.setText(cc.getUrl());
        if (cc.getUsername() != null)
            userTextBox.setText(cc.getUsername());
        if (cc.getPassword() != null)
            passwordTextBox.setText(cc.getPassword());
        if (cc.getCatalog() != null)
            catalogTextBox.setText(cc.getCatalog());
        startupCheckbox.setValue(cc.isConnectOnStartup());
        
        if (cc.getRole() != null) {
            roleTextBox.setText(cc.getRole());
        }
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
        if (idHidden != null && idHidden.getValue() != null && idHidden.getValue().length() > 0) {
            cubeConn.setId(idHidden.getValue());
        }
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
        cubeConn.setConnectOnStartup(startupCheckbox.getValue());
        cubeConn.setRole(roleTextBox.getText());
        
        return cubeConn;
    }

    /**
     * Initialize the panel.
     */
    private void init() {
        final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                // "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
        "p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.addLabel(Pat.CONSTANTS.name() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
        builder.add(nameTextBox, CellConstraints.xyw(3, 1, 5));
        builder.addLabel(Pat.CONSTANTS.xmlaUrl() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
        builder.add(urlTextBox, CellConstraints.xyw(3, 3, 5));
        builder.addLabel(Pat.CONSTANTS.username() + LABEL_SUFFIX, CellConstraints.xy(1, 5));
        builder.add(userTextBox, CellConstraints.xy(3, 5));
        builder.addLabel(Pat.CONSTANTS.password() + LABEL_SUFFIX, CellConstraints.xy(5, 5));
        builder.add(passwordTextBox, CellConstraints.xy(7, 5));
        builder.addLabel(Pat.CONSTANTS.catalog() + LABEL_SUFFIX, CellConstraints.xy(1, 7));
        builder.add(catalogTextBox, CellConstraints.xyw(3, 7, 5));
        builder.addLabel(Pat.CONSTANTS.role() + LABEL_SUFFIX, CellConstraints.xy(1, 9));
        builder.add(roleTextBox, CellConstraints.xyw(3, 9, 5));
        builder.add(startupCheckbox,CellConstraints.xy(3,11));
        saveButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final CubeConnection cc = getCubeConnection();
                if (validateConnection(cc)) {
                    saveButton.setEnabled(false);
                    ServiceFactory.getSessionInstance().saveConnection(Pat.getSessionID(), cc,
                            new AsyncCallback<String>() {
                        public void onFailure(final Throwable arg0) {
                            MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                    .failedLoadConnection(arg0.getLocalizedMessage()));
                            saveButton.setEnabled(true);
                        }

                        public void onSuccess(final String id) {
                            if (cc.isConnectOnStartup()) {
                                ConnectionManagerPanel.connectEvent(id,cc.isConnected(),true);
                            }
                            ConnectionManagerWindow.closeTabs();
                        }
                    });
                }
            }
        });

        builder.add(saveButton, CellConstraints.xy(3, 13));

        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                ConnectionManagerWindow.closeTabs();
            }
        });
        builder.add(cancelButton, CellConstraints.xy(7, 13));
        final LayoutPanel layoutPanel = builder.getPanel();
        layoutPanel.setPadding(15);
        this.getLayoutPanel().add(layoutPanel);
    }

    public boolean validateConnection(CubeConnection cc) {

        if (cc.getName().length() == 0 || cc.getUrl().length() == 0) {
            IGuiConstants inst = Pat.CONSTANTS;
            MessageBox.error(Pat.CONSTANTS.error(),
                    MessageFactory.getInstance().validationEmpty(inst.name().concat(",").concat(inst.xmlaUrl()))); //$NON-NLS-1$
            return false;
            //MessageFactory.getInstance().failedLoadConnection(arg0.getLocalizedMessage()));
        }
        return true;
    }


}
