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

	@OneToMany(fetch=FetchType.EAGER,targetEntity=SavedConnection.class,cascade=CascadeType.ALL)
	@JoinTable(
	    name="users_connections",
	    joinColumns=@JoinColumn(name="user_id",table="users",referencedColumnName="username"),
	    inverseJoinColumns=@JoinColumn(name="connection_id",table="connections",referencedColumnName="id"))
	@Cascade({org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN,org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	public Set<SavedConnection> getSavedConnections() {
		return savedConnections;
	}

	public void setSavedConnections(Set<SavedConnection> savedConnections) {
		this.savedConnections = savedConnections;
	}
}
