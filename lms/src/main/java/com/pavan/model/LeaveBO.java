package com.pavan.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t_leave")
public class LeaveBO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private LeaveTypesBO leaveType;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private UserBO user;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MANAGER_ID")
	private UserBO manager;

	@Column(name = "FROM_DATE")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fromDate;

	@Column(name = "TO_DATE")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date toDate;

	@Column(name = "REASON")
	private String reason;

	@Column(name = "STATUS")
	private String status;

	@OneToMany
	private List<LeaveDatesBO> leaveDates;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LeaveTypesBO getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveTypesBO leaveType) {
		this.leaveType = leaveType;
	}

	public UserBO getUser() {
		return user;
	}

	public void setUser(UserBO user) {
		this.user = user;
	}

	public UserBO getManager() {
		return manager;
	}

	public void setManager(UserBO manager) {
		this.manager = manager;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<LeaveDatesBO> getLeaveDates() {
		return leaveDates;
	}

	public void setLeaveDates(List<LeaveDatesBO> leaveDates) {
		this.leaveDates = leaveDates;
	}
}