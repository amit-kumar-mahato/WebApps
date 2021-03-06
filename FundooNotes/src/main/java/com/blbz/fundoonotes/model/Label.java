package com.blbz.fundoonotes.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Label {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long labelId;

	private String name;

	@JsonIgnore
	@ManyToMany(mappedBy = "labels")
	private List<Note> noteList;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userLabel;

	public User getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(User userLabel) {
		this.userLabel = userLabel;
	}

	public long getLabelId() {
		return labelId;
	}

	public void setLabelId(long labelId) {
		this.labelId = labelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Note> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<Note> noteList) {
		this.noteList = noteList;
	}

}
