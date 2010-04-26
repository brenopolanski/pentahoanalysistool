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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.LogoPanel;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.util.ConnectionItem;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * Connection Manager Panel for adding/modifying/deleting Connections
 * 
 * @created Jun 30, 2009
 * @since 0.4.0
 * @author Paul Stoellberger
 * 
 */
public class ConnectionManagerPanel extends LayoutComposite {

    private final static int PREFERREDWIDTH = 284;

    private final static int PREFERREDHEIGHT = 384;

    private static ListBox<ConnectionItem> listBox;

    private ToolBar toolBar;

    ToolButton connectionButton;
    
    private static LayoutPanel connectionsList = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    private static final DefaultListModel<ConnectionItem> CIMODEL = new DefaultListModel<ConnectionItem>();

    private final static String CONNECTION_MANAGER_PANEL = "pat-ConnectionManagerPanel"; //$NON-NLS-1$
    
    public ConnectionManagerPanel() {
        super();
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        this.setStyleName(CONNECTION_MANAGER_PANEL);
        setupConnectionList();
        baseLayoutPanel.add(connectionsList);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERREDWIDTH, PREFERREDHEIGHT);
    }

    private ListBox<ConnectionItem> createListBox() {
        final ListBox<ConnectionItem> cListBox = new ListBox<ConnectionItem>() {
            @Override
            public void onBrowserEvent(Event event) {
                super.onBrowserEvent(event);
                switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    Integer index = this.getSelectedIndex();
                    if (index >= 0) {
                        connectionButton.setEnabled(true);
                        ConnectionItem ci = this.getItem(index);

                        if (ci.isConnected()) {
                            connectionButton.invalidate();
                            connectionButton.setHTML(ButtonHelper.createButtonLabel(Pat.IMAGES.disconnect(),
                                    ConstantFactory.getInstance().disconnect(), ButtonLabelType.TEXT_ON_RIGHT));
                            connectionButton.layout();
                        }
                        else
                        {
                            connectionButton.invalidate();
                            connectionButton.setHTML(ButtonHelper.createButtonLabel(Pat.IMAGES.connect(),
                                    ConstantFactory.getInstance().connect(), ButtonLabelType.TEXT_ON_RIGHT));
                            connectionButton.layout();
                        }

                    }
                    break;
                }
            }
        };
        cListBox.setCellRenderer(new ListBox.CellRenderer<ConnectionItem>() {
            public void renderCell(final ListBox<ConnectionItem> cListBox, final int row, final int column,
                    final ConnectionItem item) {
                if (column == 0) {

                    cListBox.setWidget(row, column, createRichListBoxCell(item));
                } else {
                    throw new RuntimeException(MessageFactory.getInstance().unexpectedError());
                }
            }
        });
        cListBox.setModel(CIMODEL);
        
        
        // FIXME remove that and use style
        DOM.setStyleAttribute(cListBox.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        return cListBox;
    }

    private ToolBar createToolBar(final ListBox<ConnectionItem> linkedListBox) {

        final ToolBar toolBar = new ToolBar();
        toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.database_add(), null,
                ButtonLabelType.NO_TEXT), new ClickHandler() {
            public void onClick(final ClickEvent event) {
                ConnectionManagerWindow.closeTabs(false);
                ConnectionManagerWindow.showNewConnection();
                refreshMe();
            }
        }));

        toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.database_delete(), null,
                ButtonLabelType.NO_TEXT), new ClickHandler() {
            public void onClick(final ClickEvent event) {
                if (linkedListBox.getSelectedIndex() == -1) {
                    // TODO localize
                    MessageBox.alert("ListBox Edit", "No item selected"); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }
                final ConnectionItem item = linkedListBox.getItem(linkedListBox.getSelectedIndex());
                MessageBox.confirm("ListBox Remove", //$NON-NLS-1$
                        MessageFactory.getInstance().confirmDelete(item.getName()), new ConfirmationCallback() {
                    public void onResult(final boolean result) {
                        if (result) {
                            ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), item.getId(),
                                    new AsyncCallback<Object>() {

                                public void onFailure(final Throwable arg0) {
                                    MessageBox.alert(ConstantFactory.getInstance().error(),
                                            MessageFactory.getInstance().failedDisconnection(
                                                    arg0.getLocalizedMessage()));

                                }

                                public void onSuccess(final Object arg0) {
                                    ServiceFactory.getSessionInstance().deleteConnection(
                                            Pat.getSessionID(), item.getId(),
                                            new AsyncCallback<Object>() {

                                                public void onFailure(final Throwable arg0) {
                                                    MessageBox.alert(ConstantFactory.getInstance()
                                                            .error(), MessageFactory.getInstance()
                                                            .failedDeleteConnection(
                                                                    arg0.getLocalizedMessage()));

                                                }

                                                public void onSuccess(final Object arg0) {
                                                    refreshConnectionList();
                                                }

                                            });
                                }

                            });
                            CIMODEL.remove(linkedListBox.getSelectedIndex());
                        }
                    }
                });
            };
        }));


        final ToolButton editButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.database_edit(), null,
                ButtonLabelType.NO_TEXT), new ClickHandler() {
            public void onClick(final ClickEvent event) {
                if (linkedListBox.getSelectedIndex() == -1) {
                    MessageBox.alert("ListBox Edit", "No item selected"); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }
                
                final ConnectionItem item = linkedListBox.getItem(linkedListBox.getSelectedIndex());
                ServiceFactory.getSessionInstance().getConnection(Pat.getSessionID(), item.getId(), new AsyncCallback<CubeConnection>() {

                    public void onFailure(Throwable arg0) {
                        ConnectionManagerWindow.closeTabs();
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance().unexpectedError());
                    }

                    public void onSuccess(final CubeConnection cc) {
                        ConnectionManagerWindow.closeTabs(false);
                        if (item.isConnected()) {
                            ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), cc.getId(), new AsyncCallback<Object>() {

                                public void onFailure(Throwable arg0) {
                                    MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedConnection(
                                            arg0.getLocalizedMessage()));
                                }

                                public void onSuccess(Object arg0) {
                                    ConnectionManagerWindow.display(cc);
                                    refreshMe();
                                }
                            });
                        }
                        else {
                            ConnectionManagerWindow.display(cc);
                            refreshMe();
                        }
                    }
                });
            }
        });
        toolBar.add(editButton);


        return toolBar;
    }

    public static void addConnection(final ConnectionItem connItem) {
        CIMODEL.add(connItem);
    }

    public static void refreshConnectionList() {

        CIMODEL.clear();
        
        ServiceFactory.getSessionInstance().getConnections(Pat.getSessionID(), new AsyncCallback<CubeConnection[]>() {
            public void onFailure(final Throwable arg0) {
                MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedConnection(
                        arg0.getLocalizedMessage()));

            }

            public void onSuccess(final CubeConnection[] ccArray) {
                CIMODEL.clear();
                for (final CubeConnection element2 : ccArray) {
                    final ConnectionItem newCi = new ConnectionItem(element2.getId(), element2.getName(), element2.isConnected());
                    CIMODEL.add(newCi);
                }
                refreshMe();
            }
        });

    }

    public static void refreshMe() {
        connectionsList.invalidate();
        listBox.invalidate();
        connectionsList.layout();
        listBox.layout();
    }

    private void setupConnectionList() {
        listBox = createListBox();

        if (Pat.getApplicationState().getMode().isManageConnections()) {
            toolBar = createToolBar(listBox);
            connectionsList.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
        }
        connectionsList.add(listBox, new BoxLayoutData(FillStyle.BOTH));
        
        connectionButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.connect(),
                "Connect", ButtonLabelType.TEXT_ON_RIGHT));

        connectionButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {
                Integer index = listBox.getSelectedIndex();
                if (index >= 0) {
                    ConnectionItem ci = listBox.getItem(index);
                    if (ci != null) {
                        connectEvent(ci.getId(),ci.isConnected());
                        if (ci.isConnected()) {
                            connectionButton.setHTML(ButtonHelper.createButtonLabel(Pat.IMAGES.disconnect(),
                                    "Disconnect", ButtonLabelType.TEXT_ON_RIGHT));
                        }
                        else
                            connectionButton.setHTML(ButtonHelper.createButtonLabel(Pat.IMAGES.connect(),
                                    "Connect", ButtonLabelType.TEXT_ON_RIGHT));
                    }
                }
                
                
            }
        });
        connectionButton.setEnabled(false);
        connectionsList.add(connectionButton,new BoxLayoutData(FillStyle.HORIZONTAL));
        
    }

    private Widget createRichListBoxCell(final ConnectionItem item) {
        final FlexTable table = new FlexTable();
        final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

        table.setWidth("100%"); //$NON-NLS-1$
        table.setBorderWidth(0);
        table.setCellPadding(3);
        table.setCellSpacing(0);

        Image cImage;
        if (item.isConnected()) {
            cImage = Pat.IMAGES.connect().createImage();
        } else {
            cImage = Pat.IMAGES.disconnect().createImage();
        }
        final CustomButton cButton = new CustomButton(cImage) {
            @Override
            protected void onClick() {
                super.onClick();
                connectEvent(item.getId(),item.isConnected());
            };
        };
        table.setWidget(0, 0, cButton);
        cellFormatter.setWidth(0, 0, "25px"); //$NON-NLS-1$
        table.setHTML(0, 1, "<b>" + item.getName() + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$
        cellFormatter.setWidth(0, 1, "100%"); //$NON-NLS-1$
        return table;
    }

    /**
     * Fires a connection event
     * If the connection item is connected already, it will disconnect, otherwise connect
     * @param item - The connection item
     */
    public static void connectEvent(final String connectionId, final Boolean isConnected) {

        if(connectionId != null && isConnected != null) {
            LogoPanel.spinWheel(true);

            if (isConnected) {
                ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), connectionId,
                        new AsyncCallback<Object>() {

                    public void onFailure(final Throwable arg0) {
                        MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory
                                .getInstance().failedDisconnection(arg0.getLocalizedMessage()));
                        LogoPanel.spinWheel(false);
                    }

                    public void onSuccess(final Object arg0) {
                        LogoPanel.spinWheel(false);
                        refreshConnectionList();

                    }

                });
            } else {
                ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), connectionId,
                        new AsyncCallback<Object>() {

                    public void onFailure(final Throwable arg0) {
                        LogoPanel.spinWheel(false);
                        MessageBox.alert(
                                ConstantFactory.getInstance().error(),
                                MessageFactory.getInstance().failedConnect(arg0.getMessage())); //$NON-NLS-1$
                    }

                    public void onSuccess(final Object arg0) {
                        LogoPanel.spinWheel(false);
                        refreshConnectionList();

                    }

                });
            }
        }
    }
}
