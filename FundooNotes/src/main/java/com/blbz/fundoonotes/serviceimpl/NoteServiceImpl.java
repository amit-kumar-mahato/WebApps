package com.blbz.fundoonotes.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.NoteIdNotFoundException;
import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.dto.ReminderDto;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.NoteRepository;
import com.blbz.fundoonotes.repository.UserRepository;
import com.blbz.fundoonotes.service.ElasticSearchService;
import com.blbz.fundoonotes.service.INoteService;
import com.blbz.fundoonotes.utility.JwtGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoteServiceImpl implements INoteService {

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

	@Autowired
	ElasticSearchService elasticSearchService;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public boolean computeSave(NoteDto noteDto, String token) {

		long userId = getRedisCacheId(token);
		// long id = jwtGenerator.parseJWT(token);
		log.info("Id is :" + userId + " ,Description :" + noteDto.getDescription());

		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			note = modelMapper.map(noteDto, Note.class);
			note.setUserNotes(user.get());
			note.setCreatedAt(LocalDateTime.now());
			note.setArchiev(false);
			note.setPin(false);
			note.setTrash(false);
			note.setColour("blue");

			Note noteInfo = noteRepository.save(note);
			/*
			 * if(noteInfo!=null) { String result = elasticSearchService.createNote(note);
			 * log.info("Elastic Search :"+result); }
			 */
			return true;
		}
		/*
		 * else { throw new
		 * NoteCreationException("Something went wrong Note is not created"); }
		 */
		return false;
	}

	@Override
	public boolean deleteOneNote(long id, String token) throws NoteIdNotFoundException {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteIdAvailable = noteRepository.findById(id);
			if (isNoteIdAvailable.isPresent()) {
				isNoteIdAvailable.get().setTrash(!isNoteIdAvailable.get().isTrash());
				noteRepository.save(isNoteIdAvailable.get());
				return true;
			} else {
				throw new NoteIdNotFoundException("The note you are trying to delete is not available");
			}
		}
		return false;
	}

	@Override
	public boolean isArchived(long id, String token) {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if (isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setArchiev(!isNoteAvailable.get().isArchiev());
				isNoteAvailable.get().setPin(false);
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Note> getAllNotes(String token) {
		// long id = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		List<Note> getAllNotes = null;
		if (isUserAvailable.isPresent()) {
			getAllNotes = noteRepository.getAllNotes(userId);
		}
		return getAllNotes;
	}

	@Override
	public boolean addColor(String color, String token, long id) {

		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if (isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setColour(color);
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean pinnedNotes(long id, String token) {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if (isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setPin(!isNoteAvailable.get().isPin());
				isNoteAvailable.get().setArchiev(false);
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setReminder(long noteId, String token, ReminderDto reminderDto) {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(noteId);
			if (isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setReminder(reminderDto.getTime());
			}
		}
		return false;
	}

	@Override
	public boolean permanentDelete(long noteId, String token) {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(noteId);
			if (isNoteAvailable.isPresent()) {
				noteRepository.deleteNotesPermanently(noteId);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Note> searchByTitle(String title) {
		List<Note> notes = elasticSearchService.searchByTitle(title);
		return notes;
	}

	/*
	 * Add data to the redis cache
	 */
	private long getRedisCacheId(String token) {
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redisTemplate.opsForValue().get(redisTokenKey) == null) {
			long idForRedis = jwtGenerator.parseJWT(token);
			log.info("idForRedis is :" + idForRedis);
			redisTemplate.opsForValue().set(redisTokenKey, idForRedis, 3 * 60, TimeUnit.SECONDS);
		}
		long userId = (Long) redisTemplate.opsForValue().get(redisTokenKey);
		return userId;
	}
}
