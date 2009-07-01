/*
 * Copyright (C) 2009 Paul Stoellberger
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA *
 *
 * @created Jun 30, 2009 
 * @author Paul Stoellberger
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
import org.pentaho.pat.client.Application;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.ConnectionWindow;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

class ConnectionItem {
	private String name;
	private String id;
	private Boolean isConnected = false;

	public ConnectionItem(String id, String name, boolean isConnected) {
		this.name = name;
		this.id = id;
		this.isConnected = isConnected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}
}

public class ConnectionManagerPanel extends LayoutComposite  {

	private ListBox<ConnectionItem> listBox;
	private ToolBar toolBar;

	private static final DefaultListModel<ConnectionItem> model = new DefaultListModel<ConnectionItem>();

	public ConnectionManagerPanel() {
		super();
		final LayoutPanel baseLayoutPanel = getLayoutPanel();

		baseLayoutPanel.add(setupConnectionList());


	}

	public Widget setupConnectionList() {
		final LayoutPanel vBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		vBox.setPadding(0);
		vBox.setWidgetSpacing(0);

		model.add(new ConnectionItem("123","debug connection",false));
		
		listBox = createListBox();
		toolBar = createToolBar(listBox);


		vBox.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
		vBox.add(listBox, new BoxLayoutData(FillStyle.BOTH));

		return vBox;
	}


	private Widget createRichListBoxCell(final ConnectionItem item, final ListBox<ConnectionItem> linkedListBox) {
		final FlexTable table = new FlexTable();
		final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

		table.setWidth("100%");
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
				// TODO implement dis-/connect routine
				super.onClick();
				if (item.isConnected()) {
					MessageBox.info("Success", "Disconnected!");
					item.setConnected(false);
				}
				else {
					MessageBox.info("Success", "Connected!");
					item.setConnected(true);
				}
				final int index = linkedListBox.getSelectedIndex();
				model.set(index, item);


			};
		};

		table.setWidget(0,0,cButton);
		cellFormatter.setWidth(0, 0, "25px");
		//cellFormatter.setHeight(0, 0, "25px");
		table.setHTML(0, 1, "<b>" + item.getName() + "</b>");
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
					throw new RuntimeException("Should not happen");
				}
			}
		});
		cListBox.setModel(model);
		return cListBox;
	}

	public static void addConnection(CubeConnection cc) {
		model.add(new ConnectionItem("123",cc.getName(),false));
	}


	public ToolBar createToolBar(final ListBox<ConnectionItem> linkedListBox) {

		final ToolBar toolBar = new ToolBar();
		toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(
				Pat.IMAGES.add(), null,
				ButtonLabelType.NO_TEXT), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (Application.getConnectionWindow() == null) {
					Application.setConnectionWindow(new ConnectionWindow());
				}
				Application.getConnectionWindow().emptyForms();
				Application.getConnectionWindow().showModal(true);
			}
		}));

		toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.cross(), null,ButtonLabelType.NO_TEXT), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (linkedListBox.getSelectedIndex() == -1) {
					MessageBox.alert("ListBox Edit", "No item selected");
					return;
				}
				String item = linkedListBox.getItem(linkedListBox.getSelectedIndex()).getName();
				MessageBox.confirm("ListBox Remove",
						"Are you sure you want to permanently delete '" + item
						+ "' from the list?", new ConfirmationCallback() {
					public void onResult(boolean result) {
						if (result) {
							model.remove(linkedListBox.getSelectedIndex());
						}
					}
				});
			};
		}));

		// EDIT will be disabled for some more time
		ToolButton editButton = new ToolButton("Edit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (linkedListBox.getSelectedIndex() == -1) {
					MessageBox.alert("ListBox Edit", "No item selected");
					return;
				}
				// String item = listBox.getItem(listBox.getSelectedIndex()).getName();
				if (Application.getConnectionWindow() == null) {
					Application.setConnectionWindow(new ConnectionWindow());
				}
				Application.getConnectionWindow().emptyForms();
				Application.getConnectionWindow().showModal(true);

			}
		});
		editButton.setEnabled(false);
		toolBar.add(editButton);

		return toolBar;
	}
}
