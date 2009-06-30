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
import org.gwt.mosaic.ui.client.MessageBox.PromptCallback;
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
import org.pentaho.pat.client.listeners.ConnectionListener;
import org.pentaho.pat.client.ui.ConnectionWindow;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
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

public class ConnectionsPanel extends LayoutComposite  {


	private static DefaultListModel<ConnectionItem> model;
	
	public ConnectionsPanel() {
		super();
		final LayoutPanel baseLayoutPanel = getLayoutPanel();
		
		baseLayoutPanel.add(setupConnectionList());
		

	}
	
	 public Widget setupConnectionList() {
		    final LayoutPanel vBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		    vBox.setPadding(0);
		    vBox.setWidgetSpacing(0);

		    final ListBox<ConnectionItem> listBox = new ListBox<ConnectionItem>();

		    model = (DefaultListModel<ConnectionItem>) listBox.getModel();
		    model.add(new ConnectionItem("123","asdf_debug",false));
		    // model.add()

		    listBox.addRowSelectionHandler(new RowSelectionHandler() {
		      public void onRowSelection(RowSelectionEvent event) {
		        int index = listBox.getSelectedIndex();
		        if (index != -1) {
		          //InfoPanel.show("RowSelectionHandler", listBox.getItem(index));
		        }
		      }
		    });

		    listBox.addDoubleClickHandler(new DoubleClickHandler() {
		      public void onDoubleClick(DoubleClickEvent event) {
		        //InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, "DoubleClickListener",
		         //   listBox.getItem(listBox.getSelectedIndex()));
		      }
		    });

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
		    toolBar.add(new ToolButton(ButtonHelper.createButtonLabel(
			        Pat.IMAGES.cross(), null,
			        ButtonLabelType.NO_TEXT), new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        if (listBox.getSelectedIndex() == -1) {
		          MessageBox.alert("ListBox Edit", "No item selected");
		          return;
		        }
		        String item = listBox.getItem(listBox.getSelectedIndex()).getName();
		        MessageBox.confirm("ListBox Remove",
		            "Are you sure you want to permanently delete '" + item
		                + "' from the list?", new ConfirmationCallback() {
		              public void onResult(boolean result) {
		                if (result) {
		                  model.remove(listBox.getSelectedIndex());
		                }
		              }
		            });
		      };
		    }));
		    
		    // EDIT will be disabled for some more time
		    ToolButton editButton = new ToolButton("Edit", new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        if (listBox.getSelectedIndex() == -1) {
		          MessageBox.alert("ListBox Edit", "No item selected");
		          return;
		        }
		        String item = listBox.getItem(listBox.getSelectedIndex()).getName();
		        MessageBox.prompt("ListBox Edit", "Please enter a new value for '"
		            + item + "'", item, new PromptCallback<String>() {
		          public void onResult(String input) {
		            if (input != null) {
		              final int index = listBox.getSelectedIndex();
		              //model.set(index, input);
		            }
		          }
		        });
		      }
		    });
		    editButton.setEnabled(false);
		    toolBar.add(editButton);

		    vBox.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
		    vBox.add(createListBox(), new BoxLayoutData(FillStyle.BOTH));

		    return vBox;
		  }


	 private Widget createRichListBoxCell(final ConnectionItem item) {
		    final FlexTable table = new FlexTable();
		    final FlexCellFormatter cellFormatter = table.getFlexCellFormatter();

		    table.setWidth("100%");
		    table.setBorderWidth(0);
		    table.setCellPadding(3);
		    table.setCellSpacing(0);
		    
		    // table.setStyleName("RichListBoxCell");
		    ToolButton cButton = new ToolButton("",new ClickHandler() {
		    		public void onClick(ClickEvent event) {
				    	// TODO implement dis-/connect routine
		    			model.remove(item);
		    			if (item.isConnected()) {
		    				MessageBox.info("Success", "Disconnected!");
		    				item.setConnected(false);
		    			}
		    			else {
		    				MessageBox.info("Success", "Connected!");
		    				item.setConnected(true);
		    			}
				        model.add(item);
				        
				      };
		    	});
		    	cButton.setHTML(Pat.IMAGES.disconnect().getHTML());
		    	table.setWidget(0,0,cButton);
		    	
		    
		    cellFormatter.setRowSpan(0, 0, 1);
		    cellFormatter.setWidth(0, 0, "35px");
		    cellFormatter.setHeight(0, 0, "35px");
	        table.setHTML(0, 1, "<b>" + item.getName() + "</b>");
   		    return table;
		  }
	 
	 public ListBox<?> createListBox() {
		    final ListBox<ConnectionItem> listBox = new ListBox<ConnectionItem>();
		    listBox.setCellRenderer(new ListBox.CellRenderer<ConnectionItem>() {
		      public void renderCell(ListBox<ConnectionItem> listBox, int row, int column,
		          ConnectionItem item) {
		        switch (column) {
		          case 0:
		            listBox.setWidget(row, column, createRichListBoxCell(item));
		            break;
		          default:
		            throw new RuntimeException("Should not happen");
		        }
		      }
		    });
		    listBox.setModel(model);
		    return listBox;
	 }

	 	public static void addConnection(CubeConnection cc) {
	 		model.add(new ConnectionItem("123",cc.getName(),false));
	 	}

}
