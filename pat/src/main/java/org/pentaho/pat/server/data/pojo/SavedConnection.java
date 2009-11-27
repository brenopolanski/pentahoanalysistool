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

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * A connection saved by a user for future reuse.
 * 
 * @author Luc Boudreau
 */
@Entity
@Table(name = "CONNECTIONS")
public class SavedConnection implements Comparable<SavedConnection> {

    /**
     * The UUID of this connection.
     */
    @Basic
    private String id;

    /**
     * The name of this connection.
     */
    @Basic
    private String name;

    /**
     * Name of the
     */
    @Basic
    private String driverClassName;

    @Basic
    private String url;

    @Basic
    private String username;

    @Basic
    private String password;

    @Basic
    private String catalog;

    @Basic
    @Type(type = "org.pentaho.pat.server.data.pojo.ConnectionType")
    private ConnectionType type;

    @Column(nullable = true)
    @Type(type = "text")
    private String schemaData = null;

    public SavedConnection() {
        this(null);
    }

    public SavedConnection(String uuid) {
        if (uuid == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = uuid;
        }
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
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

    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    public String getSchemaData() {
        return schemaData == null ? "" : schemaData; //$NON-NLS-1$
    }

    public void setSchemaData(String schemaData) {
        this.schemaData = schemaData;
    }

    public int compareTo(SavedConnection o) {
        return this.getName().compareTo(o.getName());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SavedConnection))
            return false;
        return this.getId().equals(((SavedConnection) obj).getId());
    }
}
