package com.blbz.fundoonotes.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateCollaboratorDto {
	
	private long noteId;
	private List<String> newEmail;
	private List<String> removeEmail;

}
