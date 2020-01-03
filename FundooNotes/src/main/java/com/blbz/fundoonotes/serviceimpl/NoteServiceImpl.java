package com.blbz.fundoonotes.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.apigateway.model.Op;
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
			note.setArchie(false);
			note.setPin(false);
			note.setTrash(false);
			note.setColour("blue");
		
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
			isNoteIdAvailable.get().setTrash(!isNoteIdAvailable.get().isTrash());
			noteRepository.save(isNoteIdAvailable.get());
			return true;
		}else {
			throw new NoteIdNotFoundException("The note you are trying to delete is not available");
		}
	}
		return false;
	}

	@Override
	public boolean isArchived(long id, String token) throws Exception{
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if(isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setArchie(!isNoteAvailable.get().isArchiev());
				isNoteAvailable.get().setPin(false);
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Note> getAllNotes(String token) throws Exception {
		long id = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(id);
		List<Note> getAllNotes = null;
		if(isUserAvailable.isPresent()) {
			getAllNotes = noteRepository.getAllNotes(id);
		}
		return getAllNotes;
	}

	@Override
	public boolean addColor(String color, String token, long id) throws Exception {
		
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if(isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setColour(color);
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean pinnedNotes(long id, String token) throws Exception {
		long userId = jwtGenerator.parseJWT(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if(isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setPin(!isNoteAvailable.get().isPin());
				isNoteAvailable.get().setArchie(false);
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}
}
