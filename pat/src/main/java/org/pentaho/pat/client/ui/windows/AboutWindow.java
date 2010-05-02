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
package org.pentaho.pat.client.ui.windows;

import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.client.util.factory.ConstantFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * About Box 
 * @created May 2, 2010 
 * @since 0.7
 * @author Paul Stoellberger
 * 
 */
public class AboutWindow extends WindowPanel {

    private static final String TITLE = ConstantFactory.getInstance().aboutPat();
    
    private static final AboutWindow ABOUTWINDOW = new AboutWindow();
    
    private static final LayoutPanel winContentpanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    
    /**
     *  About box
     */
    public AboutWindow() {
        super(TITLE);
        LayoutPanel aboutPanel = new LayoutPanel(new FillLayout());
        HTML aboutText = new HTML();
        aboutText.setWordWrap(true);
        StringBuffer txt = new StringBuffer();
        txt.append("<h2><b>About PAT<b><h2>");
        txt.append("PAT is an open source project that aims for nothing less than creating the best next generation OLAP slicer and dicer.");
        txt.append("<br><i>Tom Barber, Paul Stoellberger</i>");
        txt.append("<br><br>");
        txt.append("<b>Donate</b>");
        txt.append("Since PAT development and all the used infrastructure is done for free, donations are more than welcome.<br>");
        
        ToolButton tb = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.donate(), "", ButtonLabelType.NO_TEXT));
        tb.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                Pat.openDonation();
                
            }
        });
        
        txt.append(tb.getHTML());
        txt.append("<b>Credits</b><br>");
        txt.append("We would like to follow the following persons for their support:<br>");
        txt.append("Peter Parker, Mary Jane");
        aboutText.setHTML(txt.toString());
        aboutPanel.add(new WidgetWrapper(aboutText) , new FillLayoutData(HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE));
        winContentpanel.add(aboutPanel, new BoxLayoutData(FillStyle.BOTH));
        ABOUTWINDOW.setWidget(winContentpanel);
    }

    public static void display() {
        ABOUTWINDOW.setSize("450px", "300px"); //$NON-NLS-1$ //$NON-NLS-2$
        ABOUTWINDOW.showModal(false);
        ABOUTWINDOW.layout();
    }

}
