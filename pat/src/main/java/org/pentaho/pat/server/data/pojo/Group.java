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
 * @author Luc Boudreau
 */
@Entity
@Table(name="GROUPS")
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
	 * @return The group name.
	 */
	@Id
	public String getName() {
		return name;
	}

	/**
	 * Defines this group's unique name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the list of current members of this group.
	 * @return A collection of {@link User} objects.
	 */
	@ManyToMany(
	    targetEntity=User.class,
        fetch=FetchType.EAGER)
    @JoinTable(
        name="GROUPS_USERS",
        joinColumns=@JoinColumn(name="group_id",table="GROUPS",referencedColumnName="name"),
        inverseJoinColumns=@JoinColumn(name="user_id",table="USERS",referencedColumnName="username"))
	public Collection<User> getMembers() {
		return members;
	}

	/**
	 * Defines the list of members of this group.
	 * @param members The new list of members of this group.
	 */
	public void setMembers(Collection<User> members) {
		this.members = members;
	}
}
