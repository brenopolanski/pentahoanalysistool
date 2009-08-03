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
import org.pentaho.pat.client.deprecated.util.ConnectionItem;
import org.pentaho.pat.client.ui.windows.ConnectionManagerWindow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * Connection Manager Panel for adding/modifying/deleting Connections
 * @created Jun 30, 2009 
 * @since 0.4.0
 * @author Paul Stoellberger
 * 
 */
public class ConnectionManagerPanel extends LayoutComposite  {

	private ListBox<ConnectionItem> listBox;
	private ToolBar toolBar;
	private LayoutPanel connectionsList;

	private static final DefaultListModel<ConnectionItem> model = new DefaultListModel<ConnectionItem>();

	public ConnectionManagerPanel() {
		super();
		final LayoutPanel baseLayoutPanel = getLayoutPanel();

		setupConnectionList();
		baseLayoutPanel.add(connectionsList);


	}

	public void setupConnectionList() {
		final LayoutPanel vBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		vBox.setPadding(0);
		vBox.setWidgetSpacing(0);

		// model.add(new ConnectionItem("123","debug connection",false));  //$NON-NLS-1$//$NON-NLS-2$
		
		listBox = createListBox();

		if (Pat.getApplicationState().getMode().isManageConnections()) {
			toolBar = createToolBar(listBox);
			vBox.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
		}
		
		vBox.add(listBox, new BoxLayoutData(FillStyle.BOTH));

		connectionsList = vBox;
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

		CustomButton cButton = new CustomButton(cImage) {
			@Override
			protected void onClick() {
				// TODO implement dis-/connect routine(externalize strings)
				super.onClick();
				if (item.isConnected()) {
					MessageBox.info("Success", "Disconnected!"); //$NON-NLS-1$ //$NON-NLS-2$
					item.setConnected(false);
				}
				else {
					MessageBox.info("Success", "Connected!");  //$NON-NLS-1$//$NON-NLS-2$
					item.setConnected(true);
				}
				final int index = linkedListBox.getSelectedIndex();
				model.set(index, item);


			};
		};

		table.setWidget(0,0,cButton);
		cellFormatter.setWidth(0, 0, "25px"); //$NON-NLS-1$
		//cellFormatter.setHeight(0, 0, "25px");
		table.setHTML(0, 1, "<b>" + item.getName() + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$
		return table;
	}

	public ListBox<ConnectionItem> createListBox() {
		final ListBox<ConnectionItem> cListBox = new ListBox<ConnectionItem>();
		cListBox.setCellRenderer(new ListBox.CellRenderer<ConnectionItem>() {
			public void renderCell(ListBox<ConnectionItem> cListBox, int row, int column,
					ConnectionItem item) {
				switch (column) {
				case 0:
					cListBox.setWidget(row, column, createRichListBoxCell(item,cListBox));
					break;
				default:
					throw new RuntimeException("Should not happen"); //$NON-NLS-1$
				}
			}
		});
		cListBox.setModel(model);
		return cListBox;
	}

	public static void addConnection(ConnectionItem ci) {
		model.add(ci);
	}


	public ToolBar createToolBar(final ListBox<ConnectionItem> linkedListBox) {

		final ToolBar toolBar = new ToolBar();
		toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(
				Pat.IMAGES.add(), null,
				ButtonLabelType.NO_TEXT), new ClickHandler() {
			public void onClick(ClickEvent event) {
				ConnectionManagerWindow.showNewConnection();
			}
		}));

		toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cross(), null,ButtonLabelType.NO_TEXT), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (linkedListBox.getSelectedIndex() == -1) {
					MessageBox.alert("ListBox Edit", "No item selected"); //$NON-NLS-1$ //$NON-NLS-2$
					return;
				}
				String item = linkedListBox.getItem(linkedListBox.getSelectedIndex()).getName();
				MessageBox.confirm("ListBox Remove", //$NON-NLS-1$
						"Are you sure you want to permanently delete '" + item //$NON-NLS-1$
						+ "' from the list?", new ConfirmationCallback() { //$NON-NLS-1$
					public void onResult(boolean result) {
						if (result) {
							model.remove(linkedListBox.getSelectedIndex());
						}
					}
				});
			};
		}));

		// EDIT will be disabled for some more time
		ToolButton editButton = new ToolButton("Edit", new ClickHandler() { //$NON-NLS-1$
			public void onClick(ClickEvent event) {
				if (linkedListBox.getSelectedIndex() == -1) {
					MessageBox.alert("ListBox Edit", "No item selected"); //$NON-NLS-1$ //$NON-NLS-2$
					return;
				}
				// String item = listBox.getItem(listBox.getSelectedIndex()).getName();
				ConnectionManagerWindow.display();
			}
		});
		editButton.setEnabled(false);
		toolBar.add(editButton);

		return toolBar;
	}
}
