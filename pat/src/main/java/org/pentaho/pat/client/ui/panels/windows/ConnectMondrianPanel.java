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
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.i18n.IGuiConstants;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * Mondrian connections panel
 * 
 * @created Mar 26, 2009
 * @since 0.2.0
 * @author Paul Stoellberger
 * 
 */

public class ConnectMondrianPanel extends LayoutComposite {



    /** Form element name of the file component. */
    private static final String FORM_NAME_FILE = "file"; //$NON-NLS-1$

    /** Encoding type for the Connection form. */
    private static final String FORM_ENCODING = "multipart/form-data"; //$NON-NLS-1$

    /** Submit method of the Connection form. */
    private static final String FORM_METHOD = "POST"; //$NON-NLS-1$

    /** Defines the action of the form. */
    private static String FORM_ACTION = "schemaupload"; //$NON-NLS-1$
    static {
        if (GWT.getModuleBaseURL().indexOf("content/pat")> -1) {
            String url = GWT.getModuleBaseURL().substring(0, GWT.getModuleBaseURL().indexOf("content/pat")) + "upload/schemaupload";
            FORM_ACTION = url;
        }
      }

    /** Height of the panel. */
    private static final Integer HEIGHT = 280;

    /** Width of the Panel. */
    private static final Integer WIDTH = 620;

    /** Suffix for label constants. */
    private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$

    // Thanks to public domain code http://www.zaharov.info/notes/3_228_0.html
    public static native String decode(final String data) /*-{

                                                                var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
                                                                var out = "", c1, c2, c3, e1, e2, e3, e4;

                                                                for (var i = 0; i < data.length; ) {
                                                                e1 = tab.indexOf(data.charAt(i++));
                                                                e2 = tab.indexOf(data.charAt(i++));
                                                                e3 = tab.indexOf(data.charAt(i++));
                                                                e4 = tab.indexOf(data.charAt(i++));
                                                                c1 = (e1 << 2) + (e2 >> 4);
                                                                c2 = ((e2 & 15) << 4) + (e3 >> 2);
                                                                c3 = ((e3 & 3) << 6) + e4;
                                                                out += String.fromCharCode(c1);

                                                                if (e3 != 64)
                                                                out += String.fromCharCode(c2);

                                                                if (e4 != 64)
                                                                out += String.fromCharCode(c3);
                                                                }

                                                                return out;

                                                                }-*/;

    /**
     * Custom start tag for recognizing the returned schema data from the backend. Has to match the one defined in the
     * backend
     */
    private final static String SCHEMA_START = "[SCHEMA_START]"; //$NON-NLS-1$

    /**
     * Custom end tag for recognizing the returned schema data from the backend. Has to match the one defined in the
     * backend.
     */
    private final static String SCHEMA_END = "[/SCHEMA_END]"; //$NON-NLS-1$

    private static final String VALIDATION_START = "[VALIDATION_START]"; //$NON-NLS-1$

    private static final String VALIDATION_END = "[/VALIDATION_END]"; //$NON-NLS-1$

    private final Hidden idHidden;
    /** Textbox for connection name. */
    private final TextBox nameTextBox;

    /** Listbox for drivers. */
    private  ListBox driverListBox;

    /** Textbox for url. */
    private final TextBox urlTextBox;

    /** Textbox for user. */
    private final TextBox userTextBox;

    /** Textbox for Password. */
    private final PasswordTextBox passwordTextBox;

    /** File upload widget. */
    private final FileUpload fileUpload;

    private final TextBox roleTextBox;
    
    /** Schema upload button. */
    private final Button uploadButton;

    private final Button viewSchemaButton;
    
    private final CheckBox startupCheckbox;
    
    private final CheckBox schemaValCheckbox;

    /** Connect button. */
    private final Button saveButton;

    /** Cancel button. */
    private final Button cancelButton;

    /** Schema uploaded data. **/
    private String schemaData;

