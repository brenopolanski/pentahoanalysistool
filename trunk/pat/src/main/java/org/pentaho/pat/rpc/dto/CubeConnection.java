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

package org.pentaho.pat.rpc.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Object that represents a connection.
 * 
 * @created Mar 26, 2009
 * @since 0.3
 * @author Paul Stoellberger
 */

public class CubeConnection implements Serializable,IsSerializable {
	
	
	private static final long serialVersionUID = 1L;

	private String id = null;
	
	private String name = null;
	
	private String driverClassName = null;
	
	private String url = null;
	
	private String catalog = null;
	
	private String username = null;
	
	private String password = null;
	
	private ConnectionType connectionType = null;

	private String schemaData = null;

	public enum ConnectionType implements IsSerializable {
		XMLA, Mondrian
	}
	
	
	public CubeConnection(ConnectionType cType) {
		connectionType = cType;
	}
	
	public CubeConnection() {
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public ConnectionType getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

    public String getSchemaData() {
        return schemaData;
    }

    public void setSchemaData(String schemaData) {
        this.schemaData = schemaData;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
