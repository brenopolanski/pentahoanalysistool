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

@Entity
@Table(name="groups")
public class Group {

    @Basic
	private String name = null;
	
	private Collection<User> members = new HashSet<User>();

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(
	    targetEntity=User.class,
        fetch=FetchType.EAGER)
    @JoinTable(
        name="groups_users",
        joinColumns=@JoinColumn(name="group_id",table="groups",referencedColumnName="name"),
        inverseJoinColumns=@JoinColumn(name="user_id",table="users",referencedColumnName="username"))
	public Collection<User> getMembers() {
		return members;
	}

	public void setMembers(Collection<User> members) {
		this.members = members;
	}
}
