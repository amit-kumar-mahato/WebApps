package com.blbz.fundoonotes.serviceimpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.NoteIdNotFoundException;
import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.NoteRepository;
import com.blbz.fundoonotes.repository.UserRepository;
import com.blbz.fundoonotes.service.INoteService;
import com.blbz.fundoonotes.utility.JwtGenerator;

@Service
public class NoteServiceImpl implements INoteService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);
	
	@Autowired
	JwtGenerator jwtGenerator;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Note note;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	NoteRepository noteRepository;

	@Override
	public boolean computeSave(NoteDto noteDto, String token) throws Exception {
		
		long id = jwtGenerator.parseJWT(token);
		LOGGER.info("Id is :"+id+" ,Description :"+noteDto.getDescription());
		
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			note = modelMapper.map(noteDto, Note.class);
			note.setUserNotes(user.get());
			note.setcreatedAt(LocalDateTime.now());
			note.setArchieved(false);
			note.setPinned(false);
			note.setTrashed(false);
			note.setColour("white");
		
			noteRepository.save(note);
			return true;
		}
		/*
		 * else { throw new
		 * NoteCreationException("Something went wrong Note is not created"); }
		 */
		return false;
	}

	@Override
	public boolean deleteOneNote(long id, String token) throws Exception {
		long userId = jwtGenerator.parseJWT(token);
	Optional<User> isUserAvailable = userRepository.findById(userId);
	if(isUserAvailable.isPresent()) {
		Optional<Note> isNoteIdAvailable = noteRepository.findById(id);
		if(isNoteIdAvailable.isPresent()) {
			note.setTrashed(!note.isTrashed());
			noteRepository.save(note);
			return true;
		}else {
			throw new NoteIdNotFoundException("The note you are trying to delete is not available");
		}
	}
		return false;
	}

}
