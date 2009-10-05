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

import java.util.ArrayList;

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
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;
import org.pentaho.pat.client.util.ConnectionItem;
import org.pentaho.pat.client.util.factory.ConstantFactory;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

    private final int preferredWidth = 284;
    
    private final int preferredHeight = 384;
    
    private static ListBox<ConnectionItem> listBox;

    private ToolBar toolBar;

    private static LayoutPanel connectionsList = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    private static final DefaultListModel<ConnectionItem> ciModel = new DefaultListModel<ConnectionItem>();

    public ConnectionManagerPanel() {
        super();
        final LayoutPanel baseLayoutPanel = getLayoutPanel();

        setupConnectionList();
        baseLayoutPanel.add(connectionsList);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(preferredWidth, preferredHeight);
    }


    private ListBox<ConnectionItem> createListBox() {
        final ListBox<ConnectionItem> cListBox = new ListBox<ConnectionItem>();
        cListBox.setCellRenderer(new ListBox.CellRenderer<ConnectionItem>() {
            public void renderCell(final ListBox<ConnectionItem> cListBox, final int row, final int column,final ConnectionItem item) {
                switch (column) {
                case 0:
                    cListBox.setWidget(row, column, createRichListBoxCell(item, cListBox));
                    break;
                default:
                    throw new RuntimeException(MessageFactory.getInstance().unexpectedError());
                }
            }
        });
        cListBox.setModel(ciModel);
        return cListBox;
    }

    private ToolBar createToolBar(final ListBox<ConnectionItem> linkedListBox) {

        final ToolBar toolBar = new ToolBar();
        toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.database_add(), null,
                ButtonLabelType.NO_TEXT), new ClickHandler() {
            public void onClick(final ClickEvent event) {
                ConnectionManagerWindow.showNewConnection();
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
                        MessageFactory.getInstance().confirmDelete(item.getName()),
                        new ConfirmationCallback() { 
                    public void onResult(final boolean result) {
                        if (result) {
                            ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), item.getId(),
                                    new AsyncCallback<Object>() {

                                public void onFailure(final Throwable arg0) {
                                    MessageBox.alert(ConstantFactory.getInstance().error(),
                                            MessageFactory.getInstance().failedDisconnection(arg0.getLocalizedMessage()));

                                }

                                public void onSuccess(final Object arg0) {
                                    ServiceFactory.getSessionInstance().deleteConnection(
                                            Pat.getSessionID(), item.getId(),
                                            new AsyncCallback<Object>() {

                                                public void onFailure(final Throwable arg0) {
                                                    MessageBox.alert(ConstantFactory.getInstance()
                                                            .error(), MessageFactory.getInstance()
                                                            .failedDeleteConnection(arg0.getLocalizedMessage()));

                                                }

                                                public void onSuccess(final Object arg0) {
                                                    refreshConnectionList();
                                                }

                                            });
                                }

                            });
                            ciModel.remove(linkedListBox.getSelectedIndex());
                        }
                    }
                });
            };
        }));

        // EDIT will be disabled for some more time
        final ToolButton editButton = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.database_edit(), null,
                ButtonLabelType.NO_TEXT), new ClickHandler() {
            public void onClick(final ClickEvent event) {
                if (linkedListBox.getSelectedIndex() == -1) {
                    MessageBox.alert("ListBox Edit", "No item selected"); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }
                // String item = listBox.getItem(listBox.getSelectedIndex()).getName();
                // ConnectionManagerWindow.display();
            }
        });
        editButton.setEnabled(false);
        toolBar.add(editButton);

        return toolBar;
    }

    public static void addConnection(final ConnectionItem ci) {
        ciModel.add(ci);
    }

    public static void refreshConnectionList() {

        final ArrayList<ConnectionItem> cList = new ArrayList<ConnectionItem>();
        ciModel.clear();
        ServiceFactory.getSessionInstance().getConnections(Pat.getSessionID(), new AsyncCallback<CubeConnection[]>() {
            public void onFailure(final Throwable arg0) {
                MessageBox
                .alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance().failedConnection(arg0.getLocalizedMessage()));

            }

            public void onSuccess(final CubeConnection[] ccArray) {
                for (final CubeConnection element2 : ccArray) {
                    final ConnectionItem newCi = new ConnectionItem(element2.getId(), element2.getName(), false);
                    cList.add(newCi);
                }
                ServiceFactory.getSessionInstance().getActiveConnections(Pat.getSessionID(),
                        new AsyncCallback<CubeConnection[]>() {

                            public void onFailure(final Throwable arg0) {
                                MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                                        .failedActiveConnection(arg0.getLocalizedMessage()));

                            }

                            public void onSuccess(final CubeConnection[] ccArray2) {
                                for (final CubeConnection element2 : ccArray2) {
                                    final ConnectionItem newCi = new ConnectionItem(element2.getId(), element2.getName(), false);

                                     if(cList.contains(newCi)) {
                                        cList.get(cList.indexOf(newCi)).setConnected(true);
                                     }
                                    else {
                                        newCi.setConnected(true);
                                        cList.add(newCi);
                                    }
                                }

                                for (final ConnectionItem cItem : cList)
                                    ciModel.add(cItem);
                                refreshMe();
                            }
                        });

            }
        });

    }

    public static void refreshMe() {
        connectionsList.invalidate();
        listBox.invalidate();
        connectionsList.layout();
        listBox.layout();
    }

    public void setupConnectionList() {
        listBox = createListBox();

        if (Pat.getApplicationState().getMode().isManageConnections()) {
            toolBar = createToolBar(listBox);
            connectionsList.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
        }
        connectionsList.add(listBox, new BoxLayoutData(FillStyle.BOTH));
    }

    private Widget createRichListBoxCell(final ConnectionItem item, final ListBox<ConnectionItem> linkedListBox) {
        final FlexTable table = new FlexTable();
        final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

        table.setWidth("100%"); //$NON-NLS-1$
        table.setBorderWidth(0);
        table.setCellPadding(3);
        table.setCellSpacing(0);

        // table.setStyleName("RichListBoxCell");
        Image cImage;
        if (item.isConnected())
            cImage = Pat.IMAGES.connect().createImage();
        else
            cImage = Pat.IMAGES.disconnect().createImage();

        final CustomButton cButton = new CustomButton(cImage) {
            @Override
            protected void onClick() {
                super.onClick();
                LogoPanel.spinWheel(true);
                if (item.isConnected())
                    ServiceFactory.getSessionInstance().disconnect(Pat.getSessionID(), item.getId(),
                            new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {
                            MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory
                                    .getInstance().failedDisconnection(arg0.getLocalizedMessage()));
                            LogoPanel.spinWheel(false);
                        }

                        public void onSuccess(final Object arg0) {
                            refreshConnectionList();
                            LogoPanel.spinWheel(false);
                        }

                    });
                else
                    ServiceFactory.getSessionInstance().connect(Pat.getSessionID(), item.getId(),
                            new AsyncCallback<Object>() {

                        public void onFailure(final Throwable arg0) {
                            LogoPanel.spinWheel(false);
                            MessageBox.alert(ConstantFactory.getInstance().error(), MessageFactory
                                    .getInstance().failedLoadConnection(arg0.getLocalizedMessage()));
                        }

                        public void onSuccess(final Object arg0) {
                            LogoPanel.spinWheel(false);
                            refreshConnectionList();
                        }

                    });
                final int index = linkedListBox.getSelectedIndex();
                ciModel.set(index, item);

            };
        };
        table.setWidget(0, 0, cButton);
        cellFormatter.setWidth(0, 0, "25px"); //$NON-NLS-1$
        // cellFormatter.setHeight(0, 0, "25px");
        table.setHTML(0, 1, "<b>" + item.getName() + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$
        cellFormatter.setWidth(0, 1, "100%"); //$NON-NLS-1$
        return table;
    }
}
