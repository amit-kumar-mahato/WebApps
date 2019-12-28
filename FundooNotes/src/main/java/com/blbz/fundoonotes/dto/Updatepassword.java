package com.blbz.fundoonotes.dto;

public class Updatepassword {
	private String newPassword;
	private String cnfPassword;
	
	public Updatepassword() {
		super();
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getCnfPassword() {
		return cnfPassword;
	}
	public void setCnfPassword(String cnfPassword) {
		this.cnfPassword = cnfPassword;
	}
}
