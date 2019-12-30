package com.blbz.fundoonotes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@NotNull
	@Size(min = 3, max = 10)
	private String firstName;

	@NotNull
	@Size(min = 3, max = 10)
	private String lastName;

	@NotNull
	@Column(unique = true)
	private String userName;

	@NotNull
	private String mobile;

	@NotNull
	private String password;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(columnDefinition = "boolean default false",nullable = false)
	private boolean isVerified;
	
	private String createdAt;
	
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String string) {
		this.createdAt = string;
	}

	//@OneToMany(mappedBy = "userNotes",cascade = CascadeType.ALL)
	//private List<Note> notes;

	public Long getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	/*
	 * public List<Note> getNotes() { return notes; }
	 * 
	 * public void setNotes(List<Note> notes) { this.notes = notes; }
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		
		this.password = password;
	}
}
