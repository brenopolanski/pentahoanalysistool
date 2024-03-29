/*
 * Copyright (C) 2009 Thomas Barber
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

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.Filter;
import org.gwt.mosaic.ui.client.list.FilterProxyListModel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.MainTabPanel;
import org.pentaho.pat.client.ui.widgets.AbstractDataWidget;
import org.pentaho.pat.client.util.factory.MessageFactory;
import org.pentaho.pat.client.util.factory.ServiceFactory;
import org.pentaho.pat.rpc.dto.QuerySaveModel;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * Creates the Save Panel for the SaveWindow.
 * 
 * @created Oct 21, 2009
 * @since 0.5.1
 * @author tom (at) wamonline.org.uk
 */
public class SaveMenuPanel extends LayoutComposite {

    /**
     */
    

    private DefaultListModel<QuerySaveModel> model;

    private ListBox<QuerySaveModel> listBox = null;

    private TextBox saveTextBox = new TextBox();

    private final static String SAVE_MENU_PANEL = "pat-SaveMenuPanel"; //$NON-NLS-1$
    /**
     */
    private FilterProxyListModel<QuerySaveModel, String> filterModel;

    private final Timer filterTimer = new Timer() {
        @Override
        public void run() {
            filterModel.filter(saveTextBox.getText());
        }
    };

    public SaveMenuPanel() {
        super();
        final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        layoutPanel.setPadding(0);
        this.setStyleName(SAVE_MENU_PANEL);
        saveTextBox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(final KeyPressEvent event) {
                filterTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
            }
        });

        final Label saveText = new Label(Pat.CONSTANTS.save() + ":"); //$NON-NLS-1$
        final LayoutPanel savePanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
        savePanel.add(saveText, new BoxLayoutData(FillStyle.VERTICAL));
        savePanel.add(saveTextBox, new BoxLayoutData(FillStyle.BOTH));



        layoutPanel.add(createListBox(), new BoxLayoutData(FillStyle.BOTH));

        layoutPanel.add(savePanel, new BoxLayoutData(FillStyle.HORIZONTAL));
        this.getLayoutPanel().add(layoutPanel);

    }

    @Override
    protected void onAttach() {

        super.onAttach();
        if (saveTextBox != null) {
            saveTextBox.setText("");
        }
        filterTimer.schedule(1);
    }

    public ListBox<?> createListBox() {
        listBox = new ListBox<QuerySaveModel>(){
            @Override
            public void onBrowserEvent(Event event) {

                super.onBrowserEvent(event);
                switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    if (listBox.getSelectedIndex() >= 0) {
                        QuerySaveModel qsm = listBox.getItem(listBox.getSelectedIndex());
                        saveTextBox.setText(qsm.getName());
                    }
                    break;
                case Event.ONDBLCLICK:
                    save();
                    break;
                }

            }
        };

        listBox.setCellRenderer(new CellRenderer<QuerySaveModel>() {
            public void renderCell(final ListBox<QuerySaveModel> listBox, final int row, final int column,
                    final QuerySaveModel item) {
                if (column == 0) {
                    listBox.setWidget(row, column, createRichListBoxCell(item));
                } else {
                    throw new RuntimeException(Pat.CONSTANTS.shouldnthappen());
                }
            }
        });

        model = (DefaultListModel<QuerySaveModel>) listBox.getModel();

        filterModel = new FilterProxyListModel<QuerySaveModel, String>(model);
        filterModel.setModelFilter(new Filter<QuerySaveModel, String>() {
            public boolean select(final QuerySaveModel element, final String filter) {
                final String regexp = ".*" + filter + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
                if (regexp == null || regexp.length() == 0) {
                    return true;
                }
                return element.getName().matches(regexp);
            }
        });

        return listBox;
    }

    public void loadSavedQueries() {
        ServiceFactory.getQueryInstance().getSavedQueries(Pat.getSessionID(),
                new AsyncCallback<List<QuerySaveModel>>() {

            public void onFailure(final Throwable arg0) {
                MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                        .failedGetQueryList(arg0.getLocalizedMessage()));
            }

            public void onSuccess(final List<QuerySaveModel> arg0) {
                listBox.setModel(model);
                if (arg0 != null) {
                    model.clear();
                    for (int i = 0; i < arg0.size(); i++) {
                        model.add(arg0.get(i));
                    }
                    listBox.setModel(filterModel);
                    filterTimer.schedule(1);


                }
            }
        });

    }

    public void save() {
        if (saveTextBox != null && saveTextBox.getText().length() > 0) {
            boolean exists = false;
            for (int i = 0; !exists && i < model.getSize();i++) {
                QuerySaveModel item = model.getElementAt(i);

                if (item.getName().equals(saveTextBox.getText())) {
                    exists = true;
                }
            }
            if (exists) {
                MessageBox.confirm(Pat.CONSTANTS.warning(),
                        MessageFactory.getInstance().confirmQueryOverwrite(), new ConfirmationCallback() {

                    public void onResult(boolean result) {
                        if (result) {
                            saveInternal();
                        }
                    }

                });
            }
            else {
                saveInternal();
            }


        }
    }

    private void saveInternal() {
        ServiceFactory.getQueryInstance().saveQuery(Pat.getSessionID(), Pat.getCurrQuery(), saveTextBox.getText(),
                Pat.getCurrConnectionId(), Pat.getCurrCube(), Pat.getCurrCubeName(), new AsyncCallback<Object>() {

            public void onFailure(final Throwable arg0) {
                MessageBox.error(Pat.CONSTANTS.error(), MessageFactory.getInstance()
                        .failedSaveQuery(arg0.getLocalizedMessage()));
            }

            public void onSuccess(final Object arg0) {
                Widget widget = MainTabPanel.getSelectedWidget();
                if (widget instanceof AbstractDataWidget) {
                    ((AbstractDataWidget) widget).setTitle(saveTextBox.getText());
                }


            }

        });

    }

    private Widget createRichListBoxCell(final QuerySaveModel item) {
        final FlexTable table = new FlexTable();
        final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

        table.setWidth("100%"); //$NON-NLS-1$
        table.setBorderWidth(0);
        table.setCellPadding(3);
        table.setCellSpacing(0);

        table.setWidget(0, 0, Pat.IMAGES.database_add().createImage());
        cellFormatter.setRowSpan(0, 0, 3);
        cellFormatter.setWidth(0, 0, "32px"); //$NON-NLS-1$

        table.setHTML(0, 1, "<b>" + item.getName() + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$

        table.setText(1, 0, "Connection: " + item.getConnection().getName()); //$NON-NLS-1$
        table.setText(2, 0, "Last Updated: " + item.getSavedDate()); //$NON-NLS-1$

        return table;
    }

    public void delete() {
        if (listBox.getSelectedIndex() >= 0) {
            QuerySaveModel delItem = listBox.getItem(listBox.getSelectedIndex());
            ServiceFactory.getQueryInstance().deleteSavedQuery(Pat.getSessionID(), delItem.getName(), new AsyncCallback<Object>() {

                public void onFailure(final Throwable arg0) {
                    MessageBox.error(Pat.CONSTANTS.error(), arg0.getMessage());
                }

                public void onSuccess(final Object arg0) {
                    loadSavedQueries();
                }

            });
        }

    }
}
