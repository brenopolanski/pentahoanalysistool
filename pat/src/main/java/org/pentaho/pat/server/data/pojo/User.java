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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "USERS")
public class User {

    @Basic
    private String username = null;

    @Basic
    private String password = null;

    @Basic
    private boolean enabled = true;

    private Collection<Group> groups = new HashSet<Group>();

    private Set<SavedConnection> savedConnections = new HashSet<SavedConnection>();

    private Set<SavedQuery> savedQueries = new HashSet<SavedQuery>();

    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToMany(targetEntity = Group.class, fetch = FetchType.EAGER, mappedBy = "members")
    public Collection<Group> getGroups() {
        return groups;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    @OneToMany(fetch = FetchType.EAGER, targetEntity = SavedConnection.class, cascade = CascadeType.ALL)
    @JoinTable(name = "USERS_CONNECTIONS", joinColumns = @JoinColumn(name = "user_id", table = "USERS", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "connection_id", table = "CONNECTIONS", referencedColumnName = "id"))
    @Cascade( {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
            org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    public Set<SavedConnection> getSavedConnections() {
        return savedConnections;
    }

    public void setSavedConnections(Set<SavedConnection> savedConnections) {
        this.savedConnections = savedConnections;
    }

    @OneToMany(fetch = FetchType.EAGER, targetEntity = SavedQuery.class, cascade = CascadeType.ALL)
    @JoinTable(name = "USERS_QUERIES", joinColumns = @JoinColumn(name = "user_id", table = "USERS", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "query_id", table = "QUERIES", referencedColumnName = "id"))
    @Cascade( {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
            org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    public Set<SavedQuery> getSavedQueries() {
        return savedQueries;
    }

    public void setSavedQueries(Set<SavedQuery> savedQueries) {
        this.savedQueries = savedQueries;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
