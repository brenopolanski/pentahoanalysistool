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
package org.pentaho.pat.client.ui.panels;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.list.DefaultListModel;

import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Create the chart options panel.
 *
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 *
 */
public class ChartOptionsPanel extends LayoutComposite{

    /**
     * 
     * Chart options constructor.
     *
     */
    public ChartOptionsPanel(){
        
        final TabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();

        tabPanel.add(generalOptionsPanel(), "General Options");
        tabPanel.add(barOptionsPanel(), "Bar Chart Options");

        this.getLayoutPanel().add(tabPanel);
    }
    
    /**
     * Create the general options panel.
     *
     * @return A LayoutPanel.
     */
    private LayoutPanel generalOptionsPanel() {
        LayoutPanel generalOptionsPanel = new LayoutPanel();
        FormLayout layout = new FormLayout(
                "right:[40dlu,pref], 3dlu, 70dlu, 7dlu, " //$NON-NLS-1$
                    + "right:[40dlu,pref], 3dlu, 70dlu", //$NON-NLS-1$
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                    + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " //$NON-NLS-1$
                    + "p, 3dlu, p, 3dlu, p, 3dlu, p"); //$NON-NLS-1$

        PanelBuilder builder = new PanelBuilder(layout);

        builder.addSeparator("Titles");

        builder.nextLine(2);

        builder.addLabel("Chart Title:");
        builder.nextColumn(2);
        builder.add(new TextBox());
        builder.nextLine(2);

        builder.addLabel("X Axis Label:");
        builder.nextColumn(2);
        builder.add(new TextBox());
        builder.nextColumn(2);
        builder.addLabel("Y Axis Label:");
        builder.nextColumn(2);
        builder.add(new TextBox());
        builder.nextLine(2);

        generalOptionsPanel.add(builder.getPanel());
        return generalOptionsPanel;
    }

    /**
     * 
     * Create the bar options panel.
     *
     * @return A LayoutPanel.
     */
    private LayoutPanel barOptionsPanel(){
        
        LayoutPanel barOptionsPanel = new LayoutPanel();
        
        final ListBox<String> listBox = new ListBox<String>();

        final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();

        model.add("Glass");
        model.add("Normal");
        model.add("Threed");
        
        listBox.addRowSelectionHandler(new RowSelectionHandler() {
            public void onRowSelection(RowSelectionEvent event) {
              int index = listBox.getSelectedIndex();
              if (index != -1) {
                InfoPanel.show("RowSelectionHandler", listBox.getItem(index));
              }
            }
          });

        return barOptionsPanel;
    }
}
