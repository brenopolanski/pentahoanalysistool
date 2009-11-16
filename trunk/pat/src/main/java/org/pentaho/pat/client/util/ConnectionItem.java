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

package org.pentaho.pat.client.util;


/**
 * Connection Item
 * 
 * @created Aug 3, 2009
 * @since 0.4.0
 * @author Paul Stoellberger
 * 
 */
public class ConnectionItem {
    private String name;

    private final String id;

    private Boolean isConnected = false;

    public ConnectionItem(final String id, final String name, final boolean isConnected) {
        this.name = name;
        this.id = id;
        this.isConnected = isConnected;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        final ConnectionItem other = (ConnectionItem) obj;
        if (!id.equals(other.id))
            return false;
        
        return true;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(final Boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void setName(final String name) {
        this.name = name;
    }
}