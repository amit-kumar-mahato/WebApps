package com.blbz.fundoonotes.service;

import java.util.List;

import com.blbz.fundoonotes.dto.CollaboratorDto;
import com.blbz.fundoonotes.dto.UpdateCollaboratorDto;
import com.blbz.fundoonotes.model.Collaborator;

public interface ICollaboratorService {

	Collaborator addCollaborator(CollaboratorDto colabDto,long noteId);

	List<Collaborator> getCollaboratorList(long noteId);

	void updateCollaborator(UpdateCollaboratorDto updateColabDto);

}
