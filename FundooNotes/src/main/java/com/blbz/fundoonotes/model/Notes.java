package com.blbz.fundoonotes.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Notes {

	@Id
	private String noteId;
	
	@NotNull
	private String title;
	
	@NotNull
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userNotes;
}
