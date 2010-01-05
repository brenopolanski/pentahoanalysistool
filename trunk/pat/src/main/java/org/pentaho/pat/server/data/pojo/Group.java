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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * A group object is used to give permissions to {@link User} objects.
 * 
 * @author Luc Boudreau
 */
@Entity
@Table(name = "PAT_GROUPS")
public class Group {

    /**
     * Unique name of this group.
     */
    @Basic
    private String name = null;

    /**
     * List of group members.
     */
    private Collection<User> members = new HashSet<User>();

    /**
     * Returns this group's unique name.
     * 
     * @return The group name.
     */
    @Id
    public String getName() {
        return name;
    }

    /**
     * Defines this group's unique name.
     * 
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the list of current members of this group.
     * 
     * @return A collection of {@link User} objects.
     */
    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinTable(name = "PAT_GROUPS_USERS", joinColumns = @JoinColumn(name = "group_id", table = "PAT_GROUPS", referencedColumnName = "name"), inverseJoinColumns = @JoinColumn(name = "user_id", table = "PAT_USERS", referencedColumnName = "username"))
    public Collection<User> getMembers() {
        return members;
    }

    /**
     * Defines the list of members of this group.
     * 
     * @param members
     *            The new list of members of this group.
     */
    public void setMembers(final Collection<User> members) {
        this.members = members;
    }
}
