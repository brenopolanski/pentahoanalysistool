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
import org.pentaho.pat.client.ui.ConnectionWindow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class ConnectionsPanel extends LayoutComposite {


	public ConnectionsPanel() {
		super();
		final LayoutPanel baseLayoutPanel = getLayoutPanel();
		
		baseLayoutPanel.add(createDemo1());
		

	}
	
	 public Widget createDemo1() {
		    final LayoutPanel vBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		    vBox.setPadding(0);
		    vBox.setWidgetSpacing(0);

		    final ListBox<String> listBox = new ListBox<String>();

		    final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();
		    model.add("foo");
		    model.add("bar");
		    model.add("baz");
		    model.add("toto");
		    model.add("tintin");

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
		    toolBar.add(new ToolButton("New", new ClickHandler() {
		      public void onClick(ClickEvent event) {
					if (ToolBarPanel.connectWindow == null) {
						ToolBarPanel.connectWindow = new ConnectionWindow();
					}
					ToolBarPanel.connectWindow.emptyForms();
					ToolBarPanel.connectWindow.showModal(true);
		      }
		    }));
		    toolBar.add(new ToolButton("Remove", new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        if (listBox.getSelectedIndex() == -1) {
		          MessageBox.alert("ListBox Edit", "No item selected");
		          return;
		        }
		        String item = listBox.getItem(listBox.getSelectedIndex());
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
		        String item = listBox.getItem(listBox.getSelectedIndex());
		        MessageBox.prompt("ListBox Edit", "Please enter a new value for '"
		            + item + "'", item, new PromptCallback<String>() {
		          public void onResult(String input) {
		            if (input != null) {
		              final int index = listBox.getSelectedIndex();
		              model.set(index, input);
		            }
		          }
		        });
		      }
		    });
		    editButton.setEnabled(false);
		    toolBar.add(editButton);

		    vBox.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
		    vBox.add(listBox, new BoxLayoutData(FillStyle.BOTH));

		    return vBox;
		  }


}
