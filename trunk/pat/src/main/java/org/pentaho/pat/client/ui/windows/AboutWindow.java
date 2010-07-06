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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
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

    private static final String TITLE = Pat.CONSTANTS.aboutPat();
    
    private static final AboutWindow ABOUTWINDOW = new AboutWindow();
    
    private final LayoutPanel winContentpanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    
    /**
     *  About box
     */
    public AboutWindow() {
        super(TITLE);
        LayoutPanel aboutPanel = new LayoutPanel(new FillLayout());
        DOM.setStyleAttribute(aboutPanel.getElement(), "background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
        HTML aboutText = new HTML();
        aboutText.setWordWrap(true);
        StringBuffer txt = new StringBuffer();
        txt.append("<h2><b>About PAT</b></h2>");
        txt.append("PAT is an open source project that aims for nothing less than creating the best next generation OLAP slicer and dicer.");
        txt.append("<p><i>- Tom Barber & Paul Stoellberger</i></p>");
        txt.append("<b>Donate</b><br>");
        txt.append("PAT development and infrastructure maintenance is a full-spare-time project and we do have expenses to cover. Donations would help a lot to keep this project going and even push it's progress.<br>");
        
        ToolButton tb = new ToolButton(ButtonHelper.createButtonLabel(Pat.IMAGES.donate(), "", ButtonLabelType.NO_TEXT));
        tb.addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent arg0) {
                Pat.openDonation();
                
            }
        });
        txt.append("<a href=\"https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=paul%2estoellberger%40gmail%2ecom&lc=GB&item_name=PAT%20Project&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted\" target=\"_blank\">");
        txt.append(tb.getHTML());
        txt.append("</a>");
        txt.append("<br><br>");
        txt.append("<b>Credits</b><br>");
        txt.append("We would like to thank all donors for their generous support of the PAT project.<br>");
        aboutText.setHTML(txt.toString());
        aboutPanel.add(new WidgetWrapper(aboutText) , new FillLayoutData(HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_TOP));
        ScrollLayoutPanel sp = new ScrollLayoutPanel(new BoxLayout());
        sp.setAnimationEnabled(true);
        sp.add(aboutPanel, new BoxLayoutData(FillStyle.BOTH));
        winContentpanel.add(sp, new BoxLayoutData(FillStyle.BOTH));
        this.setWidget(winContentpanel);
        this.layout();
    }

    public static void display() {
        ABOUTWINDOW.setSize("450px", "500px"); //$NON-NLS-1$ //$NON-NLS-2$
        ABOUTWINDOW.showModal(false);
        ABOUTWINDOW.layout();
    }

}
