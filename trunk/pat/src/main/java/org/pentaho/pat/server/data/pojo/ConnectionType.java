/*
 * Copyright (C) 2009 Luc Boudreau
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
package org.pentaho.pat.server.data.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConnectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public static final ConnectionType XMLA = new ConnectionType("XMLA"); //$NON-NLS-1$

    public static final ConnectionType Mondrian = new ConnectionType("Mondrian"); //$NON-NLS-1$

    private static final Map<String, ConnectionType> INSTANCES = new HashMap<String, ConnectionType>();

    static {
        INSTANCES.put(XMLA.toString(), XMLA);
        INSTANCES.put(Mondrian.toString(), Mondrian);
    }

    private ConnectionType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    private Object readResolve() {
        return getInstance(name);
    }

    public static ConnectionType getInstance(String name) {
        return (ConnectionType) INSTANCES.get(name);
    }
}
