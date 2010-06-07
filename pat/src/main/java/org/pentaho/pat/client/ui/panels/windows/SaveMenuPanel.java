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
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.Filter;
import org.gwt.mosaic.ui.client.list.FilterProxyListModel;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.widgets.LabelTextBox;
import org.pentaho.pat.client.util.factory.ConstantFactory;
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
    private final TextBox filtertextBox;

    private DefaultListModel<QuerySaveModel> model;

    private ListBox<QuerySaveModel> listBox = null;

    private LabelTextBox saveTextBox = new LabelTextBox();

    private final static String SAVE_MENU_PANEL = "pat-SaveMenuPanel"; //$NON-NLS-1$
    /**
     */
    private FilterProxyListModel<QuerySaveModel, String> filterModel;

    private final Timer filterTimer = new Timer() {
        @Override
        public void run() {
            filterModel.filter(filtertextBox.getText());
        }
    };

    public SaveMenuPanel() {
        super();
        final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
        layoutPanel.setPadding(0);
        this.setStyleName(SAVE_MENU_PANEL);
        filtertextBox = new TextBox();
        filtertextBox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(final KeyPressEvent event) {
                filterTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
            }
        });

        final Label filterText = new Label(ConstantFactory.getInstance().filter() + ":"); //$NON-NLS-1$
        final LayoutPanel filterPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
        filterPanel.add(filterText, new BoxLayoutData(FillStyle.VERTICAL));
        filterPanel.add(filtertextBox, new BoxLayoutData(FillStyle.BOTH));

        
        layoutPanel.add(filterPanel, new BoxLayoutData(FillStyle.HORIZONTAL));
        layoutPanel.add(createListBox(), new BoxLayoutData(FillStyle.BOTH));

        saveTextBox.setTextBoxLabelText(ConstantFactory.getInstance().save());
        layoutPanel.add(saveTextBox);

        this.getLayoutPanel().add(layoutPanel);

    }
    
    @Override
    protected void onAttach() {
     
        super.onAttach();
        if (filtertextBox != null) {
            filtertextBox.setText("");
        }
        if (saveTextBox != null) {
            saveTextBox.setTextBoxText("");
        }
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
                        saveTextBox.setTextBoxText(qsm.getName());
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
                    throw new RuntimeException(ConstantFactory.getInstance().shouldnthappen());
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
                        MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
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

                        }
                    }
                });

    }

    public void save() {
        if (saveTextBox != null && saveTextBox.getTextBoxText().length() > 0) {
            ServiceFactory.getQueryInstance().saveQuery(Pat.getSessionID(), Pat.getCurrQuery(), saveTextBox.getTextBoxText(),
                    Pat.getCurrConnectionId(), Pat.getCurrCube(), Pat.getCurrCubeName(), new AsyncCallback<Object>() {

                public void onFailure(final Throwable arg0) {
                    MessageBox.error(ConstantFactory.getInstance().error(), MessageFactory.getInstance()
                            .failedSaveQuery(arg0.getLocalizedMessage()));
                }

                public void onSuccess(final Object arg0) {
                    loadSavedQueries();

                }

            });
        }
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
}
