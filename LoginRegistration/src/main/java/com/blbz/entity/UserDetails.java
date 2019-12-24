package com.blbz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class UserDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long userId;
	
	@NotNull
	@Size(min=3, max=10)
	private String firstName;
	
	@NotNull
	@Size(min=3, max=10)
	private String lastName;
	
	@NotNull
	@Size(min=3, max=10)
	@Column(unique=true)
	private String userName;
	
	@Column(unique=true)
	private String email;
	
	@NotNull
	@Size(min=5, max=10)
	private String password;
	
	@NotNull
	private String mobile;

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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
