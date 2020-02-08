package com.blbz.fundoonotes.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.NoteIdNotFoundException;
import com.blbz.fundoonotes.dto.CollaboratorDto;
import com.blbz.fundoonotes.model.Collaborator;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.repository.CollaboratorRepository;
import com.blbz.fundoonotes.repository.NoteRepository;
import com.blbz.fundoonotes.service.ICollaboratorService;

import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollaboratorServiceImpl implements ICollaboratorService {

	@Autowired
	CollaboratorRepository collaboratorRepository;

	@Autowired
	NoteRepository noteRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public Collaborator addCollaborator(CollaboratorDto colabDto, long noteId) {
		Optional<Note> noteInfo = noteRepository.findById(noteId);
		Collaborator colabInfo = null;

		if (noteInfo.isPresent()) {
			colabInfo = modelMapper.map(colabDto, Collaborator.class);
			colabInfo.setNoteColab(noteInfo.get());
			log.info("Colab :"+colabInfo);
			return collaboratorRepository.save(colabInfo);
		}
		throw new NoteIdNotFoundException("Note Id not Found with "+noteId);
	}

	@Override
	public List<Collaborator> getCollaboratorList(long noteId) {
		Optional<Note> noteInfo = noteRepository.findById(noteId);
		if(noteInfo.isPresent()) {
			return (List<Collaborator>) collaboratorRepository.findAll();
		}
		throw new NoteIdNotFoundException("Note Id not Found with "+noteId);
	}

}
