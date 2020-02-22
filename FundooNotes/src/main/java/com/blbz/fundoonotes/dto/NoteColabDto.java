package com.blbz.fundoonotes.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class NoteColabDto {

	private long noteId;

	private String title;

	private String description;

	private boolean isArchiev;

	private boolean isPin;

	private boolean isTrash;

	private String colour;

	private LocalDateTime reminder;
	private List<String> colabList;
	private List<String> labelList;
}