    private final static String CONNECT_MONDRIAN_PANEL = "pat-ConnectMondrianPanel"; //$NON-NLS-1$
    /**
     * ConnectMondrianPanel Constructor.
     */
    public ConnectMondrianPanel() {
        this(true);
    }
    private ConnectMondrianPanel(Boolean initData) {
        super(new BorderLayout());
        idHidden = new Hidden();
        nameTextBox = new TextBox();
        saveButton = new Button(Pat.CONSTANTS.save());
        uploadButton = new Button(Pat.CONSTANTS.upload());
        viewSchemaButton = new Button(Pat.CONSTANTS.viewSchema());
        cancelButton = new Button(Pat.CONSTANTS.cancel());
        driverListBox = new ListBox();
        urlTextBox = new TextBox();
        userTextBox = new TextBox();
        passwordTextBox = new PasswordTextBox();
        roleTextBox = new TextBox();
        fileUpload = new FileUpload();
        schemaData = ""; //$NON-NLS-1$
        startupCheckbox = new CheckBox(Pat.CONSTANTS.connectStartup());
        startupCheckbox.setValue(true);
        schemaValCheckbox = new CheckBox(Pat.CONSTANTS.validateSchema());
        this.setStyleName(CONNECT_MONDRIAN_PANEL);
        init();
        if (initData) {
            initDriverListBox();
        }

    }
    
    public ConnectMondrianPanel(CubeConnection cc) {
        this(false);
        idHidden.setValue(cc.getId());
        nameTextBox.setText(cc.getName());
        urlTextBox.setText(cc.getUrl());
        if (cc.getUsername() != null)
            userTextBox.setText(cc.getUsername());
        if (cc.getPassword() != null)
            passwordTextBox.setText(cc.getPassword());
        
        
        if (cc.getDriverClassName() != null) {
            initDriverListBox(cc.getDriverClassName());
        }
        
        if (cc.getRole() != null) {
            roleTextBox.setText(cc.getRole());
        }
        schemaData = cc.getSchemaData();
        startupCheckbox.setValue(cc.isConnectOnStartup());
        saveButton.setEnabled(true);
        if (schemaData != null && schemaData.length() > 0) {
            viewSchemaButton.setEnabled(true);
            viewSchemaButton.setText(Pat.CONSTANTS.viewSchema());
        }
        else
            viewSchemaButton.setText(Pat.CONSTANTS.noSchema());
    }
    

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    /**
     * Driver Listbox setup.
     * 
     * @return the list box
     */
    private void initDriverListBox() {
        initDriverListBox(null);
    }
    private void initDriverListBox(final String selectedDriver) {
        if (driverListBox == null) {
            driverListBox = new ListBox();
        }
        driverListBox.clear();
        ServiceFactory.getDiscoveryInstance().getDrivers(new AsyncCallback<String[]>() {
            public void onFailure(final Throwable arg0) {
                MessageBox.error(Pat.CONSTANTS.error(), arg0.getMessage());
            }

            public void onSuccess(final String[] arg0) {
                if (arg0 != null && arg0.length > 0) {
                    for (final String element2 : arg0) {
                        driverListBox.addItem(element2);
                    }
                    for (int i=0; selectedDriver != null && i < driverListBox.getItemCount(); i++) {
                        if (driverListBox.getItemText(i).equals(selectedDriver)) {
                            driverListBox.setSelectedIndex(i);
                        }
                    }
                } else {
                    MessageBox.error(Pat.CONSTANTS.error(),Pat.CONSTANTS.noJdbcDriverFound());
                }
            }
        });
    }

    /**
     * Cube connection setup.
     * 
     * @return the cube connection
     */
    private CubeConnection getCubeConnection() {
        final CubeConnection cubeConn = new CubeConnection(ConnectionType.Mondrian);
        if (idHidden != null && idHidden.getValue() != null && idHidden.getValue().length() > 0) {
            cubeConn.setId(idHidden.getValue());
        }
        cubeConn.setName(nameTextBox.getText());
        cubeConn.setDriverClassName(driverListBox.getItemText(driverListBox.getSelectedIndex()));
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
        cubeConn.setSchemaData(schemaData);
        cubeConn.setConnectOnStartup(startupCheckbox.getValue());
        cubeConn.setRole(roleTextBox.getText());
        return cubeConn;
    }

