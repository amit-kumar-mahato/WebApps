package com.blbz.fundoonotes.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Entity
//@Getter
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long noteId;

	private String title;

	private String description;

	@Column(columnDefinition = "boolean default false")
	private boolean isArchiev;

	@Column(columnDefinition = "boolean default false")
	private boolean isPin;

	@Column(columnDefinition = "boolean default false")
	private boolean isTrash;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String colour;

	private LocalDateTime reminder;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User userNotes;

	
	  public long getNoteId() { return noteId; }
	  
	  public void setNoteId(long noteId) { this.noteId = noteId; }
	  
	  public String getTitle() { return title; }
	  
	  public void setTitle(String title) { this.title = title; }
	  
	  public String getDescription() { return description; }
	  
	  public void setDescription(String description) { this.description =
	  description; }
	  
	  public boolean isArchiev() { return isArchiev; }
	  
	  public void setArchie(boolean isArchiev) { this.isArchiev = isArchiev; }
	  
	  public boolean isPin() { return isPin; }
	  
	  public void setPin(boolean isPin) { this.isPin = isPin; }
	  
	  public boolean isTrash() { return isTrash; }
	  
	  public void setTrash(boolean isTrash) { this.isTrash = isTrash; }
	  
	  public LocalDateTime getcreatedAt() { return createdAt; }
	  
	  public void setcreatedAt(LocalDateTime createdAt) { this.createdAt =
	  createdAt; }
	  
	  public LocalDateTime getupdatedAt() { return updatedAt; }
	  
	  public void setupdatedAt(LocalDateTime updatedAt) { this.updatedAt =
	  updatedAt; }
	  
	  public String getColour() { return colour; }
	  
	  public void setColour(String colour) { this.colour = colour; }
	  
	  public LocalDateTime getReminder() { return reminder; }
	  
	  public void setReminder(LocalDateTime reminder) { this.reminder = reminder; }
	  
	  public User getUserNotes() { return userNotes; }
	  
	  public void setUserNotes(User userNotes) { this.userNotes = userNotes; }
	 
}
