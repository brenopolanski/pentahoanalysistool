package org.pentaho.pat.server.data.pojo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="users")
public class User {
	
    @Basic
    private String username = null;
	
    private Collection<Group> groups = new HashSet<Group>();
	
    private Set<SavedConnection> savedConnections = new HashSet<SavedConnection>();
	
	@Id
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@ManyToMany(
	    targetEntity=Group.class,
	    fetch=FetchType.EAGER,
	    mappedBy="members")
	public Collection<Group> getGroups() {
		return groups;
	}

	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
	}

	@Transient
	public Set<SavedConnection> getSavedConnections() {
		return savedConnections;
	}

	public void setSavedConnections(Set<SavedConnection> savedConnections) {
		this.savedConnections = savedConnections;
	}
}