    /**
     * Run on panel initialize.
     */
    private void init() {

        final FormPanel formPanel = new FormPanel();
        formPanel.setAction(FORM_ACTION);
        formPanel.setMethod(FORM_METHOD);
        formPanel.setEncoding(FORM_ENCODING);
        formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

            public void onSubmitComplete(final SubmitCompleteEvent arg0) {
                if (arg0 != null && arg0.getResults() != null && arg0.getResults().length() > 0) {
                    if (arg0.getResults().contains(VALIDATION_START)) {
                        final String tmp = arg0.getResults().substring(
                                arg0.getResults().indexOf(VALIDATION_START) + VALIDATION_START.length(),
                                arg0.getResults().indexOf(VALIDATION_END));
                        if(tmp != null && tmp.length() > 0) {
                            if (schemaValCheckbox.getValue()) { 
                                MessageBox.info(Pat.CONSTANTS.warning(), MessageFactory.getInstance()
                                    .schemaFileInvalid() + "<br>" + tmp); //$NON-NLS-1$
                            }
                        }
                    }
                    if (arg0.getResults().contains(SCHEMA_START)) {
                        final String tmp = arg0.getResults().substring(
                                arg0.getResults().indexOf(SCHEMA_START) + SCHEMA_START.length(),
                                arg0.getResults().indexOf(SCHEMA_END));
                        schemaData = decode(tmp);
                        saveButton.setEnabled(true);
                        viewSchemaButton.setEnabled(true);
                        viewSchemaButton.setText(Pat.CONSTANTS.viewSchema());
                        // TODO remove this later

                        Application.INSTANCE.showInfoPanel(Pat.CONSTANTS.fileUpload(), Pat.CONSTANTS.success());
                    } else {
                        MessageBox.error(Pat.CONSTANTS.error(), Pat.CONSTANTS.fileUploadFailed());
                    }
                } else {
                    MessageBox.error(Pat.CONSTANTS.error(), Pat.CONSTANTS.checkErrorLog());
                }
            }
        });
        final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
        "p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.addLabel(Pat.CONSTANTS.name() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
        builder.add(nameTextBox, CellConstraints.xyw(3, 1, 5));
        builder.addLabel(Pat.CONSTANTS.jdbcDriver() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
        builder.add(driverListBox, CellConstraints.xyw(3, 3, 5));
        builder.addLabel(Pat.CONSTANTS.jdbcUrl() + LABEL_SUFFIX, CellConstraints.xy(1, 5));
        builder.add(urlTextBox, CellConstraints.xyw(3, 5, 5));
        builder.addLabel(Pat.CONSTANTS.username() + LABEL_SUFFIX, CellConstraints.xy(1, 7));
        builder.add(userTextBox, CellConstraints.xy(3, 7));
        builder.addLabel(Pat.CONSTANTS.password() + LABEL_SUFFIX, CellConstraints.xy(5, 7));
        builder.add(passwordTextBox, CellConstraints.xy(7, 7));
        builder.addLabel(Pat.CONSTANTS.schemaFile() + LABEL_SUFFIX, CellConstraints.xy(1, 9));
        fileUpload.setName(FORM_NAME_FILE);
        builder.add(fileUpload, CellConstraints.xyw(3, 9, 5));
        builder.add(schemaValCheckbox, CellConstraints.xyw(3, 11, 5));
        uploadButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final String filename = fileUpload.getFilename();
                if (filename == null || filename.length() == 0) {
                    MessageBox.error(Pat.CONSTANTS.error(), Pat.CONSTANTS.fileUploadNoFile());
                } else {
                    formPanel.submit();
                }
            }
        });

        builder.add(uploadButton, CellConstraints.xy(3, 13));
        
        viewSchemaButton.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                
                
                final WindowPanel winPanel = new WindowPanel(Pat.CONSTANTS.schemaFile());
                final LayoutPanel wpLayoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
                wpLayoutPanel.setSize("450px", "200px"); //$NON-NLS-1$ //$NON-NLS-2$
                final RichTextArea schemaArea = new RichTextArea();
                String newStr = schemaData + "";
                newStr = newStr.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;").replaceAll(" ", "&nbsp;");
                newStr = newStr.replaceAll("\t", "&nbsp;&nbsp;&nbsp;");
                newStr = newStr.replaceAll("(\r\n)", "<br>"); //$NON-NLS-1$ //$NON-NLS-2$
