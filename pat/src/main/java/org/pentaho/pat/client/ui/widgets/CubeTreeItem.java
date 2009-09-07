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

import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.pentaho.pat.client.Pat;
import org.pentaho.pat.rpc.dto.CubeConnection;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * TreeItems for Cube Menu
 * 
 * @created Aug 7, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class CubeTreeItem extends HorizontalPanel {

    public enum ItemType {
        CONNECTION, CUBE
    }

    private CubeConnection connection;

    private String cube;

    private AbstractImagePrototype itemImage;

    private String itemName;

    private ItemType type;

    public CubeTreeItem(final CubeConnection connection, final String cube) {
        if (connection != null) {
            this.connection = connection;
            this.cube = cube;
            if (cube != null) {
                this.itemImage = Pat.IMAGES.cube();
                this.itemName = "<b>" + this.cube + "</b>"; //$NON-NLS-1$ //$NON-NLS-2$
                this.type = ItemType.CUBE;
            } else {
                this.itemImage = Pat.IMAGES.database();
                this.itemName = this.connection.getName();
                this.type = ItemType.CONNECTION;
            }

            this.setSpacing(5);
            this.add(new WidgetWrapper(new HTML(this.itemImage.getHTML())));
            this.add(new HTML(this.itemName));

            DOM.setStyleAttribute(this.getElement(), "background", "transparent"); //$NON-NLS-1$ //$NON-NLS-2$

        }

    }

    public String getConnectionId() {
        return this.connection.getId();
    }

    public String getCube() {
        return this.cube;
    }

    public ItemType getType() {
        return this.type;
    }
}
