package com.blbz.dto;

import org.springframework.stereotype.Component;

@Component
public class UserDto {

	
	private String firstName;
	
	private String lastName;
	
	private String userName;
	
	private String email;
	
	private String mobile;

	private String password;
	
	private String cnfPasword;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCnfPasword() {
		return cnfPasword;
	}

	public void setCnfPasword(String cnfPasword) {
		this.cnfPasword = cnfPasword;
	}
	
}
