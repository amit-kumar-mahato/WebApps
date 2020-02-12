package com.blbz.fundoonotes.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.NoteIdNotFoundException;
import com.blbz.fundoonotes.dto.CollaboratorDto;
import com.blbz.fundoonotes.dto.UpdateCollaboratorDto;
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
			log.info("Colab :" + colabInfo);
			return collaboratorRepository.save(colabInfo);
		}
		throw new NoteIdNotFoundException("Note Id not Found with " + noteId);
	}

	@Override
	public List<Collaborator> getCollaboratorList(long noteId) {
		Optional<Note> noteInfo = noteRepository.findById(noteId);
		if (noteInfo.isPresent()) {
			return collaboratorRepository.getAllColab(noteId);
		}
		throw new NoteIdNotFoundException("Note Id not Found with " + noteId);
	}

	@Override
	public void updateCollaborator(UpdateCollaboratorDto updateColabDto) {
		Optional<Note> note = noteRepository.findById(updateColabDto.getNoteId());
		if (note.isPresent()) {
			List<Collaborator> collaborators=updateColabDto.getNewEmail().stream().map(email -> {
				Collaborator collaborator= new Collaborator();
				collaborator.setEmail(email);
				collaborator.setNoteColab(note.get());
				return collaborator;
			}).collect(Collectors.toList());
			log.info("Colab List :"+collaborators);
			collaboratorRepository.saveAll(collaborators);
			List<Collaborator> collaborators2 = updateColabDto.getRemoveEmail().stream().map(email -> collaboratorRepository.findByEmailAndNoteColab(email, note.get())).collect(Collectors.toList());
			collaboratorRepository.deleteAll(collaborators2);
		} else {
			throw new NoteIdNotFoundException("Note Id not Found with " + updateColabDto.getNoteId());
		}

	}

}
