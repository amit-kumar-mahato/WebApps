package com.blbz.fundoonotes.serviceimpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.customexception.NoteIdNotFoundException;
import com.blbz.fundoonotes.customexception.UserNotFoundException;
import com.blbz.fundoonotes.dto.ColorDto;
import com.blbz.fundoonotes.dto.NoteColabDto;
import com.blbz.fundoonotes.dto.NoteDto;
import com.blbz.fundoonotes.dto.ReminderDto;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.CollaboratorRepository;
import com.blbz.fundoonotes.repository.LabelRepository;
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

	@Autowired
	CollaboratorRepository collaboratorRepository;

	@Autowired
	LabelRepository labelRepository;

	@Override
	public Note computeSave(NoteDto noteDto, String token) {

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
			note.setColour("white");

			Note noteInfo = noteRepository.save(note);

//			  if(noteInfo!=null) { String result = elasticSearchService.createNote(note);
//			  log.info("Elastic Search :"+result); }

		}
		/*
		 * else { throw new
		 * NoteCreationException("Something went wrong Note is not created"); }
		 */
		return note;
	}

	@Override
	public boolean deleteOneNote(long id, String token) throws NoteIdNotFoundException {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteIdAvailable = noteRepository.findById(id);
			if (isNoteIdAvailable.isPresent()) {
				isNoteIdAvailable.get().setPin(false);
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
	public boolean isArchived(long noteId, String token) {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(noteId);
			if (isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setPin(false);
				isNoteAvailable.get().setArchiev(!isNoteAvailable.get().isArchiev());
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<NoteColabDto> getAllNotes(String token) {
		// long id = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		List<NoteColabDto> getAllNotes = null;
		if (isUserAvailable.isPresent()) {
			getAllNotes = noteRepository.getAllNotes(userId).stream().map(note -> {
				NoteColabDto colabDto = modelMapper.map(note, NoteColabDto.class);
				colabDto.setColabList(collaboratorRepository.getAllColab(note.getNoteId()).stream().map(clb -> {
					return clb.getEmail();
				}).collect(Collectors.toList()));
				colabDto.setLabelList(getAllLabelsOfOneNote(note.getNoteId()));
				return colabDto;
			}).collect(Collectors.toList());
		}
		return getAllNotes;
	}

	@Override
	public Note addColor(ColorDto color, String token, long id) {

		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if (isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setColour(color.getColor());
				return noteRepository.save(isNoteAvailable.get());
			}
		}
		throw new UserNotFoundException("User Not Found");
	}

	@Override
	public boolean pinnedNotes(long id, String token) {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(id);
			if (isNoteAvailable.isPresent()) {
				isNoteAvailable.get().setArchiev(false);
				isNoteAvailable.get().setTrash(false);
				isNoteAvailable.get().setPin(!isNoteAvailable.get().isPin());
				noteRepository.save(isNoteAvailable.get());
				return true;
			}
		}
		return false;
	}

	@Override
	public Note setReminder(long noteId, String token, ReminderDto reminderDto) throws UserNotFoundException {
		// long userId = jwtGenerator.parseJWT(token);
		long userId = getRedisCacheId(token);
		Optional<User> isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable.isPresent()) {
			Optional<Note> isNoteAvailable = noteRepository.findById(noteId);
			if (isNoteAvailable.isPresent()) {
				String str = reminderDto.getDatetime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
				isNoteAvailable.get().setReminder(dateTime);
				isNoteAvailable.get().setUpdatedAt(LocalDateTime.now());
				return noteRepository.save(isNoteAvailable.get());
			}
		}
		throw new UserNotFoundException("Invalid User Id");
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
		log.info("TOKEN :" + token);
		String[] splitedToken = token.split("\\.");
		String redisTokenKey = splitedToken[1] + splitedToken[2];
		if (redisTemplate.opsForValue().get(redisTokenKey) == null) {
			long idForRedis = jwtGenerator.parseJWT(token);
			log.info("idForRedis is :" + idForRedis);
			redisTemplate.opsForValue().set(redisTokenKey, idForRedis, 3 * 60, TimeUnit.SECONDS);
		}
		return (Long) redisTemplate.opsForValue().get(redisTokenKey);
	}

	private List<String> getAllLabelsOfOneNote(long noteId) {
		List<String> labels = null;
		Optional<Note> noteInfo = noteRepository.findById(noteId);
		if (noteInfo.isPresent()) {
			labels = labelRepository.getLabelsByNoteId(noteId);
			return labels;
		}
		return labels;
	}

	@Override
	public Note updateNoteDetails(long noteId, String token, NoteDto noteDto) {
		Optional<User> isUserAvailable = userRepository.findById(getRedisCacheId(token));
		if (isUserAvailable.isPresent()) {
			Optional<Note> noteInfo = noteRepository.findById(noteId);
			if (noteInfo.isPresent()) {
				noteInfo.get().setTitle(noteDto.getTitle());
				noteInfo.get().setDescription(noteDto.getDescription());
				noteInfo.get().setUpdatedAt(LocalDateTime.now());
				return noteRepository.save(noteInfo.get());
			} else
				throw new NoteIdNotFoundException("The note you are trying to update is not available");
		}
		return null;
	}

	@Override
	public boolean deleteReminder(long noteId, String token) {
		Optional<User> isUserAvailable = userRepository.findById(getRedisCacheId(token));
		if (isUserAvailable.isPresent()) {
			Optional<Note> noteInfo = noteRepository.findById(noteId);
			if (noteInfo.isPresent()) {
				noteInfo.get().setReminder(null);
				noteRepository.save(noteInfo.get());
				return true;
			}
		}
		throw new NoteIdNotFoundException("The note you are trying to update is not available");
	}
}
