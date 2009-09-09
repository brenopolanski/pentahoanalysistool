/*
 * Copyright (C) 2009 Tom Barber
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

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.list.DefaultComboBoxModel;

/**
 *TODO JAVADOC
 * @created Aug 8, 2009
 * @since 0.5.0
 * @author tom(at)wamonline.org.uk
 *
 */
public class PropertiesPanel extends LayoutComposite {

    /**
     *TODO JAVADOC
     *
     */
    public PropertiesPanel() {
        final LayoutPanel rootPanel = getLayoutPanel();
        
        final LayoutPanel mainPanel = new LayoutPanel();
        mainPanel.addStyleName("pat-propertiesPanel"); //$NON-NLS-1$
        mainPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
        
        ToolButton checkButton1 = new ToolButton("Show Parent");
        checkButton1.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton1.setEnabled(false);

        ToolButton checkButton2 = new ToolButton("Show Filters");
        checkButton2.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton2.setEnabled(false);
        
        ToolButton checkButton3 = new ToolButton("Show Properties");
        checkButton3.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton3.setEnabled(false);
        
        ToolButton checkButton4 = new ToolButton("Hide Blank Cells");
        checkButton4.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton4.setEnabled(false);
        
        ToolButton checkButton5 = new ToolButton("Pivot");
        checkButton5.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton5.setEnabled(false);
        
        
        ComboBox<String> comboBox1 = new ComboBox<String>();
        DefaultComboBoxModel<String> model1 = (DefaultComboBoxModel<String>) comboBox1.getModel();
        model1.add("Drill Position");
        model1.add("Drill Member");
        model1.add("Drill Replace");
       
        comboBox1.setEnabled(false);

        ToolButton checkButton6 = new ToolButton("Drill Through");
        checkButton6.setStyle(ToolButtonStyle.CHECKBOX);
        checkButton6.setEnabled(false);
        
        mainPanel.add(checkButton1);
        mainPanel.add(checkButton2);
        mainPanel.add(checkButton3);
        mainPanel.add(checkButton4);
        mainPanel.add(checkButton5);
        mainPanel.add(comboBox1);
        
        rootPanel.add(mainPanel);
    }

}
