package org.pentaho.pat.server.data.pojo;

import java.util.HashSet;
import java.util.Set;

public class Group {

	private String name = null;
	
	private Set<User> members = new HashSet<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}
}
