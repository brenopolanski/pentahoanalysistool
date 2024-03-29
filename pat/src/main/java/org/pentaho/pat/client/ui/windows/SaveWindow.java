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
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.ui.panels.windows.SaveMenuPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 *TODO JAVADOC
 * 
 * @created Oct 21, 2009
 * @since 0.5.1
 * @author tom (at) wamonline.org.uk
 */
public class SaveWindow extends WindowPanel {

    private static final String SAVE_TITLE = Pat.CONSTANTS.save();

    private final static Button saveButton = new Button(Pat.CONSTANTS.save());
    
    private final static SaveMenuPanel SAVEMENUPANEL = new SaveMenuPanel();

    private final LayoutPanel winContentpanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));

    private final static SaveWindow CBW = new SaveWindow();

    /**
     * Crazy stuff, but apparently does things.
     */
    public static native void display()
    /*-{
    if (typeof top.mantle_initialized != "undefined" && top.mantle_initialized == true) {
        testo = { 
            fileSelected:function(solution,path,name,localizedFileName) { 
                @org.pentaho.pat.client.Pat::saveQueryToSolution(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(solution,path,name,localizedFileName);
            }
        } 
        top.openFileDialog(testo, "Save Analysis... ","Save","xpav");
      }
      else {
          @org.pentaho.pat.client.ui.windows.SaveWindow::displaySaveWindow()();
      }
    }-*/;

    public static native void displayCDA()
    /*-{
    if (typeof top.mantle_initialized != "undefined" && top.mantle_initialized == true) {
        testo = { 
            fileSelected:function(solution,path,name,localizedFileName) { 
                @org.pentaho.pat.client.Pat::saveQueryAsCda(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(solution,path,name,localizedFileName);
            }
        } 
        top.openFileDialog(testo, "Save Datasource... ","Save","cda");
      }
    }-*/;

    
    /**
     * Display the save window.
     */
    @SuppressWarnings("unused")
    private static void displaySaveWindow() {
        CBW.setSize("450px", "300px"); //$NON-NLS-1$ //$NON-NLS-2$
        SAVEMENUPANEL.loadSavedQueries();
        CBW.showModal(false);
        CBW.layout();
    }

    /**
     * Create the save window.
     */
    public SaveWindow() {
        super(SAVE_TITLE);

        winContentpanel.add(SAVEMENUPANEL, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(winContentpanel);
        this.setFooter(createButtonBar());
        this.layout();
    }

    /**
     * Create the button bar.
     * @return
     */
    public LayoutPanel createButtonBar() {

        saveButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {

                SAVEMENUPANEL.save();
                CBW.hide();
            }

        });
        
        Button deleteButton = new Button("Delete");
        deleteButton.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                SAVEMENUPANEL.delete();
            }
        });
        
        
        final Button cancelButton = new Button(Pat.CONSTANTS.cancel());
        cancelButton.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent arg0) {
                CBW.hide();

            }

        });

        return ButtonBarFactory.buildAddRemovePropertiesBar(saveButton, deleteButton, cancelButton);
    }
    
    public static void setSaveEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
    }
    
}
