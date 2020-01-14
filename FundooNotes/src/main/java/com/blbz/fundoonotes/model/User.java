package com.blbz.fundoonotes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
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
	private long phone;

	@NotNull
	private String pswd;
	
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
}
