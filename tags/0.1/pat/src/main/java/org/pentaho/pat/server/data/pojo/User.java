package org.pentaho.pat.server.data.pojo;

import java.util.HashSet;
import java.util.Set;

public class User {
	
	private String username = null;
	
	private Set<Group> groups = new HashSet<Group>();
	
	private Set<SavedConnection> savedConnections = new HashSet<SavedConnection>();
	
	// ACCESSORS
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Set<SavedConnection> getSavedConnections() {
		return savedConnections;
	}

	public void setSavedConnections(Set<SavedConnection> savedConnections) {
		this.savedConnections = savedConnections;
	}
}
