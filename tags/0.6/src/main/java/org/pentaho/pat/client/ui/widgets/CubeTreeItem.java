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
import org.pentaho.pat.rpc.dto.CubeItem;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Widget for Cube Menu
 * 
 * @created Aug 7, 2009
 * @since 0.5.0
 * @author Paul Stoellberger
 * 
 */
public class CubeTreeItem extends TreeItem {

    public enum ItemType {
        CONNECTION, CUBE
    }

    private CubeConnection connection;

    private CubeItem cube;

    private AbstractImagePrototype itemImage;

    private String itemName;

    private ItemType type;
    
    private boolean selected;

    private HorizontalPanel widgetPanel = null;
    
    private final static String CUBE_TREE_ITEM = "pat-TreeItem"; //$NON-NLS-1$
    
    private final static String CUBE_TREE_ITEM_SELECTED = "pat-TreeItem-selected"; //$NON-NLS-1$
    /**
     * Represents a cube item
     * 
     * @param connection
     *            - Connection the cube belongs to
     * @param cube
     *            - Name of the Cube
     */
    public CubeTreeItem(final CubeConnection connection, final CubeItem cube) {
        super();
        if (connection != null) {
            this.connection = connection;
            this.cube = cube;
            if (cube == null) {
                this.itemImage = Pat.IMAGES.database();
                this.itemName = this.connection.getName();
                this.type = ItemType.CONNECTION;
            } else {
                this.itemImage = Pat.IMAGES.cube();
                this.itemName = "<b>" + this.cube.getName() + "</b>"; //$NON-NLS-1$ //$NON-NLS-2$
                this.type = ItemType.CUBE;
            }

            widgetPanel = new HorizontalPanel();
            widgetPanel.add(new WidgetWrapper(new HTML(this.itemImage.getHTML())));
            widgetPanel.add(new HTML(this.itemName));
            this.setWidget(widgetPanel);
            

            this.setStyleName(CUBE_TREE_ITEM);
        }

    }
   
    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.TreeItem#setSelected(boolean)
     */
    @Override
    public void setSelected(boolean selected) {
        if (this.selected == selected) {
          return;
        }
        this.selected = selected;
        
        setStyleName(widgetPanel.getElement(), CUBE_TREE_ITEM_SELECTED, selected);
      }
    

    /**
     * Return the connection id.
     * @return
     */
    public String getConnectionId() {
        return this.connection.getId();
    }

    /**
     * Return the Cube Name.
     * @return
     */
    public String getCubeName() {
        return this.cube.getName();
    }

    /**
     * Return the catalog name.
     * @return
     */
    public String getCatalogName() {
        return this.cube.getCatalog();
    }

    /**
     * Return the Schema Name.
     * @return
     */
    public String getSchemaName() {
        return this.cube.getSchema();
    }

    /**
     * Return the type.
     * @return
     */
    public ItemType getType() {
        return this.type;
    }

    /**
     * Return the Cube Item.
     * @return
     */
    public CubeItem getCubeItem() {
        return this.cube;
    }
}
