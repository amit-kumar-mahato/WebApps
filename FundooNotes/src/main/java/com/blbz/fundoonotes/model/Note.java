package com.blbz.fundoonotes.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
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

	@JsonIgnore
	private LocalDateTime createdAt;

	@JsonIgnore
	private LocalDateTime updatedAt;

	private String colour;

	private LocalDateTime reminder;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userNotes;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "Label_Note", joinColumns = @JoinColumn(name = "note_id", referencedColumnName = "noteId"), inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "labelId"))
	private List<Label> labels;

}