//                newStr = newStr.replaceAll("[\r\n\t\f]", "<br>"); //$NON-NLS-1$ //$NON-NLS-2$
                schemaArea.setHTML(newStr + "");
                
                wpLayoutPanel.add(schemaArea, new BoxLayoutData(1, 0.9));
                final ToolButton saveBtn = new ToolButton(Pat.CONSTANTS.save());
                saveBtn.addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent arg0) {
                        String newStr = schemaArea.getHTML();
                        newStr = newStr.replaceAll("&lt;","\\<").replaceAll("&gt;","\\>").replaceAll("&nbsp;"," ");
                        newStr = newStr.replaceAll("&nbsp;&nbsp;&nbsp;","\t");
                        newStr = newStr.replaceAll("<br>","\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                        schemaData = newStr;
                        winPanel.hide();
                        ConnectionManagerWindow.display(false);
                    }

                });

                final ToolButton closeBtn = new ToolButton(Pat.CONSTANTS.close());
                closeBtn.addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent arg0) {
                        winPanel.hide();
                        ConnectionManagerWindow.display(false);
                    }

                });
                final LayoutPanel wpButtonPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

                wpButtonPanel.add(saveBtn);
                wpButtonPanel.add(closeBtn);
                wpLayoutPanel.add(wpButtonPanel);
                wpLayoutPanel.layout();
                winPanel.add(wpLayoutPanel);
                winPanel.layout();
                winPanel.pack();
                winPanel.setSize("700px", "520px"); //$NON-NLS-1$ //$NON-NLS-2$
                
                ConnectionManagerWindow.close();
                winPanel.center();
                
            }
        });
        viewSchemaButton.setEnabled(false);
        viewSchemaButton.setText(Pat.CONSTANTS.noSchema());
        builder.add(viewSchemaButton, CellConstraints.xy(7, 13));
        
        builder.addLabel(Pat.CONSTANTS.role() + LABEL_SUFFIX, CellConstraints.xy(1, 15));
        builder.add(roleTextBox,CellConstraints.xyw(3,15,5));
        
        builder.add(startupCheckbox,CellConstraints.xy(3,17));
        
        
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

        saveButton.setEnabled(false);
        
        builder.add(saveButton, CellConstraints.xy(3, 19));

        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                ConnectionManagerWindow.closeTabs();
            }
        });
        builder.add(cancelButton, CellConstraints.xy(7, 19));
        final LayoutPanel layoutPanel = builder.getPanel();
        layoutPanel.setPadding(15);
        formPanel.add(layoutPanel);
        this.getLayoutPanel().add(formPanel);
    }

    public boolean validateConnection(CubeConnection cc) {

        if (cc.getDriverClassName().length() == 0 || cc.getName().length() == 0  || cc.getUrl().length() == 0) {
            if (idHidden.getValue() == null && cc.getSchemaData().length() == 0) {
            IGuiConstants inst = Pat.CONSTANTS;
            MessageBox.error(Pat.CONSTANTS.error(),
                    MessageFactory.getInstance().validationEmpty(inst.name().concat(",").concat(inst.jdbcDriver()).concat(",").concat(inst.jdbcUrl())));
            return false;
            }
        }
        return true;
    }

}
