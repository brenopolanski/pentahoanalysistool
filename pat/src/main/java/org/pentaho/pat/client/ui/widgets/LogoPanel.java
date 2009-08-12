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
package org.pentaho.pat.client.ui.widgets;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * This panel contains the spinning wheel, application name and logo
 * @created Aug 12, 2009 
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class LogoPanel extends HorizontalPanel {

    private static Image wheelPlaceholder;
    
    private static LogoPanel logoPanel = new LogoPanel();
    /**
     * 
     */
    public LogoPanel() {
        wheelPlaceholder = new Image("loading.gif");
        this.setSpacing(5);
        this.add(new HTML("<h3>PAT</h3>"));
    }
    
    public static void spinWheel(boolean spin) {
        if (spin) {
            logoPanel.add(wheelPlaceholder);
        }
        else {
            logoPanel.remove(wheelPlaceholder);
        }
    }
    
    public static LogoPanel getPanel() {
        return logoPanel;
    }

}
