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

import java.util.List;

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.LevelProperties;
import org.pentaho.pat.rpc.dto.query.IAxis;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tree list of connections and cubes.
 * 
 * @created Aug 18, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class PropertiesMenu extends LayoutComposite {

    private final static String DIMENSION_MENU = "pat-DimensionMenu"; //$NON-NLS-1$

    final DefaultListModel<LevelProperties> model = new DefaultListModel<LevelProperties>();

    /**
     * 
     * DimensionMenu Constructor.
     * 
     */
    public PropertiesMenu() {
        super();
        this.setStyleName(DIMENSION_MENU);
        final LayoutPanel baseLayoutPanel = getLayoutPanel();
        baseLayoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        final LayoutPanel vBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        vBox.setPadding(0);
        vBox.setWidgetSpacing(0);

        final ListBox<LevelProperties> listBox = new ListBox<LevelProperties>();
        listBox.setCellRenderer(new CellRenderer<LevelProperties>() {
            public void renderCell(ListBox<LevelProperties> listBox, int row, int column, LevelProperties item) {
                switch (column) {
                case 0:
                    listBox.setWidget(row, column, createRichListBoxCell(item));
                    break;
                default:
                    throw new RuntimeException("Should not happen");
                }
            }
        });

        listBox.setContextMenu(createContextMenu());

        listBox.setModel(model);

        baseLayoutPanel.add(listBox, new BoxLayoutData(FillStyle.BOTH));

    }

    private PopupMenu createContextMenu() {
        Command cmd = new Command() {
            public void execute() {
                // InfoPanel.show("Menu Button", "You selected a menu item!");
                ServiceFactory.getQueryInstance().addProperty(Pat.getSessionID(), Pat.getCurrQuery(), "dimensionName",
                        "levelName", "propertyName", true, new AsyncCallback<Object>() {

                            public void onFailure(Throwable arg0) {
                                // TODO Auto-generated method stub

                            }

                            public void onSuccess(Object arg0) {
                                // TODO Auto-generated method stub

                            }

                        });
            }
        };

        PopupMenu contextMenu = new PopupMenu();

        contextMenu.addItem("Enable", cmd);
        return contextMenu;
    }

    private Widget createRichListBoxCell(LevelProperties item) {
        final FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.setBorderWidth(0);
        table.setCellPadding(3);
        table.setCellSpacing(0);
        table.setStyleName("RichListBoxCell");

        table.setHTML(0, 0, "<b>" + item.getLevelName() + "</b>");

        table.setText(0, 1, "Property: " + item.getPropertyName());

        return table;
    }

    public void loadAxisProperties(final String queryId, IAxis targetAxis) {
        ServiceFactory.getDiscoveryInstance().getDimensions(Pat.getSessionID(), Pat.getCurrQuery(), targetAxis,
                new AsyncCallback<String[]>() {

                    public void onFailure(Throwable arg0) {
                        MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                .failedMemberFetch(arg0.getLocalizedMessage()));

                    }

                    public void onSuccess(String[] dimensionList) {
                        createListBox(queryId, dimensionList[0]);
                    }
                });
    }

    public void createListBox(String queryId, String dimensionId) {
        ServiceFactory.getDiscoveryInstance().getAllLevelProperties(Pat.getSessionID(), queryId, dimensionId,
                new AsyncCallback<List<LevelProperties>>() {

                    public void onFailure(final Throwable arg0) {
                        // dimensionTree.clear();
                        MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                                .failedMemberFetch(arg0.getLocalizedMessage()));
                    }

                    public void onSuccess(final List<LevelProperties> labels) {
                        if (labels.size() > 0) {
                            model.addAll(0, labels);
                        } else {
                            model.add(new LevelProperties("N/A", "No properties available"));
                        }
                    }
                });
    }

}