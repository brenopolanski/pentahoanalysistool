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
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;
import org.pentaho.pat.rpc.dto.CubeConnection.ConnectionType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
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
    private static final String FORM_ACTION = "schemaupload"; //$NON-NLS-1$

    /** Height of the panel. */
    private static final String HEIGHT = "280px"; //$NON-NLS-1$

    /** Width of the Panel. */
    private static final String WIDTH = "620px"; //$NON-NLS-1$

    /** Suffix for label constants. */
    private static final String LABEL_SUFFIX = ":"; //$NON-NLS-1$

    /**
     * Custom start tag for recognizing the returned schema data from the backend. Has to match the one defined in the
     * backend
     */
    private static final String SCHEMA_START = "<PRE>"; //$NON-NLS-1$

    /**
     * Custom end tag for recognizing the returned schema data from the backend. Has to match the one defined in the
     * backend.
     */
    private static final String SCHEMA_END = "</PRE>"; //$NON-NLS-1$

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

    /** Textbox for connection name. */
    private final TextBox nameTextBox;

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

    /** Schema uploaded data. **/
    private String schemaData;

    /**
     * ConnectMondrianPanel Constructor.
     */
    public ConnectMondrianPanel() {
        super(new BorderLayout());

        nameTextBox = new TextBox();
        connectButton = new Button(ConstantFactory.getInstance().save());
        uploadButton = new Button(ConstantFactory.getInstance().upload());
        driverListBox = createDriverListComboBox();
        urlTextBox = new TextBox();
        userTextBox = new TextBox();
        passwordTextBox = new PasswordTextBox();
        fileUpload = new FileUpload();
        schemaData = ""; //$NON-NLS-1$

        init();

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

                if (arg0 != null && arg0.length > 0)
                    for (final String element2 : arg0)
                        listBox.addItem(element2);
                else
                    MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance()
                            .noJdbcDriverFound());
            }
        });
        return listBox;
    }

    /**
     * Cube connection setup.
     * 
     * @return the cube connection
     */
    private CubeConnection getCubeConnection() {
        final CubeConnection cc = new CubeConnection(ConnectionType.Mondrian);
        cc.setName(nameTextBox.getText());
        cc.setDriverClassName(driverListBox.getItemText(driverListBox.getSelectedIndex()));
        cc.setUrl(urlTextBox.getText());
        if (userTextBox.getText() != null && userTextBox.getText().length() > 0)
            cc.setUsername(userTextBox.getText());
        else
            cc.setUsername(null);
        if (passwordTextBox.getText() != null && passwordTextBox.getText().length() > 0)
            cc.setPassword(passwordTextBox.getText());
        else
            cc.setPassword(null);
        cc.setSchemaData(schemaData);
        return cc;
    }

    /**
     * Run on panel initialize.
     */
    private void init() {

        final FormPanel formPanel = new FormPanel();
        formPanel.setWidth(WIDTH);
        formPanel.setHeight(HEIGHT);
        formPanel.setAction(FORM_ACTION);
        formPanel.setMethod(FORM_METHOD);
        formPanel.setEncoding(FORM_ENCODING);

        formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

            public void onSubmitComplete(final SubmitCompleteEvent arg0) {
                // TODO Replace filename handling with stored schema handling
                // when implemented
                if (arg0 != null && arg0.getResults() != null && arg0.getResults().length() > 0) {
                    if (arg0.getResults().contains(SCHEMA_START)) {
                        final String tmp = arg0.getResults().substring(
                                arg0.getResults().indexOf(SCHEMA_START) + SCHEMA_START.length(),
                                arg0.getResults().indexOf(SCHEMA_END));
                        schemaData = decode(tmp);
                        connectButton.setEnabled(true);
                        // TODO remove this later
                        MessageBox.info(ConstantFactory.getInstance().fileUpload(), ConstantFactory.getInstance()
                                .success());
                    } else
                        MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance()
                                .fileUploadFailed());
                } else
                    MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance()
                            .checkErrorLog());
            }
        });
        final FormLayout layout = new FormLayout("right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                // "12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px");
                "p, 3dlu, p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p, 3dlu,p"); //$NON-NLS-1$
        final PanelBuilder builder = new PanelBuilder(layout);
        builder.addLabel(ConstantFactory.getInstance().name() + LABEL_SUFFIX, CellConstraints.xy(1, 1));
        builder.add(nameTextBox, CellConstraints.xyw(3, 1, 5));
        builder.addLabel(ConstantFactory.getInstance().jdbcDriver() + LABEL_SUFFIX, CellConstraints.xy(1, 3));
        builder.add(driverListBox, CellConstraints.xyw(3, 3, 5));
        builder.addLabel(ConstantFactory.getInstance().jdbcUrl() + LABEL_SUFFIX, CellConstraints.xy(1, 5));
        builder.add(urlTextBox, CellConstraints.xyw(3, 5, 5));
        builder.addLabel(ConstantFactory.getInstance().username() + LABEL_SUFFIX, CellConstraints.xy(1, 7));
        builder.add(userTextBox, CellConstraints.xy(3, 7));
        builder.addLabel(ConstantFactory.getInstance().password() + LABEL_SUFFIX, CellConstraints.xy(5, 7));
        builder.add(passwordTextBox, CellConstraints.xy(7, 7));
        builder.addLabel(ConstantFactory.getInstance().schemaFile() + LABEL_SUFFIX, CellConstraints.xy(1, 9));
        fileUpload.setName(FORM_NAME_FILE);
        builder.add(fileUpload, CellConstraints.xyw(3, 9, 5));

        uploadButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final String filename = fileUpload.getFilename();
                if (filename == null || filename.length() == 0)
                    MessageBox.error(ConstantFactory.getInstance().error(), ConstantFactory.getInstance()
                            .fileUploadNoFile());
                else
                    formPanel.submit();
            }
        });

        builder.add(uploadButton, CellConstraints.xyw(3, 11, 5));
        connectButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                connectButton.setEnabled(false);
                getCubeConnection();
                // TODO replace with new RPC functions
                // ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), getCubeConnection(), new
                // AsyncCallback<Object>() {
                // public void onFailure(final Throwable arg0) {
                // MessageBox.error(ConstantFactory.getInstance().error(),
                // MessageFactory.getInstance().noConnectionParam(arg0.getLocalizedMessage()));
                // connectButton.setEnabled(true);
                // }
                //
                // public void onSuccess(final Object o) {
                // connectButton.setEnabled(true);
                // MessageBox.info(ConstantFactory.getInstance().success(),
                // ConstantFactory.getInstance().connectionEstablished());
                // setConnectionEstablished(true);
                // GlobalConnectionFactory.getInstance().getConnectionListeners().fireConnectionMade(ConnectMondrianPanel.this);
                // // TODO change this once saving connections is possible
                // ConnectionManagerPanel.addConnection(new ConnectionItem("1234",getCubeConnection().getName(),false));
                // }
                // });
            }
        });

        connectButton.setEnabled(false);
        builder.add(connectButton, CellConstraints.xyw(3, 13, 5));

        final LayoutPanel layoutPanel = builder.getPanel();
        layoutPanel.setPadding(15);
        formPanel.add(layoutPanel);
        this.getLayoutPanel().add(formPanel);
    }

}
