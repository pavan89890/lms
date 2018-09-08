package com.pavan.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_user")
public class UserBO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "ENABLED")
	private boolean isEnabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<RoleBO> roles;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MANAGER_ID")
	private UserBO manager;

	@OneToMany(mappedBy = "manager", fetch = FetchType.EAGER)
	private List<UserBO> users;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<LeaveBO> leaves;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<LeaveDatesBO> leaveDates;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public List<RoleBO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleBO> roles) {
		this.roles = roles;
	}

	public UserBO getManager() {
		return manager;
	}

	public void setManager(UserBO manager) {
		this.manager = manager;
	}

	public List<UserBO> getUsers() {
		return users;
	}

	public void setUsers(List<UserBO> users) {
		this.users = users;
	}

	public List<LeaveBO> getLeaves() {
		return leaves;
	}

	public void setLeaves(List<LeaveBO> leaves) {
		this.leaves = leaves;
	}

	public List<LeaveDatesBO> getLeaveDates() {
		return leaveDates;
	}

	public void setLeaveDates(List<LeaveDatesBO> leaveDates) {
		this.leaveDates = leaveDates;
	}
}