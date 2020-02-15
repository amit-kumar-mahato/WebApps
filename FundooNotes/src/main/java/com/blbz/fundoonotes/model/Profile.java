package com.blbz.fundoonotes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;

	private String profilePicName;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userLabel;

	public Profile(String profilePicName, User user) {
		super();
		this.profilePicName = profilePicName;
		this.userLabel = user;
	}

	public Profile() {
	}
}
